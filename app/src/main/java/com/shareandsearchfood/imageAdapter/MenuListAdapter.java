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
import com.shareandsearchfood.login.User;
import com.shareandsearchfood.shareandsearchfood.R;

import java.util.List;

/**
 * Created by tiagomota on 08/11/16.
 */

public class MenuListAdapter extends ArrayAdapter<Receipt> {

    public MenuListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public MenuListAdapter(Context context, int resource, List<Receipt> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.row_cook_book, null);
        }
        Receipt p = getItem(position);
       // User    u = getItem(position);

        if (p != null) {
           // ImageView personalPhoto = (ImageView) v.findViewById(R.id.user_image);
           // TextView nickname = (TextView) v.findViewById(R.id.nickname);
            TextView titulo = (TextView) v.findViewById(R.id.titulo);
            ImageView photo = (ImageView) v.findViewById(R.id.receita_foto);
            TextView timestamp = (TextView) v.findViewById(R.id.data5);
            CheckBox favorite = (CheckBox) v.findViewById(R.id.star5);
            RatingBar rate = (RatingBar) v.findViewById((R.id.ratingBarContentMenu5));
/**
            if(personalPhoto != null){
                d.Log("ID do user: " + p.getUserId());
                Uri imageUri2 = Uri.parse(u.getPhoto(p.getUserId()));

                photo.setImageURI(imageUri2);
            }

            if(nickname != null){
                nickname.setText(u.getName(p.getUserId()));
            }
*/

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