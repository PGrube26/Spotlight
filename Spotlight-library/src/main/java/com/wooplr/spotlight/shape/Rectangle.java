package com.wooplr.spotlight.shape;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import com.wooplr.spotlight.target.Target;

/**
 * @author PGrube26
 */
public class Rectangle implements TargetShape {

    private Target target;
    private Rect drawRect;
    private Point middlePoint;
    private int padding = 20;

    /**
     * Constructor.
     *
     * @param target  the {@link Target} where to show the spotlight view
     * @param padding a padding between the targets boarder and the background
     */
    public Rectangle(Target target, int padding) {
        this.target = target;
        this.padding = padding;
        middlePoint = getFocusPoint();
        calculateRect(padding);
    }

    @Override
    public void draw(Canvas canvas, Paint eraser, int padding) {
        calculateRect(padding);
        canvas.drawRect(drawRect, eraser);
    }

    @Override
    public void reCalculateAll() {
        calculateRect(padding);
        middlePoint = getFocusPoint();
    }

    @Override
    public int getXRadius() {
        return drawRect.width() / 2;
    }

    @Override
    public int getYRadius() {
        return drawRect.height() / 2;
    }

    @Override
    public Point getPoint() {
        return middlePoint;
    }

    @Override
    public Rect getRect() {
        return drawRect;
    }

    private void calculateRect(int padding) {
        drawRect = new Rect(target.getRect());
        drawRect.left = drawRect.left - padding;
        drawRect.top = drawRect.top - padding;
        drawRect.right = drawRect.right + padding;
        drawRect.bottom = drawRect.bottom + padding;
    }

    private Point getFocusPoint() {
        return target.getPoint();
    }
}
