package com.example.srttata.appointmant;


import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.srttata.MainActivity;
import com.example.srttata.R;
import com.example.srttata.base.FragmentBase;
import com.example.srttata.config.Checkers;
import com.example.srttata.details.SharedArray;
import com.example.srttata.holder.A;
import com.example.srttata.home.DataModel;
import com.example.srttata.home.DataPojo;
import com.example.srttata.home.DataPresenter;
import com.example.srttata.search.SearchActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class Appointment extends FragmentBase implements DataModel {


    @BindView(R.id.appointmentRecycler)
    RecyclerView appointmentRecycler;
    @BindView(R.id.searchAppointment)
    SearchView searchAppointment;
    A adapter;
    public Appointment() {
        // Required empty public constructor
    }
    List<DataPojo.Results> list = new ArrayList<>();

    @Override
    protected void onViewBound(View view) {

        DataPresenter dataPresenter = new DataPresenter(getActivity(), this);
      //   dataPresenter.getDetails(Checkers.getUserToken(Objects.requireNonNull(getActivity())));
        LinearLayoutManager centerZoomLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        appointmentRecycler.setLayoutManager(centerZoomLayoutManager);
        // listening to search query text change
        EditText et = (EditText)searchAppointment. findViewById(androidx.appcompat.R.id.search_src_text);
        ImageView searchItem = searchAppointment.findViewById(androidx.appcompat.R.id.search_close_btn);
        searchItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et.setText("");
                searchAppointment.setIconified(false);
             //   searchAppointment.setQueryHint(getString(R.string.Search));
                searchAppointment.setQuery("", false);
                searchAppointment.onActionViewExpanded();
                searchAppointment.clearFocus();
            }
        });

        if (SharedArray.getResult() != null){
            list = SharedArray.getResult();
            adapter   = new A(getActivity(), true, SharedArray.getResult(),false,true);
            appointmentRecycler.setAdapter(adapter);
        }
        searchAppointment.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        MainActivity.commonSearch.setOnClickListener(view1 -> {
            if (list != null){
                    SharedArray.setArray(list);
                    Intent intent = new Intent(getActivity(), SearchActivity.class);
                    intent.putExtra("bool",true);
                    intent.putExtra("bools",true);
                    startActivity(intent);
                } else {
                    showMessage("Details are empty");
                }
        });
    }

    @Override
    protected int layoutRes() {
        return R.layout.fragment_appointment;
    }

    @Override
    public void showDatas(DataPojo.Results[] results,DataPojo.Count[] counts,int total,int alarmCount) {
        list = new ArrayList<DataPojo.Results>(Arrays.asList(results));
        adapter   = new A(getActivity(), true, list,false,true);
        appointmentRecycler.setAdapter(adapter);
    }

    @Override
    public void success() {

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showMessage(String message) {

    }
}
