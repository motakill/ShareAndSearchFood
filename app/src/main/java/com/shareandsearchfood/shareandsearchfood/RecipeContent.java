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
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TabHost;
import android.widget.TextView;

import com.shareandsearchfood.login.App;
import com.shareandsearchfood.login.DaoSession;

import org.greenrobot.greendao.query.QueryBuilder;

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
    private DaoSession daoSession;
    private Long userID;
    private Long recipeID;
    private String userPhotoIntent;
    private int flag;
    private String ingredientsIntent;
    private String stepsIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        daoSession = ((App) getApplication()).getDaoSession();

        setContentView(R.layout.activity_recipe_content);
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

        Intent intent = getIntent();
        userPhotoIntent = intent.getStringExtra("userPhoto");
        String recipePhotoIntent = intent.getStringExtra("recipePhoto");
        String userNicknameIntent = intent.getStringExtra("nickname");
        String tituloIntent = intent.getStringExtra("recipeTitle");
        Boolean favoriteIntent = intent.getBooleanExtra("favorite",false);
        ingredientsIntent = intent.getStringExtra("ingredients");
        stepsIntent = intent.getStringExtra("steps");
        flag = intent.getIntExtra("flag",-1);
        int rateIntent = intent.getIntExtra("rating",0);
        userID = intent.getLongExtra("userID",-1);
        recipeID = intent.getLongExtra("recipeID",-1);

        setTitle(tituloIntent);

        titulo = (TextView) findViewById(R.id.titulo);
        photo = (ImageView) findViewById(R.id.imageView6);
        userImage = (ImageView) findViewById(R.id.imageView4);
        nickname = (TextView) findViewById(R.id.nickname);
        rate = (RatingBar) findViewById(R.id.ratingBar2) ;
        favorite = (CheckBox) findViewById(R.id.star);
        ingredients = (TextView) findViewById(R.id.ingredientsRow);
        steps = (TextView) findViewById(R.id.stepsRow);

        titulo.setText(tituloIntent);
        photo.setImageURI(Uri.parse(recipePhotoIntent));
        nickname.setText(userNicknameIntent);
        rate.setRating(rateIntent);
        favorite.setChecked(favoriteIntent);

        try {
            if (flag == 1) {
                URL url = new URL(userPhotoIntent);
                Bitmap myBitmap = BitmapFactory.decodeStream(url.openStream());
                userImage.setImageBitmap(myBitmap);
            } else if (flag == 0) {
                userImage.setImageURI(Uri.parse(userPhotoIntent));
            } else
                userImage.setImageResource(R.drawable.com_facebook_profile_picture_blank_square);
        }catch (IOException w){}


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
    public void clickProfile(View view){
        Intent intent = new Intent(this, Visit_person.class);
        intent.putExtra("userPhoto",userPhotoIntent);
        intent.putExtra("flag",flag);
        startActivity(intent);

    }

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
    }

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