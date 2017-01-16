package com.shareandsearchfood.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.shareandsearchfood.ParcelerObjects.Notebook;
import com.shareandsearchfood.shareandsearchfood.R;

/**
 * Created by david_000 on 16/01/2017.
 */

public class FirebaseNotebookViewHolder extends RecyclerView.ViewHolder  {
    public final TextView mTextView;
    public final CheckBox delete;
    public final CheckBox check;
    public Notebook mItem;

    public FirebaseNotebookViewHolder(View v) {
        super(v);
        mTextView = (TextView) itemView.findViewById(R.id.textRow);
        delete = (CheckBox) itemView.findViewById(R.id.remove);
        check = (CheckBox) itemView.findViewById(R.id.check);
    }
}