package com.example.aplicacionfinancierajjgd.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.aplicacionfinancierajjgd.dbo.AdminSQLiteOpenHelper;

public class SessionManager {
    private static final String PREF_NAME = "LoginPref";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_EMAIL = "email";

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;

    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void createLoginSession(int userId, String email) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putInt(KEY_USER_ID, userId);
        editor.putString(KEY_EMAIL, email);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public int getUserId() {
        return pref.getInt(KEY_USER_ID, -1);
    }

    public String getUserEmail() {
        return pref.getString(KEY_EMAIL, null);
    }

    public void logoutUser() {
        editor.clear();
        editor.commit();

        // También limpiar la sesión en la base de datos
        AdminSQLiteOpenHelper dbHelper = new AdminSQLiteOpenHelper(context);
        dbHelper.cerrarSesion(getUserId());
    }
}