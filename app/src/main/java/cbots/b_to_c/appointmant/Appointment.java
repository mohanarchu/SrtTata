package cbots.b_to_c.appointmant;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import cbots.b_to_c.appointmant.alerm.Alerm;
import cbots.b_to_c.base.FragmentBase;
import cbots.b_to_c.config.Checkers;
import cbots.b_to_c.details.DetailsView;
import cbots.b_to_c.details.SharedArray;
import cbots.b_to_c.holder.A;
import cbots.b_to_c.holder.HolderClicked;
import cbots.b_to_c.home.DataModel;
import cbots.b_to_c.home.DataPojo;
import cbots.b_to_c.home.DataPresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cbots.b_to_c.R;
import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class Appointment extends FragmentBase implements DataModel, HolderClicked {


    @BindView(R.id.appointmentRecycler)
    RecyclerView appointmentRecycler;
    @BindView(R.id.searchAppointment)
    SearchView searchAppointment;
    private A adapter;
    public Appointment() {
        // Required empty public constructor
    }
    private List<DataPojo.Results> list = new ArrayList<>();
    private DataPresenter dataPresenter;
    boolean value = true,value1 = true;
    @Override
    protected void onViewBound(View view) {

      dataPresenter    = new DataPresenter(getActivity(), this);
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
            loadItems();
//            adapter   = new A(getActivity(), true, SharedArray.getResult(),false,true,this,null);
//            appointmentRecycler.setAdapter(adapter);
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
                adapter.getFilter().filter(query);
                return false;
            }
        });
    }
    private void loadItems(){
        List<DataPojo.Results> results = new ArrayList<>();
        for (int i=0;i<SharedArray.getResult().size();i++){
            DataPojo.Results re = SharedArray.getResult().get(i);
            if (addDatas(re) != null){
                results.add(re);
            }
        }
        adapter   = new A(getActivity(), true, results,false,true,this,null);
        appointmentRecycler.setAdapter(adapter);
    }
    @SuppressLint("NewApi")
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
        if (result.getAddDocs() != null){
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
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!isHidden()){
            loadItems();
        }

    }

    @Override
    protected int layoutRes() {
        return R.layout.fragment_appointment;
    }

    @Override
    public void showDatas(DataPojo.Results[] results,DataPojo.Count[] counts,int total,int alarmCount) {
          loadItems();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 102 && resultCode == Activity.RESULT_OK) {
            dataPresenter.getDetails(Checkers.getUserToken(Objects.requireNonNull(getActivity())) ,
                    Checkers.getName(getActivity()),String.valueOf(Checkers.getRoleId(getActivity())), new JsonObject());
        }
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

    @Override
    public void clicked(int position, boolean type, List<DataPojo.Results> list,boolean alarm) {

        SharedArray.setFilterResult(list);
        if (alarm){
            Intent intent = new Intent(getActivity(), Alerm.class);
            intent.putExtra("position",position);

            startActivityForResult(intent,102);
        }else {
            Intent intent = new Intent(getActivity(), DetailsView.class);
            intent.putExtra("transitionName", "");
            intent.putExtra("position", position);
            intent.putExtra("type", type);
            intent.putExtra("filter",true);
            startActivityForResult(intent, 102);
        }
    }
}
