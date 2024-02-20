package com.example.llc.android_r.fish;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * com.example.llc.android_r.fish.Fish
 *
 * @author liulongchao
 * @since 2024/2/5
 */
public class Fish extends Drawable {

    private Path mPath;
    private Paint mPaint;
    private int OTHER_ALPHA = 110;
    private int BODY_ALPHA = 160;
    // 鱼的重心
    private PointF middlePoint;
    // 鱼的主要朝向角度
    private float fishMainAngle = 0;
    /**
     * 鱼的长度值
     */
    // 绘制鱼头的半径
    private float HEAD_RADIUS = 100;
    // 鱼身长度
    private float BODY_LENGTH = HEAD_RADIUS * 3.2f;
    // 寻找鱼鳍起始点坐标的线长
    private float FIND_FINS_LENGTH = 0.9f * HEAD_RADIUS;
    // 鱼鳍的长度
    private float FINS_LENGTH = 1.3f * HEAD_RADIUS;

    // 大圆的半径
    private float BIG_CIRCLE_RADIUS = 0.7f * HEAD_RADIUS;
    // 中圆的半径
    private float MIDDLE_CIRCLE_RADIUS = 0.6f * BIG_CIRCLE_RADIUS;
    // 小圆半径
    private float SMALL_CIRCLE_RADIUS = 0.4f * MIDDLE_CIRCLE_RADIUS;
    // --寻找尾部中圆圆心的线长
    private final float FIND_MIDDLE_CIRCLE_LENGTH = BIG_CIRCLE_RADIUS * (0.6f + 1);
    // --寻找尾部小圆圆心的线长
    private final float FIND_SMALL_CIRCLE_LENGTH = MIDDLE_CIRCLE_RADIUS * (0.4f + 2.7f);
    // --寻找大三角形底边中心点的线长
    private final float FIND_TRIANGLE_LENGTH = MIDDLE_CIRCLE_RADIUS * 2.7f;

    private float animatedValue;

