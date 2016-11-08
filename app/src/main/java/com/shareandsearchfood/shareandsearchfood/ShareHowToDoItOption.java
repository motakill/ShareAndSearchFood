package com.shareandsearchfood.shareandsearchfood;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;

import com.shareandsearchfood.login.App;
import com.shareandsearchfood.login.DaoSession;
import com.shareandsearchfood.login.Session;
import com.shareandsearchfood.login.User;
import com.shareandsearchfood.login.UserDao;
import com.shareandsearchfood.shareandsearchfood.HowToDoItTable;
import com.shareandsearchfood.shareandsearchfood.HowToDoItTableDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.io.ByteArrayOutputStream;
import java.util.List;


public class ShareHowToDoItOption extends NavBar {

    private static final int PICK_IMAGE = 100;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Uri imageUri;
    private ImageView imageView;
    private AutoCompleteTextView mTitle, mObservations;
    Session session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_how_to_do_it_option);

        session = new Session(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        mTitle = (AutoCompleteTextView) findViewById(R.id.title_how_to_do_it);
        mObservations = (AutoCompleteTextView) findViewById(R.id.obs_how_to_do_it);

        Button click = (Button)findViewById(R.id.camera);
        imageView = (ImageView) findViewById(R.id.display_personal_photo);

        Button pickImageButton = (Button) findViewById(R.id.mypersonal_photo_button);
        pickImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        Button submitHowToDoIt = (Button) findViewById(R.id.Publish_how_to_do_it);
        submitHowToDoIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptPublishHowToDoIt();
            }
        });


    }

    private void attemptPublishHowToDoIt() {


        // Reset errors.
        mTitle.setError(null);
        mObservations.setError(null);

        // Store values at the time of the login attempt.
        String title = mTitle.getText().toString();
        String obs = mObservations.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (title.isEmpty()) {
            mTitle.setError(getString(R.string.how_to_do_it_tittle_empty));
            focusView = mTitle;
            cancel = true;
        }

        else if (obs.isEmpty()){
            mObservations.setError(getString(R.string.how_to_do_it_tittle_empty));
            focusView = mObservations;
            cancel = true;
        }
        //nao ta fazer bem nao ta a lançar a excepcao quando nao selecionam uma photo
        else if (imageView.toString().isEmpty()){
            mTitle.setError(getString(R.string.need_photo));
            focusView = mTitle;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

            DaoSession daoSession = ((App) getApplication()).getDaoSession();
            HowToDoItTableDao howDao = daoSession.getHowToDoItTableDao();
           // session.setEmail(email);

                howDao.insert(new HowToDoItTable(null, getUserID(session.getEmail()), title, obs,imageUri.toString(),null,null));
                Intent intent = new Intent(this, HowToDoIt.class);
                startActivity(intent);

                //  showProgress(true);
                //  mAuthTask = new RegistActivity.UserLoginTask(email, password, user, imageView);
                //  mAuthTask.execute((Void) null);

            }
    }

    private long getUserID(String email){
        DaoSession daoSession = ((App) getApplication()).getDaoSession();
        UserDao userDao = daoSession.getUserDao();
        QueryBuilder qb = userDao.queryBuilder();
        qb.where(UserDao.Properties.Email.eq(email));
        List<User> user = qb.list();
        return user.get(0).getId();
    }
    private void openGallery() {
        Intent gallery =
                new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    // carregar foto
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);

            Context context = getApplicationContext();
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), imageBitmap, "Title", null);
            imageUri = Uri.parse(path);
        }
    }

    public void dispatchTakePictureIntent(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
}
