package lueorganisation.winmall.via.ourvisitor.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Fujitsu on 14/06/2017.
 */

public class SaveAccessToken {
    private static final String SHARED_PREF_NAME = "saveuserid";
    private static final String TAG_TOKEN = "save";

    private static SaveAccessToken mInstance;
    private static Context mCtx;

    private SaveAccessToken(Context context) {
        mCtx = context;
    }

    public static synchronized SaveAccessToken getInstance(Context context)
    {
        if (mInstance == null) {
            mInstance = new SaveAccessToken(context);
        }
        return mInstance;
    }

    //this method will save the device token to shared preferences
    public boolean saveuserId(String id)
    {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TAG_TOKEN, id);
        editor.apply();
        return true;
    }

    //this method will fetch the device token from shared preferences
    public String getUserId()
    {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getString(TAG_TOKEN, null);
    }

}
