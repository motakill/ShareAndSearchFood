package com.shareandsearchfood.shareandsearchfood;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TabHost;

/**
 * Created by david_000 on 13/10/2016.
 */

public class MyProfile extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        // create the TabHost that will contain the Tabs
        TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);


        TabHost.TabSpec tab1 = tabHost.newTabSpec("First Tab");
        TabHost.TabSpec tab2 = tabHost.newTabSpec("Second Tab");
        TabHost.TabSpec tab3 = tabHost.newTabSpec("Third tab");

        // Set the Tab name and Activity
        // that will be opened when particular Tab will be selected
        tab1.setIndicator("Tab1");
        tab1.setContent(new Intent(this,Tab1Activity.class));

        tab2.setIndicator("Tab2");
        tab2.setContent(new Intent(this,Tab2Activity.class));

        tab3.setIndicator("Tab3");
        tab3.setContent(new Intent(this,Tab3Activity.class));

        /** Add the tabs  to the TabHost to display. */
        tabHost.addTab(tab1);
        tabHost.addTab(tab2);
        tabHost.addTab(tab3);
    }
}