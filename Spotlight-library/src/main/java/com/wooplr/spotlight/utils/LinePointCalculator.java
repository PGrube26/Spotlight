package com.wooplr.spotlight.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import com.wooplr.spotlight.SpotlightView;
import com.wooplr.spotlight.shape.TargetShape;
import com.wooplr.spotlight.target.AnimPoint;
import com.wooplr.spotlight.target.Target;

import java.util.ArrayList;
import java.util.List;

/**
 * @author PGrube26
 */
public class LinePointCalculator {

    /**
     * Margin from left, right, top and bottom till the line will stop
     */
    private final int gutter;

    /**
     * Extra padding around the arc
     */
    private int extraPaddingForArc = 24;

    private View descriptionView;

    /**
     * Target View
     */
    private Target targetView;

    /**
     * Constructor.
     *
     * @param context Context used to calculate px from dip
     */
    public LinePointCalculator(Context context) {
        gutter = Math.round(Utils.dipToPx(context, 16));
    }

    /**
     * Sets extra padding between the spotlight view and the path animations
     *
     * @param extraPaddingForArc extra padding
     */
    public void setExtraPaddingForArc(int extraPaddingForArc) {
        this.extraPaddingForArc = extraPaddingForArc;
    }

    /**
     * Sets the view to set in the spotlight and describe the spotlight view.
     *
     * @param descriptionView the view to set as the description view
     */
    public void setDescriptionView(View descriptionView) {
        this.descriptionView = descriptionView;
    }

    /**
     * Sets the target used to show the spotlight view to.
     *
     * @param targetView the {@link Target} where to show the spotlight view
     */
    public void setTargetView(Target targetView) {
        this.targetView = targetView;
    }

    /**
     * Calculates the points required to draw the left line of the bounding box of the description view.
     *
     * @param lastX          the last x position of the previous line path
     * @param lastY          the last y position of the previous line path
     * @param targetPosition the calculated position of the target view
     * @return a list of AnimPoints used to draw the left line of the bounding box
     */
    public List<AnimPoint> calcLeftBoundingLinePoints(float lastX, float lastY, @PositionCalculator.SpotPosition int targetPosition) {
        final List<AnimPoint> animPoints = new ArrayList<>();
        switch (targetPosition) {
            case PositionCalculator.TOP_LEFT:
            case PositionCalculator.TOP_MIDDLE:
            case PositionCalculator.TOP_RIGHT:
                animPoints.add(new AnimPoint(
                        lastX, lastY,
                        descriptionView.getX(), lastY
                ));
                animPoints.add(new AnimPoint(
                        descriptionView.getX(), lastY,
                        descriptionView.getX(), descriptionView.getY() + getHelpViewRealHeight()
                ));
                animPoints.add(new AnimPoint(
                        descriptionView.getX(), descriptionView.getY() + getHelpViewRealHeight(),
                        lastX, descriptionView.getY() + getHelpViewRealHeight()
                ));
                break;
            case PositionCalculator.MIDDLE_LEFT:
                animPoints.add(new AnimPoint(
                        lastX, lastY,
                        lastX, descriptionView.getY()
                ));
                animPoints.add(new AnimPoint(
                        lastX, descriptionView.getY(),
                        descriptionView.getX() + getHelpViewRealWidth(), descriptionView.getY()
                ));
                animPoints.add(new AnimPoint(
                        descriptionView.getX() + getHelpViewRealWidth(), descriptionView.getY(),
                        descriptionView.getX() + getHelpViewRealWidth(), lastY
                ));
                break;
            case PositionCalculator.MIDDLE_MIDDLE:
                //TODO
                break;
            case PositionCalculator.MIDDLE_RIGHT:
                animPoints.add(new AnimPoint(
                        lastX, lastY,
                        lastX, descriptionView.getY()
                ));
                animPoints.add(new AnimPoint(
                        lastX, descriptionView.getY(),
                        descriptionView.getX(), descriptionView.getY()
                ));
                animPoints.add(new AnimPoint(
                        descriptionView.getX(), descriptionView.getY(),
                        descriptionView.getX(), lastY
                ));
                break;
            case PositionCalculator.BOTTOM_LEFT:
            case PositionCalculator.BOTTOM_MIDDLE:
            case PositionCalculator.BOTTOM_RIGHT:
                animPoints.add(new AnimPoint(
                        lastX, lastY,
                        descriptionView.getX(), lastY
                ));
                animPoints.add(new AnimPoint(
                        descriptionView.getX(), lastY,
                        descriptionView.getX(), descriptionView.getY()
                ));
                animPoints.add(new AnimPoint(
                        descriptionView.getX(), descriptionView.getY(),
                        lastX, descriptionView.getY()
                ));
                break;
            default:
                break;
        }
        return animPoints;
    }

