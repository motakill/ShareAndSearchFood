package com.shareandsearchfood.shareandsearchfood;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioButton;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shareandsearchfood.Adapters.FirebaseNotebookViewHolder;
import com.shareandsearchfood.ParcelerObjects.Notebook;
import com.shareandsearchfood.Utils.Constants;
import com.shareandsearchfood.Utils.FirebaseOperations;
import com.shareandsearchfood.Login.LoginActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by david_000 on 14/10/2016.
 */

public class NotebookActivity extends NavBar implements SwipeRefreshLayout.OnRefreshListener{
    private EditText textIn;
    private RadioButton buttonAdd;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private List<Notebook> mNotes;
    private RecyclerView mRecyclerView;
    //private NoteBookRecyclerViewAdapter mAdapter;
    private FirebaseRecyclerAdapter<Notebook,FirebaseNotebookViewHolder>
            mFirebaseAdapter;
    private DatabaseReference mDatabase;

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
        mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = FirebaseDatabase
                .getInstance()
                .getReference(Constants.FIREBASE_CHILD_USERS);


        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerNoteBook);
        mNotes = new ArrayList<>();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //mAdapter = new NoteBookRecyclerViewAdapter(mNotes);

        mFirebaseAdapter = new FirebaseRecyclerAdapter<Notebook,FirebaseNotebookViewHolder>(
                Notebook.class,
                R.layout.row_notebook,
                FirebaseNotebookViewHolder.class,
                userRef.child(FirebaseOperations.encodeKey(mFirebaseUser.getEmail()))
                        .child(Constants.FIREBASE_CHILD_NOTES).getRef().orderByChild("date")) {

            @Override
            protected void populateViewHolder(final FirebaseNotebookViewHolder holder,
                                              final Notebook note, int position) {
                holder.mItem = note;
                holder.mTextView.setText(note.getNote());
                holder.check.setChecked(note.getStatus());
                holder.check.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseOperations.updateNote(mFirebaseUser.getEmail(),note.getDate()
                                ,holder.check.isChecked());
                    }
                });
                holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseOperations.removeNote(mFirebaseUser.getEmail(),note.getDate());
                    }
                });

            }

        };

        mRecyclerView.setAdapter(mFirebaseAdapter);

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
                FirebaseOperations.insertNote(mFirebaseUser.getEmail(), textIn, false);
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
    @Override
    public void onRefresh() {
        mFirebaseAdapter.notifyDataSetChanged();
    }
}