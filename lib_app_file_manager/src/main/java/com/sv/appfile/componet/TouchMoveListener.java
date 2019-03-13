package com.sv.appfile.componet;

import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class TouchMoveListener implements View.OnTouchListener {
    private int initialX;
    private int initialY;
    private float initialTouchX;
    private float initialTouchY;

    private Callback callback;
    private WindowManager.LayoutParams params;

    public TouchMoveListener(WindowManager.LayoutParams params, Callback callback) {
        this.callback = callback;
        this.params = params;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                //remember the initial position.
                initialX = params.x;
                initialY = params.y;

                //get the touch location
                initialTouchX = event.getRawX();
                initialTouchY = event.getRawY();
                return true;
            case MotionEvent.ACTION_UP:
                int xdiff = (int) (event.getRawX() - initialTouchX);
                int ydiff = (int) (event.getRawY() - initialTouchY);

                //The check for Xdiff <10 && YDiff< 10 because sometime elements moves a little while clicking.
                //So that is click event.
                if (xdiff < 10 && ydiff < 10) {
                    if(callback != null){
                        callback.onClick(xdiff, ydiff);
                    }
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                //Calculate the X and Y coordinates of the view.
                params.x = initialX + (int) (event.getRawX() - initialTouchX);
                params.y = initialY + (int) (event.getRawY() - initialTouchY);

                //Update the layout with new X & Y coordinate
                if(callback != null){
                    callback.onMove(params);
                }
                return true;
            default: break;
        }
        return false;
    }
    public interface Callback{
        void onClick(int xdiff, int ydiff);
        void onMove(WindowManager.LayoutParams params);
    }
}
