package com.sv.common.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.sv.common.R;

/**
 *  圆形 TextView
 * @author sven-ou
 */
public class CycleTextView extends View {
    /**
     * 背景
     */
	private Drawable background;

    /**
     * 外层圆背景
     */
	private int outerCircleBackgroundColor = Color.WHITE;

    /**
     * 外层圆半径
     */
    private float outerCircleRadius = 5f;

    /**
     * 内层圆背景
     */
	private int innerCircleBackgroundColor = Color.YELLOW;

    /**
     * 文字
     */
    private String text;

    /**
     * 文字颜色
     */
	private int textColor = Color.BLACK;

    /**
     * 文字大小
     */
    private float textSize = 25f;

	private final Rect textBounds = new Rect();

	public CycleTextView(Context context) {
		super(context);
		init(null, 0);
	}

	public CycleTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs, 0);
	}

	public CycleTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(attrs, defStyleAttr);
	}

	private void init(AttributeSet attrs, int defStyle) {
		// Load attributes
		final TypedArray a = getContext().obtainStyledAttributes(
				attrs, R.styleable.CycleTextView, defStyle, 0);

		if (a.hasValue(R.styleable.CycleTextView_background)) {
			background = a.getDrawable(R.styleable.CycleTextView_background);
			background.setCallback(this);
		}

		outerCircleBackgroundColor = a.getColor(R.styleable.CycleTextView_outerCircleBackgroundColor, outerCircleBackgroundColor);

		outerCircleRadius = a.getDimension(
				R.styleable.CycleTextView_outerCircleRadius,
				outerCircleRadius);

		innerCircleBackgroundColor = a.getColor(R.styleable.CycleTextView_innerCircleBackgroundColor, innerCircleBackgroundColor);

		text = a.getString(R.styleable.CycleTextView_text);

		textColor = a.getColor(R.styleable.CycleTextView_textColor, textColor);

		textSize = a.getDimension(R.styleable.CycleTextView_textSize, textSize);

		a.recycle();
	}

	@SuppressLint({"DrawAllocation" })
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		int mWidth = getWidth();

		int paddingLeft = getPaddingLeft();
		int paddingTop = getPaddingTop();
		int paddingRight = getPaddingRight();
		int paddingBottom = getPaddingBottom();

		int contentWidth = getWidth() - paddingLeft - paddingRight;
		int contentHeight = getHeight() - paddingTop - paddingBottom;

		if (background != null) {
			background.setBounds(paddingLeft, paddingTop, paddingLeft + contentWidth, paddingTop + contentHeight);
			background.draw(canvas);
		}

		/*外层圆*/
		Paint paint1 = new Paint();
		paint1.setAntiAlias(true);
		int r1 = mWidth /2;
		paint1.setColor(outerCircleBackgroundColor);
		canvas.drawCircle(r1, r1, r1, paint1);

		/*内层圆*/
		Paint paint2 = new Paint();
		paint2.setAntiAlias(true);
		float r2 = mWidth /2f -outerCircleRadius;
		paint2.setColor(innerCircleBackgroundColor);
		canvas.drawCircle(r1, r1, r2, paint2);

		/*内层文字*/
		if(!TextUtils.isEmpty(text)){
			paint1.setTextSize(textSize);
			paint1.setColor(textColor);
			paint1.getTextBounds(text, 0, text.length(), textBounds);
			canvas.drawText(text, r1 - textBounds.exactCenterX(), r1 - textBounds.exactCenterY(), paint1);
		}
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
		invalidate();
	}
}
