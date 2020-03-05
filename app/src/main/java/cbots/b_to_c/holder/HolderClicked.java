package cbots.b_to_c.holder;

import cbots.b_to_c.home.DataPojo;

import java.util.List;

public interface HolderClicked {
    void clicked(int position, boolean type, List<DataPojo.Results> list,boolean alarm);
}
