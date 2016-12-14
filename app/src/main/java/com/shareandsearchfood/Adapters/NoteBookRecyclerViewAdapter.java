package com.shareandsearchfood.Adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.shareandsearchfood.ParcelerObjects.Notebook;
import com.shareandsearchfood.Utils.FirebaseOperations;
import com.shareandsearchfood.shareandsearchfood.R;


import java.util.List;

public class NoteBookRecyclerViewAdapter extends RecyclerView.Adapter<NoteBookRecyclerViewAdapter.ViewHolder> {

    private List<Notebook> mDataSet;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    /**
     * Inner Class for a recycler view
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public final CheckBox delete;
        public CheckBox check;
        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) itemView.findViewById(R.id.textRow);
            delete = (CheckBox) itemView.findViewById(R.id.remove);
            check = (CheckBox) itemView.findViewById(R.id.check);
        }
    }

    public NoteBookRecyclerViewAdapter(List<Notebook> dataSet) {
        mDataSet = dataSet;
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
    }

    @Override
    public NoteBookRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_notebook, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Notebook note = mDataSet.get(position);
        holder.mTextView.setText(note.getNote());
        holder.check.setChecked(note.getStatus());
        holder.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseOperations.updateNote(mFirebaseUser.getEmail(),note.getDate()
                        ,holder.check.isChecked());
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseOperations.removeNote(mFirebaseUser.getEmail(),note.getDate());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}