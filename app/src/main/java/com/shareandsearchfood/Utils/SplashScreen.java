package com.shareandsearchfood.Utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.shareandsearchfood.Login.LoginActivity;
import com.shareandsearchfood.shareandsearchfood.MyProfile;
import com.shareandsearchfood.shareandsearchfood.R;

/**
 * Created by tiagomota on 08/12/16.
 */

public class SplashScreen extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(3000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    // Initialize Firebase Auth
                    FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
                    FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                    if (mFirebaseUser == null) {
                        // Not signed in, launch the Sign In activity
                        startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                        finish();
                        return;
                    }
                    else
                    startActivity(new Intent(SplashScreen.this,MyProfile.class));
                }
            }
        };
        timerThread.start();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }
}
