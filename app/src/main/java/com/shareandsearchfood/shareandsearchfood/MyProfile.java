package com.shareandsearchfood.shareandsearchfood;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.shareandsearchfood.imageAdapter.MyFavoritesListAdapter;
import com.shareandsearchfood.imageAdapter.MyPubsListAdapter;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by david_000 on 13/10/2016.
 */

public class MyProfile extends NavBar {
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
    private RatingBar rate;
    Button saveReceipt;
    Button pubReceipt;
    private MyPubsListAdapter customAdapter;
    private MyFavoritesListAdapter customAdapterFav;
    private  List<Receipt> updated;
    private  List<Receipt> updatedFav;
    TabHost host;
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

        host = (TabHost)findViewById(R.id.tabHost);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Feed");
        spec.setContent(R.id.FEED);
        spec.setIndicator("Feed");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("MyPubs");
        spec.setContent(R.id.MYPUBS);
        spec.setIndicator("MyPubs");
        host.addTab(spec);

        //Tab 3
        spec = host.newTabSpec("Favorites");
        spec.setContent(R.id.FAVORITES);
        spec.setIndicator("Favorites");
        host.addTab(spec);

        //Tab 4
        spec = host.newTabSpec("Share");
        spec.setContent(R.id.SHARE);
        spec.setIndicator("Share");
        host.addTab(spec);

        //Tab 5
        spec = host.newTabSpec("Badges");
        spec.setContent(R.id.BADGES);
        spec.setIndicator("Badges");
        host.addTab(spec);


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

        //cria a view das receitas criadas
        createPubs();
        //cria a view das receitas favoritas
        createFav();
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

        Log.d("bandeira",Integer.toString(flag));
        Log.d("foto",photoUri);

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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            photoReceipt = data.getData();
            TextView photoName = (TextView) findViewById(R.id.photoName);
            photoName.setText(photoReceipt.getPath());
        }
    }
    public void saveReceipt(int buttomId){
        DaoSession daoSession = ((App) getApplication()).getDaoSession();
        receiptDao = daoSession.getReceiptDao();

        title_receipt = (AutoCompleteTextView) findViewById(R.id.title_receita);
        ingredients = (AutoCompleteTextView) findViewById(R.id.ingredients);
        steps = (AutoCompleteTextView) findViewById(R.id.Step_by_Step);


       if(buttomId == saveReceipt.getId())
                receiptDao.insert(new Receipt(null,  title_receipt.getText().toString(), ingredients.toString(),
                        steps.toString(), photoReceipt.toString(),null,0,getUserID(session.getEmail()),new Date(),0,false));


        else
                receiptDao.insert(new Receipt(null,  title_receipt.getText().toString(), ingredients.toString(),
                        steps.toString(), photoReceipt.toString(),null,1,getUserID(session.getEmail()),new Date(),0,false));
    }

    //MyPubs
    private List<Receipt> getUserReceipts(){
        long userId = getUserID(session.getEmail());
        DaoSession daoSession = ((App) getApplication()).getDaoSession();
        ReceiptDao receiptDao = daoSession.getReceiptDao();
        QueryBuilder qb = receiptDao.queryBuilder();
        qb.where(ReceiptDao.Properties.UserId.eq(userId));
        List<Receipt> receipts = qb.list();
        return receipts;
    }
    private void createPubs(){
        ListView yourListView = (ListView) findViewById(R.id.myPubsList);
        List<Receipt> userReceipts = getUserReceipts();
        updated = checkIfFavorite(userReceipts);
        customAdapter = new MyPubsListAdapter(this, R.layout.row_my_pubs,updated);
        yourListView.setAdapter(customAdapter);
    }

    //Favorites
    public void saveFavorite(View v){
        DaoSession daoSession = ((App) getApplication()).getDaoSession();
        favoriteDao = daoSession.getFavoriteDao();

        View parentView = (View) v.getParent();
        favorite = (CheckBox) parentView.findViewById(R.id.star2);

        Receipt receipt = customAdapter.getItem(favorite.getVerticalScrollbarPosition());

        if(favorite.isChecked()){
            favoriteDao.insert(new Favorite(null,getUserID(session.getEmail()),receipt.getId()));
        }
        else{
            favoriteDao.deleteByKeyInTx(getUserID(session.getEmail()),receipt.getId());
            unchecked(getUserID(session.getEmail()),receipt.getId());
        }
    }
    public List<Receipt> checkIfFavorite(List<Receipt> firstList){
        List<Receipt> checked = firstList;

        DaoSession daoSession = ((App) getApplication()).getDaoSession();
        FavoriteDao favoriteDao= daoSession.getFavoriteDao();
        List<Favorite> favorites = favoriteDao.loadAll();

        for (Receipt receipt: checked) {
            for (Favorite favorite:favorites) {
                if(receipt.getUserId() == favorite.getUserId() && receipt.getId() == favorite.getReceiptId())
                    receipt.setFavorite(true);
            }
        }

        return checked;
    }
    public void unchecked(Long userId, Long receiptId){
        for (Receipt receipt: updated) {
                if(receipt.getUserId() == userId && receipt.getId() == receiptId)
                    receipt.setFavorite(false);
        }
    }

    //My Favorites
    private void createFav(){
        ListView yourListView = (ListView) findViewById(R.id.myFavList);
        List<Receipt> userFavReceipts = getUserFavReceipts();
        updatedFav = checkIfFavorite(userFavReceipts);
        customAdapterFav = new MyFavoritesListAdapter(this, R.layout.row_my_favorites,updatedFav);
        yourListView.setAdapter(customAdapterFav);
    }
    private List<Receipt> getUserFavReceipts(){
        long userId = getUserID(session.getEmail());
        DaoSession daoSession = ((App) getApplication()).getDaoSession();
        FavoriteDao favoriteDao = daoSession.getFavoriteDao();
        QueryBuilder qb = favoriteDao.queryBuilder();
        qb.where(FavoriteDao.Properties.UserId.eq(userId));
        List<Favorite> favorites = qb.list();

        return getReceiptsById(favorites);

    }
    private List<Receipt> getReceiptsById(List<Favorite> favorites){
        DaoSession daoSession = ((App) getApplication()).getDaoSession();
        ReceiptDao receiptDao= daoSession.getReceiptDao();
        List<Receipt> receipts = receiptDao.loadAll();
        List<Receipt> receiptsByID = new ArrayList<>();
        for (Receipt receipt: receipts) {
            for (Favorite favorite: favorites)  {
                if(receipt.getId() == favorite.getReceiptId())
                    receiptsByID.add(receipt);
            }
        }
        return  receiptsByID;
    }

}
