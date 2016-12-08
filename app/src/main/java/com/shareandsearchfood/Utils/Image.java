package com.shareandsearchfood.Utils;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by david_000 on 08/12/2016.
 */

public class Image {
    public  static void download(Context ctx, ImageView imageView, String url){
        Picasso.Builder picassoBuilder = new Picasso.Builder(ctx);
        Picasso picasso = picassoBuilder.build();
        picasso.with(ctx)
                .load(url)
                .fit()
                .into(imageView);
    }
}
