package cbots.b_to_c.home;

import cbots.b_to_c.base.ViewModel;

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
