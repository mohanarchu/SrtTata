package cbots.b_to_c.decorations;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import androidx.recyclerview.widget.RecyclerView;

public class OffsetItemDecoration extends RecyclerView.ItemDecoration {
    private int firstViewWidth = -1;
    private int lastViewWidth = -1;
    private Context ctx;
   private  int edgePadding,viewWidth;
    public OffsetItemDecoration(Context ctx, int edgePadding, int viewWidth) {

        this.ctx = ctx;
        this.edgePadding = edgePadding;
        this.viewWidth = viewWidth;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        super.getItemOffsets(outRect, view, parent, state);



        final int itemPosition = parent.getChildAdapterPosition(view);
        if (itemPosition == RecyclerView.NO_POSITION) {
            return;
        }

        final int itemCount = state.getItemCount();

        /** first position */
        if (itemPosition == 0) {
           view.setVisibility(View.INVISIBLE);
        }
        /** last position */
        else if (itemCount > 0 && itemPosition == itemCount - 1) {
            view.setVisibility(View.INVISIBLE);
        }
        /** positions between first and last */
        else {
            view.setVisibility(View.VISIBLE);
        }

    }
    private void setupOutRect(Rect rect, int offset, boolean start) {
        if (start) {
            rect.left = offset;
        } else {
            rect.right = offset;
        }
    }
    private int getScreenWidth() {
        WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }
}