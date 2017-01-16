package com.shareandsearchfood.Adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shareandsearchfood.ParcelerObjects.Comments;
import com.shareandsearchfood.Utils.FirebaseOperations;
import com.shareandsearchfood.Utils.Tools;
import com.shareandsearchfood.shareandsearchfood.R;

import java.util.List;

/**
 * Created by david_000 on 10/12/2016.
 */

public class CommentsRecyclerViewAdapter extends RecyclerView.Adapter<CommentsRecyclerViewAdapter.ViewHolder>  {
    private List<Comments> mDataSet;
    private Context ctx;
    /**
     * Inner Class for a recycler view
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView userNickname;
        public TextView mTextView;
        public ImageView userPhoto;
        public TextView data;
        public RelativeLayout commentImage;
        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) itemView.findViewById(R.id.textView);
            userNickname = (TextView) itemView.findViewById(R.id.nickname);
            userPhoto = (ImageView) itemView.findViewById(R.id.imageView4);
            data = (TextView) itemView.findViewById(R.id.dataComments);
            commentImage = (RelativeLayout) itemView.findViewById(R.id.commentImage);

        }
    }

    public CommentsRecyclerViewAdapter(List<Comments> dataSet, Context ctx) {
        mDataSet = dataSet;
        this.ctx = ctx;
    }

    @Override
    public CommentsRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_comments, parent, false);

        return new CommentsRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentsRecyclerViewAdapter.ViewHolder holder, int position) {
        Comments comment = mDataSet.get(position);
        holder.mTextView.setText(comment.getComment());
        ImageView image = new ImageView(ctx);
        LinearLayout.LayoutParams vp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        image.setLayoutParams(vp);
        holder.commentImage.setMinimumHeight(250);
        holder.commentImage.addView(image);
        Tools.ImageDownload(ctx, image, comment.getPhoto());

        holder.data.setText(comment.getDate());
        FirebaseOperations.setUserContent(comment.getUserID(),holder.userNickname,holder.userPhoto,ctx);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}
