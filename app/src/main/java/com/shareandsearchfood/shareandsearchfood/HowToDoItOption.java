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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.shareandsearchfood.Utils.FirebaseOperations;
import com.shareandsearchfood.Utils.Image;
import com.shareandsearchfood.login.LoginActivity;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;

/**
 * Created by david_000 on 16/10/2016.
 */

public class HowToDoItOption extends NavBar {
    private TextView titulo;
    private TextView obs;
    private ImageView photo;
    private TextView timestamp;
    private ImageView userImage ;
    private TextView nickname;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_do_it_option);

        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

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
        String tituloIntent = intent.getStringExtra("htoTitulo");
        String obsIntent = intent.getStringExtra("htoObs");
        String dateIntent = intent.getStringExtra("htoDate");
        String photoIntent = intent.getStringExtra("htoPhoto");

        titulo = (TextView) findViewById(R.id.titulo);
        obs = (TextView) findViewById(R.id.textView6);
        photo = (ImageView) findViewById(R.id.imageView6);
        timestamp = (TextView) findViewById(R.id.data1);
        userImage = (ImageView) findViewById(R.id.imageView4);
        nickname = (TextView) findViewById(R.id.nickname);

        FirebaseOperations.setUserContent(FirebaseOperations.encodeKey(mFirebaseUser.getEmail()),
                nickname,userImage,HowToDoItOption.this);
        titulo.setText(tituloIntent);
        obs.setText(obsIntent);
        timestamp.setText(dateIntent);

        Image.download(HowToDoItOption.this,photo,photoIntent);


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
        switch (item.getItemId()) {
            case R.id.action_sign_out:
                mFirebaseAuth.signOut();
                startActivity(new Intent(this, LoginActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
