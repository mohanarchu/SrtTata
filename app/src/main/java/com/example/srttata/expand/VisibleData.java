package com.example.srttata.expand;

import android.view.View;

public class VisibleData {
    private int visibility = View.VISIBLE;
    private int visibilitys = View.GONE;
    public VisibleData(  int longitude) {
        this.visibilitys = longitude;

    }
    public int getVisibility() {
        return visibilitys;
    }

    public void setVisibility(int visibility) {
        this.visibilitys = visibility;
    }

}
