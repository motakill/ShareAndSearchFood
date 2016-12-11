package com.shareandsearchfood.shareandsearchfood;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shareandsearchfood.Adapters.NoteBookRecyclerViewAdapter;
import com.shareandsearchfood.ParcelerObjects.Notebook;
import com.shareandsearchfood.Utils.Constants;
import com.shareandsearchfood.Utils.FirebaseOperations;
import com.shareandsearchfood.Login.LoginActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by david_000 on 14/10/2016.
 */

public class NotebookActivity extends NavBar{
    private EditText textIn;
    private RadioButton buttonAdd;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private List<Notebook> mNotes;
    private RecyclerView mRecyclerView;
    private NoteBookRecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_notebook);

        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerNoteBook);
        mNotes = new ArrayList<>();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new NoteBookRecyclerViewAdapter(mNotes);
        mRecyclerView.setAdapter(mAdapter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        textIn = (EditText)findViewById(R.id.textin);
        buttonAdd = (RadioButton)findViewById(R.id.add);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseOperations.insertNote(mFirebaseUser.getEmail(), textIn);
            }
        });

        DatabaseReference userRef = FirebaseDatabase
                .getInstance()
                .getReference(Constants.FIREBASE_CHILD_USERS);

        userRef.child(FirebaseOperations.encodeKey(mFirebaseUser.getEmail()))
                .child(Constants.FIREBASE_CHILD_NOTES).getRef()
                .addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                    try{
                        Notebook model = dataSnapshot.getValue(Notebook.class);
                        mNotes.add(model);
                        mRecyclerView.scrollToPosition(mNotes.size() - 1);
                        mAdapter.notifyItemInserted(mNotes.size() - 1);
                    } catch (Exception ex) {
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                    try{
                        Notebook model = dataSnapshot.getValue(Notebook.class);
                        mNotes.remove(model);
                        mRecyclerView.scrollToPosition(mNotes.size() - 1);
                        mAdapter.notifyItemRemoved(mNotes.size() - 1);
                    } catch (Exception ex) {
                    }
                }
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
}