package com.shareandsearchfood.Adapters;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.shareandsearchfood.ParcelerObjects.HowToFirebase;
import com.shareandsearchfood.ParcelerObjects.NotebookFirebase;
import com.shareandsearchfood.ParcelerObjects.RecipeFirebase;
import com.shareandsearchfood.Utils.FirebaseOperations;
import com.shareandsearchfood.Utils.Image;
import com.shareandsearchfood.login.LoginActivity;
import com.shareandsearchfood.shareandsearchfood.HowToDoItOption;
import com.shareandsearchfood.shareandsearchfood.R;
import com.shareandsearchfood.shareandsearchfood.RecipeContent;
import com.shareandsearchfood.shareandsearchfood.Visit_person;
import com.squareup.picasso.Picasso;


import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.List;

public class HowToDoItRecyclerViewAdapter extends RecyclerView.Adapter<HowToDoItRecyclerViewAdapter.ViewHolder> {
    private final Context ctx;
    private List<HowToFirebase> mDataSet;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    /**
     * Inner Class for a recycler view
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView titulo;
        public final ImageView photo;
        public final TextView timestamp;
        public final ImageView userImage ;
        public final TextView nickname;

        public ViewHolder(View v) {
            super(v);
            nickname = (TextView) v.findViewById(R.id.nicknameHowToDoIt);
            titulo = (TextView) v.findViewById(R.id.tituloHowToDoIt);
            photo = (ImageView) v.findViewById(R.id.how_to_do_it_foto);
            timestamp = (TextView) v.findViewById(R.id.data5_how_to_do_it);
            userImage = (ImageView) v.findViewById(R.id.user_imageHowToDoIt);
        }
    }

    public HowToDoItRecyclerViewAdapter(List<HowToFirebase> dataSet, Context ctx) {
        mDataSet = dataSet;
        this.ctx = ctx;
        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
    }

    @Override
    public HowToDoItRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_how_to_do_it, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final HowToFirebase howTo = mDataSet.get(position);
        holder.titulo.setText(howTo.getTitle());
        FirebaseOperations.setUserContent(mFirebaseUser.getEmail(),holder.nickname,holder.userImage,ctx);
        Image.download(ctx,holder.photo,howTo.getPhoto());


        holder.photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, HowToDoItOption.class);
                intent.putExtra("htoPhoto",howTo.getPhoto());
                intent.putExtra("htoTitulo",howTo.getTitle());
                intent.putExtra("htoObs",howTo.getObs());
                intent.putExtra("htoDate",howTo.getDate());
                ctx.startActivity(intent);
            }
        });

        //vai ser how to do its
        holder.timestamp.setText(howTo.getDate());
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}