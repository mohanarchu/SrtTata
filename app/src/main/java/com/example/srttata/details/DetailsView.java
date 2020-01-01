package com.example.srttata.details;


import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.example.srttata.BaseActivity;
import com.example.srttata.MainActivity;
import com.example.srttata.R;
import com.example.srttata.base.FragmentBase;
import com.example.srttata.config.Checkers;
import com.example.srttata.config.DateConversion;
import com.example.srttata.holder.A;
import com.example.srttata.holder.DocumentAdapter;
import com.example.srttata.home.DataPojo;
import com.github.angads25.toggle.interfaces.OnToggledListener;
import com.github.angads25.toggle.model.ToggleableView;
import com.github.angads25.toggle.widget.LabeledSwitch;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsView extends BaseActivity implements UpdateModel {

    @BindView(R.id.transtionLayout)
    LinearLayout transtionLayout;
    @BindView(R.id.date)
    TextView date;
    @BindView(R.id.dayOfweek)
    TextView dayOfweek;

    @BindView(R.id.relative)
    RelativeLayout relative;
    @BindView(R.id.notoficationChoooser)
    ConstraintLayout notoficationChoooser;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.mobileNumbr)
    TextView mobileNumbr;
    @BindView(R.id.tokenNumber)
    TextView tokenNumber;
    @BindView(R.id.addressReccyler)
    RecyclerView addressReccyler;
    @BindView(R.id.sharedLayout)
    LinearLayout sharedLayout;
    @BindView(R.id.swipe_layout)
    SwipeRevealLayout swipeLayout;
    @BindView(R.id.fullAddress)
    TextView fullAddress;
    @BindView(R.id.customerType)
    TextView customerType;
    @BindView(R.id.alternamtePhone)
    EditText alternamtePhone;
    @BindView(R.id.switchAlternateNumber)
    LabeledSwitch switchAlternateNumber;
    @BindView(R.id.documentRecycler)
    RecyclerView documentRecycler;
    @BindView(R.id.visiblelayout)
    RelativeLayout visiblelayout;
    @BindView(R.id.clickLayout)
    RelativeLayout clickLayout;
    @BindView(R.id.closeEdit)
    LinearLayout closeEdit;
    @BindView(R.id.saveEdit)
    LinearLayout saveEdit;
    @BindView(R.id.dullLayout)
    CardView dullLayout;
    @BindView(R.id.remarksExe)
    TextView remarksEze;
    @BindView(R.id.displayAddress)
    TextView displayAddress;
    @BindView(R.id.linear)
    LinearLayout remarksBox;
    @BindView(R.id.fixedAppointments)
    ImageView fixedAppoinments;
    boolean alternareState;
    UpdatePresenter updatePresenter;
    public static boolean[] booleans,addBooleans;
    public static String[] dates1,addDates;
     List<UploadDatas> uploadData,addData;
    DataPojo.Results results;
    @Override
    protected void onViewBound() {
        uploadData = new ArrayList<>();
        addData = new ArrayList<>();
        updatePresenter = new UpdatePresenter(getApplicationContext(),this);
        Intent intent = getIntent();
        if (intent != null) {
            String transitionName = intent.getStringExtra("transitionName");
            int position =  intent.getIntExtra("position",0);
            boolean type =  intent.getBooleanExtra("type",false);
            if (type){
                remarksBox.setVisibility(View.GONE);
            }
          results =  SharedArray.getResult().get(position);
            showDetails(results);
        }

        new Handler().postDelayed(() -> visiblelayout.setVisibility(View.VISIBLE),600);

        closeEdit.setOnClickListener(view -> finish());

        switchAlternateNumber.setOnToggledListener((toggleableView, isOn) -> alternareState = isOn);

        saveEdit.setOnClickListener(view -> {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("alternateMobileNumber", Checkers.getText(alternamtePhone));
            jsonObject.addProperty("alternateMobileToggle",alternareState);
            JsonArray  jsonElements = new JsonArray();
            for (int i=0;i<uploadData.size();i++){
                JsonObject jsons= new JsonObject();
                jsons.addProperty("date",DateConversion.getServerDate(dates1[i]));
                jsons.addProperty("proof",uploadData.get(i).getProof());
                jsons.addProperty("checked",uploadData.get(i).getChecked());
                jsons.addProperty("collected",booleans[i]);
                jsonElements.add(jsons);
            }

            JsonArray addDocsElements = new JsonArray();
            for (int i=0;i<addData.size();i++){
                JsonObject jsons= new JsonObject();
                jsons.addProperty("date",DateConversion.getServerDate(addDates[i]));
                jsons.addProperty("proof",uploadData.get(i).getProof());
                jsons.addProperty("checked",uploadData.get(i).getChecked());
                jsons.addProperty("collected",booleans[i]);
                addDocsElements.add(jsons);
            }
            jsonObject.add("docs",jsonElements);
            jsonObject.add("addDocs",addDocsElements);
            if (results != null){
                updatePresenter.update(results.getOrderNo(),jsonObject);
            }
            Log.d("TAG","documents"+jsonObject);
        });
    }

    private void showDetails(DataPojo.Results result){
        List<String> elephantList = Arrays.asList(DateConversion.getDay(result.getOrderDate()).split("-"));
        date.setText(elephantList.get(0).trim());
        dayOfweek.setText(elephantList.get(1).toUpperCase());
        String upperStringName = result.getContactName().substring(0, 1).toUpperCase() + result.getContactName().substring(1);
        name.setText(upperStringName);
        mobileNumbr.setText(result.getContactPhones());
        String part = result.getOrderNo().substring(result.getOrderNo().length() - 6);
        tokenNumber.setText(part);
        alternamtePhone.setText(  result.getAlternateMobileNumber() ==  null ?
                "---" : result.getAlternateMobileNumber());
        fullAddress.setText(result.getContactFullAddress());
        LinearLayoutManager centerZoomLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        documentRecycler.setLayoutManager(centerZoomLayoutManager);
        documentRecycler.setAdapter(new DocumentAdapter(result.getDocs(),result.getAddDocs(),getApplicationContext()));
        switchAlternateNumber.setOn(Boolean.getBoolean(result.getAlternateMobileToggle()));
        remarksEze.setText(result.getExeRemarks());
        List<String> dates = Arrays.asList(result.getContactFullAddress().split(","));
        displayAddress.setText(dates.get(0)+","+dates.get(1));
        alternareState = Boolean.valueOf(result.getAlternateMobileToggle());
        booleans = new boolean[result.getDocs().length];
        addBooleans = new boolean[result.getAddDocs().length];
        dates1 = new String[result.getDocs().length];
        addDates = new String[result.getAddDocs().length];
        if (result.getDocs() != null) {
            for (int i=0;i<result.getDocs().length;i++){
                UploadDatas uploadDatas = new UploadDatas();
                uploadDatas.setChecked(Boolean.valueOf(result.getDocs()[i].getChecked()));
                uploadDatas.setCollected(Boolean.valueOf(result.getDocs()[i].getCollected()));
                uploadDatas.setDate(result.getDocs()[i].getDate() == null ? "" : result.getDocs()[i].getDate());
                uploadDatas.setProof(result.getDocs()[i].getProof());
                dates1[i] = result.getDocs()[i].getDate() == null ? "" : result.getDocs()[i].getDate();
                booleans[i] = Boolean.valueOf(result.getDocs()[i].getCollected());
                uploadData.add(uploadDatas);
            }
        }
        if (result.getAddDocs()  != null){
            for (int i=0;i<result.getAddDocs().length;i++){
                UploadDatas uploadDatas = new UploadDatas();
                uploadDatas.setChecked(Boolean.valueOf(result.getAddDocs()[i].getChecked()));
                uploadDatas.setCollected(Boolean.valueOf(result.getAddDocs()[i].getCollected()));
                uploadDatas.setDate(result.getAddDocs()[i].getDate() == null ? "" : result.getAddDocs()[i].getDate());
                uploadDatas.setProof(result.getAddDocs()[i].getProof());
                addDates[i] = result.getAddDocs()[i].getDate() == null ? "" : result.getAddDocs()[i].getDate();
                addBooleans[i] = Boolean.valueOf(result.getAddDocs()[i].getCollected());
                addData.add(uploadDatas);
            }
        }


        if (result.getAlarm() != null || Boolean.valueOf(result.getAlarm()))
            fixedAppoinments.setVisibility(View.VISIBLE);
        else
            fixedAppoinments.setVisibility(View.GONE);

    }
    void addJson(DataPojo.Results result) {

    }

    @Override
    protected int layoutRes() {
        return R.layout.fragment_details_view;
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
