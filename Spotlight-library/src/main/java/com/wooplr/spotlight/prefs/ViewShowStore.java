package com.wooplr.spotlight.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;

/**
 * @author PGrube26
 */
public class ViewShowStore {

    private static final String PREFS_FILE_NAME = "ViewHighlighterShowStore";
    private static final String PREFS_SHOW_KEY = "show_";

    private final Context context;

    /**
     * Constructor of the ViewShowStore.
     *
     * @param context the context to get shared preferences from
     */
    public ViewShowStore(Context context) {
        this.context = context;
    }

    /**
     * Saves the shown state of the highlight view if it should be shown only once (set by setting a highlight id).
     */
    public void saveSpotLightShown(int spotlightId) {
        final SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
        if (!sharedPreferences.contains(PREFS_SHOW_KEY + spotlightId)) {
            context.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE).edit().putBoolean(PREFS_SHOW_KEY + spotlightId, false).apply();
        }
    }

    /**
     * Returns true if the ViewHighlight should be shown to the user, false otherwise.
     *
     * @return true if the ViewHighlight should be shown
     */
    public boolean shouldShowHighlight(int spotLightId) {
        final SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(PREFS_SHOW_KEY + spotLightId, true);
    }

    /**
     * Resets all preferences for the spotlight view.
     */
    public static void resetPref(Context context) {
        final SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
    }
}
