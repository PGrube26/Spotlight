package com.wooplr.spotlight.shape;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import com.wooplr.spotlight.target.Target;

/**
 * @author jitender on 10/06/16
 */

public class Circle implements TargetShape {

    private Target target;
    private int radius;
    private Point circlePoint;
    private int padding = 20;

    public Circle(Target target, int padding) {
        this.target = target;
        this.padding = padding;
        circlePoint = getFocusPoint();
        calculateRadius(padding);
    }

    @Override
    public void draw(Canvas canvas, Paint eraser, int padding) {
        calculateRadius(padding);
        circlePoint = getFocusPoint();
        canvas.drawCircle(circlePoint.x, circlePoint.y, radius, eraser);
    }

    @Override
    public void reCalculateAll() {
        calculateRadius(padding);
        circlePoint = getFocusPoint();
    }

    @Override
    public int getYRadius() {
        return radius;
    }

    @Override
    public int getXRadius() {
        return radius;
    }

    @Override
    public Point getPoint() {
        return circlePoint;
    }

    @Override
    public Rect getRect() {
        return null;
    }

    private Point getFocusPoint() {
        return target.getPoint();
    }

    private void calculateRadius(int padding) {
        final int minSide = Math.min(target.getRect().width() / 2, target.getRect().height() / 2);
        final int maxSide = Math.max(target.getRect().width() / 2, target.getRect().height() / 2);
        final int side = (minSide + maxSide) / 2;
        radius = side + padding;
    }
}
