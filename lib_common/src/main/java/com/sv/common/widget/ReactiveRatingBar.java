package com.sv.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.sv.common.R;
import com.sv.common.util.Logger;
import com.sv.common.util.UIUtils;

/**
 * @author sven-ou
 */
public class ReactiveRatingBar extends View {
    private static final String TAG = ReactiveRatingBar.class.getSimpleName();
    private Drawable lightStar;
    private Drawable darkStar;
    /**
     * 自动计算星星高度，宽度，间隔
     */
    private boolean isAutoCalculateHeight = true;
    /**
     * 星星数
     */
    private int numStars = 5;
    /**
     * 星星之间最大间隔
     */
    private float maxStarSpace = 50;
    /**
     * 星星与顶部或者底部的间隔
     */
    private float topBottomSpace = 0;
    /**
     * 星星的宽度
     */
    private float starWidth = 100;
    /**
     * 星星高度
     */
    private float starHeight = 100;
    /**
     * 星星分数（高亮星星数）
     */
    private int rating = 0;

    private Bitmap lightStarBitmap;
    private Bitmap darkStarBitmap;

    private ReactiveRatingBarListener reactiveRatingBarListener;

    public ReactiveRatingBar(Context context) {
        super(context);
        init(null, 0);
    }

    public ReactiveRatingBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public ReactiveRatingBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }
    private void init(AttributeSet attrs, int defStyle) {
        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ReactiveRatingBar, defStyle, 0);
        if (a.hasValue(R.styleable.ReactiveRatingBar_lightStar)) {
            final int drawableResId = a.getResourceId(R.styleable.ReactiveRatingBar_lightStar, -1);
            lightStar = ContextCompat.getDrawable(getContext(), drawableResId);
            lightStar.setCallback(this);
        }else {
            String error = "lightStar drawable must set";
            Logger.e(TAG, "--------- " + error + " -----------");
            throw new RuntimeException(error);
        }
        if (a.hasValue(R.styleable.ReactiveRatingBar_darkStar)) {
            final int drawableResId = a.getResourceId(R.styleable.ReactiveRatingBar_darkStar, -1);
            darkStar = ContextCompat.getDrawable(getContext(), drawableResId);
            darkStar.setCallback(this);
        }else {
            String error = "darkStar drawable must set";
            Logger.e(TAG, "----------- " + error + " ---------------");
            throw new RuntimeException(error);
        }
        isAutoCalculateHeight = a.getBoolean(R.styleable.ReactiveRatingBar_isAutoCalculateHeight, isAutoCalculateHeight);
        maxStarSpace = a.getDimension(R.styleable.ReactiveRatingBar_maxStarSpace, maxStarSpace);
        topBottomSpace = a.getDimension(R.styleable.ReactiveRatingBar_topBottomSpace, topBottomSpace);
        starWidth = a.getDimension(R.styleable.ReactiveRatingBar_starWidth, starWidth);
        starHeight = a.getDimension(R.styleable.ReactiveRatingBar_starHeight, starHeight);
        rating = a.getInteger(R.styleable.ReactiveRatingBar_rating, rating);
        numStars = a.getInteger(R.styleable.ReactiveRatingBar_numStars, numStars);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        double viewWidth = getWidth(), viewHeight = getHeight(), leftOrRightSpace = 0;
        if (isAutoCalculateHeight) {
            starHeight = (int) (viewHeight - 2.0 * topBottomSpace);
            starWidth = starHeight;
            maxStarSpace = starWidth;
        }
        if (null == lightStarBitmap) {
            lightStarBitmap =  Bitmap.createScaledBitmap(UIUtils.getBitmapFromVectorDrawable(getContext(), lightStar),
                    (int)Math.floor(starWidth), (int)Math.floor(starHeight), false);
        }
        if (null == darkStarBitmap) {
            darkStarBitmap = Bitmap.createScaledBitmap(UIUtils.getBitmapFromVectorDrawable(getContext(), darkStar),
                    (int)Math.floor(starWidth), (int)Math.floor(starHeight), false);
        }
        double startSpace = (viewWidth - numStars * starWidth) / (numStars + 1.0);
        if (startSpace < maxStarSpace) {
            leftOrRightSpace = startSpace;
        } else {
            startSpace = maxStarSpace;
            leftOrRightSpace = (int) ((((viewWidth - numStars * starWidth) - ((numStars - 1) * startSpace))) / 2.0);
        }
        float left, top = topBottomSpace;
        for (int i = 1; i <= numStars; i++) {
            left = (float)(leftOrRightSpace + (i - 1) * (starWidth + startSpace));
            if (i <= rating) {
                canvas.drawBitmap(lightStarBitmap, left, top, null);
            } else {
                canvas.drawBitmap(darkStarBitmap, left, top, null);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int starRating = calculateStarRating(event);
        if (null != reactiveRatingBarListener) {
            reactiveRatingBarListener.onRatingChanged(this, starRating);
        }
        this.setRating(starRating);
        this.invalidate();
        return super.onTouchEvent(event);
    }

    /**
     * @return 0表示左侧区域，星星分数为0, -1表示无效
     */
    private int calculateStarRating(MotionEvent event) {
        double rating = 0, viewWidth = getWidth(), leftOrRightSpace, x = (int) event.getX();
        double startSpace = (int) ((viewWidth - numStars * starWidth) / (numStars + 1.0));
        if (startSpace < maxStarSpace) {
            leftOrRightSpace = startSpace;
        } else {
            startSpace = maxStarSpace;
            leftOrRightSpace = (int) ((((viewWidth - numStars * starWidth) - ((numStars - 1) * startSpace))) / 2.0);
        }
        if (x <= leftOrRightSpace) {
            return (int)Math.floor(rating);
        } else if (x > (viewWidth - leftOrRightSpace)) {
            rating = numStars;
            return (int)Math.floor(rating);
        }
        double startPointStart, startPointEnd;
        for (int i = 1; i <= numStars; i++) {
            if (i == 1) {
                startPointStart = leftOrRightSpace;
                startPointEnd = startPointStart + starWidth;
                if (x > startPointStart && x <= startPointEnd) {
                    rating = 1;
                    break;
                }
            } else {
                startPointStart = (leftOrRightSpace + starWidth) + (i - 2) * (startSpace + starWidth);
                startPointEnd = startPointStart + startSpace + starWidth;
                if (x > startPointStart && x <= startPointEnd) {
                    rating = i;
                    break;
                }
            }
        }
        return (int)Math.floor(rating);
    }

    public interface ReactiveRatingBarListener {
        void onRatingChanged(ReactiveRatingBar reactiveRatingBar, int rating);
    }

    public ReactiveRatingBarListener getReactiveRatingBarListener() {
        return reactiveRatingBarListener;
    }

    public void setReactiveRatingBarListener(ReactiveRatingBarListener reactiveRatingBarListener) {
        this.reactiveRatingBarListener = reactiveRatingBarListener;
    }

    /**
     * @param rating setRating and refresh
     */
    public void setRating(int rating) {
        if (rating > numStars) {
            throw new RuntimeException("rating must <= numStars !");
        }
        this.rating = rating;
        invalidate();
    }

    public int getRating() {
        return rating;
    }
}
