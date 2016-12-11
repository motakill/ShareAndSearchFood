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
import android.widget.ImageView;
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

public class HowToDoItOption extends NavBar {
    private TextView titulo;
    private TextView obs;
    private ImageView photo;
    private TextView timestamp;
    private ImageView userImage;
    private ImageView userImage2;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String userID;
    private TextView comment;
    private TextView comment2;
    private List<Comments> mComments;
    private List<Comments> mComments2;
    private RecyclerView mRecyclerView2;
    private RecyclerView mRecyclerView;
    private CommentsRecyclerViewAdapter mCAdapter;
    private VideoRecyclerViewAdapter mCAdapter2;
    private Uri videoComment;
    private static final int PICK_IMAGE = 100;
    private static final int PICK_VIDEO = 100;
    private Uri howToPhoto;
    private String howToID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_how_to_do_it_option);

        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        Toolbar toolbar2 = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar2);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar2, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        Intent intent = getIntent();
        String obsIntent = intent.getStringExtra("htoObs");
        String dateIntent = intent.getStringExtra("htoDate");
        String photoIntent = intent.getStringExtra("htoPhoto");
        howToID = intent.getStringExtra("htoID");

        obs = (TextView) findViewById(R.id.textView6);
        photo = (ImageView) findViewById(R.id.imageView6);
        timestamp = (TextView) findViewById(R.id.data1);
        userImage = (ImageView) findViewById(R.id.imageViewComments);
        userID = intent.getStringExtra("userID");
        userImage = (ImageView) findViewById(R.id.imageViewComments);
        userImage2 = (ImageView) findViewById(R.id.imageViewComments2);
        comment = (TextView) findViewById(R.id.editText3);
        comment2 = (TextView) findViewById(R.id.editText32);

        FirebaseOperations.setUserContent(mFirebaseUser.getEmail(),
                null,userImage,HowToDoItOption.this);
        FirebaseOperations.setUserContent(mFirebaseUser.getEmail(),
                null,userImage2,HowToDoItOption.this);

        obs.setText(obsIntent);
        timestamp.setText(dateIntent);

        Tools.ImageDownload(HowToDoItOption.this,photo,photoIntent);


        TabHost host = (TabHost)findViewById(R.id.tabHost);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Comments");
        spec.setContent(R.id.Comments);
        spec.setIndicator("Comments");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Videos");
        spec.setContent(R.id.Videos);
        spec.setIndicator("Videos");
        host.addTab(spec);
        final DatabaseReference userRef = FirebaseDatabase
                .getInstance()
                .getReference(Constants.FIREBASE_CHILD_HOWTO);

        mComments = new ArrayList<>();
        FirebaseOperations.setUserContent(mFirebaseUser.getEmail(),null,userImage
                ,HowToDoItOption.this);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerComents);
        mCAdapter = new CommentsRecyclerViewAdapter(mComments,HowToDoItOption.this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(HowToDoItOption.this));
        mRecyclerView.setAdapter(mCAdapter);
        userRef.child(howToID).child(Constants.FIREBASE_CHILD_COMENTS).getRef()
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
                .getReference(Constants.FIREBASE_CHILD_HOWTO);

        mComments2 = new ArrayList<>();
        mRecyclerView2 = (RecyclerView) findViewById(R.id.recyclerComents2);
        FirebaseOperations.setUserContent(mFirebaseUser.getEmail(),null,userImage2
                ,HowToDoItOption.this);
        mCAdapter2 = new VideoRecyclerViewAdapter(mComments2,HowToDoItOption.this);
        mRecyclerView2.setLayoutManager(new LinearLayoutManager(HowToDoItOption.this));
        mRecyclerView2.setAdapter(mCAdapter2);
        userRef2.child(howToID).child(Constants.FIREBASE_CHILD_COMENTS).getRef()
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
    public void clickProfile(View view){
        if(mFirebaseUser.getEmail().equals(userID))
            startActivity(new Intent(this,MyProfile.class));
        else {
            Intent intent = new Intent(this, Visit_person.class);
            intent.putExtra("userID", userID);
            startActivity(intent);
        }

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
                howToPhoto = data.getData();

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), howToPhoto);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 0, bytes);
                String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
                howToPhoto = Uri.parse(path);
                Toast.makeText(HowToDoItOption.this, "Photo added",Toast.LENGTH_SHORT).show();
            }catch (Exception e){}

        }
        if (resultCode == RESULT_OK && requestCode == PICK_VIDEO) {
            try{
                videoComment = data.getData();
                Toast.makeText(HowToDoItOption.this, "Video added",Toast.LENGTH_SHORT).show();
            }catch (Exception e){}

        }

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
        FirebaseOperations.insertComment(this,howToID,userID,comment,howToPhoto, Constants.FIREBASE_CHILD_HOWTO);
        howToPhoto = null;
    }
    public void sendCommentVideo(View view){
        FirebaseOperations.storeCommnetVideoToFirebase(videoComment,userID,comment2,
                howToID,this,Constants.FIREBASE_CHILD_HOWTO);
        videoComment = null;
    }
}
