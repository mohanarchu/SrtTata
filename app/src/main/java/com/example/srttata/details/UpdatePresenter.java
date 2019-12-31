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
    public void update(String orderId, JsonObject jsonObject){

        NetworkingUtils.getUserApiInstance().updateData (Checkers.getUserToken(context),orderId,jsonObject).subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<UpdatePojo>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(UpdatePojo updatePojo) {

                Log.i("TAG","Update success"+ updatePojo.getStatus());
            }

            @Override
            public void onError(Throwable e) {
                Log.i("TAG","Update success"+ e.toString());
            }

            @Override
            public void onComplete() {

            }
        });
    }
}
