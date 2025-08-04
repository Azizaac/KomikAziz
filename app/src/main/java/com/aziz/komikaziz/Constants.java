package com.aziz.komikaziz;

public class Constants {
    // API Endpoints
    public static final String JIKAN_API_BASE = "https://api.jikan.moe/v4/";
    public static final String KITSU_API_BASE = "https://kitsu.io/api/edge/";
    public static final String MANGADEX_API_BASE = "https://api.mangadex.org/";

    // Shared Preferences Keys
    public static final String PREF_IS_LOGGED_IN = "isLoggedIn";
    public static final String PREF_USER_EMAIL = "userEmail";
    public static final String PREF_USER_NAME = "userName";
    public static final String PREF_AUTH_TOKEN = "authToken";

    // Request Codes
    public static final int REQUEST_LOGIN = 1001;
    public static final int REQUEST_REGISTER = 1002;

    // API Timeouts
    public static final int API_TIMEOUT = 15000; // 15 seconds
    public static final int API_RETRY_COUNT = 3;

    // Cache Duration
    public static final long CACHE_DURATION = 5 * 60 * 1000; // 5 minutes

    // Pagination
    public static final int ITEMS_PER_PAGE = 25;
    public static final int GRID_SPAN_COUNT = 2;
}
