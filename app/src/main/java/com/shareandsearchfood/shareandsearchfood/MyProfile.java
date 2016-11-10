package com.shareandsearchfood.shareandsearchfood;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.shareandsearchfood.Adapters.FavoriteRecyclerViewAdapter;
import com.shareandsearchfood.Fragments.FavoriteFragment;
import com.shareandsearchfood.Fragments.MyPubsFragment;
import com.shareandsearchfood.Fragments.ShareFragment;
import com.shareandsearchfood.login.App;
import com.shareandsearchfood.login.DaoSession;
import com.shareandsearchfood.login.Receipt;
import com.shareandsearchfood.login.ReceiptDao;
import com.shareandsearchfood.login.Session;
import com.shareandsearchfood.login.User;
import com.shareandsearchfood.login.UserDao;
import org.greenrobot.greendao.query.QueryBuilder;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;

/**
 * Created by david_000 on 13/10/2016.
 */

public class MyProfile extends NavBar implements MyPubsFragment.OnListFragmentInteractionListener,FavoriteFragment.OnListFragmentInteractionListenerFav{
    private LinearLayout mLayout, mLayout2;
    private EditText mEditText, mEditText2;
    private Button mButton, mButton2;
    private Session session;
    private static final int PICK_IMAGE = 100;
    private Uri photoReceipt;
    private ReceiptDao receiptDao;
    private FavoriteDao favoriteDao;
    private AutoCompleteTextView title_receipt;
    private AutoCompleteTextView ingredients;
    private AutoCompleteTextView steps;
    private CheckBox favorite;
    Button saveReceipt;
    Button pubReceipt;
    private TextView photoName;
    private boolean added_ingredients = false;
    private boolean added_steps = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        session = new Session(this);

        TextView textView3 = (TextView)findViewById(R.id.username);
        textView3.setText(getUser(session.getEmail()));

        try{
        setPhoto();}
        catch (IOException w){}

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        TextView myPubs = (TextView) findViewById(R.id.myPubs);
        myPubs.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.frame, new MyPubsFragment());
                    ft.addToBackStack(null).commit();

            }});

        TextView myFav = (TextView) findViewById(R.id.myFav);
        myFav.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frame, new FavoriteFragment());
                ft.addToBackStack(null).commit();

            }});
        TextView share = (TextView) findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frame, new ShareFragment());
                ft.addToBackStack(null).commit();

            }});
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

    public void clickRecipe(View view){
        Intent intent = new Intent(this, RecipeContent.class);
        startActivity(intent);

    }
    public void clickProfile(View view){
        Intent intent = new Intent(this, Visit_person.class);
        startActivity(intent);

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


    //Reload page
    private  void reload(){
        finish();
        startActivity(getIntent());
    }

    //Data from profile
    private String getUser(String email){
        DaoSession daoSession = ((App) getApplication()).getDaoSession();
        UserDao userDao = daoSession.getUserDao();
        QueryBuilder qb = userDao.queryBuilder();
        qb.where(UserDao.Properties.Email.eq(email));
        List<User> user = qb.list();
        return user.get(0).getUsername();
    }
    private String getPhotoUri(String email){
        DaoSession daoSession = ((App) getApplication()).getDaoSession();
        UserDao userDao = daoSession.getUserDao();
        QueryBuilder qb = userDao.queryBuilder();
        qb.where(UserDao.Properties.Email.eq(email));
        List<User> user = qb.list();
        return user.get(0).getPhoto();
    }
    private int getuserFlag(String email){
        DaoSession daoSession = ((App) getApplication()).getDaoSession();
        UserDao userDao = daoSession.getUserDao();
        QueryBuilder qb = userDao.queryBuilder();
        qb.where(UserDao.Properties.Email.eq(email));
        List<User> user = qb.list();
        return user.get(0).getFlag();
    }
    private void setPhoto()throws IOException {
        String photoUri = getPhotoUri(session.getEmail());
        ImageView photo = (ImageView) findViewById(R.id.profileImage);
        int flag = getuserFlag(session.getEmail());

        if (photoUri != null && flag == 1 ) {
            URL url = new URL(photoUri);
            Bitmap myBitmap = BitmapFactory.decodeStream(url.openStream());
            photo.setImageBitmap(myBitmap);
        }else if (photoUri != null && flag == 0 ) {
            photo.setImageURI(Uri.parse(photoUri));
        } else
            photo.setImageResource(R.drawable.com_facebook_profile_picture_blank_square);
    }

    //Share receipt
    private Long getUserID(String email){
        DaoSession daoSession = ((App) getApplication()).getDaoSession();
        UserDao userDao = daoSession.getUserDao();
        QueryBuilder qb = userDao.queryBuilder();
        qb.where(UserDao.Properties.Email.eq(email));
        List<User> user = qb.list();
        return user.get(0).getId();
    }
    public void openGalleryShare(View v) {
        Intent gallery =
                new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }
    public void addIngredients(View v){

        View view;
        LayoutInflater inflater = (LayoutInflater)   getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.fragment_share, null);

        mLayout = (LinearLayout) view.findViewById(R.id.layoutIngredientes);
        mEditText = (EditText) view.findViewById(R.id.ingredients);
        mButton = (Button) view.findViewById(R.id.moreIngredients);
        mButton.setOnClickListener(onClick());
    }
    public void addSteps(View v){
        View view;
        LayoutInflater inflater = (LayoutInflater)   getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.fragment_share, null);


        mLayout2 = (LinearLayout) view.findViewById(R.id.layoutSteps);
        mEditText2 = (EditText) view.findViewById(R.id.Step_by_Step);
        mButton2 = (Button) view.findViewById(R.id.moreSteps);
        mButton2.setOnClickListener(onClick());

        title_receipt = (AutoCompleteTextView) view.findViewById(R.id.title_receita_share);
        ingredients = (AutoCompleteTextView) view.findViewById(R.id.ingredients);
        steps = (AutoCompleteTextView) view.findViewById(R.id.Step_by_Step);



    }
    public void safeReceipt(View v){
        View view;
        LayoutInflater inflater = (LayoutInflater)   getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.fragment_share, null);

        saveReceipt = (Button) view.findViewById(R.id.Save) ;
        saveReceipt(saveReceipt.getId());



    }
    public void pubReceipt(View v){
        View view;
        LayoutInflater inflater = (LayoutInflater)   getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.fragment_share, null);

        pubReceipt = (Button) view.findViewById(R.id.Publish) ;
        saveReceipt(pubReceipt.getId());


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

            reload();
        }
    }

    //Favorites
    public void saveFavorite(View v){
        DaoSession daoSession = ((App) getApplication()).getDaoSession();
        favoriteDao = daoSession.getFavoriteDao();

        View parentView = (View) v.getParent();
        favorite = (CheckBox) parentView.findViewById(R.id.star2);

        Receipt receipt = null;

        if(favorite.isChecked()) {
            favoriteDao.insert(new Favorite(null,getUserID(session.getEmail()),receipt.getId()));
        }
        else if(!favorite.isChecked()){
            favoriteDao.deleteByKeyInTx(getUserID(session.getEmail()),receipt.getId());
        }
    }


    @Override
    public void onListFragmentInteraction(Receipt position) {
    }
    @Override
    public void onListFragmentInteractionFav(Receipt position) {
        // The user selected the headline of an article from the HeadlinesFragment
        // Do something here to display that article
    }
}
