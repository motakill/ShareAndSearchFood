package com.shareandsearchfood.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.shareandsearchfood.ParcelerObjects.HowTo;
import com.shareandsearchfood.Utils.FirebaseOperations;
import com.shareandsearchfood.Utils.Tools;
import com.shareandsearchfood.shareandsearchfood.HowToDoItOption;
import com.shareandsearchfood.shareandsearchfood.MyProfile;
import com.shareandsearchfood.shareandsearchfood.R;
import com.shareandsearchfood.shareandsearchfood.Visit_person;


import java.util.List;

public class HowToDoItRecyclerViewAdapter extends RecyclerView.Adapter<HowToDoItRecyclerViewAdapter.ViewHolder> {
    private final Context ctx;
    private List<HowTo> mDataSet;
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
        public final TextView nickname;

        public ViewHolder(View v) {
            super(v);
            nickname = (TextView) v.findViewById(R.id.nicknameHowToDoIt);
            titulo = (TextView) v.findViewById(R.id.tituloHowToDoIt);
            photo = (ImageView) v.findViewById(R.id.how_to_do_it_foto);
            timestamp = (TextView) v.findViewById(R.id.data5_how_to_do_it);
            userImage = (ImageView) v.findViewById(R.id.user_imageHowToDoIt);
        }
    }

    public HowToDoItRecyclerViewAdapter(List<HowTo> dataSet, Context ctx) {
        mDataSet = dataSet;
        this.ctx = ctx;
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
    }

    @Override
    public HowToDoItRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_how_to_do_it, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final HowTo howTo = mDataSet.get(position);
        holder.titulo.setText(howTo.getTitle());
        FirebaseOperations.setUserContent(howTo.getUserId(),holder.nickname,holder.userImage,ctx);
        Tools.ImageDownload(ctx,holder.photo,howTo.getPhoto());


        holder.photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(ctx, HowToDoItOption.class);
                    intent.putExtra("htoPhoto", howTo.getPhoto());
                    intent.putExtra("htoID", howTo.getHowToID());
                    intent.putExtra("htoObs", howTo.getObs());
                    intent.putExtra("htoDate", howTo.getDate());
                    intent.putExtra("userID", howTo.getUserId());
                    ctx.startActivity(intent);
            }
        });
        holder.userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(mFirebaseUser.getEmail().equals(howTo.getUserId()))
                ctx.startActivity(new Intent(ctx,MyProfile.class));
            else {
                Intent intent = new Intent(ctx, Visit_person.class);
                intent.putExtra("userID", howTo.getUserId());
                ctx.startActivity(intent);
            }
            }
        });
        //vai ser how to do its
        holder.timestamp.setText(howTo.getDate());
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}