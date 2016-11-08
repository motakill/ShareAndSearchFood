package com.shareandsearchfood.shareandsearchfood;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RatingBar;

import com.shareandsearchfood.Fragments.CookBookFragment;
import com.shareandsearchfood.login.Receipt;


public class MenuActivity extends NavBar implements CookBookFragment.OnListFragmentInteractionListenerCookBook{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook_book);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        RatingBar simpleRatingBar = (RatingBar) findViewById(R.id.ratingBarContentMenu5);
        // initiate a rating bar
        // int numberOfStars = simpleRatingBar.getNumStars(); // get total number of stars of rating bar

        //cria a view das receitas criadas
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_menu, new CookBookFragment());
        ft.addToBackStack(null).commit();
    }

    public void clickProfile(View view){
        Intent intent = new Intent(this, Visit_person.class);
        startActivity(intent);

    }
    public void clickRecipe(View view){
        Intent intent = new Intent(this, RecipeContent.class);
        startActivity(intent);

    }
    public void onListFragmentInteractionCookBook(Receipt position) {
        // The user selected the headline of an article from the HeadlinesFragment
        // Do something here to display that article
    }
}
