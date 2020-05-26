package curso.android.minitwitter.common;

import android.content.Context;
import android.content.SharedPreferences;

import curso.android.minitwitter.R;

import static curso.android.minitwitter.common.Constantes.APP_SETTINGS_FILE;

public class SharedPreferencesManager {
    public SharedPreferencesManager(){}


    private static SharedPreferences getSharedPreferences(){
        return MyApp.getContext().getSharedPreferences(
                APP_SETTINGS_FILE, Context.MODE_PRIVATE);
    }
    
    public static void setStringValue(String stringLabel, String stringValue){
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(stringLabel, stringValue);
        editor.commit();
        
    }

    public static void setBooleanValue(String stringLabel, boolean stringValue){
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putBoolean(stringLabel, stringValue);
        editor.commit();

    }

    public static String getSomeStringValue(String dataLabel){
        return getSharedPreferences().getString(dataLabel, null);
    }

    public static boolean getSomeBooleanValue(String dataLabel){
        return getSharedPreferences().getBoolean(dataLabel, false);
    }
}
