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


import com.shareandsearchfood.ParcelerObjects.Recipe;
import com.shareandsearchfood.Utils.Image;
import com.shareandsearchfood.shareandsearchfood.R;
import com.shareandsearchfood.shareandsearchfood.RecipeContent;


import java.util.List;

public class VisitPersonRecyclerViewAdapter extends RecyclerView.Adapter<VisitPersonRecyclerViewAdapter.ViewHolder> {
    private final Context ctx;
    private List<Recipe> mDataSet;

    /**
     * Inner Class for a recycler view
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView titulo;
        public final ImageView photo ;
        public final TextView timestamp ;
        public final CheckBox favorite ;
        public final RatingBar rate ;

        public ViewHolder(View v) {
            super(v);
            titulo = (TextView) v.findViewById(R.id.titulo1);
            photo = (ImageView) v.findViewById(R.id.imageView4);
            timestamp = (TextView) v.findViewById(R.id.data2);
            favorite = (CheckBox) v.findViewById(R.id.star2);
            rate = (RatingBar) v.findViewById((R.id.ratingBar6));

        }
    }

    public VisitPersonRecyclerViewAdapter(List<Recipe> dataSet, Context ctx) {
        mDataSet = dataSet;
        this.ctx = ctx;
    }

    @Override
    public VisitPersonRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_visit_person, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Recipe recipe = mDataSet.get(position);

        holder.titulo.setText(recipe.getTitle());
        Image.download(ctx,holder.photo,recipe.getPhotoRecipe());
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

        holder.timestamp.setText(recipe.getDate());
        holder.rate.setRating(recipe.getRate());
        holder.favorite.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
            }
        });
        holder.favorite.setChecked(false);

    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}