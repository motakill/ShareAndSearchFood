package com.shareandsearchfood.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.shareandsearchfood.ParcelerObjects.NotebookFirebase;
import com.shareandsearchfood.Utils.FirebaseOperations;
import com.shareandsearchfood.shareandsearchfood.R;


import java.util.List;

public class NoteBookRecyclerViewAdapter extends RecyclerView.Adapter<NoteBookRecyclerViewAdapter.ViewHolder> {

    private List<NotebookFirebase> mDataSet;

    /**
     * Inner Class for a recycler view
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public final CheckBox delete;

        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) itemView.findViewById(R.id.textRow);
            delete = (CheckBox) v.findViewById(R.id.remove);

        }
    }

    public NoteBookRecyclerViewAdapter(List<NotebookFirebase> dataSet) {
        mDataSet = dataSet;
    }

    @Override
    public NoteBookRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_notebook, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NotebookFirebase note = mDataSet.get(position);
        holder.mTextView.setText(note.getNote());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseOperations.removeNote();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}