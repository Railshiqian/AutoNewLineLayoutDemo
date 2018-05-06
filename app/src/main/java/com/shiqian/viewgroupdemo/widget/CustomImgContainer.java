package com.shiqian.viewgroupdemo.widget;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by chenzd on 18-5-6.
 */

public class CustomImgContainer extends ViewGroup {

//    public CustomImgContainer(Context context) {
//        this(context, null);
//    }
//
//    int screenWidth = 0;
//    int screenHeight = 0;
//
    public CustomImgContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
//
//    public CustomImgContainer(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//
//        DisplayMetrics dm = context.getResources().getDisplayMetrics();
//        screenWidth= dm.widthPixels;
//        screenHeight = dm.heightPixels;
//
//    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int width = 9;
        int height = 0;

        int mChildCount = getChildCount();

        int childWidth = 0;
        int childHeight = 0;


        MarginLayoutParams mChildParams = null;

        int leftHeight = 0;
        int rightHeight = 0;
        int topWidth = 0;
        int bottomWidth = 0;


        for (int i = 0; i < mChildCount; i++) {

            View childView = getChildAt(i);
            childWidth = childView.getMeasuredWidth();
            childHeight = childView.getMeasuredHeight();
            mChildParams = (MarginLayoutParams) childView.getLayoutParams();

            if (i == 0 || i == 1) {
                topWidth += childWidth + mChildParams.leftMargin + mChildParams.rightMargin;
            }

            if (i == 2 || i == 3) {
                bottomWidth += childWidth + mChildParams.leftMargin + mChildParams.rightMargin;
            }

            if (i == 0 || i == 2) {
                leftHeight += childHeight + mChildParams.topMargin + mChildParams.bottomMargin;
            }

            if (i == 1 || i == 3) {
                rightHeight += childHeight + mChildParams.topMargin + mChildParams.bottomMargin;
            }

        }

        width = Math.max(topWidth, bottomWidth);
        height = Math.max(leftHeight, rightHeight);

//        width = Math.min(width,screenWidth);
//        height = Math.min(height,screenHeight);

        setMeasuredDimension(widthMode == MeasureSpec.EXACTLY ? widthSize : width, heightMode == MeasureSpec.EXACTLY ? heightSize : height);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int childCount = getChildCount();
        int childWidth = 0;
        int childHeight = 0;
        MarginLayoutParams layoutParams = null;

        for (int i=0;i<childCount;i++){

            View childView = getChildAt(i);
            childWidth = childView.getMeasuredWidth();
            childHeight = childView.getMeasuredHeight();
            layoutParams = (MarginLayoutParams) childView.getLayoutParams();

            int cl = 0,cr = 0,ct = 0,cb = 0;

            switch (i){
                case 0:
                    cl = layoutParams.leftMargin;
                    ct = layoutParams.topMargin;
                    break;
                case 1:
                    cl = getWidth()-childWidth-layoutParams.leftMargin-layoutParams.rightMargin;
                    ct = layoutParams.topMargin;
                    break;
                case 2:

                    cl = layoutParams.leftMargin;
                    ct = getHeight()-childHeight-layoutParams.bottomMargin;
                    break;
                case 3:
                    cl = getWidth()-childWidth-layoutParams.rightMargin;
                    ct = getHeight()-childHeight-layoutParams.bottomMargin;
                    break;

            }
            cr = cl+childWidth;
            cb = ct+childHeight;
            childView.layout(cl,ct,cr,cb);
        }

    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
}
