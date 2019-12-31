package com.example.srttata.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.srttata.R;

import java.util.Objects;

public class SnapCenterListener extends RecyclerView.OnScrollListener {

    private final Object object;
    Context context;
    int prevCenterPos;

    public interface CenterChangeListener {
        void onCenterViewChange(View view, int position);
    }

    public SnapCenterListener() {
        this(null);
    }

    public SnapCenterListener(Object object) {
        this.object = object;
    }

    @Override
    public void onScrollStateChanged(RecyclerView rv, int state) {
        super.onScrollStateChanged(rv, state);
        if (state == RecyclerView.SCROLL_STATE_IDLE) {
            LinearLayoutManager lm = (LinearLayoutManager) rv.getLayoutManager();
            setScroll(rv, lm);
        }
    }
    private void setScroll(RecyclerView rv, LinearLayoutManager lm) {
        int mid = rv.getHeight() / 2;

        int minDistance = Integer.MAX_VALUE;
        View resultView = null;
        int position = 0;
        int result = 0;
        for (int i = lm.findFirstVisibleItemPosition(); i <= lm.findLastVisibleItemPosition(); i++) {
            View view = lm.findViewByPosition(i);
            int difference = getDiff(view, mid, true);
            int distance = Math.abs(difference);
            View prevViews = Objects.requireNonNull(rv.getLayoutManager()).findViewByPosition(i);
            assert prevViews != null;
            @SuppressLint("CutPasteId") TextView button = prevViews.findViewById(R.id.vocab);
            button.setTextColor(Color.CYAN);
            Log.i("TAG","PostionView"+lm.findFirstVisibleItemPosition()+"  "+i+"  "+ lm.findLastVisibleItemPosition());
            minDistance = distance;
            resultView = view;
            result = difference;
            assert view != null;
            position = lm.getPosition(view);
            View prevView = Objects.requireNonNull(rv.getLayoutManager()).findViewByPosition( position +1 );
            if (prevView != null) {
                TextView buttons = prevView.findViewById(R.id.vocab);
                buttons.setTextColor(Color.YELLOW);
            }
            if (distance < minDistance) {

            }
        }
        RecyclerView.Adapter adapter = rv.getAdapter();
        if (adapter instanceof CenterChangeListener) {
            ((CenterChangeListener) adapter).onCenterViewChange(resultView, position);
        }
        if (object != null) {
            ((CenterChangeListener) object).onCenterViewChange(resultView, position);
        }
        // to compensate rounding errors
        if (position == 0 && result == -1) {
            result = 0;
        }
        if (true) {
            rv.smoothScrollBy(result, 0);
        } else {
            rv.smoothScrollBy(0, result);
        }
    }

    private int getDiff(View v, int mid, boolean horizontal) {
        return  (horizontal)? (v.getLeft() + (v.getRight() - v.getLeft()) / 2) - mid :
                (v.getTop() + (v.getBottom() - v.getTop()) / 2) - mid;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
    }
}