package eu.darken.rxshell.extra;


import timber.log.Timber;

public class RXSDebug {
    private static final String TAG = "RXS:Debug";
    private static boolean DEBUG = false;

    public static void setDebug(boolean debug) {
        Timber.tag(TAG).i("setDebug(debug=%b)", debug);
        DEBUG = debug;
    }

    public static boolean isDebug() {
        return DEBUG;
    }
}
