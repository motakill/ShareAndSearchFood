package com.shareandsearchfood.shareandsearchfood;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Visit_person extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_person);
    }
    public void clickRecipe(View view){
        Intent intent = new Intent(this, RecipeContent.class);
        startActivity(intent);

    }
}
