package com.andy.apparch.demos;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import com.andy.apparch.R;


public class StackFrame extends FrameLayout {


    private int stackSize;
    private Drawable bgDrawable;

    private int bgWidth;
    private int bgHeight;
    private int bgOff;

    public StackFrame(Context context) {
        super(context);
    }

    public StackFrame(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttributes(context, attrs);
    }

    public StackFrame(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttributes(context, attrs);
    }

    @TargetApi(21)
    public StackFrame(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttributes(context, attrs);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (stackSize <= 0) return;
        createChildren();
    }

    private void createChildren() {
        removeAllViews();
        for (int i = 0; i < stackSize; i++) {
            View child = new View(getContext());
            LayoutParams lp = new LayoutParams(
                    bgWidth, bgHeight);
            lp.leftMargin = (stackSize - i ) * bgOff;
            child.setLayoutParams(lp);
            child.setBackground(bgDrawable);
            addView(child);
        }
    }

    public void setStackSize(int stackSize) {
        this.stackSize = stackSize;
        createChildren();
    }

    private void initAttributes(Context context, AttributeSet attrs) {
        TypedArray attr = getTypedArray(context, attrs, R.styleable.StackBgView);
        try {
            stackSize = attr.getInt(R.styleable.StackBgView_stackSize, 0);
            bgDrawable = attr.getDrawable(R.styleable.StackBgView_bgDrawable);
            bgWidth = attr.getDimensionPixelSize(R.styleable.StackBgView_bgWidth, 0);
            bgHeight = attr.getDimensionPixelSize(R.styleable.StackBgView_bgHeight, 0);
            bgOff = attr.getDimensionPixelSize(R.styleable.StackBgView_bgOff, 0);
        } finally {
            attr.recycle();
        }
    }


    private TypedArray getTypedArray(Context context, AttributeSet attributeSet, int[] attr) {
        return context.obtainStyledAttributes(attributeSet, attr, 0, 0);
    }
}
