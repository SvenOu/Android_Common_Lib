package com.sv.common.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

import com.sv.common.R;

/**
 *
 * @author sven-ou
 * dots view
 */
public class PagingView extends View {

	/**
	 * 背景色
	 */
	private int backgroundColor = Color.BLACK;
	/**
	 * 未选中的点颜色
	 */
	private int unselectedDotColor = Color.BLUE;
	/**
	 * 选中的点颜色
	 */
	private int selectedDotColor = Color.YELLOW;

	/**
	 * 总的点数
	 */
	private int totalCount = 5;
	/**
	 * 当前选中点
	 */
	private int currentPage = 0;
	/**
	 * 空白间隔长度
	 */
	private float blankWidth = 15;
	/**
	 * 点的宽高
	 */
	private float pointWidthHeight = 30;

	/**
	 * 是否根据宽度动态计算每个点的大小
	 */
	private boolean isDynamic = true;

	public PagingView(Context context) {
		super(context);
		init(null, 0);
	}

	public PagingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs, 0);
	}

	public PagingView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(attrs, defStyle);
	}

	private void init(AttributeSet attrs, int defStyle) {
		final TypedArray a = getContext().obtainStyledAttributes(
				attrs, R.styleable.PagingView, defStyle, 0);
		backgroundColor = a.getColor(R.styleable.PagingView_backgroundColor, backgroundColor);
		unselectedDotColor = a.getColor(R.styleable.PagingView_unselectedDotColor, unselectedDotColor);
		selectedDotColor = a.getColor(R.styleable.PagingView_selectedDotColor, selectedDotColor);
		totalCount = a.getInteger(R.styleable.PagingView_totalCount, totalCount);
		currentPage = a.getInteger(R.styleable.PagingView_currentPage, currentPage);
		blankWidth = a.getDimension(R.styleable.PagingView_blankWidth, blankWidth);
		pointWidthHeight = a.getDimension(R.styleable.PagingView_pointWidthHeight, pointWidthHeight);
		isDynamic = a.getBoolean(R.styleable.PagingView_isDynamic, isDynamic);
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		int thisHeight = getHeight();
		int thisWidth = getWidth();

		if (isDynamic) {
			pointWidthHeight = thisWidth / (totalCount + totalCount / 3);
//			float pointHeight = pointWidthHeight;
			blankWidth = (thisWidth - totalCount * pointWidthHeight)
					/ (totalCount - 1);
		}
		super.onDraw(canvas);
		canvas.drawColor(backgroundColor);
		Paint paint = new Paint();
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(unselectedDotColor);
		paint.setStyle(Style.FILL);

		if (isDynamic) {
			for (int i = 0; i < totalCount; i++) {
				if (i == currentPage) {
					paint.setColor(selectedDotColor);
					canvas.drawCircle((float) (i * (blankWidth + pointWidthHeight)
							* 1.0 + pointWidthHeight / 2.0),
							(float) (thisHeight / 2.0),
							(float) (pointWidthHeight / 2.0), paint);
					paint.setColor(unselectedDotColor);
				} else {
					canvas.drawCircle((float) (i * (blankWidth + pointWidthHeight)
							* 1.0 + pointWidthHeight / 2.0),
							(float) (thisHeight / 2.0),
							(float) (pointWidthHeight / 2.0), paint);
				}
			}
		}else{
			for (int i = 0; i < totalCount; i++) {
				float cx = (float) ((float) (i * (blankWidth + pointWidthHeight)* 1.0 + pointWidthHeight / 2.0)+
						(thisWidth - totalCount * (pointWidthHeight+blankWidth))/ 2.0);
				if (i == currentPage) {
					paint.setColor(selectedDotColor);
					canvas.drawCircle(cx,
							(float) (thisHeight / 2.0),
							(float) (pointWidthHeight / 2.0), paint);
					paint.setColor(unselectedDotColor);
				} else {
					canvas.drawCircle(cx,
							(float) (thisHeight / 2.0),
							(float) (pointWidthHeight / 2.0), paint);
				}
			}
		}
	}

	public void updatePage(int totalCount, int currentPage) {
		this.currentPage = currentPage;
		this.totalCount = totalCount;
		this.invalidate();
	}
	public void updatePage(int currentPage) {
		this.currentPage = currentPage;
		this.invalidate();
	}
}
