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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;


import com.shareandsearchfood.Adapters.MyPubsRecyclerViewAdapter;
import com.shareandsearchfood.login.App;
import com.shareandsearchfood.login.DaoSession;
import com.shareandsearchfood.login.Receipt;
import com.shareandsearchfood.login.ReceiptDao;
import com.shareandsearchfood.login.Session;
import com.shareandsearchfood.login.User;
import com.shareandsearchfood.login.UserDao;
import com.shareandsearchfood.shareandsearchfood.R;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;


public class ShareFragment extends Fragment {

    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private enum LayoutManagerType {
        LINEAR_LAYOUT_MANAGER
    }
    LinearLayout mLayout2;
    EditText mEditText2;
    Button mButton2;
    AutoCompleteTextView title_receipt;
    AutoCompleteTextView ingredients;
    AutoCompleteTextView steps;
    LinearLayout mLayout;
    EditText mEditText;
    Button mButton;
    Button saveReceipt;
    Button pubReceipt;

    protected LayoutManagerType mCurrentLayoutManagerType;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ShareFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_share, container, false);
        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }


        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // save views as variables in this method
        // "view" is the one returned from onCreateView

        mLayout2 = (LinearLayout) view.findViewById(R.id.layoutSteps);
        mEditText2 = (EditText) view.findViewById(R.id.Step_by_Step);
        mButton2 = (Button) view.findViewById(R.id.moreSteps);

        title_receipt = (AutoCompleteTextView) view.findViewById(R.id.title_receita_share);
        ingredients = (AutoCompleteTextView) view.findViewById(R.id.ingredients);
        steps = (AutoCompleteTextView) view.findViewById(R.id.Step_by_Step);

        mLayout = (LinearLayout) view.findViewById(R.id.layoutIngredientes);
        mEditText = (EditText) view.findViewById(R.id.ingredients);
        mButton = (Button) view.findViewById(R.id.moreIngredients);

        saveReceipt = (Button) view.findViewById(R.id.Save) ;
        pubReceipt = (Button) view.findViewById(R.id.Publish) ;


    }


}
