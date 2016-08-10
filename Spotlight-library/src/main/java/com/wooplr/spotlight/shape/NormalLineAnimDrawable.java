package com.wooplr.spotlight.shape;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;

import com.wooplr.spotlight.target.AnimPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapted from github.com/dupengtao/LineAnimation
 */
@SuppressWarnings("unused")
public class NormalLineAnimDrawable extends Drawable implements ValueAnimator.AnimatorUpdateListener {

    private static final String FACTOR_X = "factorX";
    private static final String FACTOR_Y = "factorY";
    private final Path pathLeft;
    private final Paint paintLeft;
    private final Path pathRight;
    private final Paint paintRight;
    private float factorY, factorX;
    private AnimPoint curAnimLeftPoint = null;
    private AnimPoint curAnimRightPoint = null;
    private int moveTimes;
    private boolean twoWay;
    private List<AnimPoint> animPointsLeft = new ArrayList<>();
    private List<AnimPoint> animPointsRight = new ArrayList<>();
    private ObjectAnimator lineAnim;
    private DisplayMode curDisplayMode = DisplayMode.Appear;
    private long lineAnimDuration = 400;
    private int lineColor = Color.parseColor("#eb273f");
    private int lineStroke = 8;

    private Animator.AnimatorListener animatorListener;

    public NormalLineAnimDrawable() {
        this(null);
    }

    public NormalLineAnimDrawable(Paint paint) {
        pathLeft = new Path();
        paintLeft = paint == null ? getDefaultPaint() : paint;
        pathRight = new Path();
        paintRight = paint == null ? getDefaultPaint() : paint;
    }

    private Paint getDefaultPaint() {
        final Paint p = new Paint();
        p.setAntiAlias(true);
        p.setDither(true);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeJoin(Paint.Join.ROUND);
        p.setStrokeCap(Paint.Cap.ROUND);
        p.setStrokeWidth(lineStroke);
        p.setColor(lineColor);
        return p;
    }

