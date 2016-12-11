package com.shareandsearchfood.shareandsearchfood;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shareandsearchfood.Adapters.VisitPersonRecyclerViewAdapter;
import com.shareandsearchfood.ParcelerObjects.Recipe;
import com.shareandsearchfood.ParcelerObjects.User;
import com.shareandsearchfood.Utils.Constants;
import com.shareandsearchfood.Utils.FirebaseOperations;
import com.shareandsearchfood.Login.LoginActivity;

import java.util.ArrayList;
import java.util.List;

public class Visit_person extends NavBar {
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private List<Recipe> mRecipe;
    private RecyclerView mRecyclerView;
    private VisitPersonRecyclerViewAdapter mAdapter;
    private Boolean favoriteIntent;
    private String recipePhotoIntent;
    private String tituloIntent;
    private int rateIntent;
    private int statusIntent;
    private String dateIntent;
    public TextView titulo;
    public ImageView photo;
    public TextView timestamp;
    public TextView nickname;
    private Button button;
    private String recipeId;
    private String userID;
    private String ingredientsIntent;
    private String stepsIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_person);

        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ImageView userImage = (ImageView) findViewById(R.id.imageView2);


        Intent intent = getIntent();
        recipeId = intent.getStringExtra("recipeID");
        recipePhotoIntent = intent.getStringExtra("recipePhoto");
        tituloIntent = intent.getStringExtra("recipeTitle");
        favoriteIntent = intent.getBooleanExtra("favorite",false);
        ingredientsIntent = intent.getStringExtra("ingredients");
        stepsIntent = intent.getStringExtra("steps");
        rateIntent = intent.getIntExtra("rating",0);
        userID = intent.getStringExtra("userID");
        statusIntent = intent.getIntExtra("status",0);
        dateIntent = intent.getStringExtra("date");

        button = (Button) findViewById(R.id.followButton);
        FirebaseOperations.isFriend(mFirebaseUser.getEmail(),userID,button);
        FirebaseOperations.setUserContent(userID,null,userImage,Visit_person.this);
        setUserName(userID);

        TextView totalRecipes = (TextView) findViewById(R.id.totalRecipes);
        TextView totalFollowers = (TextView) findViewById(R.id.totalFollowers);
        TextView totalFollowing = (TextView) findViewById(R.id.totalFollowings);

        FirebaseOperations.totalRecipes(userID,totalRecipes);
        FirebaseOperations.totalFF(userID,totalFollowers,totalFollowing);

        //cria a view das receitas criadas
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecipe = new ArrayList<>();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(Visit_person.this));
        mAdapter = new VisitPersonRecyclerViewAdapter(mRecipe,Visit_person.this);
        mRecyclerView.setAdapter(mAdapter);

        DatabaseReference userRef = FirebaseDatabase
                .getInstance()
                .getReference(Constants.FIREBASE_CHILD_USERS);
        userRef.child(FirebaseOperations.encodeKey(userID))
                .child(Constants.FIREBASE_CHILD_RECIPES)
                .getRef().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                    try{
                        Recipe model = dataSnapshot.getValue(Recipe.class);
                        mRecipe.add(model);
                        mRecyclerView.scrollToPosition(mRecipe.size() - 1);
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
    public void clickRecipe(View view){
        Intent intent = new Intent(this, RecipeContent.class);
        intent.putExtra("favorite",favoriteIntent);
        startActivity(intent);

    }
    public void setFavoriteStatus(View view){
        FirebaseOperations.setFavoriteStatus(mFirebaseUser.getEmail(),
                new Recipe(tituloIntent, ingredientsIntent, stepsIntent, recipePhotoIntent,
                        null,statusIntent, mFirebaseUser.getEmail(), dateIntent,rateIntent, favoriteIntent,recipeId));

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
    public void setUserName(String email){
        DatabaseReference userRef = FirebaseDatabase
                .getInstance()
                .getReference(Constants.FIREBASE_CHILD_USERS);
        userRef.child(FirebaseOperations.encodeKey(email))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        setTitle(user.getUsername());
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }
    public void follow(View v){
        if(button.getText().toString().equals("Follow"))
            FirebaseOperations.followUser(mFirebaseUser.getEmail(), userID);
        else
            FirebaseOperations.unFollowUser(mFirebaseUser.getEmail(),userID);
        try {
            Thread.sleep(500);
            refresh();
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

    }
    public void refresh(){
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}
