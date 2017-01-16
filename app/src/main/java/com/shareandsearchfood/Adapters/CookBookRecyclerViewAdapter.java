package com.shareandsearchfood.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.shareandsearchfood.ParcelerObjects.Recipe;
import com.shareandsearchfood.Utils.FirebaseOperations;
import com.shareandsearchfood.Utils.Tools;
import com.shareandsearchfood.shareandsearchfood.MyProfile;
import com.shareandsearchfood.shareandsearchfood.R;
import com.shareandsearchfood.shareandsearchfood.RecipeContent;
import com.shareandsearchfood.shareandsearchfood.Visit_person;


import java.util.List;

public class CookBookRecyclerViewAdapter extends RecyclerView.Adapter<CookBookRecyclerViewAdapter.ViewHolder> {
    private final Context ctx;
    private List<Recipe> mDataSet;
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

    public CookBookRecyclerViewAdapter(List<Recipe> dataSet, Context ctx) {
        mDataSet = dataSet;
        this.ctx = ctx;
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
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Recipe recipe = mDataSet.get(position);
        holder.titulo.setText(recipe.getTitle());
        FirebaseOperations.setUserContent(recipe.getUserId(),holder.nickname,holder.userImage,ctx);
        Tools.ImageDownload(ctx,holder.photo,recipe.getPhotoRecipe());

        holder.photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(ctx, RecipeContent.class);
                    intent.putExtra("recipePhoto", recipe.getPhotoRecipe());
                    intent.putExtra("recipeTitle", recipe.getTitle());
                    intent.putExtra("favorite", recipe.getFavorite());
                    intent.putExtra("ingredients", recipe.getIngredients());
                    intent.putExtra("steps", recipe.getSteps());
                    intent.putExtra("rating", recipe.getRateBar().getRates());
                    intent.putExtra("ratingValue", recipe.getRateBar().getValue());
                    intent.putExtra("ratingPeople", recipe.getRateBar().getPeople());
                    intent.putExtra("userID", recipe.getUserId());
                    intent.putExtra("recipeID", recipe.getRecipeId());
                    intent.putExtra("status", recipe.getStatus());
                    intent.putExtra("date", recipe.getDate());
                    intent.putExtra("confectionTime", recipe.getConfectionTime());
                    intent.putExtra("prepareTime", recipe.getPrepareTime());
                    intent.putExtra("numPeople", recipe.getNumPeople());
                    intent.putExtra("categoy", recipe.getCategory());

                ctx.startActivity(intent);
            }
        });

        holder.userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mFirebaseUser.getEmail().equals(recipe.getUserId()))
                    ctx.startActivity(new Intent(ctx,MyProfile.class));
                else {
                    Intent intent = new Intent(ctx, Visit_person.class);
                    intent.putExtra("userID", recipe.getUserId());
                    intent.putExtra("favorite", recipe.getFavorite());
                    ctx.startActivity(intent);
                }
            }
        });

        holder.timestamp.setText(recipe.getDate().toString());
        FirebaseOperations.isChecked(recipe,mFirebaseUser.getEmail(),holder.favorite);
        holder.favorite.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                recipe.setFavorite(holder.favorite.isChecked());
                FirebaseOperations.setFavoriteStatus(mFirebaseUser.getEmail(),recipe);
                }
        });
        holder.rate.setRating(recipe.getRateBar().getValue());
        holder.rate.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged (RatingBar ratingBar, float rating,
                                         boolean fromUser) {
                FirebaseOperations.insertRate(recipe.getUserId(), recipe.getRecipeId(), holder.rate.getRating());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}