    private ObjectAnimator getLineAnim() {
        final PropertyValuesHolder pvMoveY = PropertyValuesHolder.ofFloat(FACTOR_Y, 0f, 1f);
        final PropertyValuesHolder pvMoveX = PropertyValuesHolder.ofFloat(FACTOR_X, 0f, 1f);
        final ObjectAnimator lineAnim = ObjectAnimator.ofPropertyValuesHolder(this, pvMoveY, pvMoveX).setDuration(lineAnimDuration);
        lineAnim.setRepeatMode(ValueAnimator.RESTART);
        lineAnim.setRepeatCount(animPointsLeft.size() - 1);
        lineAnim.addUpdateListener(this);
        if (android.os.Build.VERSION.SDK_INT > 17) {
            lineAnim.setAutoCancel(true);
        }
        lineAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                moveTimes = 0;
                curAnimLeftPoint = animPointsLeft.get(moveTimes);
                if (twoWay) {
                    curAnimRightPoint = animPointsRight.get(moveTimes);
                }
                if (animatorListener != null)
                    animatorListener.onAnimationStart(animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (animatorListener != null)
                    animatorListener.onAnimationEnd(animation);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                if (animatorListener != null)
                    animatorListener.onAnimationCancel(animation);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                moveTimes++;
                curAnimLeftPoint = animPointsLeft.get(moveTimes);
                if (twoWay) {
                    curAnimRightPoint = animPointsRight.get(moveTimes);
                }
                if (animatorListener != null)
                    animatorListener.onAnimationRepeat(animation);
            }
        });
        //lineAnim.addListener(animatorListener);
        return lineAnim;
    }

    public void setAnimatorListener(Animator.AnimatorListener animatorListener) {
        this.animatorListener = animatorListener;
    }

    public void playAnim(List<AnimPoint> animPoints) {
        if (animPoints != null) {
            animPointsLeft = animPoints;
        }
        if (lineAnim == null) {
            lineAnim = getLineAnim();
        }
        if (lineAnim.isRunning()) {
            lineAnim.cancel();
        }
        lineAnim.start();
    }

    public void playAnim() {
        playAnim(null);
    }


    public float getFactorY() {
        return factorY;
    }

    public void setFactorY(float factorY) {
        this.factorY = factorY;
    }

    public float getFactorX() {
        return factorX;
    }

    public void setFactorX(float factorX) {
        this.factorX = factorX;
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        invalidateSelf();
    }

    private void drawLine(List<AnimPoint> animPoints, int num) {
        drawLine(animPoints, num, animPoints.size());
    }

    private void drawLine(List<AnimPoint> animPoints, int num, int size) {
        for (int i = num; i < size; i++) {
            final AnimPoint p = animPoints.get(i);
            pathLeft.moveTo(p.getCurX(), p.getCurY());
            pathLeft.lineTo(p.getMoveX(), p.getMoveY());
        }
    }

    private void drawLineRight(List<AnimPoint> animPoints, int num) {
        drawLineRight(animPoints, num, animPoints.size());
    }

    private void drawLineRight(List<AnimPoint> animPoints, int num, int size) {
        for (int i = num; i < size; i++) {
            final AnimPoint p = animPoints.get(i);
            pathRight.moveTo(p.getCurX(), p.getCurY());
            pathRight.lineTo(p.getMoveX(), p.getMoveY());
        }
    }

    public DisplayMode getCurDisplayMode() {
        return curDisplayMode;
    }

    public void setCurDisplayMode(DisplayMode curDisplayMode) {
        this.curDisplayMode = curDisplayMode;
    }

    @Override
    public void draw(Canvas canvas) {
        if (curAnimLeftPoint != null) {
            pathLeft.rewind();
            final float curX = curAnimLeftPoint.getCurX();
            final float curY = curAnimLeftPoint.getCurY();
            final float moveX = curAnimLeftPoint.getMoveX();
            final float moveY = curAnimLeftPoint.getMoveY();
            if (curDisplayMode == DisplayMode.Disappear) {
                pathLeft.moveTo(curX == moveX ? moveX : curX + ((moveX - curX) * factorX), curY == moveY ? moveY : curY + ((moveY - curY) * factorY));
                pathLeft.lineTo(moveX, moveY);
                drawLine(animPointsLeft, moveTimes + 1);
            } else if (curDisplayMode == DisplayMode.Appear) {
                drawLine(animPointsLeft, 0, moveTimes);
                pathLeft.moveTo(curX, curY);
                pathLeft.lineTo(curX == moveX ? moveX : curX + ((moveX - curX) * factorX), curY == moveY ? moveY : curY + ((moveY - curY) * factorY));
            }
            canvas.drawPath(pathLeft, paintLeft);
        } else {
            canvas.drawPath(pathLeft, paintLeft);
        }
        if (twoWay) {
            if (curAnimRightPoint != null) {
                pathRight.rewind();
                final float curX = curAnimRightPoint.getCurX();
                final float curY = curAnimRightPoint.getCurY();
                final float moveX = curAnimRightPoint.getMoveX();
                final float moveY = curAnimRightPoint.getMoveY();
                if (curDisplayMode == DisplayMode.Disappear) {
                    pathRight.moveTo(curX == moveX ? moveX : curX + ((moveX - curX) * factorX), curY == moveY ? moveY : curY + ((moveY - curY) * factorY));
                    pathRight.lineTo(moveX, moveY);
                    drawLineRight(animPointsRight, moveTimes + 1);
                } else if (curDisplayMode == DisplayMode.Appear) {
                    drawLineRight(animPointsRight, 0, moveTimes);
                    pathRight.moveTo(curX, curY);
                    pathRight.lineTo(curX == moveX ? moveX : curX + ((moveX - curX) * factorX), curY == moveY ? moveY : curY + ((moveY - curY) * factorY));
                }
                canvas.drawPath(pathRight, paintRight);
            } else {
                canvas.drawPath(pathRight, paintRight);
            }
        }
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter cf) {

    }

    @Override
    public int getOpacity() {
        return 0;
    }

    public List<AnimPoint> getPoints() {
        return animPointsLeft;
    }

    public void setPoints(List<AnimPoint> animPoints) {
        animPointsLeft = animPoints;
    }

    public void setSecondPoints(List<AnimPoint> secondPoints) {
        twoWay = true;
        animPointsRight = secondPoints;
    }

    public void setLineAnimDuration(long lineAnimDuration) {
        this.lineAnimDuration = lineAnimDuration;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    public void setLineStroke(int lineStroke) {
        this.lineStroke = lineStroke;
    }

    /**
     * How to display the LineAnim
     */
    public enum DisplayMode {

        /**
         * Lets disappear the lines.
         */
        Disappear,

        /**
         * Lets appear the lines.
         */
        Appear,
    }

}