    /**
     * Calculates the points required to draw the right line of the bounding box of the description view.
     *
     * @param lastX          the last x position of the previous line path
     * @param lastY          the last y position of the previous line path
     * @param targetPosition the calculated position of the target view
     * @return a list of AnimPoints used to draw the right line of the bounding box
     */
    public List<AnimPoint> calcRightBoundingLinePoints(float lastX, float lastY, @PositionCalculator.SpotPosition int targetPosition) {
        final List<AnimPoint> animPoints = new ArrayList<>();
        switch (targetPosition) {
            case PositionCalculator.TOP_LEFT:
            case PositionCalculator.TOP_MIDDLE:
            case PositionCalculator.TOP_RIGHT:
                animPoints.add(new AnimPoint(
                        lastX, lastY,
                        descriptionView.getX() + getHelpViewRealWidth(), lastY
                ));
                animPoints.add(new AnimPoint(
                        descriptionView.getX() + getHelpViewRealWidth(), lastY,
                        descriptionView.getX() + getHelpViewRealWidth(), descriptionView.getY() + getHelpViewRealHeight()
                ));
                animPoints.add(new AnimPoint(
                        descriptionView.getX() + getHelpViewRealWidth(), descriptionView.getY() + getHelpViewRealHeight(),
                        lastX, descriptionView.getY() + getHelpViewRealHeight()
                ));
                break;
            case PositionCalculator.MIDDLE_LEFT:
                animPoints.add(new AnimPoint(
                        lastX, lastY,
                        lastX, descriptionView.getY() + getHelpViewRealHeight()
                ));
                animPoints.add(new AnimPoint(
                        lastX, descriptionView.getY() + getHelpViewRealHeight(),
                        descriptionView.getX() + getHelpViewRealWidth(), descriptionView.getY() + getHelpViewRealHeight()
                ));
                animPoints.add(new AnimPoint(
                        descriptionView.getX() + getHelpViewRealWidth(), descriptionView.getY() + getHelpViewRealHeight(),
                        descriptionView.getX() + getHelpViewRealWidth(), lastY
                ));
                break;
            case PositionCalculator.MIDDLE_MIDDLE:
                //TODO
                break;
            case PositionCalculator.MIDDLE_RIGHT:
                animPoints.add(new AnimPoint(
                        lastX, lastY,
                        lastX, descriptionView.getY() + getHelpViewRealHeight()
                ));
                animPoints.add(new AnimPoint(
                        lastX, descriptionView.getY() + getHelpViewRealHeight(),
                        descriptionView.getX(), descriptionView.getY() + getHelpViewRealHeight()
                ));
                animPoints.add(new AnimPoint(
                        descriptionView.getX(), descriptionView.getY() + getHelpViewRealHeight(),
                        descriptionView.getX(), lastY
                ));
                break;
            case PositionCalculator.BOTTOM_LEFT:
            case PositionCalculator.BOTTOM_MIDDLE:
            case PositionCalculator.BOTTOM_RIGHT:
                animPoints.add(new AnimPoint(
                        lastX, lastY,
                        descriptionView.getX() + getHelpViewRealWidth(), lastY
                ));
                animPoints.add(new AnimPoint(
                        descriptionView.getX() + getHelpViewRealWidth(), lastY,
                        descriptionView.getX() + getHelpViewRealWidth(), descriptionView.getY()
                ));
                animPoints.add(new AnimPoint(
                        descriptionView.getX() + getHelpViewRealWidth(), descriptionView.getY(),
                        lastX, descriptionView.getY()
                ));
                break;
            default:
                break;
        }
        return animPoints;
    }

