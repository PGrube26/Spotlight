package com.wooplr.spotlight.utils;

import android.support.annotation.IntDef;

import com.wooplr.spotlight.target.Target;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author PGrube26
 */
public class PositionCalculator {

    /**
     * Possible positions of the target view.
     */
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TOP_LEFT, TOP_MIDDLE, TOP_RIGHT, MIDDLE_LEFT, MIDDLE_MIDDLE, MIDDLE_RIGHT, BOTTOM_LEFT, BOTTOM_MIDDLE, BOTTOM_RIGHT})
    public @interface SpotPosition {
    }

    //CS: STOP JAVADOC CHECK
    public static final int TOP_LEFT = 11;
    public static final int TOP_MIDDLE = 12;
    public static final int TOP_RIGHT = 13;
    public static final int MIDDLE_LEFT = 21;
    public static final int MIDDLE_MIDDLE = 22;
    public static final int MIDDLE_RIGHT = 23;
    public static final int BOTTOM_LEFT = 31;
    public static final int BOTTOM_MIDDLE = 32;
    public static final int BOTTOM_RIGHT = 33;
    //CS: RESUME JAVADOC CHECK

    private static final int TOP = 10;
    private static final int MIDDLE_V = 20;
    private static final int BOTTOM = 30;

    private static final int LEFT = 1;
    private static final int MIDDLE_H = 2;
    private static final int RIGHT = 3;

    private static final int PERCENT_HEIGHT_TOP = 40;
    private static final int PERCENT_HEIGHT_MIDDLE = 20;

    private static final int PERCENT_WIDTH_TOP_BOTTOM_LEFT = 45;
    private static final int PERCENT_WIDTH_TOP_BOTTOM_MIDDLE = 10;
    private static final int PERCENT_WIDTH_MIDDLE_LEFT = 20;
    private static final int PERCENT_WIDTH_MIDDLE_MIDDLE = 60;

    /**
     * Calculates the position of the target view in the screen.
     * - TOP if the views middle point is in 40% of the screens height
     * - MIDDLE_H if the views middle point is between 40% and 60% of the screens height
     * - BOTTOM if the views middle point is between 60% and 100% of the screens height
     * <p>
     * - LEFT if the views middle point is in 45% of the screens width when in TOP or BOTTOM
     * - LEFT if the views middle point is in 20% of the screens width when in MIDDLE_H
     * - MIDDLE if the views middle point is between 45% and 55% of the screens width when in TOP or BOTTOM
     * - MIDDLE if the views middle point is between 20% and 80% of the screens width when in MIDDLE_H
     * - RIGHT if the views middle point is between 55% and 100% of the screens width when IN TOP or BOTTOM
     * - RIGHT if the views middle point is between 80% and 100% of the screens width when in MIDDLE_H
     *
     * @param screenWidth  the devices screen width
     * @param screenHeight the devices screen height
     * @param target       the {@link Target} to check
     * @return the position of the target
     */
    public static int calculatePosition(int screenWidth, int screenHeight, Target target) {
        final int vertPos = calcVertPosition(screenHeight, target.getPoint().y);
        final int horPos = calcHorPosition(screenWidth, target.getPoint().x, vertPos == MIDDLE_V);

        return vertPos + horPos;
    }

    private static int calcVertPosition(int screenHeight, int targetMiddleY) {
        final int percentHeightTop = screenHeight * PERCENT_HEIGHT_TOP / 100;
        final int percentHeightMiddle = screenHeight * (PERCENT_HEIGHT_TOP + PERCENT_HEIGHT_MIDDLE) / 100;

        if (targetMiddleY < percentHeightTop) {
            return TOP;
        } else if (targetMiddleY < percentHeightMiddle) {
            return MIDDLE_V;
        } else {
            return BOTTOM;
        }
    }

    private static int calcHorPosition(int screenWidth, int targetMiddleX, boolean middleH) {
        final int percentWidthLeftTB = screenWidth * PERCENT_WIDTH_TOP_BOTTOM_LEFT / 100;
        final int percentWidthMiddleTB = screenWidth * (PERCENT_WIDTH_TOP_BOTTOM_LEFT + PERCENT_WIDTH_TOP_BOTTOM_MIDDLE) / 100;

        final int percentWidthLeftM = screenWidth * PERCENT_WIDTH_MIDDLE_LEFT / 100;
        final int percentWidthMiddleM = screenWidth * (PERCENT_WIDTH_MIDDLE_LEFT + PERCENT_WIDTH_MIDDLE_MIDDLE) / 100;

        if (middleH) {
            if (targetMiddleX < percentWidthLeftM) {
                return LEFT;
            } else if (targetMiddleX < percentWidthMiddleM) {
                return MIDDLE_H;
            } else {
                return RIGHT;
            }
        }

        if (targetMiddleX < percentWidthLeftTB) {
            return LEFT;
        } else if (targetMiddleX < percentWidthMiddleTB) {
            return MIDDLE_H;
        } else {
            return RIGHT;
        }
    }
}
