package com.example.seonoh2.smarttoliet01.manager;

/**
 * Created by 선오 on 2016-10-06.
 */
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.example.seonoh2.smarttoliet01.util.MyApplication;

//import com.begentgroup.miniapplication.MyApplication;
//import com.begentgroup.miniapplication.login.User;

/**
 * Created by dongja94 on 2016-05-16.
 */
public class PropertyManager {
    private static PropertyManager instance;
    public static PropertyManager getInstance() {
        if (instance == null) {
            instance = new PropertyManager();
        }
        return instance;
    }
    SharedPreferences mPrefs;
    SharedPreferences.Editor mEditor;

    private PropertyManager() {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
        mEditor = mPrefs.edit();
    }

    private static final String FIELD_TOKEN = "access_token";
    public void setAccess_token(String access_token) {
        mEditor.putString(FIELD_TOKEN, access_token);
        mEditor.commit();
    }
    public String getAccess_token() {
        return mPrefs.getString(FIELD_TOKEN,"");
    }



//    private static final String FIELD_PASSWORD = "password";
//    public void setPassword(String password) {
//        mEditor.putString(FIELD_PASSWORD, password);
//        mEditor.commit();
//    }
//    public String getPassword() {
//        return mPrefs.getString(FIELD_PASSWORD, "");
//    }


//    public static final String FIELD_FACEBOOK_ID = "facebookid";
//    public void setFacebookId(String facebookId) {
//        mEditor.putString(FIELD_FACEBOOK_ID, facebookId);
//        mEditor.commit();
//    }
//    public String getFacebookId() {
//        return mPrefs.getString(FIELD_FACEBOOK_ID, "");
//    }

//    private boolean isLogin = false;
//    public void setLogin(boolean login) {
//        isLogin = login;
//    }
//    public boolean isLogin() {
//        return isLogin;
//    }

//    private User user = null;
//    public void setUser(User user) {
//        this.user = user;
//    }
//    public User getUser() {
//        return user;
//    }
//
//    private static final String FIELD_REGISTRATION_ID = "regid";
//    public void setRegistrationToken(String token) {
//        mEditor.putString(FIELD_REGISTRATION_ID, token);
//        mEditor.commit();
//    }
//    public String getRegistrationToken(){
//        return mPrefs.getString(FIELD_REGISTRATION_ID, "");
//    }
}
