package com.andromeda.ara.util;

import android.graphics.*;
import android.graphics.drawable.Drawable;

/**
 * Image with rounded corners
 *
 * You can find the original source here:
 * http://www.curious-creature.org/2012/12/11/android-recipe-1-image-with-rounded-corners/
 *
 * @author Gabriele Mariotti (gabri.mariotti@gmail.com)
 */
public class RoundCornersDrawable extends Drawable {

    private final float mCornerRadius;
    private final RectF mRect = new RectF();
    private final Paint mPaint;
    private final int mMargin;

    public RoundCornersDrawable(Bitmap bitmap, float cornerRadius, int margin) {
        mCornerRadius = cornerRadius;

        //private final RectF mRectBottomR = new RectF();
        //private final RectF mRectBottomL = new RectF();
        BitmapShader mBitmapShader = new BitmapShader(bitmap,
                Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setShader(mBitmapShader);

        mMargin = margin;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        mRect.set(mMargin, mMargin, bounds.width() - mMargin, bounds.height() - mMargin);
        //mRectBottomR.set( (bounds.width() -mMargin) / 2, (bounds.height() -mMargin)/ 2,bounds.width() - mMargin, bounds.height() - mMargin);
       // mRectBottomL.set( 0,  (bounds.height() -mMargin) / 2, (bounds.width() -mMargin)/ 2, bounds.height() - mMargin);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRoundRect(mRect, mCornerRadius, mCornerRadius, mPaint);
        //canvas.drawRect(mRectBottomR, mPaint); //only bottom-right corner not rounded
        //canvas.drawRect(mRectBottomL, mPaint); //only bottom-left corner not rounded

    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mPaint.setColorFilter(cf);
    }


}