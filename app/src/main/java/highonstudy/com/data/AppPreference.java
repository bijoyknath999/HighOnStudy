package highonstudy.com.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AppPreference {

    // declare context
    private static Context mContext;

    // singleton
    private static AppPreference appPreference = null;

    // common
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences settingsPreferences;
    private final SharedPreferences.Editor editor;

    public static AppPreference getInstance(Context context) {
        if (appPreference == null) {
            mContext = context;
            appPreference = new AppPreference();
        }
        return appPreference;
    }

    private AppPreference() {
        sharedPreferences = mContext.getSharedPreferences(PrefKey.APP_PREF_NAME, Context.MODE_PRIVATE);
        settingsPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        editor = sharedPreferences.edit();
    }

    public void setClicks(int value) {
        editor.putInt("clicks", value);
        editor.commit();
    }

    public int getClicks() {
        return sharedPreferences.getInt("clicks", 0);
    }

    public void setItem(int value) {
        editor.putInt("item", value);
        editor.commit();
    }

    public int getItem() {
        return sharedPreferences.getInt("item", 0);
    }


    public void setNotify(boolean setValidity) {
        editor.putBoolean("perf_notification", setValidity);
        editor.commit();
    }

    public boolean isNotificationOn() {
        return sharedPreferences.getBoolean("perf_notification", true);
    }

}
