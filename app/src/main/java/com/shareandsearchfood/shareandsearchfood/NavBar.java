package com.shareandsearchfood.shareandsearchfood;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.shareandsearchfood.login.LoginActivity;
import com.shareandsearchfood.settings.AboutSSFood;
import com.shareandsearchfood.settings.FeedBackSSFood;


/**
 * Created by david_000 on 24/10/2016.
 */

public class NavBar extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if(id == R.id.action_feedBack){
            Intent intent = new Intent(this, FeedBackSSFood.class);
            startActivity(intent);
            return true;
        }
        else if(id == R.id.action_about){
            Intent intent = new Intent(this, AboutSSFood.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_book) {
            startActivity(new Intent(getApplicationContext(), MenuActivity.class));
            return true;
        } else if (id == R.id.nav_profile) {
            startActivity(new Intent(getApplicationContext(), MyProfile.class));
            return true;
        } else if (id == R.id.nav_notebook) {
            startActivity(new Intent(getApplicationContext(), NotebookActivity.class));
            return true;
        } else if (id == R.id.nav_how) {
            startActivity(new Intent(getApplicationContext(), HowToDoIt.class));
            return true;
        } else if (id == R.id.nav_eatTime) {
            startActivity(new Intent(getApplicationContext(), Search_places.class));
            return true;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
