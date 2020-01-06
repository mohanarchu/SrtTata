package com.example.srttata.holder;

import com.example.srttata.home.DataPojo;

import java.util.List;

public interface HolderClicked {
    void clicked(int position, boolean type, List<DataPojo.Results> list,boolean alarm);
}
