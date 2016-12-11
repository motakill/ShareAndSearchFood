package com.shareandsearchfood.shareandsearchfood;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;
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
import com.shareandsearchfood.Adapters.CommentsRecyclerViewAdapter;
import com.shareandsearchfood.Adapters.VideoRecyclerViewAdapter;
import com.shareandsearchfood.ParcelerObjects.Comments;
import com.shareandsearchfood.ParcelerObjects.Recipe;
import com.shareandsearchfood.Utils.Constants;
import com.shareandsearchfood.Utils.FirebaseOperations;
import com.shareandsearchfood.Utils.Tools;
import com.shareandsearchfood.Login.LoginActivity;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by david_000 on 16/10/2016.
 */

public class RecipeContent extends NavBar {
    public TextView titulo;
    public ImageView photo;
    public TextView timestamp;
    public ImageView userImage ;
    public TextView nickname;
    private TextView ingredients;
    private TextView steps;
    private RatingBar rate;
    private CheckBox favorite;
    private String recipeId;
    private String userID;
    private String ingredientsIntent;
    private String stepsIntent;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private Boolean favoriteIntent;
    private String recipePhotoIntent;
    private String tituloIntent;
    private int rateIntent;
    private int statusIntent;
    private String dateIntent;
    private static final int PICK_IMAGE = 100;
    private static final int PICK_VIDEO = 100;
    private Uri photoRecipe;
    private TextView comment;
    private TextView comment2;
    private List<Comments> mComments;
    private List<Comments> mComments2;
    private RecyclerView mRecyclerView2;
    private RecyclerView mRecyclerView;
    private CommentsRecyclerViewAdapter mCAdapter;
    private VideoRecyclerViewAdapter mCAdapter2;
    private ImageView commetUserPhoto;
    private ImageView commetUserPhoto2;
    private Uri videoComment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_recipe_content);

        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        Intent intent = getIntent();
        recipeId = FirebaseOperations.encodeKey(intent.getStringExtra("recipeID"));
        recipePhotoIntent = intent.getStringExtra("recipePhoto");
        tituloIntent = intent.getStringExtra("recipeTitle");
        favoriteIntent = intent.getBooleanExtra("favorite",false);
        ingredientsIntent = intent.getStringExtra("ingredients");
        stepsIntent = intent.getStringExtra("steps");
        rateIntent = intent.getIntExtra("rating",0);
        userID = intent.getStringExtra("userID");
        statusIntent = intent.getIntExtra("status",0);
        dateIntent = intent.getStringExtra("date");

        titulo = (TextView) findViewById(R.id.titulo);
        photo = (ImageView) findViewById(R.id.imageView6);
        rate = (RatingBar) findViewById(R.id.ratingBar2) ;
        favorite = (CheckBox) findViewById(R.id.star);
        ingredients = (TextView) findViewById(R.id.ingredientsRow);
        steps = (TextView) findViewById(R.id.stepsRow);
        comment = (TextView) findViewById(R.id.editText3);
        comment2 = (TextView) findViewById(R.id.editText32);
        commetUserPhoto = (ImageView) findViewById(R.id.imageViewComments) ;
        commetUserPhoto2 = (ImageView) findViewById(R.id.imageViewComments2) ;

        setTitle(tituloIntent);
        Tools.ImageDownload(this,photo,recipePhotoIntent);

        rate.setRating(rateIntent);
        favorite.setChecked(favoriteIntent);

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

        populateTable();

        TabHost host = (TabHost)findViewById(R.id.tabHost);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Ingredients");
        spec.setContent(R.id.Ingredients);
        spec.setIndicator("Ingredients");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Preparation");
        spec.setContent(R.id.Preparation);
        spec.setIndicator("Preparation");
        host.addTab(spec);

        //Tab 4
        spec = host.newTabSpec("Comments");
        spec.setContent(R.id.Comments);
        spec.setIndicator("Comments");
        host.addTab(spec);

        //Tab 5
        spec = host.newTabSpec("Video");
        spec.setContent(R.id.Video);
        spec.setIndicator("Video");
        host.addTab(spec);

        final DatabaseReference userRef = FirebaseDatabase
                .getInstance()
                .getReference(Constants.FIREBASE_CHILD_RECIPES);

        mComments = new ArrayList<>();
        FirebaseOperations.setUserContent(mFirebaseUser.getEmail(),null,commetUserPhoto
                                ,RecipeContent.this);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerComents);
        mCAdapter = new CommentsRecyclerViewAdapter(mComments,RecipeContent.this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(RecipeContent.this));
        mRecyclerView.setAdapter(mCAdapter);
        userRef.child(recipeId).child(Constants.FIREBASE_CHILD_COMENTS).getRef()
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                            try{
                                Comments model = dataSnapshot.getValue(Comments.class);
                                if(model.getVideo() == null) {
                                    mComments.add(model);
                                    mCAdapter.notifyItemInserted(mComments.size() - 1);
                                }
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

        final DatabaseReference userRef2 = FirebaseDatabase
                .getInstance()
                .getReference(Constants.FIREBASE_CHILD_RECIPES);

        mComments2 = new ArrayList<>();
        mRecyclerView2 = (RecyclerView) findViewById(R.id.recyclerComents2);
        FirebaseOperations.setUserContent(mFirebaseUser.getEmail(),null,commetUserPhoto2
                                ,RecipeContent.this);
        mCAdapter2 = new VideoRecyclerViewAdapter(mComments2,RecipeContent.this);
        mRecyclerView2.setLayoutManager(new LinearLayoutManager(RecipeContent.this));
        mRecyclerView2.setAdapter(mCAdapter2);
        userRef2.child(recipeId).child(Constants.FIREBASE_CHILD_COMENTS).getRef()
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                            try{
                                Comments model = dataSnapshot.getValue(Comments.class);
                                if(model.getVideo() != null) {
                                    mComments2.add(model);
                                    mCAdapter2.notifyItemInserted(mComments2.size() - 1);
                                }
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            try{
                Context context = getApplicationContext();
                photoRecipe = data.getData();

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), photoRecipe);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 0, bytes);
                String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
                photoRecipe = Uri.parse(path);
                Toast.makeText(RecipeContent.this, "Photo added",Toast.LENGTH_SHORT).show();
            }catch (Exception e){}

        }
        if (resultCode == RESULT_OK && requestCode == PICK_VIDEO) {
            try{
                videoComment = data.getData();
                Toast.makeText(RecipeContent.this, "Video added",Toast.LENGTH_SHORT).show();
            }catch (Exception e){}

        }

    }
    public void clickProfile(View view){
        Intent intent = new Intent(this, Visit_person.class);
        intent.putExtra("userID",userID);
        startActivity(intent);
    }
    public void setFavoriteStatus(View view){
        FirebaseOperations.setFavoriteStatus(mFirebaseUser.getEmail(),
                new Recipe(tituloIntent, ingredientsIntent, stepsIntent, recipePhotoIntent,
                null,statusIntent, mFirebaseUser.getEmail(), dateIntent,rateIntent, favoriteIntent,recipeId));

    }
    private void populateTable(){
        int i = 1;
        String [] split = ingredientsIntent.split(";");
        String [] split2 = stepsIntent.split(";");
        StringBuilder sb = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        for (String s:split) {
            if(i!=split.length)
                sb.append(i + ": " + s +'\n');
            i++;
        }
        i = 1;
        for (String s:split2) {
            if(i!=split.length)
                sb2.append(i + ": " + s +'\n');
            i++;
        }
        ingredients.setText(sb.toString());
        steps.setText(sb2.toString());
    }
    public void openGalleryContent(View v) {
        Intent gallery =
                new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }
    public void openGalleryVideoContent(View v) {
        Intent gallery =
                new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Video.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_VIDEO);
    }
    public void sendComment(View view){
        FirebaseOperations.insertComment(this,recipeId,userID,comment,photoRecipe,Constants.FIREBASE_CHILD_RECIPES);
        photoRecipe = null;
    }
    public void sendCommentVideo(View view){
        FirebaseOperations.storeCommnetVideoToFirebase(videoComment,userID,comment2,
                recipeId,this,Constants.FIREBASE_CHILD_RECIPES);
        videoComment = null;
    }
}