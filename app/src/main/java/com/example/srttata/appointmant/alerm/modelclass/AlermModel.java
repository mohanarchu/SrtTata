package com.example.srttata.appointmant.alerm.modelclass;

import com.example.srttata.base.ViewModel;

public interface AlermModel extends ViewModel {
    @Override
    void showProgress();

    @Override
    void hideProgress();

    @Override
    void showMessage(String message);

    void showResults();
}
