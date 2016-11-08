package com.shareandsearchfood.shareandsearchfood;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.shareandsearchfood.imageAdapter.MenuListAdapter;
import com.shareandsearchfood.login.App;
import com.shareandsearchfood.login.DaoSession;
import com.shareandsearchfood.login.Receipt;
import com.shareandsearchfood.login.ReceiptDao;
import com.shareandsearchfood.login.Session;
import com.shareandsearchfood.login.User;
import com.shareandsearchfood.login.UserDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;


public class MenuActivity extends NavBar {

    private MenuListAdapter customMenuAdapter;
    private  List<Receipt> updated;
    private Session session;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook_book);
        session = new Session(this);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        RatingBar simpleRatingBar = (RatingBar) findViewById(R.id.ratingBarContentMenu5); // initiate a rating bar
//        int numberOfStars = simpleRatingBar.getNumStars(); // get total number of stars of rating bar

        //cria a view das receitas criadas
        createPubs();
    }

    public void clickProfile(View view){
        Intent intent = new Intent(this, Visit_person.class);
        startActivity(intent);

    }
    public void clickRecipe(View view){
        Intent intent = new Intent(this, RecipeContent.class);
        startActivity(intent);

    }

    //CookBook
    private List<Receipt> getAllReceipts(){
        //long userId = getUserID(session.getEmail());
        DaoSession daoSession = ((App) getApplication()).getDaoSession();
        ReceiptDao receiptDao = daoSession.getReceiptDao();
        QueryBuilder qb = receiptDao.queryBuilder();
        //qb.where(ReceiptDao.Properties.UserId.eq(userId));
        List<Receipt> allReceipts = qb.list();
        return allReceipts;
    }
    private void createPubs(){
        ListView yourListView = (ListView) findViewById(R.id.myCookBookList);
        List<Receipt> userReceipts = getAllReceipts();
        updated = checkIfFavorite(userReceipts);
        customMenuAdapter = new MenuListAdapter(this, R.layout.row_cook_book,updated);
        yourListView.setAdapter(customMenuAdapter);
    }

    public List<Receipt> checkIfFavorite(List<Receipt> firstList){
        List<Receipt> checked = firstList;

        DaoSession daoSession = ((App) getApplication()).getDaoSession();
        FavoriteDao favoriteDao= daoSession.getFavoriteDao();
        List<Favorite> favorites = favoriteDao.loadAll();

        for (Receipt receipt: checked) {
            for (Favorite favorite:favorites) {
                if(receipt.getUserId() == favorite.getUserId() && receipt.getId() == favorite.getReceiptId())
                    receipt.setFavorite(true);
            }
        }

        return checked;
    }
}