    /**
     * Calculates the points required to draw a line from the spotlight to the bounding box of the description view.
     *
     * @param targetPosition the calculated position of the target view
     * @param screenWidth    the devices screen width
     * @param screenHeight   the devices screen height
     * @param targetShape    the {@link TargetShape} used to draw the spotlight view
     * @param shapeType      the type of the TargetShape
     * @return a list of AnimPoints used to draw a line
     */
    public List<AnimPoint> calcLinePoints(@PositionCalculator.SpotPosition int targetPosition, int screenWidth, int screenHeight, TargetShape targetShape,
                                          @SpotlightView.ShapeType int shapeType) {
        switch (targetPosition) {
            case PositionCalculator.TOP_LEFT:
                return linesForTopLeft(shapeType, screenWidth, screenHeight, targetShape);
            case PositionCalculator.TOP_MIDDLE:
                return linesForTopMiddle(shapeType, screenHeight, targetShape);
            case PositionCalculator.TOP_RIGHT:
                return linesForTopRight(shapeType, screenWidth, screenHeight, targetShape);
            case PositionCalculator.MIDDLE_LEFT:
                return linesForMiddleLeft(shapeType, screenWidth, screenHeight, targetShape);
            case PositionCalculator.MIDDLE_MIDDLE:
                return null;
            //TODO
            case PositionCalculator.MIDDLE_RIGHT:
                return linesForMiddleRight(shapeType, screenHeight, targetShape);
            case PositionCalculator.BOTTOM_LEFT:
                return linesForBottomLeft(shapeType, screenWidth, targetShape);
            case PositionCalculator.BOTTOM_MIDDLE:
                return linesForBottomMiddle(shapeType, targetShape);
            case PositionCalculator.BOTTOM_RIGHT:
                return linesForBottomRight(shapeType, screenWidth, targetShape);
            default:
                return null;
        }
    }

    private List<AnimPoint> linesForTopLeft(int shapeType, int screenWidth, int screenHeight, TargetShape targetShape) {
        calcTopLayoutParams(screenHeight);
        if (shapeType == SpotlightView.SHAPE_TYPE_CIRCLE) {
            final List<AnimPoint> animPoints = new ArrayList<>();
            animPoints.add(new AnimPoint(
                    targetView.getPoint().x + targetShape.getXRadius() + extraPaddingForArc,
                    targetView.getViewBottom() - targetView.getViewHeight() / 2,
                    (screenWidth - targetView.getViewRight()) / 2 + targetView.getViewRight(),
                    targetView.getViewBottom() - targetView.getViewHeight() / 2
            ));
            animPoints.add(new AnimPoint(
                    (screenWidth - targetView.getViewRight()) / 2 + targetView.getViewRight(),
                    targetView.getViewBottom() - targetView.getViewHeight() / 2,
                    (screenWidth - targetView.getViewRight()) / 2 + targetView.getViewRight(),
                    ((FrameLayout.LayoutParams) descriptionView.getLayoutParams()).topMargin
            ));
            return animPoints;
        } else {
            return linesForTopRectViews(targetShape, screenWidth);
        }
    }

    private List<AnimPoint> linesForTopRight(int shapeType, int screenWidth, int screenHeight, TargetShape targetShape) {
        calcTopLayoutParams(screenHeight);
        if (shapeType == SpotlightView.SHAPE_TYPE_CIRCLE) {
            final List<AnimPoint> animPoints = new ArrayList<>();
            animPoints.add(new AnimPoint(
                    targetView.getPoint().x - targetShape.getXRadius() - extraPaddingForArc,
                    targetView.getViewBottom() - targetView.getViewHeight() / 2,
                    targetView.getViewLeft() / 2,
                    targetView.getViewBottom() - targetView.getViewHeight() / 2
            ));
            animPoints.add(new AnimPoint(
                    targetView.getViewLeft() / 2,
                    targetView.getViewBottom() - targetView.getViewHeight() / 2,
                    targetView.getViewLeft() / 2,
                    ((FrameLayout.LayoutParams) descriptionView.getLayoutParams()).topMargin
            ));
            return animPoints;
        } else {
            return linesForTopRectViews(targetShape, screenWidth);
        }
    }

