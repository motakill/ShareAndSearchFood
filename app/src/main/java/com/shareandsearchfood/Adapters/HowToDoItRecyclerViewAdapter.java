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
import android.widget.ImageView;
import android.widget.TextView;


import com.shareandsearchfood.Fragments.HowToDoItFragment;
import com.shareandsearchfood.login.App;
import com.shareandsearchfood.login.DaoSession;
import com.shareandsearchfood.login.Session;
import com.shareandsearchfood.login.User;
import com.shareandsearchfood.login.UserDao;
import com.shareandsearchfood.shareandsearchfood.HowToDoItTable;
import com.shareandsearchfood.shareandsearchfood.R;

import org.greenrobot.greendao.query.QueryBuilder;

import java.io.IOException;
import java.net.URL;
import java.util.List;



public class HowToDoItRecyclerViewAdapter extends RecyclerView.Adapter<HowToDoItRecyclerViewAdapter.ViewHolder> {

    private List<HowToDoItTable> howToDoItTable;
    private final HowToDoItFragment.OnListFragmentInteractionListenerHowToDoIT mListener;
    private UserDao userDao;
    private Context ctx;
    private Session session;
    private DaoSession daoSession;
    public HowToDoItRecyclerViewAdapter(List<HowToDoItTable> howToDoItTable, Context ctx, Application app, HowToDoItFragment.OnListFragmentInteractionListenerHowToDoIT listener) {
        daoSession = ((App) app).getDaoSession();
        userDao = daoSession.getUserDao();
        this.howToDoItTable = howToDoItTable;
        mListener = listener;
        this.ctx = ctx;
        session = new Session(ctx);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_how_to_do_it, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        User user = getUserByID(howToDoItTable.get(position).getUserId());
        //vai ser how to do its
        holder.mItem = howToDoItTable.get(position);
        holder.nickname.setText(user.getName());
        //vai ser how to do its
        holder.titulo.setText(howToDoItTable.get(position).getTitle());
        //vai ser how to do its
        Uri imageUri = Uri.parse(howToDoItTable.get(position).getPhoto());
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

        //vai ser how to do its
        holder.timestamp.setText(howToDoItTable.get(position).getDate().toString());
        // holder.favorite.setChecked(existFav(getUserID(session.getEmail()),receipts.get(position).getId()));
        // holder.rate.setRating(receipts.get(position).getRate());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface
                    mListener.OnListFragmentInteractionListenerHowToDoIT(holder.mItem);
                }
            }
        });
    }

    public void setNewData(List<HowToDoItTable> howToDoItTable) {
        this.howToDoItTable = howToDoItTable;
    }

    @Override
    public int getItemCount() {
        return howToDoItTable.size();
    }

    private User getUserByID(Long id) {
        QueryBuilder qb = userDao.queryBuilder();
        qb.where(UserDao.Properties.Id.eq(id));
        List<User> user = qb.list();
        Log.d("user name:",user.get(0).getName() );
        return user.get(0);
    }
    /**
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
     */
    /**
    private Long getUserID(String email) {
        QueryBuilder qb = userDao.queryBuilder();
        qb.where(UserDao.Properties.Email.eq(email));
        List<User> user = qb.list();
        return user.get(0).getId();
    }
*/
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView titulo;
        public final ImageView photo;
        public final TextView timestamp;
        public final ImageView userImage ;
        public final TextView nickname;

        public HowToDoItTable mItem;

        public ViewHolder(View v) {
            super(v);
            mView = v;
            nickname = (TextView) v.findViewById(R.id.nicknameHowToDoIt);
            titulo = (TextView) v.findViewById(R.id.tituloHowToDoIt);
            photo = (ImageView) v.findViewById(R.id.how_to_do_it_foto);
            timestamp = (TextView) v.findViewById(R.id.data5_how_to_do_it);
            userImage = (ImageView) v.findViewById(R.id.user_imageHowToDoIt);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + titulo.getText() + "'";
        }
    }
}