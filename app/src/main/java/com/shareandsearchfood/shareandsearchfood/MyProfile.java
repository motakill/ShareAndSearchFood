package com.shareandsearchfood.shareandsearchfood;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.shareandsearchfood.login.App;
import com.shareandsearchfood.login.DaoSession;
import com.shareandsearchfood.login.Session;
import com.shareandsearchfood.login.User;
import com.shareandsearchfood.login.UserDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by david_000 on 13/10/2016.
 */

public class MyProfile extends NavBar {
    private LinearLayout mLayout, mLayout2;
    private EditText mEditText, mEditText2;
    private Button mButton, mButton2;
    private Session session;

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

        TabHost host = (TabHost)findViewById(R.id.tabHost);
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
        mEditText = (EditText) findViewById(R.id.Ingredientes);
        mButton = (Button) findViewById(R.id.moreIngredients);
        mButton.setOnClickListener(onClick());
        TextView textView = new TextView(this);
        textView.setText("More ingredients");

        mLayout2 = (LinearLayout) findViewById(R.id.layoutSteps);
        mEditText2 = (EditText) findViewById(R.id.Step_by_Step);
        mButton2 = (Button) findViewById(R.id.moreSteps);
        mButton2.setOnClickListener(onClick());
        TextView textView2 = new TextView(this);
        textView2.setText("More Steps");

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
    private void setPhoto()throws IOException {
        String photoUri = getPhotoUri(session.getEmail());
        ImageView photo = (ImageView) findViewById(R.id.profileImage);

        if (photoUri != null) {
            URL url = new URL(photoUri);
            Bitmap myBitmap = BitmapFactory.decodeStream(url.openStream());
            photo.setImageBitmap(myBitmap);
        } else
            photo.setImageResource(R.drawable.com_facebook_profile_picture_blank_square);
    }
}
