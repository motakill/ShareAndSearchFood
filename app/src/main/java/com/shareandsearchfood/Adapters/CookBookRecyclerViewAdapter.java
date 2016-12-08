package com.shareandsearchfood.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.shareandsearchfood.ParcelerObjects.NotebookFirebase;
import com.shareandsearchfood.ParcelerObjects.RecipeFirebase;
import com.shareandsearchfood.Utils.FirebaseOperations;
import com.shareandsearchfood.login.LoginActivity;
import com.shareandsearchfood.shareandsearchfood.R;
import com.shareandsearchfood.shareandsearchfood.RecipeContent;
import com.shareandsearchfood.shareandsearchfood.Visit_person;


import java.io.IOException;
import java.net.URL;
import java.util.List;

public class CookBookRecyclerViewAdapter extends RecyclerView.Adapter<CookBookRecyclerViewAdapter.ViewHolder> {
    private final Context ctx;
    private List<RecipeFirebase> mDataSet;
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
        public final CheckBox favorite;
        public final RatingBar rate;
        public final TextView nickname;

        public ViewHolder(View v) {
            super(v);
            nickname = (TextView) v.findViewById(R.id.nicknameCookBook);
            titulo = (TextView) v.findViewById(R.id.titulo);
            photo = (ImageView) v.findViewById(R.id.receita_foto);
            timestamp = (TextView) v.findViewById(R.id.data5);
            favorite = (CheckBox) v.findViewById(R.id.star5);
            rate = (RatingBar) v.findViewById((R.id.ratingBarContentMenu5));
            userImage = (ImageView) v.findViewById(R.id.user_image);

        }
    }

    public CookBookRecyclerViewAdapter(List<RecipeFirebase> dataSet, Context ctx) {
        mDataSet = dataSet;
        this.ctx = ctx;
        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

    }

    @Override
    public CookBookRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_cook_book, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final RecipeFirebase recipe = mDataSet.get(position);

        FirebaseOperations.setUserContent(mFirebaseUser.getEmail(),holder.nickname,holder.userImage);
        holder.titulo.setText(recipe.getTitle());
        try {
            URL newurl = new URL(recipe.getPhotoRecipe());
            Bitmap bitmap =  BitmapFactory.decodeStream(newurl.openConnection() .getInputStream());
            holder.photo.setImageBitmap(bitmap);
        }catch (Exception e){}

        holder.photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, RecipeContent.class);
                intent.putExtra("recipePhoto",recipe.getPhotoRecipe());
                intent.putExtra("recipeTitle",recipe.getTitle());
                intent.putExtra("favorite",false);
                intent.putExtra("ingredients",recipe.getIngredients());
                intent.putExtra("steps",recipe.getSteps());
                intent.putExtra("rating",recipe.getRate());
                intent.putExtra("userID",recipe.getUserId());
                ctx.startActivity(intent);
            }
        });


        holder.userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ctx.startActivity(new Intent(ctx, Visit_person.class));
            }
        });

        holder.timestamp.setText(recipe.getDate().toString());
        holder.favorite.setChecked(false);
        holder.favorite.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });
        holder.rate.setRating(recipe.getRate());
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}