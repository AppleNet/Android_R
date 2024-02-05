package com.example.llc.android_r.photo;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.OverScroller;

import androidx.annotation.Nullable;

/**
 * com.example.llc.android_r.photo.CustomPhotoView
 *
 * @author liulongchao
 * @since 2024/2/2
 */
public class CustomPhotoView extends View {

    private static final float IMAGE_WIDTH = Utils.dpToPixel(300);
    private Bitmap bitmap;
    private Paint paint;
    private float originalOffsetX;
    private float originalOffsetY;
    private float smallScale;
    private float bigScale;
    private float currentScale;
    private float OVER_SCALE_FACTOR = 1.5f;
    private GestureDetector gestureDetector;
    private boolean isEnLarge = false;
    private ObjectAnimator scaleAnimator;
    private float offsetX;
    private float offsetY;
    private OverScroller overScroller;
    private ScaleGestureDetector scaleGestureDetector;




    public CustomPhotoView(Context context) {
        this(context, null);
    }

    public CustomPhotoView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomPhotoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        bitmap = Utils.getPhoto(getResources(), (int) IMAGE_WIDTH);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        gestureDetector = new GestureDetector(context, new PhotoGestureDetector());
        overScroller = new OverScroller(context);
        scaleGestureDetector = new ScaleGestureDetector(context, new PhotoScaleGestureListener());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //
        float scaleFaction = (currentScale - smallScale) / (bigScale - smallScale);
        canvas.translate(offsetX * scaleFaction, offsetY * scaleFaction);
        canvas.scale(currentScale, currentScale, (float) getWidth() / 2, (float) getHeight() / 2);
        canvas.drawBitmap(bitmap, originalOffsetX, originalOffsetY, paint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        originalOffsetX = (float) getWidth() / 2f - (float) bitmap.getWidth() / 2f;
        originalOffsetY = (float) getHeight() / 2f - (float) bitmap.getHeight() / 2f;

        if ((float) bitmap.getWidth() / bitmap.getHeight() > (float) getWidth() / getHeight()) {
            smallScale = (float) getWidth() / bitmap.getWidth();
            bigScale = (float) getHeight() / bitmap.getHeight() * OVER_SCALE_FACTOR;
        } else {
            smallScale = (float) getHeight() / bitmap.getHeight();
            bigScale = (float) getWidth() / bitmap.getWidth() * OVER_SCALE_FACTOR;
        }

        currentScale = smallScale;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean result = scaleGestureDetector.onTouchEvent(event);
        if (!scaleGestureDetector.isInProgress()) {
            result = gestureDetector.onTouchEvent(event);
        }
        return result;
    }

    /**
     *
     */
    private class PhotoGestureDetector extends GestureDetector.SimpleOnGestureListener {

        public PhotoGestureDetector() {
            super();
        }

        /**
         * 长按事件处理
         * @param e The initial on down motion event that started the longpress.
         */
        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
        }

        /**
         * @param e1        手指按下位置.
         * @param e2        当前位置.
         * @param distanceX 旧位置 - 新位置.
         * @param distanceY 旧位置 - 新位置.
         * @return true
         */
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            // 放大才能滑动
            if (isEnLarge) {
                offsetX = offsetX - distanceX;
                offsetY = offsetY - distanceY;
                fixOffset();
                invalidate();
            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        /**
         * up 手指抬起之后的惯性活动
         *
         * @param e1        The first down motion event that started the fling.
         * @param e2        The move motion event that triggered the current onFling.
         * @param velocityX x 轴方向运动速度，px/s.
         * @param velocityY y 轴方向运动速度，px/s.
         * @return true
         */
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (isEnLarge) {
                overScroller.fling((int) offsetX, (int) offsetY, (int) velocityX, (int) velocityY,
                        (int) (-(bitmap.getWidth() * bigScale - getWidth())/2),
                        (int) ((bitmap.getWidth() * bigScale - getWidth())/2),
                        (int) (-(bitmap.getHeight() * bigScale - getHeight())/2),
                        (int) ((bitmap.getHeight() * bigScale - getHeight())/2),
                        400, 400);

                postOnAnimation(new Runnable() {
                    @Override
                    public void run() {
                        if (overScroller.computeScrollOffset()) {
                            offsetX = overScroller.getCurrX();
                            offsetY = overScroller.getCurrY();
                            invalidate();
                            postOnAnimation(this);
                        }
                    }
                });

            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        /**
         * 延迟 100ms 触发，用来处理点击效果
         * @param e The down motion event
         */
        @Override
        public void onShowPress(MotionEvent e) {
            super.onShowPress(e);
        }


        /**
         * 只要想 响应事件，这里就需要返回 true
         * @param e The down motion event.
         * @return true
         */
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        /**
         * 双击触发时间 需要 40ms- 300ms 之间来处理
         *
         * @param e The down motion event of the first tap of the double-tap.
         * @return true
         */
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            isEnLarge = !isEnLarge;
            if (isEnLarge) {
                offsetX = (e.getX() - getWidth() / 2f) -
                        (e.getX() - getWidth() / 2f) * bigScale / smallScale;
                offsetY = (e.getY() - getHeight() / 2f) -
                        (e.getY() - getHeight() / 2f) * bigScale / smallScale;
                fixOffset();
                getScaleAnimator().start();
            } else {
                getScaleAnimator().reverse();
            }
            return super.onDoubleTap(e);
        }

        /**
         * 双击的第二次down、move、up都触发
         * @param e The motion event that occurred during the double-tap gesture.
         * @return true
         */
        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return super.onDoubleTapEvent(e);
        }

        /**
         *  up的时候触发，非长按单击或者双击的第一次点击触发
         * @param e The up motion event that completed the first tap
         * @return true
         */
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return super.onSingleTapUp(e);
        }

        /**
         * 单击按下时触发，双击不触发，down、up时都可能触发，但是不会同时触发
         * @param e The down motion event of the single-tap.
         * @return true
         */
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public boolean onContextClick(MotionEvent e) {
            return super.onContextClick(e);
        }
    }

    private class PhotoScaleGestureListener implements ScaleGestureDetector.OnScaleGestureListener {

        float initScale;

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            if ((currentScale > smallScale && !isEnLarge) || (currentScale == smallScale && isEnLarge)) {
                isEnLarge = !isEnLarge;
            }
            // 缩放因子
            currentScale = initScale * detector.getScaleFactor();
            invalidate();
            return false;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            initScale = currentScale;
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {

        }
    }

    private void fixOffset() {
        offsetX = Math.min(offsetX, ((bitmap.getWidth() * bigScale - getWidth()) / 2));
        offsetX = Math.max(offsetX, -((bitmap.getWidth() * bigScale - getWidth()) / 2));
        offsetY = Math.min(offsetY, ((bitmap.getHeight() * bigScale - getHeight()) / 2));
        offsetY = Math.max(offsetY, -((bitmap.getHeight() * bigScale - getHeight()) / 2));
;    }

    private ObjectAnimator getScaleAnimator() {
        if (scaleAnimator == null) {
            scaleAnimator = ObjectAnimator.ofFloat(this, "currentScale", 0);
        }
        scaleAnimator.setFloatValues(smallScale, bigScale);
        return scaleAnimator;
    }

    public float getCurrentScale() {
        return currentScale;
    }

    public void setCurrentScale(float currentScale) {
        this.currentScale = currentScale;
        invalidate();
    }
}
