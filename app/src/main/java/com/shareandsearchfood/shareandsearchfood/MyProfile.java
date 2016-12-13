package com.shareandsearchfood.shareandsearchfood;

import android.content.ClipData;
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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shareandsearchfood.Adapters.FavoriteRecyclerViewAdapter;
import com.shareandsearchfood.Adapters.MyPubsRecyclerViewAdapter;
import com.shareandsearchfood.EatTime.SearchPlaces;
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
    private List<Recipe> mFeed;
    private List<Recipe> mRecipe;
    private List<Recipe> mFavRecipe;
    private RecyclerView mRecyclerView;
    private RecyclerView mRecyclerView2;
    private RecyclerView mRecyclerView3;
    private FavoriteRecyclerViewAdapter mFeedAdapter;
    private MyPubsRecyclerViewAdapter mAdapter;
    private MyPubsRecyclerViewAdapter mAdapter2;
    private FavoriteRecyclerViewAdapter mFAdapter;
   // private View search;

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
        final ImageView meatBadge = (ImageView) findViewById(R.id.meatBadge);
        final ImageView fishBadge = (ImageView) findViewById(R.id.fishBadge);
        final ImageView cakeBadge = (ImageView) findViewById(R.id.cakeBadge);
        final ImageView drinkBadge = (ImageView) findViewById(R.id.drinkBadge);
        final ImageView snackBadge = (ImageView) findViewById(R.id.snackBadge);
        final ImageView seafoodBadge = (ImageView) findViewById(R.id.seafoodBadge);
        final ImageView followersBadge = (ImageView) findViewById(R.id.followersBadge);
        final ImageView pubsBadge = (ImageView) findViewById(R.id.pubsBadge);
        final ImageView veganBadge = (ImageView) findViewById(R.id.veganfoodBadge);

        FirebaseOperations.setUserContent(mFirebaseUser.getEmail(),textView3,photo,MyProfile.this);
        FirebaseOperations.totalRecipes(mFirebaseUser.getEmail(),totalRecipes);
        FirebaseOperations.totalFF(mFirebaseUser.getEmail(),totalFollowers,totalFollowing);

        // create the TabHost that will contain the Tabs
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final TabHost host = (TabHost)findViewById(R.id.tabHost);
        host.setup();

        TabHost.TabSpec spec;

        //Tab 1
        spec = host.newTabSpec("Feed");
        spec.setContent(R.id.Feed);
        spec.setIndicator("Feed");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("MyPubs");
        spec.setContent(R.id.MyPubs);
        spec.setIndicator("MyPubs");
        host.addTab(spec);

        //Tab 3
        spec = host.newTabSpec("MyFavorites");
        spec.setContent(R.id.MyFavorites);
        spec.setIndicator("MyFavorites");
        host.addTab(spec);

        //Tab 4
        spec = host.newTabSpec("Badges");
        spec.setContent(R.id.Badges);
        spec.setIndicator("Badges");
        host.addTab(spec);

        host.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                final DatabaseReference userRef = FirebaseDatabase
                        .getInstance()
                        .getReference(Constants.FIREBASE_CHILD_USERS);

                final DatabaseReference recipeRef = FirebaseDatabase
                        .getInstance()
                        .getReference(Constants.FIREBASE_CHILD_RECIPES);

                final DatabaseReference userRef2 = FirebaseDatabase
                        .getInstance()
                        .getReference(Constants.FIREBASE_CHILD_USERS);

                mFeed = new ArrayList<>();
                final List<String> friends = FirebaseOperations.getFriends(mFirebaseUser.getEmail());
                mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(MyProfile.this));
                mFeedAdapter = new FavoriteRecyclerViewAdapter(mFeed, MyProfile.this);
                mRecyclerView.setAdapter(mFeedAdapter);

                    recipeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(host.getCurrentTabTag().equals("Feed"))
                                Toast.makeText(MyProfile.this
                                    , "Feed updating...", Toast.LENGTH_SHORT).show();
                            if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                                try {
                                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                                        Recipe model = child.getValue(Recipe.class);
                                        if (friends.contains(model.getUserId()))
                                            mFeed.add(model);
                                    }
                                    mFeedAdapter.notifyItemInserted(0);
                                } catch (Exception ex) {
                                }
                            }
                            if (mFeed.isEmpty() && host.getCurrentTabTag().equals("Feed"))
                                Toast.makeText(MyProfile.this
                                        , "Nothing to show :(", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(DatabaseError firebaseError) {

                        }
                    });

                mRecipe = new ArrayList<>();
                mRecyclerView2 = (RecyclerView) findViewById(R.id.recyclerView1);
                mRecyclerView2.setLayoutManager(new LinearLayoutManager(MyProfile.this));
                mAdapter2 = new MyPubsRecyclerViewAdapter(mRecipe, MyProfile.this);
                mRecyclerView2.setAdapter(mAdapter2);
                userRef.child(FirebaseOperations.encodeKey(mFirebaseUser.getEmail()))
                        .child(Constants.FIREBASE_CHILD_RECIPES).getRef()
                        .addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                                    try {
                                        Recipe model = dataSnapshot.getValue(Recipe.class);
                                        mRecipe.add(model);
                                        mAdapter2.notifyItemInserted(mRecipe.size() - 1);
                                    } catch (Exception ex) {
                                    }
                                }
                                if (mRecipe.isEmpty())
                                    Toast.makeText(MyProfile.this
                                            , "Nothing to show :(", Toast.LENGTH_SHORT).show();
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

                mFavRecipe = new ArrayList<>();
                mRecyclerView3 = (RecyclerView) findViewById(R.id.recyclerView2);
                mRecyclerView3.setLayoutManager(new LinearLayoutManager(MyProfile.this));
                mFAdapter = new FavoriteRecyclerViewAdapter(mFavRecipe, MyProfile.this);
                mRecyclerView3.setAdapter(mFAdapter);
                userRef2.child(FirebaseOperations.encodeKey(mFirebaseUser.getEmail()))
                        .child(Constants.FIREBASE_CHILD_FAVORITES).getRef()
                        .addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                                    try {
                                        Recipe model = dataSnapshot.getValue(Recipe.class);
                                        mFavRecipe.add(model);
                                        mFAdapter.notifyItemInserted(mFavRecipe.size() - 1);

                                    } catch (Exception ex) {
                                    }
                                }
                                if (mFavRecipe.isEmpty())
                                    Toast.makeText(MyProfile.this
                                            , "Nothing to show :(", Toast.LENGTH_SHORT).show();
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

                if (host.getCurrentTabTag().equals("Badges")){
                    FirebaseOperations.meatBadge(mFirebaseUser.getEmail(),meatBadge);
                    FirebaseOperations.fishBadge(mFirebaseUser.getEmail(),fishBadge);
                    FirebaseOperations.cakesBadge(mFirebaseUser.getEmail(),cakeBadge);
                    FirebaseOperations.drinksBadge(mFirebaseUser.getEmail(),drinkBadge);
                    FirebaseOperations.veganBadge(mFirebaseUser.getEmail(),veganBadge);
                    FirebaseOperations.snacksBadge(mFirebaseUser.getEmail(),snackBadge);
                    FirebaseOperations.followersBadge(mFirebaseUser.getEmail(),followersBadge);
                    FirebaseOperations.pubsBadge(mFirebaseUser.getEmail(),pubsBadge);
                    FirebaseOperations.seafoodBadge(mFirebaseUser.getEmail(),seafoodBadge);
                }
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
                Intent intent = new Intent(this, LoginActivity.class);
                intent.putExtra("finish", true); // if you are checking for this in your other Activities
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
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
