package com.sv.common.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.sv.common.R;

/**
 * Created by sven-ou on 2017/7/26.
 */

public class CycleProgressView extends View {

    /**
     * 进度环的宽度
     */
    private float strokeWidth = 10f;
    /**
     * 进度环已填颜色
     */
    private int progressColor = Color.RED;
    /**
     * 进度环未填颜色
     */
    private int unProgressColor = Color.YELLOW;
    /**
     * 值
     */
    private float value = 0f;
    /**
     * 最大值
     */
    private float maxValue = 100f;
    /**
     * 进度起点旋转角度
     */
    private float rotate = 0f;
    private Paint paint;
    public CycleProgressView(Context context) {
        super(context);
        init(null, 0);
    }

    public CycleProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public CycleProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyle) {
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.CycleProgressView, defStyle, 0);

        strokeWidth = a.getDimension(R.styleable.CycleProgressView_strokeWidth, strokeWidth);
        progressColor = a.getColor(R.styleable.CycleProgressView_progressColor, progressColor);
        unProgressColor = a.getColor(R.styleable.CycleProgressView_unProgressColor, unProgressColor);
        rotate = a.getFloat(R.styleable.CycleProgressView_rotate, rotate);
        value = a.getFloat(R.styleable.CycleProgressView_value, value);
        maxValue = a.getFloat(R.styleable.CycleProgressView_maxValue, maxValue);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int viewWidth = getWidth(), viewHeight = getHeight(), pxStrokeWidth = dipTopx(getContext(),strokeWidth);
        int rectLength, strokeOffset = (int) Math.round(pxStrokeWidth / 2.0);
        int offset = (int) Math.abs(Math.round((viewHeight - viewWidth) / 2.0));
        int drawValue = Math.round((value / maxValue) * 360);
        RectF rectfHead;
        if (viewWidth > viewHeight) {
            rectLength = viewHeight;
            rectfHead = new RectF(strokeOffset + offset, strokeOffset,
                    rectLength - strokeOffset + offset, rectLength - strokeOffset);
        } else {
            rectLength = viewWidth;
            rectfHead = new RectF(strokeOffset, strokeOffset + offset,
                    rectLength - strokeOffset, rectLength - strokeOffset + offset);
        }
        if (null == paint) {
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(pxStrokeWidth);
        }

        // progress
        paint.setColor(progressColor);
        canvas.drawArc(rectfHead, rotate, drawValue, false, paint);

        //un progress
        if(drawValue < 360){
            paint.setColor(unProgressColor);
            canvas.drawArc(rectfHead, drawValue + rotate -1, 362 - drawValue, false, paint);
        }
    }

	/**
	 * 将dip或dp值转换为px值，保证尺寸大小不变
	 */
	public static int dipTopx(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
        invalidate();
    }

    public float getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(float maxValue) {
        this.maxValue = maxValue;
        invalidate();
    }
}
