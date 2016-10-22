package com.shareandsearchfood.shareandsearchfood;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;

/**
 * Created by david_000 on 13/10/2016.
 */

public class MyProfile extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        // create the TabHost that will contain the Tabs
        TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);


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
}
