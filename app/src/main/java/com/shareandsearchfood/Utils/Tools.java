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
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
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
        b.getWindow().setLayout(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        b.show();

        VideoView mVideoView = (VideoView)b.findViewById(R.id.videoView);
        FrameLayout controllerAnchor = (FrameLayout)b.findViewById(R.id.controllerAnchor);
        Toast.makeText(ctx, "Video will be charge, wait please",Toast.LENGTH_LONG).show();
        MediaController mediaController = new MediaController(ctx);
        mediaController.setAnchorView(controllerAnchor);
        mVideoView.setMediaController(mediaController);

        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.BOTTOM;
        mediaController.setLayoutParams(lp);
        ((ViewGroup) mediaController.getParent()).removeView(mediaController);
        ((FrameLayout) b.findViewById(R.id.controllerAnchor)).addView(mediaController);

        mVideoView.setVideoURI(Uri.parse(url));
        mVideoView.start();
    }
}