    public Fish() {
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setARGB(OTHER_ALPHA, 244, 92, 71);

        middlePoint = new PointF(4.19f * HEAD_RADIUS, 4.19f * HEAD_RADIUS);

        // 动画周期
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 720f);
        // 动画时长
        valueAnimator.setDuration(2000);
        // 重复模式，重新开始
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        // 重复次数 无线循环
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                animatedValue = (float) animator.getAnimatedValue();
                invalidateSelf();
            }
        });
        valueAnimator.start();
    }

    /**
     * 设置透明度
     * @param canvas The canvas to draw into
     */
    @Override
    public void draw(@NonNull Canvas canvas) {
        float fishAngle = (float) (fishMainAngle + Math.sin(Math.toRadians(animatedValue)) * 10);
        // 鱼头的圆心坐标
        PointF headPoint = calculatePoint(middlePoint, BODY_LENGTH / 2, fishAngle);
        // 画鱼头
        canvas.drawCircle(headPoint.x, headPoint.y, HEAD_RADIUS, mPaint);
        // 画鱼鳍
        // 右鱼鳍的起始点坐标
        PointF rightFinsPoint = calculatePoint(headPoint, FIND_FINS_LENGTH, fishAngle - 110);
        makeFins(canvas, rightFinsPoint, fishAngle, true);
        // 左鱼鳍的起始点坐标
        PointF leftFinsPoint = calculatePoint(headPoint, FIND_FINS_LENGTH, fishAngle + 110);
        makeFins(canvas, leftFinsPoint, fishAngle, false);

        PointF bodyBottomCenterPoint = calculatePoint(headPoint, BODY_LENGTH, fishAngle - 180);
        PointF middlePointF = makeSegment(canvas, bodyBottomCenterPoint, FIND_MIDDLE_CIRCLE_LENGTH, BIG_CIRCLE_RADIUS, MIDDLE_CIRCLE_RADIUS, fishAngle, true);
        //
        makeSegment(canvas, middlePointF, MIDDLE_CIRCLE_RADIUS, SMALL_CIRCLE_RADIUS,
                FIND_SMALL_CIRCLE_LENGTH, fishAngle, false);
        // 画三角形
        float findTriangleAngle = (float) Math.abs(Math.sin(Math.toRadians(animatedValue * 1.5)) * BIG_CIRCLE_RADIUS);
        makeTriangle(canvas, middlePointF, FIND_TRIANGLE_LENGTH, findTriangleAngle, fishAngle);
        makeTriangle(canvas, middlePointF, FIND_TRIANGLE_LENGTH - 10,
                findTriangleAngle - 20, fishAngle);
        // 画身体
        makeBody(canvas, headPoint, bodyBottomCenterPoint, fishAngle);
    }

    private void makeBody(Canvas canvas, PointF headPoint, PointF bodyBottomCenterPoint, float fishAngle) {
        // 身体的四个点求出来
        PointF topLeftPoint = calculatePoint(headPoint, HEAD_RADIUS, fishAngle + 90);
        PointF topRightPoint = calculatePoint(headPoint, HEAD_RADIUS, fishAngle - 90);
        PointF bottomLeftPoint = calculatePoint(bodyBottomCenterPoint, BIG_CIRCLE_RADIUS,
                fishAngle + 90);
        PointF bottomRightPoint = calculatePoint(bodyBottomCenterPoint, BIG_CIRCLE_RADIUS,
                fishAngle - 90);

        // 二阶贝塞尔曲线的控制点
        PointF controlLeft = calculatePoint(headPoint, BODY_LENGTH * 0.56f,
                fishAngle + 130);
        PointF controlRight = calculatePoint(headPoint, BODY_LENGTH * 0.56f,
                fishAngle - 130);

        // 绘制
        mPath.reset();
        mPath.moveTo(topLeftPoint.x, topLeftPoint.y);
        mPath.quadTo(controlLeft.x, controlLeft.y, bottomLeftPoint.x, bottomLeftPoint.y);
        mPath.lineTo(bottomRightPoint.x, bottomRightPoint.y);
        mPath.quadTo(controlRight.x, controlRight.y, topRightPoint.x, topRightPoint.y);
        mPaint.setAlpha(BODY_ALPHA);
        canvas.drawPath(mPath, mPaint);
    }

    private void makeTriangle(Canvas canvas, PointF startPoint, float findTriangleLength,
                              float bigCircleRadius, float fishAngle) {
        float triangleAngle = (float) (fishAngle + Math.sin(Math.toRadians(animatedValue * 1.5)) * 20);
        // 三角形底边中心点坐标
        PointF centerPoint = calculatePoint(startPoint, findTriangleLength, triangleAngle);
        // 三角形底边两点
        PointF leftPoint = calculatePoint(centerPoint, bigCircleRadius, triangleAngle + 90);
        PointF rightPoint = calculatePoint(centerPoint, bigCircleRadius, triangleAngle - 90);

        mPath.reset();
        mPath.moveTo(startPoint.x, startPoint.y);
        mPath.lineTo(leftPoint.x,  leftPoint.y);
        mPath.lineTo(rightPoint.x, rightPoint.y);
        canvas.drawPath(mPath, mPaint);
    }

    /**
     * 画节肢
     *
     * @param canvas               画布
     * @param bottomCenterPoint    大圆中心点坐标
     * @param findMiddleCircleLength 中圆长度
     * @param bigCircleRadius 大圆半径
     * @param middleCircleRadius 中圆半径
     * @param fishAngle 角度
     */
    private PointF makeSegment(Canvas canvas, PointF bottomCenterPoint, float findMiddleCircleLength,
                               float bigCircleRadius, float middleCircleRadius, float fishAngle, boolean hasBigCircle) {

        float segmentAngle;
        if (hasBigCircle) {
            segmentAngle = (float) (fishAngle + Math.cos(Math.toRadians(animatedValue * 1.5)) * 15);
        } else {
            segmentAngle = (float) (fishAngle + Math.sin(Math.toRadians(animatedValue * 1.5)) * 20);
        }
        // 计算中圆坐标(梯形上底圆的圆心)
        PointF upperCenterPoint = calculatePoint(bottomCenterPoint, findMiddleCircleLength, segmentAngle - 180);
        // 计算梯形的四个点
        PointF bottomLeftPoint = calculatePoint(bottomCenterPoint, bigCircleRadius, segmentAngle + 90);
        PointF bottomRightPoint = calculatePoint(bottomCenterPoint, bigCircleRadius, segmentAngle - 90);
        PointF upperLeftPoint = calculatePoint(upperCenterPoint, middleCircleRadius, segmentAngle + 90);
        PointF upperRightPoint = calculatePoint(upperCenterPoint, middleCircleRadius, segmentAngle - 90);
        if (hasBigCircle) {
            // 画大圆
            canvas.drawCircle(bottomCenterPoint.x, bottomCenterPoint.y, bigCircleRadius, mPaint);
        }
        // 画小圆
        canvas.drawCircle(upperCenterPoint.x, upperCenterPoint.y, middleCircleRadius, mPaint);
        // 画梯形
        mPath.reset();
        mPath.moveTo(upperLeftPoint.x, upperLeftPoint.y);
        mPath.lineTo(upperRightPoint.x, upperRightPoint.y);
        mPath.lineTo(bottomRightPoint.x, bottomRightPoint.y);
        mPath.lineTo(bottomLeftPoint.x, bottomLeftPoint.y);
        canvas.drawPath(mPath, mPaint);
        return upperCenterPoint;
    }

    /**
     * 画鱼鳍
     *
     * @param canvas 画布
     * @param startPoint 鱼鳍坐标
     * @param fishAngle 鱼鳍的角度
     */
    private void makeFins(Canvas canvas, PointF startPoint, float fishAngle, boolean isRight) {
        // 控制点的弧度，用来计算控制点的坐标
        float controlAngle = 115;
        // 鱼鳍的结束点坐标
        PointF endPoint = calculatePoint(startPoint, FINS_LENGTH, fishAngle - 180);
        // 鱼鳍的控制点坐标
        PointF controlPoint = calculatePoint(startPoint, FINS_LENGTH * 1.8f,
                isRight ? fishAngle - controlAngle : fishAngle + controlAngle);
        // 绘制
        mPath.reset();
        // 画笔移动到起始点
        mPath.moveTo(startPoint.x, startPoint.y);
        // 绘制二阶贝塞尔曲线，需要传入的是 控制点坐标和结束点坐标
        mPath.quadTo(controlPoint.x, controlPoint.y, endPoint.x, endPoint.y);
        canvas.drawPath(mPath, mPaint);
    }

    /**
     * 计算点位函数
     *
     * @param startPoint 起始点
     * @param length 起始点到终点的距离
     * @param angle 起始点和终点的角度
     */
    private PointF calculatePoint(PointF startPoint, float length, float angle) {
        // X 轴坐标 也就是临边 b 的长度 b = cosA * 斜边 c
        float deltaX = (float) (Math.cosh(Math.toRadians(angle)) * length);
        // Y 轴坐标 也就是对边 a 的长度 a = sinA * 斜边 c
        float deltaY = (float) (Math.sin(Math.toRadians(angle - 180)) * length);
        return new PointF(startPoint.x + deltaX, startPoint.y + deltaY);
    }



    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    /**
     * 设置颜色过滤器，在绘制出来之前，被绘制的内容的每一个像素都会被颜色过滤器改变
     * @param colorFilter The color filter to apply, or {@code null} to remove the
     *                    existing color filter
     */
    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    /**
     * 这个值，可以根据 setAlpha 中设置的值进行调整，比如，alpha == 0 的时候设置为 PixelFormat.TRANSPARENT。
     * 在alpha == 255 时这是为 PixelFormat.OPAQUE。在其他时候设置为 PixelFormat.TRANSLUCENT。
     * PixelFormat.OPAQUE 完全不透明，遮盖在它下面的所有内容上
     * PixelFormat.TRANSPARENT 透明，完全不显示任何东西
     * PixelFormat.TRANSLUCENT 只有绘制的地方才覆盖底下的内容
     * @return PixelFormat.TRANSLUCENT
     */
    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public int getIntrinsicHeight() {
        return (int) (8.38f * HEAD_RADIUS);
    }

    @Override
    public int getIntrinsicWidth() {
        return (int) (8.38f * HEAD_RADIUS);
    }
}
