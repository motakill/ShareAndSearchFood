package com.shareandsearchfood.Adapters;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

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



import com.shareandsearchfood.Fragments.VisitPersonFragment;
import com.shareandsearchfood.login.App;
import com.shareandsearchfood.login.DaoSession;
import com.shareandsearchfood.login.Recipe;
import com.shareandsearchfood.login.Session;
import com.shareandsearchfood.login.User;
import com.shareandsearchfood.login.UserDao;
import com.shareandsearchfood.shareandsearchfood.Favorite;
import com.shareandsearchfood.shareandsearchfood.FavoriteDao;

import com.shareandsearchfood.shareandsearchfood.R;
import com.shareandsearchfood.shareandsearchfood.RecipeContent;

import org.greenrobot.greendao.query.QueryBuilder;


import java.util.List;



public class VisitPersonRecyclerViewAdapter extends RecyclerView.Adapter<VisitPersonRecyclerViewAdapter.ViewHolder> {

    private List<Recipe> receipts;
    private final VisitPersonFragment.OnListFragmentInteractionListenerVisitPerson mListener;
    private UserDao userDao;
    private Context ctx;
    private VisitPersonRecyclerViewAdapter.ViewHolder holder;
    private DaoSession daoSession;
    private User user;
    private Session session;

    public VisitPersonRecyclerViewAdapter(List<Recipe> receipts, Context ctx, Application app, VisitPersonFragment.OnListFragmentInteractionListenerVisitPerson listener) {
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
        user = getUserByID(receipts.get(position).getUserId());
        holder.mItem = receipts.get(position);
        holder.titulo.setText(receipts.get(position).getTitle());
        Uri imageUri = Uri.parse(receipts.get(position).getPhotoReceipt());
        holder.photo.setImageURI(imageUri);
        holder.photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, RecipeContent.class);
                intent.putExtra("userPhoto",user.getPhoto());
                intent.putExtra("recipePhoto",receipts.get(position).getPhotoReceipt());
                intent.putExtra("flag",user.getFlag());
                intent.putExtra("nickname",user.getName());
                intent.putExtra("recipeTitle",receipts.get(position).getTitle());
                intent.putExtra("favorite",existFav(getUserID(session.getEmail()),receipts.get(position).getId()) );
                intent.putExtra("ingredients",receipts.get(position).getIngredients());
                intent.putExtra("steps",receipts.get(position).getSteps());
                intent.putExtra("rating",receipts.get(position).getRate());
                intent.putExtra("userID",user.getId());
                intent.putExtra("recipeID",receipts.get(position).getId());
                ctx.startActivity(intent);
            }
        });

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

    public void setNewData(List<Recipe> receipts){
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
    private User getUserByID(Long id) {
        QueryBuilder qb = userDao.queryBuilder();
        qb.where(UserDao.Properties.Id.eq(id));
        List<User> user = qb.list();
        Log.d("user name:",user.get(0).getName() );
        return user.get(0);
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
        public Recipe mItem;

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