    private List<AnimPoint> linesForTopRectViews(TargetShape targetShape, int screenWidth) {
        final int targetShapeDescriptionViewSpaceMiddle =
                (((FrameLayout.LayoutParams) descriptionView.getLayoutParams()).topMargin -
                        (targetView.getPoint().y + targetShape.getYRadius() + extraPaddingForArc)) / 2;

        final List<AnimPoint> animPoints = new ArrayList<>();
        animPoints.add(new AnimPoint(
                targetView.getPoint().x,
                targetView.getPoint().y + targetShape.getYRadius() + extraPaddingForArc,
                targetView.getPoint().x,
                targetView.getPoint().y + targetShape.getYRadius() + extraPaddingForArc + targetShapeDescriptionViewSpaceMiddle
        ));
        animPoints.add(new AnimPoint(
                targetView.getPoint().x,
                targetView.getPoint().y + targetShape.getYRadius() + extraPaddingForArc + targetShapeDescriptionViewSpaceMiddle,
                screenWidth / 2,
                targetView.getPoint().y + targetShape.getYRadius() + extraPaddingForArc + targetShapeDescriptionViewSpaceMiddle
        ));
        animPoints.add(new AnimPoint(
                screenWidth / 2,
                targetView.getPoint().y + targetShape.getYRadius() + extraPaddingForArc + targetShapeDescriptionViewSpaceMiddle,
                screenWidth / 2,
                ((FrameLayout.LayoutParams) descriptionView.getLayoutParams()).topMargin
        ));
        return animPoints;
    }

    private List<AnimPoint> linesForTopMiddle(int shapeType, int screenHeight, TargetShape targetShape) {
        calcTopLayoutParams(screenHeight);
        final List<AnimPoint> animPoints = new ArrayList<>();
        if (shapeType == SpotlightView.SHAPE_TYPE_CIRCLE) {
            animPoints.add(new AnimPoint(
                    targetView.getViewRight() - targetView.getViewWidth() / 2,
                    targetView.getPoint().y + targetShape.getXRadius() + extraPaddingForArc,
                    targetView.getViewRight() - targetView.getViewWidth() / 2,
                    ((FrameLayout.LayoutParams) descriptionView.getLayoutParams()).topMargin
            ));
        } else {
            animPoints.add(new AnimPoint(
                    targetView.getPoint().x,
                    targetView.getPoint().y + targetShape.getYRadius() + extraPaddingForArc,
                    targetView.getPoint().x,
                    ((FrameLayout.LayoutParams) descriptionView.getLayoutParams()).topMargin
            ));
        }
        return animPoints;
    }

    private List<AnimPoint> linesForMiddleLeft(int shapeType, int screenWidth, int screenHeight, TargetShape targetShape) {
        calcMiddleLeftLayoutParams(screenWidth, screenHeight);
        final List<AnimPoint> animPoints = new ArrayList<>();
        if (shapeType == SpotlightView.SHAPE_TYPE_CIRCLE) {
            animPoints.add(new AnimPoint(
                    targetView.getPoint().x + targetShape.getXRadius() + extraPaddingForArc,
                    targetView.getViewBottom() - targetView.getViewHeight() / 2,
                    ((FrameLayout.LayoutParams) descriptionView.getLayoutParams()).leftMargin,
                    targetView.getViewBottom() - targetView.getViewHeight() / 2
            ));
        } else {
            animPoints.add(new AnimPoint(
                    targetView.getPoint().x,
                    targetView.getPoint().y + targetShape.getYRadius() + extraPaddingForArc,
                    targetView.getPoint().x,
                    targetView.getPoint().y + targetShape.getYRadius() + extraPaddingForArc + gutter
            ));
            animPoints.add(new AnimPoint(
                    targetView.getPoint().x,
                    targetView.getPoint().y + targetShape.getYRadius() + extraPaddingForArc + gutter,
                    ((FrameLayout.LayoutParams) descriptionView.getLayoutParams()).leftMargin,
                    targetView.getPoint().y + targetShape.getYRadius() + extraPaddingForArc + gutter
            ));
        }

        return animPoints;
    }

    private List<AnimPoint> linesForMiddleRight(int shapeType, int screenHeight, TargetShape targetShape) {
        calcMiddleRightLayoutParams(screenHeight);
        final List<AnimPoint> animPoints = new ArrayList<>();
        if (shapeType == SpotlightView.SHAPE_TYPE_CIRCLE) {
            animPoints.add(new AnimPoint(
                    targetView.getPoint().x - targetShape.getXRadius() - extraPaddingForArc,
                    targetView.getViewBottom() - targetView.getViewHeight() / 2,
                    descriptionView.getX() + getHelpViewRealWidth() + gutter,
                    targetView.getViewBottom() - targetView.getViewHeight() / 2
            ));
        } else {
            animPoints.add(new AnimPoint(
                    targetView.getPoint().x,
                    targetView.getPoint().y + targetShape.getYRadius() + extraPaddingForArc,
                    targetView.getPoint().x,
                    targetView.getPoint().y + targetShape.getYRadius() + extraPaddingForArc + gutter
            ));
            animPoints.add(new AnimPoint(
                    targetView.getPoint().x,
                    targetView.getPoint().y + targetShape.getYRadius() + extraPaddingForArc,
                    descriptionView.getX() + getHelpViewRealWidth() + gutter,
                    targetView.getPoint().y + targetShape.getYRadius() + extraPaddingForArc
            ));
        }
        return animPoints;
    }

