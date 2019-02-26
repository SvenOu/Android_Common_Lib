package com.sv.common.util;
import android.os.Handler;
import android.os.Looper;

import java.util.HashMap;

/**
 * Utility class for scheduling runnables on the UI thread, only keeping the last call within a given timed margin.
 */
public class Debouncer {

    private static final String TAG = Debouncer.class.getSimpleName();
    private static Debouncer sInstance;

    private final HashMap<String, Runnable> runnables;
    private final Handler handler;

    private Debouncer() {
        runnables = new HashMap<String, Runnable>();
        handler = new Handler(Looper.getMainLooper());
    }

    public static Debouncer getInstance() {
        if(sInstance == null) {
            sInstance = new Debouncer();
        }
        return sInstance;
    }

    /**
     * @param identifier A {@link String} identifier to debounce on
     * @param r {@link Runnable} The runnable to debounce
     * @param millis {@link int} The minimum amount of time allowed to elapse between debounce calls before executing the last called runnable with the given identifier
     * @return Always {@literal true}
     */
    public static boolean debounce(final String identifier, final Runnable r, final int millis) {
        if(getInstance().runnables.containsKey(identifier)) {
            // debounce
            Logger.d(TAG, String.format("Debouncing runnable with identifier \"%s\"", identifier));
            Runnable old = getInstance().runnables.get(identifier);
            getInstance().handler.removeCallbacks(old);
        }

        getInstance().insertRunnable(identifier, r, millis);

        return true;
    }

    private void insertRunnable(final String identifier, final Runnable r, final int millis) {
        Runnable chained = new Runnable() {
            @Override
            public void run() {
                handler.post(r);
                runnables.remove(identifier);
                Logger.d(TAG, String.format("Runnable with name \"%s\" posted and cannot be debounced from now on", identifier));
            }
        };
        runnables.put(identifier, chained);
        handler.postDelayed(chained, millis);
        Logger.d(TAG, String.format("Runnable with name \"%s\" scheduled in %s milliseconds", identifier, millis));
    }

}