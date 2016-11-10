package com.shareandsearchfood.Adapters;

import android.app.Application;
import android.content.Context;
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

import com.shareandsearchfood.Fragments.CookBookFragment;
import com.shareandsearchfood.Fragments.MyPubsFragment;
import com.shareandsearchfood.login.App;
import com.shareandsearchfood.login.DaoSession;
import com.shareandsearchfood.login.Receipt;
import com.shareandsearchfood.login.ReceiptDao;
import com.shareandsearchfood.login.Session;
import com.shareandsearchfood.login.User;
import com.shareandsearchfood.login.UserDao;
import com.shareandsearchfood.shareandsearchfood.Favorite;
import com.shareandsearchfood.shareandsearchfood.FavoriteDao;
import com.shareandsearchfood.shareandsearchfood.R;

import org.greenrobot.greendao.query.QueryBuilder;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * Created by david_000 on 08/11/2016.
 */

public class CookBookRecyclerViewAdapter extends RecyclerView.Adapter<CookBookRecyclerViewAdapter.ViewHolder> {

    private List<Receipt> receipts;
    private final CookBookFragment.OnListFragmentInteractionListenerCookBook mListener;
    private UserDao userDao;
    private Context ctx;
    private Session session;
    private DaoSession daoSession;
    public CookBookRecyclerViewAdapter(List<Receipt> receipts, Context ctx, Application app, CookBookFragment.OnListFragmentInteractionListenerCookBook listener) {
        daoSession = ((App) app).getDaoSession();
        userDao = daoSession.getUserDao();
        this.receipts = receipts;
        mListener = listener;
        this.ctx = ctx;
        session = new Session(ctx);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_cook_book, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        User user = getUserByID(receipts.get(position).getUserId());
        holder.mItem = receipts.get(position);
        holder.nickname.setText(user.getName());
        holder.titulo.setText(receipts.get(position).getTitle());
        Uri imageUri = Uri.parse(receipts.get(position).getPhotoReceipt());
        holder.photo.setImageURI(imageUri);
        int flag = user.getFlag();

        try {
            if (user.getPhoto() != null && flag == 1) {
                URL url = new URL(user.getPhoto());
                Bitmap myBitmap = BitmapFactory.decodeStream(url.openStream());
                holder.userImage.setImageBitmap(myBitmap);
            } else if (user.getPhoto() != null && flag == 0) {
                holder.userImage.setImageURI(Uri.parse(user.getPhoto()));
            } else
                holder.userImage.setImageResource(R.drawable.com_facebook_profile_picture_blank_square);
        }catch (IOException w){}

        holder.timestamp.setText(receipts.get(position).getDate().toString());
        holder.favorite.setChecked(existFav(getUserID(session.getEmail()),receipts.get(position).getId()));
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
        holder.rate.setRating(receipts.get(position).getRate());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface
                    mListener.onListFragmentInteractionCookBook(holder.mItem);
                }
            }
        });
    }

    public void setNewData(List<Receipt> receipts) {
        this.receipts = receipts;
    }

    @Override
    public int getItemCount() {
        return receipts.size();
    }
    private Long findFavorite(Long userId, Long receiptID){
        FavoriteDao favoriteDao= daoSession.getFavoriteDao();
        QueryBuilder qb = favoriteDao.queryBuilder();
        qb.and(FavoriteDao.Properties.UserId.eq(userId),FavoriteDao.Properties.ReceiptId.eq(receiptID));
        List<Favorite> favorites = qb.list();
        return favorites.get(0).getId();
    }
    private User getUserByID(Long id) {
        QueryBuilder qb = userDao.queryBuilder();
        qb.where(UserDao.Properties.Id.eq(id));
        List<User> user = qb.list();
        Log.d("user name:",user.get(0).getName() );
        return user.get(0);
    }
    private boolean existFav(Long userId, Long receiptId){
        boolean status = false;
        FavoriteDao favoriteDao = daoSession.getFavoriteDao();
        List<Favorite> favorites = favoriteDao.loadAll();
        for (Favorite favorite:favorites) {
            if(userId== favorite.getUserId() && receiptId == favorite.getReceiptId())
                status = true;
        }
        return status;
    }
    private Long getUserID(String email) {
        QueryBuilder qb = userDao.queryBuilder();
        qb.where(UserDao.Properties.Email.eq(email));
        List<User> user = qb.list();
        return user.get(0).getId();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView titulo;
        public final ImageView photo;
        public final TextView timestamp;
        public final ImageView userImage ;
        public final CheckBox favorite;
        public final RatingBar rate;
        public final TextView nickname;

        public Receipt mItem;

        public ViewHolder(View v) {
            super(v);
            mView = v;
            nickname = (TextView) v.findViewById(R.id.nicknameCookBook);
            titulo = (TextView) v.findViewById(R.id.titulo);
            photo = (ImageView) v.findViewById(R.id.receita_foto);
            timestamp = (TextView) v.findViewById(R.id.data5);
            favorite = (CheckBox) v.findViewById(R.id.star5);
            rate = (RatingBar) v.findViewById((R.id.ratingBarContentMenu5));
            userImage = (ImageView) v.findViewById(R.id.user_image);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + titulo.getText() + "'";
        }
    }
}