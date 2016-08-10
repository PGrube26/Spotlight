package com.wooplr.spotlight;

import android.graphics.Color;
import android.graphics.Typeface;

import com.wooplr.spotlight.utils.Utils;

/**
 * @author jitender on 14/06/16
 */
@SuppressWarnings("unused")
public class SpotlightConfig {
    private int maskColor;
    private long introAnimationDuration;
    private boolean isRevealAnimationEnabled = true;
    private int padding;
    private boolean dismissOnTouch;
    private boolean dismissOnBackPress;
    private boolean isPerformClick;
    private long lineAnimationDuration;
    private int lineStroke;
    private int lineAndArcColor;

    public SpotlightConfig() {
        this.maskColor = 0x70000000;
        this.introAnimationDuration = 400;
        this.isRevealAnimationEnabled = true;
        this.padding = 20;
        this.dismissOnTouch = true;
        this.isPerformClick = true;
        this.dismissOnBackPress = true;
        this.lineAnimationDuration = 300;
        this.lineStroke = Utils.dpToPx(4);
        this.lineAndArcColor = Color.parseColor("#eb273f");
    }

    public int getMaskColor() {
        return maskColor;
    }

    public void setMaskColor(int maskColor) {
        this.maskColor = maskColor;
    }

    public long getIntroAnimationDuration() {
        return introAnimationDuration;
    }

    public void setIntroAnimationDuration(long introAnimationDuration) {
        this.introAnimationDuration = introAnimationDuration;
    }

    public boolean isRevealAnimationEnabled() {
        return isRevealAnimationEnabled;
    }

    public void setRevealAnimationEnabled(boolean revealAnimationEnabled) {
        isRevealAnimationEnabled = revealAnimationEnabled;
    }

    public int getPadding() {
        return padding;
    }

    public void setPadding(int padding) {
        this.padding = padding;
    }

    public boolean isDismissOnTouch() {
        return dismissOnTouch;
    }

    public void setDismissOnTouch(boolean dismissOnTouch) {
        this.dismissOnTouch = dismissOnTouch;
    }

    public boolean isPerformClick() {
        return isPerformClick;
    }

    public void setPerformClick(boolean performClick) {
        isPerformClick = performClick;
    }

    public long getLineAnimationDuration() {
        return lineAnimationDuration;
    }

    public void setLineAnimationDuration(long lineAnimationDuration) {
        this.lineAnimationDuration = lineAnimationDuration;
    }

    public int getLineStroke() {
        return lineStroke;
    }

    public void setLineStroke(int lineStroke) {
        this.lineStroke = lineStroke;
    }

    public int getLineAndArcColor() {
        return lineAndArcColor;
    }

    public void setLineAndArcColor(int lineAndArcColor) {
        this.lineAndArcColor = lineAndArcColor;
    }

    public boolean isDismissOnBackPress() {
        return dismissOnBackPress;
    }

    public void setDismissOnBackPress(boolean dismissOnBackPress) {
        this.dismissOnBackPress = dismissOnBackPress;
    }
}
