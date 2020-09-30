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
import android.view.View;

import androidx.annotation.Nullable;

import com.andy.apparch.R;


public class StackBgView extends View {


    private int stackSize;
    private Drawable bgDrawable;

    private Rect[] rects;
    private int bgWidth;
    private int bgHeight;
    private int bgOff;
    private Paint paint = new Paint();

    public StackBgView(Context context) {
        super(context);
    }

    public StackBgView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttributes(context, attrs);
    }

    public StackBgView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttributes(context, attrs);
    }

    @TargetApi(21)
    public StackBgView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttributes(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (stackSize <= 0 || bgWidth <= 0 || bgHeight <= 0) {
            super.onMeasure(0, 0);
            return;
        }
        int width = bgWidth + (stackSize) * bgOff;
        setMeasuredDimension(width, bgHeight);
        Log.i("StackBgView", "width measure:" + width + ", stackSize:" + stackSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (stackSize <= 0 || bgWidth <= 0 || bgHeight <= 0 || bgDrawable == null) {
            return;
        }
        for (int i =0 ; i < stackSize; i++) {
            bgDrawable.setBounds(rects[i]);
            bgDrawable.draw(canvas);
            canvas.drawBitmap(((BitmapDrawable)bgDrawable).getBitmap(), null, rects[i], paint);
        }
    }

    public void setStackSize(int stackSize) {
        this.stackSize = stackSize;
        initRects();
        paint = new Paint();
        invalidate();
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
        initRects();
    }

    private void initRects() {
        if (stackSize <= 0 || bgWidth <= 0 || bgHeight <= 0) {
            return;
        }
        rects = new Rect[stackSize];
        int left = 0;
        int top = 0;
        for (int i = 0; i < stackSize; i++) {
            int offH =  bgOff * (stackSize - i);
            rects[i] = new Rect(left + offH, top, left + offH + bgWidth, top + bgHeight);
        }
    }

    private TypedArray getTypedArray(Context context, AttributeSet attributeSet, int[] attr) {
        return context.obtainStyledAttributes(attributeSet, attr, 0, 0);
    }
}
