package com.wooplr.spotlight.utils;

/**
 * @author PGrube26
 */
public interface SpotlightListener {

    /**
     * Called when the spotlight is dismissed.
     *
     * @param spotlightViewId the id of the spotlight which is dismissed
     */
    void onSpotlightDismissed(int spotlightViewId);

    /**
     * Called when the spotlight was not shown because of any failure.
     */
    void onSpotlightError(String errorMessage);
}
