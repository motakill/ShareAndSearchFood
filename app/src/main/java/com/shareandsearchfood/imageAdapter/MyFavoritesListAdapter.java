package com.shareandsearchfood.imageAdapter;

import android.content.Context;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.shareandsearchfood.login.Receipt;
import com.shareandsearchfood.shareandsearchfood.R;

import java.util.List;

/**
 * Created by david_000 on 07/11/2016.
 */

public class MyFavoritesListAdapter extends ArrayAdapter<Receipt> {

    public MyFavoritesListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public MyFavoritesListAdapter(Context context, int resource, List<Receipt> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.row_my_favorites, null);
        }

        Receipt p = getItem(position);

        if (p != null) {
            TextView titulo = (TextView) v.findViewById(R.id.titulo1_fav);
            ImageView photo = (ImageView) v.findViewById(R.id.imageView4_fav);
            TextView timestamp = (TextView) v.findViewById(R.id.data2_fav);
            CheckBox favorite = (CheckBox) v.findViewById(R.id.star2_fav);
            RatingBar rate = (RatingBar) v.findViewById((R.id.ratingBar6_fav));

            if (titulo != null) {
                titulo.setText(p.getTitle());
            }

            if (photo != null) {
                Uri imageUri = Uri.parse(p.getPhotoReceipt());

                photo.setImageURI(imageUri);
            }

            if (timestamp != null) {
                timestamp.setText(p.getDate().toString());
            }

            if(favorite != null)
                favorite.setChecked(p.getFavorite());

            if(rate != null)
                rate.setRating(p.getRate());
        }

        return v;
    }

}