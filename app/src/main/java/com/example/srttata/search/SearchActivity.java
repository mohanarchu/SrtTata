package com.example.srttata.search;

import android.annotation.SuppressLint;
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
import com.example.srttata.appointmant.alerm.Alerm;
import com.example.srttata.details.DetailsView;
import com.example.srttata.details.SharedArray;
import com.example.srttata.holder.A;
import com.example.srttata.holder.HolderClicked;
import com.example.srttata.home.DataModel;
import com.example.srttata.home.DataPojo;
import com.example.srttata.home.DataPresenter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends BaseActivity implements HolderClicked {

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
            values = intent.getBooleanExtra("type",false);
            value1 = intent.getBooleanExtra("bools",false);
        }


        Log.i(  "TAG","Search value"+ values + value1);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                searchView.setActivated(true);
                searchView.onActionViewExpanded();
                searchView.setIconified(false);
                searchView.setFocusable(true);
                if (SharedArray.getResult() != null){
                    if (values && value1){
                        List<DataPojo.Results> results = new ArrayList<>();
                        for (int i=0;i<SharedArray.getResult().size();i++){
                            DataPojo.Results re = SharedArray.getResult().get(i);
                            if (addDatas(re) != null){
                                results.add(re);
                            }
                        }
                        adapter   = new A(getApplicationContext(), values, results,true,value1,
                                SearchActivity.this,null);
                        searchRecycler.setAdapter(adapter);
                    }else if (!value1 && !values){
                        Calendar calendar = Calendar.getInstance();
                        dateFilter(calendar);

                    }else {

                        adapter = new A(getApplicationContext(), values, getList(),true,value1,
                                SearchActivity.this,null);
                        searchRecycler.setAdapter(adapter);
                    }

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
    @SuppressLint("NewApi")
    List<DataPojo.Results> getList(){
        List<DataPojo.Results> list = SharedArray.getResult();
        list = list.stream().filter(pulse ->  pulse.getPendingDocsCount() == null || !pulse.getPendingDocsCount().equals("0")).collect(Collectors.toList());
        return list;
    }

    private void dateFilter(Calendar calendar){

        List<DataPojo.Results> result = new ArrayList<DataPojo.Results>();
        NumberFormat f = new DecimalFormat("00");
        for (DataPojo.Results person : getList()) {
            if (person.getAlarmDate() != null ){
                List<String> elephantList = Arrays.asList(person.getAlarmDate().split(","));
                if (elephantList.get(0).equals(
                        f.format(calendar.get(Calendar.DATE)) + "/" +f.format(calendar.get(Calendar.MONTH)+1) + "/" + calendar.get(Calendar.YEAR))) {
                    result.add(person);
                }
            }
        }

        //MainActivity.addThirdView(result.size(),getActivity());
        adapter   = new A(getApplicationContext(), false, result,false,false, this, null);
        searchRecycler.setAdapter(adapter);
    }

    private DataPojo.Results addDatas(DataPojo.Results result){
        StringBuilder values= new StringBuilder();
        if (result.getDocs() != null){
            for (int j=0;j<result.getDocs().length;j++){
                if (!Boolean.valueOf(result.getDocs()[j].getChecked()) &&
                        Boolean.valueOf(result.getDocs()[j].getCollected()) ){
                    values.append(result.getDocs()[j].getProof()).append(",");
                }
            }
        }
        if (result.getAddDocs() != null ){
            for (int j=0;j<result.getAddDocs().length;j++){
                if (!Boolean.valueOf(result.getAddDocs()[j].getChecked()) &&
                        Boolean.valueOf(result.getAddDocs()[j].getCollected()) ){
                    values.append(result.getAddDocs()[j].getProof());
                }
            }
        }
        if (!values.toString().isEmpty()){
            return  result;
        } else {
            return null;
        }
    }
    @Override
    protected int layoutRes() {
        return R.layout.activity_search;
    }


    @Override
    public void clicked(int position, boolean type, List<DataPojo.Results> list,boolean alarm) {
        if (alarm){
            Intent intent = new Intent(getApplicationContext(), Alerm.class);
            intent.putExtra("position",position);
            SharedArray.setFilterResult(list);
            startActivityForResult(intent,100);
        } else {
            Intent intent = new Intent(getApplicationContext(), DetailsView.class);
            intent.putExtra("transitionName", "");
            intent.putExtra("position", position);
            intent.putExtra("type", type);
            intent.putExtra("filter", false);
            SharedArray.setFilterResult(list);
            startActivityForResult(intent, 100);
        }
    }
}
