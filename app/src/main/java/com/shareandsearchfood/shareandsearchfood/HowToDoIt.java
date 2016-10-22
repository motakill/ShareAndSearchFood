package com.shareandsearchfood.shareandsearchfood;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;


/**
 * Created by david_000 on 16/10/2016.
 */

public class HowToDoIt extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_do_it);

    GridView gridview = (GridView) findViewById(R.id.gridview);
    gridview.setAdapter(new ImageAdapter(this));

    gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View v,
        int position, long id) {
            Toast.makeText(HowToDoIt.this, "" + position,
                    Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), HowToDoItOption.class));
        }
    });


}
}
