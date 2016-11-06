package com.shareandsearchfood.shareandsearchfood;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.shareandsearchfood.login.App;
import com.shareandsearchfood.login.DaoSession;
import com.shareandsearchfood.login.Notebook;
import com.shareandsearchfood.login.NotebookDao;
import com.shareandsearchfood.login.Session;
import com.shareandsearchfood.login.User;
import com.shareandsearchfood.login.UserDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by david_000 on 14/10/2016.
 */

public class NotebookActivity extends NavBar {
    EditText textIn;
    RadioButton buttonAdd;
    Button buttonRemove;
    Session session;
    ListView listView;
    List<String> list;
    ArrayAdapter<String> itemsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notebook);
        session = new Session(this);
        listView = (ListView) findViewById(R.id.listView);
        createNote();

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
                saveNote();
            }
        });
        deleteNote();
    }

    private void createNote(){
        list = getUserNotes(session.getEmail());
        if(list != null) {
            itemsAdapter = new ArrayAdapter<>(this, R.layout.row, R.id.textRow, list);
            listView.setAdapter(itemsAdapter);
        }
    }

    private void deleteNote(){
        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View row = layoutInflater.inflate(R.layout.row, null);
        buttonRemove = (Button)row.findViewById(R.id.remove);
        buttonRemove.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                DaoSession daoSession = ((App) getApplication()).getDaoSession();
                NotebookDao noteDao = daoSession.getNotebookDao();
                noteDao.delete(getNoteByContentAndUserId(itemsAdapter.getItem(row.getId()),session.getEmail()));
                finish();
                startActivity(getIntent());
            }
        });
    }

    private void saveNote(){
        DaoSession daoSession = ((App) getApplication()).getDaoSession();
        NotebookDao noteDao = daoSession.getNotebookDao();
        noteDao.insert(new Notebook(null,getUserID(session.getEmail()),textIn.getText().toString(),new Date()));
        itemsAdapter.notifyDataSetChanged();
        finish();
        startActivity(getIntent());

    }
    private long getUserID(String email){
        DaoSession daoSession = ((App) getApplication()).getDaoSession();
        UserDao userDao = daoSession.getUserDao();
        QueryBuilder qb = userDao.queryBuilder();
        qb.where(UserDao.Properties.Email.eq(email));
        List<User> user = qb.list();
        return user.get(0).getId();
    }
    private List<String> getUserNotes(String email){
        DaoSession daoSession = ((App) getApplication()).getDaoSession();
        UserDao userDao = daoSession.getUserDao();
        QueryBuilder qb = userDao.queryBuilder();
        qb.where(UserDao.Properties.Email.eq(email));
        List<User> user = qb.list();
        List<String> notes = new ArrayList<String>();
        List<Notebook> userNotes = user.get(0).getNotes();
        for (Notebook note : userNotes) {
            notes.add(note.getNote());
        }
        return notes;
    }

    private Notebook getNoteByContentAndUserId(String content,String email){
        DaoSession daoSession = ((App) getApplication()).getDaoSession();
        NotebookDao noteDao = daoSession.getNotebookDao();
        QueryBuilder qb = noteDao.queryBuilder();
        qb.where(NotebookDao.Properties.Note.eq(content),NotebookDao.Properties.UserId.eq(getUserID(email)));
        List<Notebook> note = qb.list();
        return note.get(0);
    }
}