package com.aziz.komikaziz;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesManager {
    private static final String PREF_NAME = "ComicPrefs";
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public PreferencesManager(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    // User session management
    // ✅ Perbaikan
    public void setUserLoggedIn(boolean isLoggedIn) {
        editor.putBoolean(Constants.PREF_IS_LOGGED_IN, isLoggedIn);
        editor.apply();
    }


    public boolean isUserLoggedIn() {
        return preferences.getBoolean(Constants.PREF_IS_LOGGED_IN, false);
    }


    public void setUserInfo(String email, String name, String token) {
        editor.putString(Constants.PREF_USER_EMAIL, email);
        editor.putString(Constants.PREF_USER_NAME, name);
        editor.putString(Constants.PREF_AUTH_TOKEN, token);
        editor.apply();
    }

    public String getUserEmail() {
        return preferences.getString(Constants.PREF_USER_EMAIL, "");
    }

    public String getUserName() {
        return preferences.getString(Constants.PREF_USER_NAME, "User");
    }

    public String getAuthToken() {
        return preferences.getString(Constants.PREF_AUTH_TOKEN, "");
    }

    // Favorite management
    public void addToFavorites(int comicId) {
        editor.putBoolean("fav_" + comicId, true);
        editor.apply();
    }

    public void removeFromFavorites(int comicId) {
        editor.remove("fav_" + comicId);
        editor.apply();
    }

    public boolean isFavorite(int comicId) {
        return preferences.getBoolean("fav_" + comicId, false);
    }

    // Clear all preferences (logout)
    public void clearAll() {
        editor.clear();
        editor.apply();
    }

    // App settings
    public void setFirstTime(boolean isFirstTime) {
        editor.putBoolean("first_time", isFirstTime);
        editor.apply();
    }

    public boolean isFirstTime() {
        return preferences.getBoolean("first_time", true);
    }
}