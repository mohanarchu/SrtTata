package com.example.srttata.search;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.srttata.BaseActivity;
import com.example.srttata.R;
import com.example.srttata.details.SharedArray;
import com.example.srttata.holder.A;
import com.example.srttata.home.DataModel;
import com.example.srttata.home.DataPojo;
import com.example.srttata.home.DataPresenter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends BaseActivity {

    @BindView(R.id.CommonSearchIcon)
    SearchView searchView;
    A adapter;
    DataPresenter dataPresenter;
    @BindView(R.id.searchRecycler)
    RecyclerView searchRecycler;
    boolean values = true;
    boolean value1 = false;
    @Override
    protected void onViewBound() {
        Intent intent = getIntent();
        if (intent != null){
            values = intent.getBooleanExtra("bool",true);
            value1 = intent.getBooleanExtra("bools",false);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                searchView.setActivated(true);
                searchView.onActionViewExpanded();
                searchView.setIconified(false);
                searchView.setFocusable(true);
                if (SharedArray.getResult() != null){
                    adapter = new A(getApplicationContext(), values, SharedArray.getResult(),true,false);
                    searchRecycler.setAdapter(adapter);
                }
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        // filter recycler view when query submitted
                        adapter.getFilter().filter(query);
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String query) {
                        // filter recycler view when text is changed
                        adapter.getFilter().filter(query);
                        return false;
                    }
                });
            }
        }, 500);
        EditText et = (EditText) searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        ImageView searchItem = searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        searchItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et.setText("");
                searchView.setIconified(false);
                //   searchAppointment.setQueryHint(getString(R.string.Search));
                searchView.setQuery("", false);
                searchView.onActionViewExpanded();
                searchView.clearFocus();
                finish();
            }
        });

        LinearLayoutManager centerZoomLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        searchRecycler.setLayoutManager(centerZoomLayoutManager);


    }

    @Override
    protected int layoutRes() {
        return R.layout.activity_search;
    }




}
