package com.example.srttata.home;

import com.example.srttata.base.ViewModel;

public interface DataModel extends ViewModel {
    void showDatas(DataPojo.Results[] results,DataPojo.Count[] counts,int total,int alarmCount);

    void success();

    @Override
    void showProgress();

    @Override
    void hideProgress();

    @Override
    void showMessage(String message);
}
