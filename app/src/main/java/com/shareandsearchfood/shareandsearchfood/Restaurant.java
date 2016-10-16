package com.shareandsearchfood.shareandsearchfood;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TabHost;

public class Restaurant extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);
        // create the TabHost that will contain the Tabs
        TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);


        TabHost host = (TabHost)findViewById(R.id.tabHost);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Menus");
        spec.setContent(R.id.Menus);
        spec.setIndicator("Menus");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Photos");
        spec.setContent(R.id.Photos);
        spec.setIndicator("Photos");
        host.addTab(spec);

        //Tab 3
        spec = host.newTabSpec("Adress");
        spec.setContent(R.id.Adress);
        spec.setIndicator("Adress");
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
