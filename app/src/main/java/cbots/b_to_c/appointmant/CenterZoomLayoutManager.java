package cbots.b_to_c.appointmant;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

public class CenterZoomLayoutManager extends LinearLayoutManager {

    private final float mShrinkAmount = 0.1f;
    private final float mShrinkDistance = 0.2f;

    private RecyclerView recyclerView;

    public CenterZoomLayoutManager(Context context, RecyclerView recyclerView) {
        super(context);
    }

    public CenterZoomLayoutManager(Context context, int orientation, boolean reverseLayout,RecyclerView recyclerView) {
        super(context, orientation, reverseLayout);
        this.recyclerView =recyclerView;
    }

    @Override
    public void requestLayout() {
        super.requestLayout();
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {

        int orientation =  getOrientation();
        if (orientation == VERTICAL) {
            int scrolled = super.scrollVerticallyBy(dy, recycler, state);
            float midpoint = getHeight() / 2.f;
            float d0 = 0.f;
            float d1 = mShrinkDistance * midpoint;
            float s0 = 1.f;
            float s1 = 1.f - mShrinkAmount;
            for (int i = 0; i < getChildCount(); i++) {

                View child = getChildAt(i);
                float childMidpoint =
                        (getDecoratedBottom(child) + getDecoratedTop(child)) / 2.f;
                float d = Math.min(d1, Math.abs(midpoint - childMidpoint));

                float scale = s0 + (s1 - s0) * (d - d0) / (d1 - d0);
                child.setScaleX(scale);
                child.setScaleY(scale);
            }

            return scrolled;
        } else {
            return 0;
        }
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        super.smoothScrollToPosition(recyclerView, state, position);
        LinearSmoothScroller smoothScroller = new LinearSmoothScroller(recyclerView.getContext()) {

            @Override
            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                return 90f / displayMetrics.densityDpi;
            }

        };


        smoothScroller.setTargetPosition(position);
        startSmoothScroll(smoothScroller);
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int orientation = getOrientation();
        if (orientation == HORIZONTAL) {
            int scrolled = super.scrollHorizontallyBy(dx, recycler, state);
            float midpoint = getWidth() / 2.f;
            float d0 = 0.f;
            float d1 = mShrinkDistance * midpoint;
            float s0 = 1.f;
            float s1 = 1.f - mShrinkAmount;
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                assert child != null;
                float childMidpoint =
                        (getDecoratedRight(child) + getDecoratedLeft(child)) / 2.f;
                float d = Math.min(d1, Math.abs(midpoint - childMidpoint));
                float scale = s0 + (s1 - s0) * (d - d0) / (d1 - d0);
                child.setScaleX(scale);
//                Log.i("TAG","Off set Pos"+ scale);
                child.setScaleY(scale);
                float halfWidth = child.getWidth() * 0.5f;
                float parentHalfWidth = recyclerView.getWidth() * 0.5f;
                float x = child.getX();
                float rot = parentHalfWidth - halfWidth - x;
                ViewCompat.setPivotY(child, 0.0f);
                ViewCompat.setPivotX(child, halfWidth);
                ViewCompat.setRotation(child, -rot * 0.05f);
                int mCircleOffset = 500;
                float mDegToRad = 1.0f / 180.0f * (float) Math.PI;
                float mTranslationRatio = 0.15f;
                ViewCompat.setTranslationY(child, (float) (-Math.cos(rot * mTranslationRatio * mDegToRad) + 1) * mCircleOffset);
                float mScalingRatio = 0.001f;
                float scales = 1.0f - Math.abs(parentHalfWidth - halfWidth - x) * mScalingRatio;
                ViewCompat.setScaleX(child, scales);
                ViewCompat.setScaleY(child, scales);

            }
            return scrolled;
        } else {
            return 0;
        }
    }

}