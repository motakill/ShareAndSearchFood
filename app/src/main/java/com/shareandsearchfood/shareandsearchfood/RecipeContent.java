package com.shareandsearchfood.shareandsearchfood;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TabHost;

/**
 * Created by david_000 on 16/10/2016.
 */

public class RecipeContent extends NavBar {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_content);
        // create the TabHost that will contain the Tabs
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        TabHost host = (TabHost)findViewById(R.id.tabHost);
        host.setup();
        // create the TabHost that will contain the Tabs

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
        startActivity(intent);

    }
}
