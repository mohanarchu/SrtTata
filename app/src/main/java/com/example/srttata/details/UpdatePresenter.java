package com.example.srttata.details;

import android.content.Context;
import android.util.Log;

import com.example.srttata.Clients.NetworkingUtils;
import com.example.srttata.config.Checkers;
import com.google.gson.JsonObject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class UpdatePresenter {
    Context context;
    UpdateModel updateModel;
    public UpdatePresenter(Context context,UpdateModel updateModel){

        this.context = context;
        this.updateModel = updateModel;
    }
    public void update(String orderId, JsonObject jsonObject,int position){

        NetworkingUtils.getUserApiInstance().updateData (Checkers.getUserToken(context),orderId,jsonObject).subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<UpdatePojo>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(UpdatePojo updatePojo) {

                if (Integer.valueOf(updatePojo.getStatus()) ==  200){
                    updateModel.showMessage("success");
                    updateModel.success(position);
                }

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }
    public void setAlarm(String orderId, JsonObject jsonObject,int position){

        NetworkingUtils.getUserApiInstance().setAlarm(Checkers.getUserToken(context),orderId,jsonObject).subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<UpdatePojo>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(UpdatePojo updatePojo) {

                Log.i("TAG","Alarm set"+ updatePojo.getMessage());

                if (Integer.valueOf(updatePojo.getStatus()) ==  200){
                    updateModel.showMessage("success");
                    updateModel.success(position);
                }

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

    }
    public void cancelAlarm(String orderId,  int alarmValue,int position) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("alarmInt", alarmValue);
        jsonObject.addProperty("id",orderId);
        NetworkingUtils.getUserApiInstance().cancelAlarm(Checkers.getUserToken(context), jsonObject).subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<UpdatePojo>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(UpdatePojo updatePojo) {

                Log.i("TAG","Canceled"+ updatePojo.getStatus() +"  "+ jsonObject);
                if (Integer.valueOf(updatePojo.getStatus()) == 200) {
                    updateModel.showMessage("success");
                    updateModel.success(position);
                }

            }

            @Override
            public void onError(Throwable e) {
                Log.i("TAG","Canceled"+ e.toString());
            }

            @Override
            public void onComplete() {

            }
        });
    }
}
