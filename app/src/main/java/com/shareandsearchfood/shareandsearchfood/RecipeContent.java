package com.shareandsearchfood.shareandsearchfood;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TabHost;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.shareandsearchfood.Utils.FirebaseOperations;
import com.shareandsearchfood.Utils.Image;
import com.shareandsearchfood.login.LoginActivity;
import com.squareup.picasso.Picasso;


import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * Created by david_000 on 16/10/2016.
 */

public class RecipeContent extends NavBar {
    public TextView titulo;
    public ImageView photo;
    public TextView timestamp;
    public ImageView userImage ;
    public TextView nickname;
    private TextView ingredients;
    private TextView steps;
    private RatingBar rate;
    private CheckBox favorite;
    private String userID;
    private String userPhotoIntent;
    private String ingredientsIntent;
    private String stepsIntent;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_content);

        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        Intent intent = getIntent();
        userPhotoIntent = intent.getStringExtra("userPhoto");
        String recipePhotoIntent = intent.getStringExtra("recipePhoto");
        String tituloIntent = intent.getStringExtra("recipeTitle");
        Boolean favoriteIntent = intent.getBooleanExtra("favorite",false);
        ingredientsIntent = intent.getStringExtra("ingredients");
        stepsIntent = intent.getStringExtra("steps");
        int rateIntent = intent.getIntExtra("rating",0);
        userID = intent.getStringExtra("userID");

        titulo = (TextView) findViewById(R.id.titulo);
        photo = (ImageView) findViewById(R.id.imageView6);
        userImage = (ImageView) findViewById(R.id.imageView4);
        nickname = (TextView) findViewById(R.id.nickname);
        rate = (RatingBar) findViewById(R.id.ratingBar2) ;
        favorite = (CheckBox) findViewById(R.id.star);
        ingredients = (TextView) findViewById(R.id.ingredientsRow);
        steps = (TextView) findViewById(R.id.stepsRow);

        FirebaseOperations.setUserContent(mFirebaseUser.getEmail(),nickname,userImage,RecipeContent.this);
        setTitle(tituloIntent);
        titulo.setText(tituloIntent);
        Image.download(this,photo,recipePhotoIntent);

        rate.setRating(rateIntent);
        favorite.setChecked(favoriteIntent);

        // create the TabHost that will contain the Tabs
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        populateTable();

        TabHost host = (TabHost)findViewById(R.id.tabHost);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Ingredients");
        spec.setContent(R.id.Ingredients);
        spec.setIndicator("Ingredients");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Preparation");
        spec.setContent(R.id.Preparation);
        spec.setIndicator("Preparation");
        host.addTab(spec);

        //Tab 3
        spec = host.newTabSpec("Nutrition");
        spec.setContent(R.id.Nutrition);
        spec.setIndicator("Nutrition");
        host.addTab(spec);

        //Tab 4
        spec = host.newTabSpec("Comments");
        spec.setContent(R.id.Comments);
        spec.setIndicator("Comments");
        host.addTab(spec);

        //Tab 5
        spec = host.newTabSpec("Video Help");
        spec.setContent(R.id.Video);
        spec.setIndicator("video");
        host.addTab(spec);


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_sign_out:
                mFirebaseAuth.signOut();
                startActivity(new Intent(this, LoginActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void clickProfile(View view){
        Intent intent = new Intent(this, Visit_person.class);
        intent.putExtra("userPhoto",userPhotoIntent);
        startActivity(intent);

    }
/*
    public void setFavorite (View view){

        FavoriteDao favoriteDao = daoSession.getFavoriteDao();
        CheckBox favorite = (CheckBox) view.findViewById(R.id.star);
        if(favorite.isChecked()) {
            favoriteDao.insert(new Favorite(null,userID,recipeID));
        }
        else if(!favorite.isChecked()){
            favoriteDao.deleteByKey(findFavorite(userID,recipeID));
        }
    }

    private Long findFavorite(Long userId, Long receiptID){

        FavoriteDao favoriteDao= daoSession.getFavoriteDao();
        QueryBuilder qb = favoriteDao.queryBuilder();
        qb.and(FavoriteDao.Properties.UserId.eq(userId),FavoriteDao.Properties.ReceiptId.eq(receiptID));
        List<Favorite> favorites = qb.list();
        return favorites.get(0).getId();
    }*/

    private void populateTable(){
        int i = 1;
        String [] split = ingredientsIntent.split(";");
        String [] split2 = stepsIntent.split(";");
        StringBuilder sb = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        for (String s:split) {
            if(i!=split.length)
                sb.append(i + ": " + s +'\n');
            i++;
        }
        i = 1;
        for (String s:split2) {
            if(i!=split.length)
                sb2.append(i + ": " + s +'\n');
            i++;
        }
        ingredients.setText(sb.toString());
        steps.setText(sb2.toString());
    }
}