package com.shareandsearchfood.shareandsearchfood;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by tiagomota on 17/10/16.
 */

public class Searchable {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }
    }
}
