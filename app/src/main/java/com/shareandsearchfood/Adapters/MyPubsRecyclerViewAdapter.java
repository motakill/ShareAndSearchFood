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

import com.shareandsearchfood.Fragments.MyPubsFragment;
import com.shareandsearchfood.login.App;
import com.shareandsearchfood.login.DaoSession;
import com.shareandsearchfood.login.Receipt;
import com.shareandsearchfood.login.UserDao;
import com.shareandsearchfood.shareandsearchfood.R;

import java.util.List;


public class MyPubsRecyclerViewAdapter extends RecyclerView.Adapter<MyPubsRecyclerViewAdapter.ViewHolder>{

    private List<Receipt> receipts;
    private final MyPubsFragment.OnListFragmentInteractionListener mListener;
    private UserDao userDao;
    private Context ctx;


    public MyPubsRecyclerViewAdapter(List<Receipt> receipts, Context ctx, Application app, MyPubsFragment.OnListFragmentInteractionListener listener) {
        DaoSession daoSession = ((App) app).getDaoSession();
        userDao = daoSession.getUserDao();
        this.receipts = receipts;
        mListener = listener;
        this.ctx = ctx;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_my_pubs, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = receipts.get(position);
        holder.titulo.setText(receipts.get(position).getTitle());
        Uri imageUri = Uri.parse(receipts.get(position).getPhotoReceipt());
        holder.photo.setImageURI(imageUri);
        holder.timestamp.setText(receipts.get(position).getDate().toString());
        holder.favorite.setChecked(receipts.get(position).getFavorite());
        holder.rate.setRating(receipts.get(position).getRate());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface
                    mListener.onListFragmentInteraction(holder.mItem);
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
