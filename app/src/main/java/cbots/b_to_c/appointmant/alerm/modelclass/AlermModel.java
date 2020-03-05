package cbots.b_to_c.appointmant.alerm.modelclass;

import cbots.b_to_c.base.ViewModel;

public interface AlermModel extends ViewModel {
    @Override
    void showProgress();

    @Override
    void hideProgress();

    @Override
    void showMessage(String message);

    void showResults();
}
