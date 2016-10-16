package com.shareandsearchfood.shareandsearchfood;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TabHost;
import android.widget.Toast;

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
        spec = host.newTabSpec("Contacts");
        spec.setContent(R.id.Contacts);
        spec.setIndicator("Contacts");
        host.addTab(spec);

        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapterMenus(this));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(Restaurant.this, "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });

        GridView gridview1 = (GridView) findViewById(R.id.gridview1);
        gridview1.setAdapter(new ImageAdapterRestaurantPhotos(this));

        gridview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(Restaurant.this, "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });

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
