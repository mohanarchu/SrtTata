package com.example.srttata.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.srttata.Clients.NetworkingUtils;
import com.example.srttata.LoginScreen;
import com.example.srttata.MainActivity;
import com.example.srttata.R;
import com.example.srttata.base.ViewModel;
import com.example.srttata.config.Checkers;
import com.example.srttata.config.DateConversion;
import com.example.srttata.details.SharedArray;
import com.example.srttata.login.LoginPojo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

public class DataPresenter  {

    private Context context;
    private DataModel dataModel;
    public DataPresenter(Context context, DataModel dataModel){
        this.dataModel = dataModel;
        this.context = context;
    }

   public void getDetails(String token){
        dataModel.showProgress();
        NetworkingUtils.getUserApiInstance().getDatas (token,Checkers.getName(context)).subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<DataPojo>() {
            @Override
            public void onSubscribe(Disposable d) {

            }
            @SuppressLint("NewApi")
            @Override
            public void onNext(DataPojo dataPojo) {
                if (dataPojo.getStatus().equals("200")) {
                    Checkers.setUserLoggedInStatus(context,true);
                    int value = 0,addDocus=0,alermCount = 0;
                    for (int i=0;i<dataPojo.getResults().length;i++) {
                        if (dataPojo.getResults()[i].getDocs() != null) {
                            for (int j=0;j<dataPojo.getResults()[i].getDocs().length;j++) {
                            if (!Boolean.valueOf(dataPojo.getResults()[i].getDocs()[j].getChecked()) &&
                                    Boolean.valueOf(dataPojo.getResults()[i].getDocs()[j].getCollected())) {
                                value++;
                               }
                            }
                        }
                        if (dataPojo.getResults()[i].getAddDocs() != null){
                            for (int j=0;j<dataPojo.getResults()[i].getAddDocs().length;j++){
                                if (!Boolean.valueOf(dataPojo.getResults()[i].getAddDocs()[j].getChecked()) &&
                                        Boolean.valueOf(dataPojo.getResults()[i].getAddDocs()[j].getCollected()) ) {
                                    addDocus++;
                                }
                            }
                        }
                    }
                    NumberFormat f = new DecimalFormat("00");
                    Calendar calendar = Calendar.getInstance();
                    for (int i=0;i<dataPojo.getResults().length;i++){
                        if (dataPojo.getResults()[i] .getAlarmDate() != null){
                            List<String> elephantList = Arrays.asList(dataPojo.getResults()[i] .getAlarmDate().split(","));
                            if (elephantList.get(0).equals(f.format(calendar.get(Calendar.DATE)) + "/" +f.format(calendar.get(Calendar.MONTH)+1) + "/" + calendar.get(Calendar.YEAR)))
                                if (Boolean.valueOf(dataPojo.getResults()[i].getAlarm()))
                                    alermCount++;
                        }
                    }
                    MainActivity.addSecondView(value+addDocus,context);
                    MainActivity.addThirdView( alermCount,context);
                    ArrayList<DataPojo.Results>  list = new ArrayList<>(Arrays.asList(dataPojo.getResults()));
                    SharedArray.setArray(list);
                    SharedArray.setFilterResult(list);
                    List<DataPojo.Results>  lists = new ArrayList<>(Arrays.asList(dataPojo.getResults()));
                    lists = lists.stream().filter(pulse -> pulse.getPendingDocsCount() == null ||  !pulse.getPendingDocsCount().equals("0")).collect(Collectors.toList());
                    MainActivity.addFirstView(lists.size(),context);
                    dataModel.showDatas(dataPojo.getResults(),dataPojo.getCount(),value+addDocus,alermCount);
                } else {
                    dataModel.showMessage(dataPojo.getStatus());
                }
            }
            @Override
            public void onError(Throwable e) {
                dataModel.showMessage("Try again or login again");
                Checkers.clearLoggedInEmailAddress(context);
                Intent myAct = new Intent(context, LoginScreen.class);
                myAct.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context. startActivity(myAct);
//                HttpException error = (HttpException)e;
//                try {
//                    String errorBody = error.response().errorBody().string();
//                    JSONObject jsonObject = new JSONObject(errorBody);
//                    dataModel.showMessage("Invalid Token");
//                } catch (IOException | JSONException e1) {
//                    e1.printStackTrace();
//                 }
            }

            @Override
            public void onComplete() {

            }
        });
    }

}
