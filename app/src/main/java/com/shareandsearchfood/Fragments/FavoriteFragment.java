package com.shareandsearchfood.Fragments;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.shareandsearchfood.Adapters.FavoriteRecyclerViewAdapter;
import com.shareandsearchfood.login.App;
import com.shareandsearchfood.login.DaoSession;
import com.shareandsearchfood.login.Receipt;
import com.shareandsearchfood.login.ReceiptDao;
import com.shareandsearchfood.login.Session;
import com.shareandsearchfood.login.User;
import com.shareandsearchfood.login.UserDao;
import com.shareandsearchfood.shareandsearchfood.Favorite;
import com.shareandsearchfood.shareandsearchfood.FavoriteDao;
import com.shareandsearchfood.shareandsearchfood.R;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListenerFav}
 * interface.
 */
public class FavoriteFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private OnListFragmentInteractionListenerFav mListener;
    SwipeRefreshLayout swipeLayout;
    protected RecyclerView mRecyclerView;
    protected FavoriteRecyclerViewAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 2;
    private Session session;
    private DaoSession daoSession;
    private ReceiptDao receiptDao;

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    protected LayoutManagerType mCurrentLayoutManagerType;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FavoriteFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_favorite_list, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        daoSession = ((App) getActivity().getApplication()).getDaoSession();
        receiptDao = daoSession.getReceiptDao();

        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);
        mAdapter = new FavoriteRecyclerViewAdapter(getUserFavReceipts(), getContext(), getActivity().getApplication(), mListener);
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container_fav);
        swipeLayout.setOnRefreshListener(this);

        return rootView;
    }


    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                mLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT);
                mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
            case LINEAR_LAYOUT_MANAGER:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        session = new Session(context);
        if (context instanceof OnListFragmentInteractionListenerFav) {
            mListener = (OnListFragmentInteractionListenerFav) context;

        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (receiptDao != null) {
            mAdapter.setNewData(getUserFavReceipts());
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;

    }

    @Override
    public void onRefresh() {
        mAdapter.setNewData(getUserFavReceipts());
        mAdapter.notifyDataSetChanged();
        swipeLayout.setRefreshing(false);

    }

    private List<Receipt> getUserFavReceipts(){
        long userId = getUserID(session.getEmail());
        FavoriteDao favoriteDao = daoSession.getFavoriteDao();
        QueryBuilder qb = favoriteDao.queryBuilder();
        qb.where(FavoriteDao.Properties.UserId.eq(userId));
        List<Favorite> favorites = qb.list();

        return getReceiptsById(favorites);

    }
    private List<Receipt> getReceiptsById(List<Favorite> favorites){
        ReceiptDao receiptDao= daoSession.getReceiptDao();
        List<Receipt> receipts = receiptDao.loadAll();
        List<Receipt> receiptsByID = new ArrayList<>();
        for (Receipt receipt: receipts) {
            for (Favorite favorite: favorites)  {
                if(receipt.getId() == favorite.getReceiptId())
                    receiptsByID.add(receipt);
            }
        }
        return  receiptsByID;
    }

    private Long getUserID(String email) {
        DaoSession daoSession = ((App) getActivity().getApplication()).getDaoSession();
        UserDao userDao = daoSession.getUserDao();
        QueryBuilder qb = userDao.queryBuilder();
        qb.where(UserDao.Properties.Email.eq(email));
        List<User> user = qb.list();
        return user.get(0).getId();
    }


    public interface OnListFragmentInteractionListenerFav {
        void onListFragmentInteractionFav(Receipt item);
    }
}