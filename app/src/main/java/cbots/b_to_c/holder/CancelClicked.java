package cbots.b_to_c.holder;

import cbots.b_to_c.home.DataPojo;

import java.util.List;

public interface CancelClicked {
    void cancel(int position,  List<DataPojo.Results> list , String time);
}
