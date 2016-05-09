package com.weigan.googlephotoselect;

import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Administrator on 2016/5/7.
 */
public class DragSelectTouchListener implements RecyclerView.OnItemTouchListener {

    private boolean isActive;
    private int start, end;

    private onSelectListener selectListener;

    private RecyclerView recyclerView;

    private static final int DELAY = 25;

    private int autoScrollDistance = (int) (Resources.getSystem().getDisplayMetrics().density * 56);

    private int mTopBound, mBottomBound;

    private boolean inTopSpot, inBottomSpot;

    private Handler autoScrollHandler = new Handler(Looper.getMainLooper());

    private int scrollDistance;

    private float lastX, lastY;

    private static final int MAX_SCROLL_DISTANCE = 28;

    private Runnable scrollRunnable = new Runnable() {
        @Override
        public void run() {
            if (!inTopSpot && !inBottomSpot) {
                return;
            }
            scrollBy(scrollDistance);
            autoScrollHandler.postDelayed(this, DELAY);
        }
    };

    public void setSelectListener(onSelectListener selectListener) {
        this.selectListener = selectListener;
    }

    public interface onSelectListener{
        void onSelect(int start, int end);

        void reset();
    };

    public DragSelectTouchListener() {
        reset();
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        if (!isActive || rv.getAdapter().getItemCount() == 0) {
            return false;
        }
        int action = MotionEventCompat.getActionMasked(e);
        switch (action) {
            case MotionEvent.ACTION_POINTER_DOWN:
                Log.d("debug", "onInterceptTouchEvent ACTION_POINTER_DOWN");
                break;
            case MotionEvent.ACTION_DOWN:
                Log.d("debug", "onInterceptTouchEvent ACTION_DOWN");
                reset();
                break;
        }
        recyclerView = rv;
        int height = rv.getHeight();
        mTopBound = -20;
        mBottomBound = height - autoScrollDistance;
        return true;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        int action = MotionEventCompat.getActionMasked(e);
        switch (action) {
            case MotionEvent.ACTION_MOVE:
                if (!inTopSpot && !inBottomSpot) {
                    updateSelectedRange(rv, e);
                }
                processAutoScroll(e);
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.d("debug", "ACTION_POINTER_UP");
            case MotionEvent.ACTION_UP:
                Log.d("debug", "ACTION_UP");
            case MotionEvent.ACTION_POINTER_UP:
                Log.d("debug", "ACTION_POINTER_UP");
                reset();
                break;
        }
    }

    private void updateSelectedRange(RecyclerView rv, MotionEvent e) {
        updateSelectedRange(rv, e.getX(), e.getY());
    }

    private void updateSelectedRange(RecyclerView rv, float x, float y) {
        View child = rv.findChildViewUnder(x, y);
        if (child != null) {
            int position = rv.getChildAdapterPosition(child);
            if (position != RecyclerView.NO_POSITION && end != position) {
                end = position;
                notifySelectRangeChange();
            }
        }
    }


    private void processAutoScroll(MotionEvent event) {
        int y = (int) event.getY();
        if (y < mTopBound) {

            if (!inTopSpot) {
                inTopSpot = true;
                autoScrollHandler.removeCallbacks(scrollRunnable);
                autoScrollHandler.postDelayed(scrollRunnable, DELAY);
            }
            lastX = event.getX();
            lastY = event.getY();
            scrollDistance = -(mTopBound - y) / 3;
        }else if (y > mBottomBound) {
            if (!inBottomSpot) {
                inBottomSpot = true;
                autoScrollHandler.removeCallbacks(scrollRunnable);
                autoScrollHandler.postDelayed(scrollRunnable, DELAY);
            }
            lastX = event.getX();
            lastY = event.getY();
            scrollDistance = (y - mBottomBound) / 3;
        } else {
            autoScrollHandler.removeCallbacks(scrollRunnable);
            inBottomSpot = false;
            inTopSpot = false;
            lastX = Float.MIN_VALUE;
            lastY = Float.MIN_VALUE;
        }
    }

    private void notifySelectRangeChange() {
        if (selectListener != null) {
            selectListener.onSelect(start, end);
        }
    }

    private void reset() {
        setIsActive(false);
        start = RecyclerView.NO_POSITION;
        end = RecyclerView.NO_POSITION;
        if (selectListener != null) {
            selectListener.reset();
        }
        autoScrollHandler.removeCallbacks(scrollRunnable);
        inTopSpot = false;
        inBottomSpot = false;
        lastX = Float.MIN_VALUE;
        lastY = Float.MIN_VALUE;
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    private void scrollBy(int distance) {
        int scrollDistance;
        if (distance > 0) {
            scrollDistance = Math.min(distance, MAX_SCROLL_DISTANCE);
        } else {
            scrollDistance = Math.max(distance, -MAX_SCROLL_DISTANCE);
        }
        recyclerView.scrollBy(0, scrollDistance);
        if (lastX != Float.MIN_VALUE && lastY != Float.MIN_VALUE) {
            updateSelectedRange(recyclerView, lastX, lastY);
        }
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public void setStartSelectPosition(int position) {
        setIsActive(true);
        start = position;
        end = position;
    }
}
