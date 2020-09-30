package com.andy.apparch.demos;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.DrawFilter;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.andy.apparch.R;

@SuppressLint("AppCompatCustomView")
public class ClipImageView extends ImageView {
    private Path path = new Path();
    
    private PaintFlagsDrawFilter paintFlagsFilter = new PaintFlagsDrawFilter(0, 3);
    
    private RectF rectF = new RectF();
    private int leftTopRadius;
    private int rightTopRadius;
    private int rightBottomRadius;
    private int leftBottomRadius;

    
    public final Path getPath() {
        return this.path;
    }

    public final void setPath( Path var1) {
        this.path = var1;
    }

    
    public final PaintFlagsDrawFilter getPaintFlagsFilter() {
        return this.paintFlagsFilter;
    }

    public final void setPaintFlagsFilter( PaintFlagsDrawFilter var1) {
        this.paintFlagsFilter = var1;
    }

    
    public final RectF getRectF() {
        return this.rectF;
    }

    public final void setRectF( RectF var1) {
        this.rectF = var1;
    }

    public final int getLeftTopRadius() {
        return this.leftTopRadius;
    }

    public final void setLeftTopRadius(int var1) {
        this.leftTopRadius = var1;
    }

    public final int getRightTopRadius() {
        return this.rightTopRadius;
    }

    public final void setRightTopRadius(int var1) {
        this.rightTopRadius = var1;
    }

    public final int getRightBottomRadius() {
        return this.rightBottomRadius;
    }

    public final void setRightBottomRadius(int var1) {
        this.rightBottomRadius = var1;
    }

    public final int getLeftBottomRadius() {
        return this.leftBottomRadius;
    }

    public final void setLeftBottomRadius(int var1) {
        this.leftBottomRadius = var1;
    }

    @SuppressLint({"Recycle"})
    public final void initAttr(@Nullable Context context, @Nullable AttributeSet attrs) {
        if (context != null) {
            TypedArray var10000 = context.obtainStyledAttributes(attrs, R.styleable.ClipImageView);
            if (var10000 != null) {
                TypedArray ta = var10000;
                this.leftTopRadius = ta.getDimensionPixelSize(R.styleable.ClipImageView_leftTopRadius, 0);
                this.rightTopRadius = ta.getDimensionPixelSize(R.styleable.ClipImageView_rightTopRadius, 0);
                this.rightBottomRadius = ta.getDimensionPixelSize(R.styleable.ClipImageView_rightBottomRadius, 0);
                this.leftBottomRadius = ta.getDimensionPixelSize(R.styleable.ClipImageView_leftBottomRadius, 0);
                ta.recycle();
                return;
            }
        }

    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.rectF = new RectF(0.0F, 0.0F, (float)w, (float)h);
    }

    protected void onDraw(@Nullable Canvas canvas) {
        this.path.reset();
        this.path.addRoundRect(this.rectF, new float[]{(float)this.leftTopRadius, (float)this.leftTopRadius, (float)this.rightTopRadius, (float)this.rightTopRadius, (float)this.rightBottomRadius, (float)this.rightBottomRadius, (float)this.leftBottomRadius, (float)this.leftBottomRadius}, Path.Direction.CW);
        if (canvas != null) {
            canvas.setDrawFilter((DrawFilter)this.paintFlagsFilter);
        }

        if (canvas != null) {
            canvas.save();
        }

        if (canvas != null) {
            canvas.clipPath(this.path);
        }

        super.onDraw(canvas);
        if (canvas != null) {
            canvas.restore();
        }

    }

    public ClipImageView(@Nullable Context context) {
        super(context);
        this.initAttr(context, (AttributeSet)null);
    }

    public ClipImageView(@Nullable Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.initAttr(context, attrs);
    }

    public ClipImageView(@Nullable Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initAttr(context, attrs);
    }

    @TargetApi(21)
    public ClipImageView(@Nullable Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.initAttr(context, attrs);
    }
}
