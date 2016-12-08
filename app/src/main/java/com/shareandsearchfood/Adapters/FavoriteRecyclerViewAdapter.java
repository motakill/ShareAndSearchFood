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

import com.shareandsearchfood.shareandsearchfood.R;
import com.shareandsearchfood.shareandsearchfood.RecipeContent;
import com.shareandsearchfood.shareandsearchfood.Visit_person;


import java.io.IOException;
import java.net.URL;
import java.util.List;


public class FavoriteRecyclerViewAdapter extends RecyclerView.Adapter<FavoriteRecyclerViewAdapter.ViewHolder>{



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_my_favorites, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

    }


    @Override
    public int getItemCount() {
        return 0;
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
