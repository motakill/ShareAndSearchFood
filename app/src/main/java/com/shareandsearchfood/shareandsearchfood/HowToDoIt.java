package com.shareandsearchfood.shareandsearchfood;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.shareandsearchfood.Adapters.ImageAdapterRestaurantPhotos;
import com.shareandsearchfood.Fragments.CookBookFragment;
import com.shareandsearchfood.Fragments.HowToDoItFragment;
import com.shareandsearchfood.login.Receipt;


/**
 * Created by david_000 on 16/10/2016.
 */

public class HowToDoIt extends NavBar {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_do_it);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /**
        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapterRestaurantPhotos(this));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(HowToDoIt.this, "" + position,
                        Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), HowToDoItOption.class));
            }
        });
         */

        //cria a view das receitas criadas
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_how_to_do_it, new HowToDoItFragment());
        ft.addToBackStack(null).commit();

}
    public void shareHowToDoIt (View view){
        Intent intent = new Intent(this, ShareHowToDoItOption.class);
        startActivity(intent);
    }

    public void OnListFragmentInteractionListenerHowToDoIT (HowToDoItTable position) {
        // The user selected the headline of an article from the HeadlinesFragment
        // Do something here to display that article
    }


}
