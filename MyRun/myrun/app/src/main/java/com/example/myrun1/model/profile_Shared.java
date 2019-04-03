package com.example.myrun1.model;

import android.content.Context;
import android.content.SharedPreferences;


public class profile_Shared {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public profile_Shared(Context context, String preference_name, int mode)
    {
        sharedPreferences = context.getSharedPreferences(preference_name, mode);      //Context.MODE_PRIVATE
//        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
    }

    public void setString(String key_string, String key)
    {
        editor.putString(key_string, key);
        editor.commit();
    }

    public void setInt(String key_string, int key)
    {

    }

    public String getString(String key)
    {

        String data = sharedPreferences.getString(key, "n/a") ;
        return data;
    }

}
