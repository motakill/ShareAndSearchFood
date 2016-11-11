package com.shareandsearchfood.Adapters;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;


import com.shareandsearchfood.Fragments.HowToDoItFragment;
import com.shareandsearchfood.Fragments.MyPubsFragment;
import com.shareandsearchfood.Fragments.VisitPersonFragment;
import com.shareandsearchfood.login.App;
import com.shareandsearchfood.login.DaoSession;
import com.shareandsearchfood.login.Receipt;
import com.shareandsearchfood.login.Session;
import com.shareandsearchfood.login.User;
import com.shareandsearchfood.login.UserDao;
import com.shareandsearchfood.shareandsearchfood.Favorite;
import com.shareandsearchfood.shareandsearchfood.FavoriteDao;
import com.shareandsearchfood.shareandsearchfood.HowToDoIt;
import com.shareandsearchfood.shareandsearchfood.HowToDoItOption;
import com.shareandsearchfood.shareandsearchfood.HowToDoItTable;
import com.shareandsearchfood.shareandsearchfood.R;

import org.greenrobot.greendao.query.QueryBuilder;

import java.io.IOException;
import java.net.URL;
import java.util.List;



public class VisitPersonRecyclerViewAdapter extends RecyclerView.Adapter<VisitPersonRecyclerViewAdapter.ViewHolder> {

    private List<Receipt> receipts;
    private final VisitPersonFragment.OnListFragmentInteractionListenerVisitPerson mListener;
    private UserDao userDao;
    private Context ctx;
    private VisitPersonRecyclerViewAdapter.ViewHolder holder;
    private DaoSession daoSession;
    private int position;
    private Session session;

    public VisitPersonRecyclerViewAdapter(List<Receipt> receipts, Context ctx, Application app, VisitPersonFragment.OnListFragmentInteractionListenerVisitPerson listener) {
        daoSession = ((App) app).getDaoSession();
        userDao = daoSession.getUserDao();
        this.receipts = receipts;
        mListener = listener;
        this.ctx = ctx;
        session = new Session(ctx);
    }

    @Override
    public VisitPersonRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_visit_person, parent, false);
        return new VisitPersonRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final VisitPersonRecyclerViewAdapter.ViewHolder holder, final int position) {
        this.holder = holder;
        holder.mItem = receipts.get(position);
        holder.titulo.setText(receipts.get(position).getTitle());
        Uri imageUri = Uri.parse(receipts.get(position).getPhotoReceipt());
        holder.photo.setImageURI(imageUri);
        holder.timestamp.setText(receipts.get(position).getDate().toString());
        holder.rate.setRating(receipts.get(position).getRate());
        holder.favorite.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                FavoriteDao favoriteDao = daoSession.getFavoriteDao();
                if(holder.favorite.isChecked()) {
                    favoriteDao.insert(new Favorite(null,getUserID(session.getEmail()),receipts.get(position).getId()));
                }
                else if(!holder.favorite.isChecked()){
                    favoriteDao.deleteByKey(findFavorite(getUserID(session.getEmail()),receipts.get(position).getId()));
                }
            }
        });
        holder.favorite.setChecked(existFav(getUserID(session.getEmail()),receipts.get(position).getId()));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface
                    mListener.OnListFragmentInteractionListenerVisitPerson(holder.mItem);
                }
            }
        });
    }

    public void setNewData(List<Receipt> receipts){
        this.receipts=receipts;
    }



    @Override
    public int getItemCount() {
        return receipts.size();
    }
    private Long getUserID(String email) {
        UserDao userDao = daoSession.getUserDao();
        QueryBuilder qb = userDao.queryBuilder();
        qb.where(UserDao.Properties.Email.eq(email));
        List<User> user = qb.list();
        return user.get(0).getId();
    }
    private boolean existFav(Long userId, Long receiptId){
        boolean status = false;
        FavoriteDao favoriteDao= daoSession.getFavoriteDao();
        List<Favorite> favorites = favoriteDao.loadAll();
        for (Favorite favorite:favorites) {
            if(userId== favorite.getUserId() && receiptId == favorite.getReceiptId())
                status = true;
        }
        return status;
    }
    private Long findFavorite(Long userId, Long receiptID){
        FavoriteDao favoriteDao= daoSession.getFavoriteDao();
        QueryBuilder qb = favoriteDao.queryBuilder();
        qb.and(FavoriteDao.Properties.UserId.eq(userId),FavoriteDao.Properties.ReceiptId.eq(receiptID));
        List<Favorite> favorites = qb.list();
        return favorites.get(0).getId();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView titulo;
        public final ImageView photo ;
        public final TextView timestamp ;
        public final CheckBox favorite ;
        public final RatingBar rate ;
        public Receipt mItem;

        public ViewHolder(View v) {
            super(v);
            mView = v;
            titulo = (TextView) v.findViewById(R.id.titulo1);
            photo = (ImageView) v.findViewById(R.id.imageView4);
            timestamp = (TextView) v.findViewById(R.id.data2);
            favorite = (CheckBox) v.findViewById(R.id.star2);
            rate = (RatingBar) v.findViewById((R.id.ratingBar6));
        }

        @Override
        public String toString() {
            return super.toString() + " '" + titulo.getText() + "'";
        }
    }
}