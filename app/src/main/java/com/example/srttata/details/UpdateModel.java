package com.example.srttata.details;

import com.example.srttata.base.ViewModel;

public interface UpdateModel extends ViewModel {

    @Override
    void showProgress();

    @Override
    void hideProgress();

    @Override
    void showMessage(String message);

    void success(int pos);

}
