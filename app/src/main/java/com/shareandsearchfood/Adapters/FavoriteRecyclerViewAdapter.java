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

import com.shareandsearchfood.Fragments.FavoriteFragment;
import com.shareandsearchfood.login.App;
import com.shareandsearchfood.login.DaoSession;
import com.shareandsearchfood.login.Receipt;
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


public class FavoriteRecyclerViewAdapter extends RecyclerView.Adapter<FavoriteRecyclerViewAdapter.ViewHolder>{

    private List<Receipt> receipts;
    private final FavoriteFragment.OnListFragmentInteractionListenerFav mListener;
    private UserDao userDao;
    private Context ctx;
    private DaoSession daoSession;
    private Session session;
    public FavoriteRecyclerViewAdapter(List<Receipt> receipts, Context ctx, Application app, FavoriteFragment.OnListFragmentInteractionListenerFav listener) {
        daoSession = ((App) app).getDaoSession();
        userDao = daoSession.getUserDao();
        session = new Session(ctx);
        this.receipts = receipts;
        mListener = listener;
        this.ctx = ctx;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_my_favorites, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        User user = getUserByID(getUserID(session.getEmail()));

        holder.mItem = receipts.get(position);
        holder.nickname.setText(user.getName());
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

        holder.titulo.setText(receipts.get(position).getTitle());
        Uri imageUri = Uri.parse(receipts.get(position).getPhotoReceipt());
        holder.photo.setImageURI(imageUri);
        holder.timestamp.setText(receipts.get(position).getDate().toString());
        holder.favorite.setChecked(existFav(getUserID(session.getEmail()),receipts.get(position).getId()));
        holder.rate.setRating(receipts.get(position).getRate());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface
                    mListener.onListFragmentInteractionFav(holder.mItem);
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
    private Long getUserID(String email) {
        UserDao userDao = daoSession.getUserDao();
        QueryBuilder qb = userDao.queryBuilder();
        qb.where(UserDao.Properties.Email.eq(email));
        List<User> user = qb.list();
        return user.get(0).getId();
    }
    private User getUserByID(Long id) {
        QueryBuilder qb = userDao.queryBuilder();
        qb.where(UserDao.Properties.Id.eq(id));
        List<User> user = qb.list();
        Log.d("user name:",user.get(0).getName() );
        return user.get(0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView titulo;
        public final ImageView photo ;
        public final TextView timestamp ;
        public final CheckBox favorite ;
        public final RatingBar rate ;
        public final ImageView userImage ;
        public final TextView nickname;

        public Receipt mItem;

        public ViewHolder(View v) {
            super(v);
            mView = v;

            nickname = (TextView) v.findViewById(R.id.nickname_my_favorites);
            userImage = (ImageView) v.findViewById(R.id.user_image_my_favorites);
            titulo = (TextView) v.findViewById(R.id.titulo_my_favorites);
            photo = (ImageView) v.findViewById(R.id.receita_foto_my_favorites);
            timestamp = (TextView) v.findViewById(R.id.data5_my_favorites);
            favorite = (CheckBox) v.findViewById(R.id.star5_my_favorites);
            rate = (RatingBar) v.findViewById((R.id.ratingBarContentMenu5_my_favorites));
        }

        @Override
        public String toString() {
            return super.toString() + " '" + titulo.getText() + "'";
        }
    }


}