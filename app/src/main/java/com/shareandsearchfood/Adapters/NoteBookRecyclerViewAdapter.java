package com.shareandsearchfood.Adapters;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.shareandsearchfood.Fragments.CookBookFragment;
import com.shareandsearchfood.Fragments.NoteBookFragment;
import com.shareandsearchfood.login.App;
import com.shareandsearchfood.login.DaoSession;
import com.shareandsearchfood.login.Notebook;
import com.shareandsearchfood.login.NotebookDao;
import com.shareandsearchfood.login.Session;
import com.shareandsearchfood.login.User;
import com.shareandsearchfood.login.UserDao;

import com.shareandsearchfood.shareandsearchfood.R;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public class NoteBookRecyclerViewAdapter extends RecyclerView.Adapter<NoteBookRecyclerViewAdapter.ViewHolder> {

    private List<Notebook> note;
    private final NoteBookFragment.OnListFragmentInteractionListenerNoteBook mListener;
    private UserDao userDao;
    private Context ctx;
    private Session session;
    private DaoSession daoSession;
    public NoteBookRecyclerViewAdapter(List<Notebook> note, Context ctx, Application app, NoteBookFragment.OnListFragmentInteractionListenerNoteBook listener) {
        daoSession = ((App) app).getDaoSession();
        userDao = daoSession.getUserDao();
        this.note = note;
        mListener = listener;
        this.ctx = ctx;
        session = new Session(ctx);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_notebook, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        User user = getUserByID(note.get(position).getUserId());
        holder.mItem = note.get(position);
        holder.note.setText(note.get(position).getNote());

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotebookDao noteDao = daoSession.getNotebookDao();
                noteDao.delete(getNoteByContentAndUserId(note.get(position).getNote(),session.getEmail()));

            }
        });

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface
                    mListener.onListFragmentInteractionNoteBook(holder.mItem);
                }
            }
        });
    }

    private Notebook getNoteByContentAndUserId(String content,String email){
        NotebookDao noteDao = daoSession.getNotebookDao();
        QueryBuilder qb = noteDao.queryBuilder();
        qb.and(NotebookDao.Properties.Note.eq(content),NotebookDao.Properties.UserId.eq(getUserID(email)));
        List<Notebook> note = qb.list();
        return note.get(0);
    }

    private long getUserID(String email){
        UserDao userDao = daoSession.getUserDao();
        QueryBuilder qb = userDao.queryBuilder();
        qb.where(UserDao.Properties.Email.eq(email));
        List<User> user = qb.list();
        return user.get(0).getId();
    }

    public void setNewData(List<Notebook> note) {
        this.note = note;
    }

    @Override
    public int getItemCount() {
        return note.size();
    }

    private User getUserByID(Long id) {
        QueryBuilder qb = userDao.queryBuilder();
        qb.where(UserDao.Properties.Id.eq(id));
        List<User> user = qb.list();
        Log.d("user name:",user.get(0).getName() );
        return user.get(0);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView note;
        public final CheckBox delete;
        public final CheckBox check;

        public Notebook mItem;

        public ViewHolder(View v) {
            super(v);
            mView = v;
            note = (TextView) v.findViewById(R.id.textRow);
            delete = (CheckBox) v.findViewById(R.id.remove);
            check = (CheckBox) v.findViewById(R.id.check);


        }

        @Override
        public String toString() {
            return super.toString() + " '" + note.getText() + "'";
        }
    }
}