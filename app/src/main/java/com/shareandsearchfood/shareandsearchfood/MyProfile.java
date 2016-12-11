package com.shareandsearchfood.shareandsearchfood;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shareandsearchfood.Adapters.FavoriteRecyclerViewAdapter;
import com.shareandsearchfood.Adapters.MyPubsRecyclerViewAdapter;
import com.shareandsearchfood.ParcelerObjects.Recipe;
import com.shareandsearchfood.Utils.Constants;
import com.shareandsearchfood.Utils.FirebaseOperations;
import com.shareandsearchfood.Login.LoginActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by david_000 on 13/10/2016.
 */

public class MyProfile extends NavBar {

    private Uri photoReceipt;
    private TextView photoName;
    private CheckBox favorite;
    private static final int PICK_IMAGE = 100;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private List<Recipe> mRecipe;
    private List<Recipe> mFavRecipe;
    private RecyclerView mRecyclerView;
    private MyPubsRecyclerViewAdapter mAdapter;
    private FavoriteRecyclerViewAdapter mFAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        setContentView(R.layout.activity_my_profile);

        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        TextView textView3 = (TextView) findViewById(R.id.username);
        ImageView photo = (ImageView) findViewById(R.id.profileImage);
        TextView totalRecipes = (TextView) findViewById(R.id.totalRecipes);
        TextView totalFollowers = (TextView) findViewById(R.id.totalFollowers);
        TextView totalFollowing = (TextView) findViewById(R.id.totalFollowings);

        FirebaseOperations.setUserContent(mFirebaseUser.getEmail(),textView3,photo,MyProfile.this);
        FirebaseOperations.totalRecipes(mFirebaseUser.getEmail(),totalRecipes);
        FirebaseOperations.totalFF(mFirebaseUser.getEmail(),totalFollowers,totalFollowing);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        final DatabaseReference userRef = FirebaseDatabase
                .getInstance()
                .getReference(Constants.FIREBASE_CHILD_USERS);

        TextView myPubs = (TextView) findViewById(R.id.myPubs);
        myPubs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecipe = new ArrayList<>();
                mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(MyProfile.this));
                mAdapter = new MyPubsRecyclerViewAdapter(mRecipe,MyProfile.this);
                mRecyclerView.setAdapter(mAdapter);
                userRef.child(FirebaseOperations.encodeKey(mFirebaseUser.getEmail()))
                        .child(Constants.FIREBASE_CHILD_RECIPES).getRef()
                        .addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                                    try{
                                        Recipe model = dataSnapshot.getValue(Recipe.class);
                                        mRecipe.add(model);
                                        mAdapter.notifyItemInserted(mRecipe.size() - 1);
                                    } catch (Exception ex) {
                                    }
                                }
                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {

                            }

                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onCancelled(DatabaseError firebaseError) {

                            }
                        });

            }
        });

        TextView myFav = (TextView) findViewById(R.id.myFav);
        myFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFavRecipe = new ArrayList<>();
                mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(MyProfile.this));
                mFAdapter = new FavoriteRecyclerViewAdapter(mFavRecipe,MyProfile.this);
                mRecyclerView.setAdapter(mFAdapter);
                userRef.child(FirebaseOperations.encodeKey(mFirebaseUser.getEmail()))
                        .child(Constants.FIREBASE_CHILD_FAVORITES).getRef()
                        .addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                                    try{
                                        Recipe model = dataSnapshot.getValue(Recipe.class);
                                        mFavRecipe.add(model);
                                        mFAdapter.notifyItemInserted(mFavRecipe.size() - 1);
                                    } catch (Exception ex) {
                                    }
                                }
                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {

                            }

                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onCancelled(DatabaseError firebaseError) {

                            }
                        });

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_sign_out:
                    mFirebaseAuth.signOut();
                startActivity(new Intent(this, LoginActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void clickRecipe(View view){
        startActivity(new Intent(this, RecipeContent.class));

    }
    public void clickProfile(View view){
        startActivity(new Intent(this, Visit_person.class));

    }
    public void share(View view){
        startActivity(new Intent(this, ShareContent.class));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            photoReceipt = data.getData();
            photoName = (TextView) findViewById(R.id.photoName);
            photoName.setText(photoReceipt.getPath());
        }
    }

}
