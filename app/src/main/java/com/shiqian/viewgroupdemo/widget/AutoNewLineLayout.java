package com.shiqian.viewgroupdemo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by chenzd on 18-5-6.
 */

public class AutoNewLineLayout extends ViewGroup {

    private int screenWidth = 0;
    private int screenHeight = 0;
    private int maxWidth = 0;
    private int width;
    private int height;

    public AutoNewLineLayout(Context context) {
        this(context, null);
    }

    public AutoNewLineLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measuredHeight = MeasureSpec.getSize(heightMeasureSpec);
        int maxWidth = measuredWidth;

        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int count = getChildCount();

        int tempHeight = 0;//记录一行最高View
        int tempWidth = 0;//记录当前宽度

        boolean isLineFirstView = true;

        for (int i = 0; i < count; i++) {

            View childView = getChildAt(i);
            MarginLayoutParams layoutParams = (MarginLayoutParams) childView.getLayoutParams();
            int viewWidth = childView.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
            int viewHeight = childView.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;
            boolean nextLine = false;

            tempWidth += viewWidth;

            if (tempWidth > maxWidth) {

                if (widthMode == MeasureSpec.EXACTLY) {//不调整宽度，换行

                    if (isLineFirstView) {
                        height += viewHeight;
                        isLineFirstView = true;
                        nextLine = true;

                    } else {
                        i--;
                        height += tempHeight;
                        nextLine = true;
                    }

                } else {//width可调整

                    if (isLineFirstView) {
                        if (tempWidth >= screenWidth) {
                            maxWidth = screenWidth;
                            height += viewHeight;
                            nextLine = true;
                        } else {
                            maxWidth = Math.max(maxWidth, tempWidth);
                            tempHeight = Math.max(tempHeight, viewHeight);
                            isLineFirstView = false;
                            nextLine = false;
                        }
                    } else {

                        if (tempWidth > screenWidth) {

                            maxWidth = Math.max(maxWidth, tempWidth - viewWidth);
                            height += tempHeight;
                            i--;
                            nextLine = true;

                        } else {

                            maxWidth = Math.max(maxWidth, tempWidth);
                            tempHeight = Math.max(tempHeight, viewHeight);
                            nextLine = false;

                        }

                    }

                }
                if (nextLine) {
                    tempHeight = 0;
                    tempWidth = 0;
                    isLineFirstView = true;
                }
            } else {
                tempHeight = tempHeight < viewHeight ? viewHeight : tempHeight;
                isLineFirstView = false;
            }
        }

        if (widthMode == MeasureSpec.EXACTLY) {
            width = measuredWidth;
        } else {
            width = maxWidth;
        }

        setMeasuredDimension(width, height);

    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    float startY;
    float y;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction()) {
//
//            case MotionEvent.ACTION_DOWN:
//                startY = event.getY();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                y = event.getY() - startY;
//                if (y > 15 || y < -15) {
//                    postInvalidate();
//                }
//                break;
//        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        if (y > 15) {
//            canvas.translate(0, y);
//        }
        super.onDraw(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {


        int tempWidth = 0;
        int currentHeight = 0;
        int nextHeight = 0;
        boolean isLineFirstView = true;

        int count = getChildCount();

        for (int i = 0; i < count; i++) {

            View childView = getChildAt(i);
            int childWidth = childView.getMeasuredWidth();
            int childHeight = childView.getMeasuredHeight();
            MarginLayoutParams layoutParams = (MarginLayoutParams) childView.getLayoutParams();
            int childTotalWidth = childWidth + layoutParams.leftMargin + layoutParams.rightMargin;
            int childTotlaHeight = childHeight + layoutParams.topMargin + layoutParams.bottomMargin;

            int cl = 0, ct = 0, cr = 0, cb = 0;

            if (tempWidth + childTotalWidth <= width) {//布局此View
                cl = tempWidth + layoutParams.leftMargin;
                ct = currentHeight + layoutParams.topMargin;
                childView.layout(cl, ct, cl + childWidth, ct + childHeight);
                tempWidth += childTotalWidth;
                nextHeight = Math.max(nextHeight, childTotlaHeight);
                isLineFirstView = false;
            } else {//超出布局

                if (isLineFirstView) {

                    cl = tempWidth + layoutParams.leftMargin;
                    ct = currentHeight + layoutParams.topMargin;
                    childView.layout(cl, ct, cl + childWidth, ct + childHeight);
                    currentHeight += childTotlaHeight;
                    tempWidth = 0;
                    nextHeight = 0;


                } else {//不需要布局此文件；

                    currentHeight += nextHeight;
                    i--;
                    tempWidth = 0;
                    nextHeight = 0;
                    isLineFirstView = true;
                }
            }

        }

    }
}
