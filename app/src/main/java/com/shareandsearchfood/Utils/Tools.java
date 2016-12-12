package com.shareandsearchfood.Utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.shareandsearchfood.shareandsearchfood.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class Tools extends AppCompatActivity {
    public  static void ImageDownload(Context ctx, ImageView imageView, String url){
        Picasso.Builder picassoBuilder = new Picasso.Builder(ctx);
        Picasso picasso = picassoBuilder.build();
        picasso.with(ctx)
                .load(url)
                .fit()
                .into(imageView);
    }
    public static void downloadVideo(final Context ctx, String url){


        AlertDialog.Builder dialog = new AlertDialog.Builder(ctx);
        dialog.setView(R.layout.video_pop_up);

        AlertDialog b = dialog.create();
        b.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        b.show();

        VideoView mVideoView = (VideoView)b.findViewById(R.id.videoView);
        Toast.makeText(ctx, "Video will be charge, wait please",Toast.LENGTH_LONG).show();
        MediaController mediaController = new MediaController(ctx);
        mediaController.setAnchorView(mVideoView);
        mVideoView.setMediaController(mediaController);
        mVideoView.setVideoURI(Uri.parse(url));
        mVideoView.requestFocus();
        mVideoView.start();
    }
}

