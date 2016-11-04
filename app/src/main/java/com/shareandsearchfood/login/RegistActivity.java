package com.shareandsearchfood.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.shareandsearchfood.shareandsearchfood.MenuActivity;
import com.shareandsearchfood.shareandsearchfood.R;

public class RegistActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
    }

    public void signIn(View view){
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);

    }
}
