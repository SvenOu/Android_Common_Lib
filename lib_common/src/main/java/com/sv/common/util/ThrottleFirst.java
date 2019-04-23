package com.sv.common.util;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.HashMap;

/**
 * Utility class for scheduling runnables on the UI thread, only keeping the last call within a given timed margin.
 */
public class ThrottleFirst {

    private static final String TAG = ThrottleFirst.class.getSimpleName();
    private static ThrottleFirst _instance;

    private final HashMap<String, Runnable> runnables;
    private final Handler handler;

    private ThrottleFirst() {
        runnables = new HashMap<String, Runnable>();
        handler = new Handler(Looper.getMainLooper());
    }

    private static ThrottleFirst getInstance() {
        if (_instance == null) {
            _instance = new ThrottleFirst();
        }
        return _instance;
    }

    /**
     * @param identifier A {@link String} identifier to debounce on
     * @param r          {@link Runnable} The runnable to debounce
     * @param millis     {@link int} The minimum amount of time allowed to elapse between debounce calls before executing the last called runnable with the given identifier
     * @return Always {@literal true}
     */
    public static boolean throttleFirst(final String identifier, final Runnable r, final int millis) {
        if (getInstance().runnables.containsKey(identifier)) {
            // debounce
            Log.d(TAG, String.format("ThrottleFirst runnable with identifier \"%s\"", identifier));
            return false;
        }

        getInstance().insertRunnable(identifier, r, millis);

        return true;
    }

    private void insertRunnable(final String identifier, final Runnable r, final int millis) {
        Runnable chained = new Runnable() {
            @Override
            public void run() {
                runnables.remove(identifier);
                Log.d(TAG, String.format("Runnable with name \"%s\" posted throttleFirst complete.", identifier));
            }
        };
        runnables.put(identifier, chained);
        handler.postDelayed(chained, millis);
        Log.d(TAG, String.format("Runnable with name \"%s\"", identifier));
        handler.post(r);
    }

}