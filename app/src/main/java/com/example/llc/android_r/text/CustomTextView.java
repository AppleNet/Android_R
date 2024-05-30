package com.example.llc.android_r.text;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

/**
 * com.example.llc.android_r.text.CustomTextView
 *
 * @author liulongchao
 * @since 2024/1/10
 */
public class CustomTextView extends AppCompatTextView {

    private float mPercent;

    public float getPercent() {
        return mPercent;
    }

    public void setPercent(float mPercent) {
        this.mPercent = mPercent;
        invalidate();
    }

    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public CustomTextView(@NonNull Context context) {
        super(context);
    }

    public CustomTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCenterLineX(canvas);
        drawCenterLineX1(canvas);
        drawCenterLineY(canvas);
        drawCenterLineY1(canvas);
//        drawTextBlack(canvas);
//        drawTextRed(canvas);
    }

    private void drawTextBlack(Canvas canvas) {
        paint.setTextSize(40);
        paint.setColor(Color.BLACK);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float width = paint.measureText("我是老A");
        float baseLine = getHeight() / 2 + ((fontMetrics.descent - fontMetrics.ascent) / 2) - fontMetrics.descent;
        float x = getWidth() / 2 - width / 2;
        canvas.drawText("我是老A", x,
                baseLine,
                paint);
    }
    private void drawTextRed(Canvas canvas) {
        canvas.save();
        paint.setTextSize(40);
        paint.setColor(Color.RED);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float width = paint.measureText("我是老A");
        float baseLine = getHeight() / 2 + ((fontMetrics.descent - fontMetrics.ascent) / 2) - fontMetrics.descent;
        float x = getWidth() / 2 - width / 2;
        float right = x + width*mPercent;
        Rect rect = new Rect((int) x, 0, (int) right, getHeight());
        canvas.clipRect(rect);
        canvas.drawText("我是老A", x,
                baseLine,
                paint);
        canvas.restore();
    }

    private void drawCenterLineX(Canvas canvas) {
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(3);
        canvas.drawLine(getWidth() / 3,0, getWidth() / 3, getHeight(), paint);
    }
    private void drawCenterLineX1(Canvas canvas) {
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(3);
        canvas.drawLine(2 * (getWidth() / 3),0, 2 * (getWidth() / 3), getHeight(), paint);
    }

    private void drawCenterLineY(Canvas canvas) {
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(3);
        canvas.drawLine(0,getHeight() / 3, getWidth(), getHeight() / 3, paint);
    }
    private void drawCenterLineY1(Canvas canvas) {
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(3);
        canvas.drawLine(0,2* (getHeight() / 3), getWidth(), 2* (getHeight() / 3), paint);
    }
}
