package cbots.b_to_c.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import cbots.b_to_c.Clients.MainInterface;
import cbots.b_to_c.Clients.NetworkingUtils;
import cbots.b_to_c.LoginScreen;
import cbots.b_to_c.MainActivity;

import cbots.b_to_c.config.Checkers;
import cbots.b_to_c.details.SharedArray;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DataPresenter  {

    private Context context;
    private DataModel dataModel;
    public DataPresenter(Context context, DataModel dataModel){
        this.dataModel = dataModel;
        this.context = context;
    }

   public void getDetails(String token, String name, String role, JsonObject jsonObject){
        dataModel.showProgress();
        //Checkers.getName(context)
        NetworkingUtils.getUserApiInstance().getDatas (token,name,role,jsonObject).subscribeOn(Schedulers.io()).
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
                    for (int i=0;i<dataPojo.getResults().length;i++){
                        if (dataPojo.getResults()[i] .getAlarms() != null){
                            for (DataPojo.Alarms alarms : dataPojo.getResults()[i] .getAlarms()){
                                List<String> elephantList = Arrays.asList(alarms .getAlarmDate().split(","));
                                Calendar calendar = Calendar.getInstance();
                                calendar.add(Calendar.MONTH , 1);
                                if (elephantList.get(0).equals(f.format(calendar.get(Calendar.DATE)) + "/" +f.format(calendar.get(Calendar.MONTH)) +
                                        "/" + calendar.get(Calendar.YEAR)))
                                        alermCount++;
                            }
                        }
                    }
                    ArrayList<DataPojo.Results>  list = new ArrayList<>(Arrays.asList(dataPojo.getResults()));
                    SharedArray.setArray(list);
                    SharedArray.setFilterResult(list);

                    List<DataPojo.Results>  lists = new ArrayList<>(Arrays.asList(dataPojo.getResults()));
                    lists = lists.stream().filter(pulse -> pulse.getPendingDocsCount() == null ||  !pulse.getPendingDocsCount().equals("0")).collect(Collectors.toList());

                    // bottom nav view badge
                    if(!role.equals(MainInterface.TEAMLEADER) && !role.equals(MainInterface.MASTER)) {
                        MainActivity.addSecondView(value+addDocus,context);
                        MainActivity.addThirdView( alermCount,context);
                        MainActivity.addFirstView(lists.size(),context);
                    }

                    dataModel.showDatas(dataPojo.getResults(),dataPojo.getCount(),value+addDocus,alermCount);
                } else {
                    dataModel.showMessage(dataPojo.getStatus());
                }
                dataModel.hideProgress();
            }
            @Override
            public void onError(Throwable e) {
                Log.i("TAG","My results" + e.toString());
                dataModel.hideProgress();
//                dataModel.showMessage("Try again or login again");
//                Checkers.clearLoggedInEmailAddress(context);
//                Intent myAct = new Intent(context, LoginScreen.class);
//                myAct.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context. startActivity(myAct);



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
