package com.shareandsearchfood.Utils;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;


public class Tools extends AppCompatActivity {
    public  static void ImageDownload(Context ctx, ImageView imageView, String url){
        Picasso.Builder picassoBuilder = new Picasso.Builder(ctx);
        Picasso picasso = picassoBuilder.build();
        picasso.with(ctx)
                .load(url)
                .fit()
                .into(imageView);
    }
    public static  void downloadVideo(Context ctx, String url, final VideoView videoView){
        final MediaController mediaController = new MediaController(ctx);
        //Set the videoView that acts as the anchor for the MediaController.
        mediaController.setAnchorView(videoView);
        // Set MediaController for VideoView
        videoView.setMediaController(mediaController);
        videoView.setVideoPath(url);
        videoView.requestFocus();
    }

}

