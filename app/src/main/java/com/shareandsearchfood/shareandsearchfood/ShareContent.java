package com.shareandsearchfood.shareandsearchfood;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shareandsearchfood.login.App;
import com.shareandsearchfood.login.DaoSession;
import com.shareandsearchfood.login.Receipt;
import com.shareandsearchfood.login.ReceiptDao;
import com.shareandsearchfood.login.Session;
import com.shareandsearchfood.login.User;
import com.shareandsearchfood.login.UserDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.Date;
import java.util.List;

/**
 * Created by david_000 on 10/11/2016.
 */

public class ShareContent extends NavBar{
    private LinearLayout mLayout, mLayout2;
    private EditText mEditText, mEditText2;
    private Button mButton, mButton2;
    private AutoCompleteTextView title_receipt;
    private AutoCompleteTextView ingredients;
    private AutoCompleteTextView steps;
    private Button saveReceipt;
    private  Button pubReceipt;
    private boolean added_ingredients = false;
    private boolean added_steps = false;
    private ReceiptDao receiptDao;
    private Uri photoReceipt;
    private Session session;
    private static final int PICK_IMAGE = 100;
    private TextView photoName;


    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        session = new Session(this);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // cenas para adicionar mais ingredientes no share
        mLayout = (LinearLayout) findViewById(R.id.layoutIngredientes);
        mEditText = (EditText) findViewById(R.id.ingredients);
        mButton = (Button) findViewById(R.id.moreIngredients);
        mButton.setOnClickListener(onClick());
        TextView textView = new TextView(this);
        textView.setText("More ingredients");

        // cenas para adicionar mais steps no share
        mLayout2 = (LinearLayout) findViewById(R.id.layoutSteps);
        mEditText2 = (EditText) findViewById(R.id.Step_by_Step);
        mButton2 = (Button) findViewById(R.id.moreSteps);
        mButton2.setOnClickListener(onClick());
        TextView textView2 = new TextView(this);
        textView2.setText("More Steps");


        title_receipt = (AutoCompleteTextView) findViewById(R.id.title_receita_share);
        ingredients = (AutoCompleteTextView) findViewById(R.id.ingredients);
        steps = (AutoCompleteTextView) findViewById(R.id.Step_by_Step);


        //Save and Pubb receipts
        saveReceipt = (Button) findViewById(R.id.Save) ;
        pubReceipt = (Button) findViewById(R.id.Publish) ;
        saveReceipt.setOnClickListener(new View.OnClickListener (){
            @Override
            public void onClick(View v) {
                saveReceipt(saveReceipt.getId());
            }
        });

        pubReceipt.setOnClickListener(new View.OnClickListener (){
            @Override
            public void onClick(View v) {
                saveReceipt(pubReceipt.getId());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            photoReceipt = data.getData();
            photoName = (TextView) findViewById(R.id.photoName);
            photoName.setText(photoReceipt.getPath());
        }
    }

    private View.OnClickListener onClick() {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                mLayout.addView(createNewTextView(mEditText.getText().toString()));
                mLayout2.addView(createNewTextView2(mEditText2.getText().toString()));
                added_ingredients = true;
                added_steps = true;
                mEditText.setText("");
                mEditText2.setText("");

            }
        };
    }

    private TextView createNewTextView(String text) {
        final DrawerLayout.LayoutParams lparams = new DrawerLayout.LayoutParams(DrawerLayout.LayoutParams.WRAP_CONTENT, DrawerLayout.LayoutParams.WRAP_CONTENT);
        final TextView textView = new TextView(this);
        textView.setLayoutParams(lparams);
        if (!text.isEmpty()) {

            textView.setText(text + "; ");
            return textView;
        }
        else {

            textView.setText("");
            return textView;
        }

    }
    private TextView createNewTextView2(String text) {
        final DrawerLayout.LayoutParams lparams = new DrawerLayout.LayoutParams(DrawerLayout.LayoutParams.WRAP_CONTENT, DrawerLayout.LayoutParams.WRAP_CONTENT);
        final TextView textView2 = new TextView(this);
        textView2.setLayoutParams(lparams);

        if (!text.isEmpty()) {

            textView2.setText(text + "; ");
            return textView2;
        }
        else {

            textView2.setText("");
            return textView2;
        }
    }
    public void saveReceipt(int buttomId) {

        boolean cancel = false;
        View focusView = null;

        DaoSession daoSession = ((App) getApplication()).getDaoSession();
        receiptDao = daoSession.getReceiptDao();

        // Store values at the time of the login attempt.
        String title = title_receipt.getText().toString();
        //String name_photo = photoName.toString();


        if (title.isEmpty()) {
            Log.d("incha: ", "devia de dar o setError");
            title_receipt.setError(getString(R.string.how_to_do_it_tittle_empty));
            focusView = title_receipt;
            cancel = true;
        }

        else if (added_ingredients == false){
            ingredients.setError(getString(R.string.how_to_do_it_tittle_empty));
            focusView = ingredients;
            cancel = true;
        }
        else if (added_steps == false){
            steps.setError(getString(R.string.how_to_do_it_tittle_empty));
            focusView = steps;
            cancel = true;
        }
        // nao sei se funcaaaaaa
        /**  else if (name_photo.isEmpty()){
         Log.d("inchaporca: ", "devia de dar o setError");
         steps.setError(getString(R.string.need_photo));
         focusView = steps;
         cancel = true;
         }
         */
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {


            if (buttomId == saveReceipt.getId())
                receiptDao.insert(new Receipt(null, title_receipt.getText().toString(), ingredients.toString(),
                        steps.toString(), photoReceipt.toString(), null, 0, getUserID(session.getEmail()), new Date(), 0, false));


            else
                receiptDao.insert(new Receipt(null, title_receipt.getText().toString(), ingredients.toString(),
                        steps.toString(), photoReceipt.toString(), null, 1, getUserID(session.getEmail()), new Date(), 0, false));

            Intent intent = new Intent(this, MyProfile.class);
            startActivity(intent);
        }
    }
    public void openGalleryShare(View v) {
        Intent gallery =
                new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }
    private Long getUserID(String email){
        DaoSession daoSession = ((App) getApplication()).getDaoSession();
        UserDao userDao = daoSession.getUserDao();
        QueryBuilder qb = userDao.queryBuilder();
        qb.where(UserDao.Properties.Email.eq(email));
        List<User> user = qb.list();
        return user.get(0).getId();
    }


}