    private List<AnimPoint> linesForBottomLeft(int shapeType, int screenWidth, TargetShape targetShape) {
        calcBelowLayoutParams();
        if (shapeType == SpotlightView.SHAPE_TYPE_CIRCLE) {
            final List<AnimPoint> animPoints = new ArrayList<>();
            animPoints.add(new AnimPoint(
                    targetView.getPoint().x + targetShape.getXRadius() + extraPaddingForArc,
                    targetView.getViewBottom() - targetView.getViewHeight() / 2,
                    (screenWidth - targetView.getViewRight()) / 2 + targetView.getViewRight(),
                    targetView.getViewBottom() - targetView.getViewHeight() / 2
            ));
            animPoints.add(new AnimPoint(
                    (screenWidth - targetView.getViewRight()) / 2 + targetView.getViewRight(),
                    targetView.getViewBottom() - targetView.getViewHeight() / 2,
                    (screenWidth - targetView.getViewRight()) / 2 + targetView.getViewRight(),
                    ((FrameLayout.LayoutParams) descriptionView.getLayoutParams()).topMargin + getHelpViewRealHeight()
            ));
            return animPoints;
        } else {
            return linesForBottomRectViews(targetShape, screenWidth);
        }
    }

    private List<AnimPoint> linesForBottomRight(int shapeType, int screenWidth, TargetShape targetShape) {
        calcBelowLayoutParams();
        if (shapeType == SpotlightView.SHAPE_TYPE_CIRCLE) {
            final List<AnimPoint> animPoints = new ArrayList<>();
            animPoints.add(new AnimPoint(
                    targetView.getPoint().x - targetShape.getXRadius() - extraPaddingForArc,
                    targetView.getViewBottom() - targetView.getViewHeight() / 2,
                    targetView.getViewLeft() / 2,
                    targetView.getViewBottom() - targetView.getViewHeight() / 2
            ));
            animPoints.add(new AnimPoint(
                    targetView.getViewLeft() / 2,
                    targetView.getViewBottom() - targetView.getViewHeight() / 2,
                    targetView.getViewLeft() / 2,
                    ((FrameLayout.LayoutParams) descriptionView.getLayoutParams()).topMargin + getHelpViewRealHeight()
            ));
            return animPoints;
        } else {
            return linesForBottomRectViews(targetShape, screenWidth);
        }
    }

    private List<AnimPoint> linesForBottomRectViews(TargetShape targetShape, int screenWidth) {
        final int targetShapeDescriptionViewSpace = (targetView.getPoint().y - targetShape.getYRadius() - extraPaddingForArc) -
                (((FrameLayout.LayoutParams) descriptionView.getLayoutParams()).topMargin + getHelpViewRealHeight());
        final int targetShapeDescriptionViewSpaceMiddle = targetShapeDescriptionViewSpace / 2;

        final List<AnimPoint> animPoints = new ArrayList<>();
        animPoints.add(new AnimPoint(
                targetView.getPoint().x,
                targetView.getPoint().y - targetShape.getYRadius() - extraPaddingForArc,
                targetView.getPoint().x,
                targetView.getPoint().y - targetShape.getYRadius() - extraPaddingForArc - targetShapeDescriptionViewSpaceMiddle
        ));
        animPoints.add(new AnimPoint(
                targetView.getPoint().x,
                targetView.getPoint().y - targetShape.getYRadius() - extraPaddingForArc - targetShapeDescriptionViewSpaceMiddle,
                screenWidth / 2,
                targetView.getPoint().y - targetShape.getYRadius() - extraPaddingForArc - targetShapeDescriptionViewSpaceMiddle
        ));
        animPoints.add(new AnimPoint(
                screenWidth / 2,
                targetView.getPoint().y - targetShape.getYRadius() - extraPaddingForArc - targetShapeDescriptionViewSpaceMiddle,
                screenWidth / 2,
                ((FrameLayout.LayoutParams) descriptionView.getLayoutParams()).topMargin + getHelpViewRealHeight()
        ));
        return animPoints;
    }

