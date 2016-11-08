package com.shareandsearchfood.Adapters;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
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

        holder.mItem = receipts.get(position);
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
            titulo = (TextView) v.findViewById(R.id.titulo1_fav);
            photo = (ImageView) v.findViewById(R.id.imageView4_fav);
            timestamp = (TextView) v.findViewById(R.id.data2_fav);
            favorite = (CheckBox) v.findViewById(R.id.star2_fav);
            rate = (RatingBar) v.findViewById((R.id.ratingBar6_fav));
        }

        @Override
        public String toString() {
            return super.toString() + " '" + titulo.getText() + "'";
        }
    }

    public boolean existFav(Long userId, Long receiptId){
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
}
