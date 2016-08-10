package com.wooplr.spotlight.shape;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

/**
 * @author PGrube26
 */
@SuppressWarnings("unused")
public interface TargetShape {

    /**
     * Called to draw the shape in the given canvas.
     *
     * @param canvas  the canvas where to draw the shape
     * @param eraser  the paint used to draw the shape
     * @param padding a padding between the shape and the target
     */
    void draw(Canvas canvas, Paint eraser, int padding);

    /**
     * Recalculated the shape position and appearance.
     */
    void reCalculateAll();

    /**
     * Returns the radius in x dimension of the shape.
     *
     * @return the radius in x dimension of the shape
     */
    int getXRadius();

    /**
     * Returns the radius in y dimension of the shape.
     *
     * @return the radius in y dimension of the shape
     */
    int getYRadius();

    /**
     * Returns the middle point of the shape.
     *
     * @return the middle point of the shape
     */
    Point getPoint();

    /**
     * Returns the bounding rect of the shape.
     *
     * @return the bounding rect of the shape
     */
    Rect getRect();
}