    private List<AnimPoint> linesForBottomMiddle(int shapeType, TargetShape targetShape) {
        final List<AnimPoint> animPoints = new ArrayList<>();
        calcBelowLayoutParams();
        if (shapeType == SpotlightView.SHAPE_TYPE_CIRCLE) {
            animPoints.add(new AnimPoint(
                    targetView.getViewRight() - targetView.getViewWidth() / 2,
                    targetView.getPoint().y - targetShape.getXRadius() - extraPaddingForArc,
                    targetView.getViewRight() - targetView.getViewWidth() / 2,
                    ((FrameLayout.LayoutParams) descriptionView.getLayoutParams()).topMargin + getHelpViewRealHeight()
            ));
        } else {
            animPoints.add(new AnimPoint(
                    targetView.getPoint().x,
                    targetView.getPoint().y - targetShape.getYRadius() - extraPaddingForArc,
                    targetView.getPoint().x,
                    ((FrameLayout.LayoutParams) descriptionView.getLayoutParams()).topMargin + getHelpViewRealHeight()
            ));
        }
        return animPoints;
    }

    private int getHelpViewRealWidth() {
        return ((FrameLayout.LayoutParams) descriptionView.getLayoutParams()).width < 0 ?
                descriptionView.getWidth() : ((FrameLayout.LayoutParams) descriptionView.getLayoutParams()).width;
    }

    private int getHelpViewRealHeight() {
        return ((FrameLayout.LayoutParams) descriptionView.getLayoutParams()).height < 0 ?
                descriptionView.getHeight() : ((FrameLayout.LayoutParams) descriptionView.getLayoutParams()).height;
    }

    private void calcTopLayoutParams(int screenHeight) {
        final FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) descriptionView.getLayoutParams();
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;

        final int restHeight = screenHeight - targetView.getViewBottom() - gutter;

        if (descriptionView.getHeight() > restHeight) {
            layoutParams.height = restHeight;
        }

        final int restHeightMiddle = restHeight / 2;
        layoutParams.topMargin = restHeightMiddle - descriptionView.getHeight() / 2 + targetView.getViewBottom() + gutter / 2;
        descriptionView.setLayoutParams(layoutParams);
    }

    private void calcBelowLayoutParams() {
        final FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) descriptionView.getLayoutParams();
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;

        final int restHeight = targetView.getViewTop() - 2 * gutter;

        if (descriptionView.getHeight() > restHeight) {
            layoutParams.height = restHeight;
        }

        final int restHeightMiddle = restHeight / 2;
        layoutParams.topMargin = restHeightMiddle - descriptionView.getHeight() / 2 + gutter;

        descriptionView.setLayoutParams(layoutParams);
    }

    private void calcMiddleLeftLayoutParams(int screenWidth, int screenHeight) {
        final FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) descriptionView.getLayoutParams();

        final int restHeight = screenHeight - 2 * gutter;

        if (descriptionView.getHeight() > restHeight) {
            layoutParams.height = restHeight;
        }
        layoutParams.gravity = Gravity.CENTER_VERTICAL;

        final int restWidth = screenWidth - targetView.getViewRight() - 2 * gutter;

        if (descriptionView.getWidth() > restWidth) {
            layoutParams.width = restWidth;
        }
        final int restWidthMiddle = restWidth / 2;
        layoutParams.leftMargin = targetView.getViewRight() + gutter + restWidthMiddle - (layoutParams.width < 0 ? descriptionView.getWidth() : layoutParams.width)
                / 2;

        descriptionView.setLayoutParams(layoutParams);
    }

    private void calcMiddleRightLayoutParams(int screenHeight) {
        final FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) descriptionView.getLayoutParams();

        final int restHeight = screenHeight - 2 * gutter;

        if (descriptionView.getHeight() > restHeight) {
            layoutParams.height = restHeight;
        }
        layoutParams.gravity = Gravity.CENTER_VERTICAL;

        final int restWidth = targetView.getViewLeft() - 2 * gutter;

        if (descriptionView.getWidth() > restWidth) {
            layoutParams.width = restWidth;
        }
        final int restWidthMiddle = restWidth / 2;
        layoutParams.leftMargin = gutter + restWidthMiddle - (layoutParams.width < 0 ? descriptionView.getWidth() : layoutParams.width) / 2;

        descriptionView.setLayoutParams(layoutParams);
    }
}
