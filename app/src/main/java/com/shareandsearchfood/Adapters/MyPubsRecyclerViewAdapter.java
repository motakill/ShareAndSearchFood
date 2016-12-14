package com.shareandsearchfood.Adapters;

import android.content.Context;
import android.content.Intent;
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
import com.shareandsearchfood.ParcelerObjects.Recipe;
import com.shareandsearchfood.Utils.FirebaseOperations;
import com.shareandsearchfood.Utils.Tools;
import com.shareandsearchfood.shareandsearchfood.R;
import com.shareandsearchfood.shareandsearchfood.RecipeContent;


import java.util.List;

public class MyPubsRecyclerViewAdapter extends RecyclerView.Adapter<MyPubsRecyclerViewAdapter.ViewHolder> {
    private final Context ctx;
    private List<Recipe> mDataSet;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    /**
     * Inner Class for a recycler view
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView titulo;
        private final ImageView photo ;
        private final TextView timestamp ;
        private final CheckBox favorite ;
        private final RatingBar rate ;

        public ViewHolder(View v) {
            super(v);
            titulo = (TextView) v.findViewById(R.id.titulo1);
            photo = (ImageView) v.findViewById(R.id.imageView4);
            timestamp = (TextView) v.findViewById(R.id.data2);
            favorite = (CheckBox) v.findViewById(R.id.star2);
            rate = (RatingBar) v.findViewById((R.id.ratingBar6));

        }
    }

    public MyPubsRecyclerViewAdapter(List<Recipe> dataSet, Context ctx) {
        mDataSet = dataSet;
        this.ctx = ctx;
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
    }

    @Override
    public MyPubsRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_my_pubs, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Recipe recipe = mDataSet.get(position);
        holder.titulo.setText(recipe.getTitle());
        Tools.ImageDownload(ctx,holder.photo,recipe.getPhotoRecipe());


        holder.photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, RecipeContent.class);
                intent.putExtra("recipePhoto",recipe.getPhotoRecipe());
                intent.putExtra("recipeTitle",recipe.getTitle());
                intent.putExtra("favorite",recipe.getFavorite() );
                intent.putExtra("ingredients",recipe.getIngredients());
                intent.putExtra("steps",recipe.getSteps());
                intent.putExtra("recipeID",recipe.getRecipeId());
                intent.putExtra("rating", recipe.getRateBar().getRates());
                intent.putExtra("ratingValue", recipe.getRateBar().getValue());
                intent.putExtra("ratingPeople", recipe.getRateBar().getPeople());
                intent.putExtra("userID",recipe.getUserId());
                intent.putExtra("status",recipe.getStatus());
                intent.putExtra("date",recipe.getDate());
                intent.putExtra("confectionTime", recipe.getConfectionTime());
                intent.putExtra("prepareTime", recipe.getPrepareTime());
                intent.putExtra("numPeople", recipe.getNumPeople());
                intent.putExtra("categoy", recipe.getCategory());

                ctx.startActivity(intent);
            }
        });

        holder.timestamp.setText(recipe.getDate().toString());
        holder.rate.setRating(recipe.getRateBar().getValue());
        holder.favorite.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                recipe.setFavorite(holder.favorite.isChecked());
                FirebaseOperations.setFavoriteStatus(mFirebaseUser.getEmail(),recipe);
            }
        });
        FirebaseOperations.isChecked(recipe,mFirebaseUser.getEmail(),holder.favorite);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}