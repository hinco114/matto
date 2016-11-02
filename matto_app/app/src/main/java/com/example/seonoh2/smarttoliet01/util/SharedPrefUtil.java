package com.example.seonoh2.smarttoliet01.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by 선오 on 2016-09-08.
 */
public class SharedPrefUtil {

  Context context;

  public SharedPrefUtil(Context context) {
    this.context = context;
  }

  public void setPreference(String name, String string) {
    SharedPreferences sharedPreferences = context.getSharedPreferences("shared_preference", Context.MODE_PRIVATE);
    sharedPreferences.edit().putString(name, string).apply();
  }

  public String getPreference(String name) {
    SharedPreferences sharedPreferences = context.getSharedPreferences("shared_preference", Context.MODE_PRIVATE);
    return sharedPreferences.getString(name, "not_found");
  }

  public void removePreference(String string) {
    SharedPreferences sharedPreferences = context.getSharedPreferences("shared_preference", Context.MODE_PRIVATE);
    sharedPreferences.edit().remove(string).apply();
  }

}
