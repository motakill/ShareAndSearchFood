package com.shareandsearchfood.shareandsearchfood;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;

/**
 * Created by david_000 on 16/10/2016.
 */

public class HowToDoItOption extends NavBar {
    public TextView titulo;
    public TextView obs;
    public ImageView photo;
    public TextView timestamp;
    public ImageView userImage ;
    public TextView nickname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_do_it_option);

        Toolbar toolbar2 = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar2);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar2, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        Intent intent = getIntent();
        String userIdIntent = intent.getStringExtra("htoUserName");
        String tituloIntent = intent.getStringExtra("htoTitulo");
        String obsIntent = intent.getStringExtra("htoObs");
        String dateIntent = intent.getStringExtra("htoDate");
        String photoIntent = intent.getStringExtra("htoPhoto");
        int flag = intent.getIntExtra("htoFlag",-1);
        String userPhotoIntent = intent.getStringExtra("htoUserPhoto");

        titulo = (TextView) findViewById(R.id.titulo);
        obs = (TextView) findViewById(R.id.textView6);
        photo = (ImageView) findViewById(R.id.imageView6);
        timestamp = (TextView) findViewById(R.id.data1);
        userImage = (ImageView) findViewById(R.id.imageView4);
        nickname = (TextView) findViewById(R.id.nickname);

        titulo.setText(tituloIntent);
        obs.setText(obsIntent);
        photo.setImageURI(Uri.parse(photoIntent));
        timestamp.setText(dateIntent);
        nickname.setText(userIdIntent);

        try {
            if (userIdIntent != null && flag == 1) {
                URL url = new URL(userPhotoIntent);
                Bitmap myBitmap = BitmapFactory.decodeStream(url.openStream());
                userImage.setImageBitmap(myBitmap);
            } else if (userIdIntent != null && flag == 0) {
                userImage.setImageURI(Uri.parse(userPhotoIntent));
            } else
                userImage.setImageResource(R.drawable.com_facebook_profile_picture_blank_square);
        }catch (IOException w){}


        TabHost host = (TabHost)findViewById(R.id.tabHost);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Comments");
        spec.setContent(R.id.Comments);
        spec.setIndicator("Comments");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Videos");
        spec.setContent(R.id.Videos);
        spec.setIndicator("Videos");
        host.addTab(spec);


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
}
