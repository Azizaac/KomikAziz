package com.aziz.komikaziz;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class ApiService {
    private static ApiService instance;
    private RequestQueue requestQueue;
    private Context context;

    // Komikku API Endpoint
    public static final String KOMIKKU_BASE_URL = "https://komiku-api.fly.dev/api/";

    private ApiService(Context context) {
        this.context = context.getApplicationContext();
        this.requestQueue = getRequestQueue();
    }

    public static synchronized ApiService getInstance(Context context) {
        if (instance == null) {
            instance = new ApiService(context);
        }
        return instance;
    }

    public void getMangaList(ApiCallback callback) {
        String url = KOMIKKU_BASE_URL + "comic/list";

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, url, null,
                callback::onSuccess,
                error -> callback.onError(error.getMessage() != null ? error.getMessage() : "Gagal memuat daftar komik")
        );

        addToRequestQueue(request);
    }

    public void searchManga(String query, ApiCallback callback) {
        String url = KOMIKKU_BASE_URL + "comic/search/" + query;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, url, null,
                callback::onSuccess,
                error -> callback.onError(error.getMessage() != null ? error.getMessage() : "Gagal mencari komik")
        );

        addToRequestQueue(request);
    }

    public void getComicDetail(String endpoint, ApiCallback callback) {
        String url = KOMIKKU_BASE_URL + "comic/info/" + endpoint;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, url, null,
                callback::onSuccess,
                error -> callback.onError(error.getMessage() != null ? error.getMessage() : "Gagal memuat detail komik")
        );

        addToRequestQueue(request);
    }

    public void getChapterImages(String chapterEndpoint, ApiCallback callback) {
        String url = KOMIKKU_BASE_URL + "comic/chapter" + chapterEndpoint;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, url, null,
                callback::onSuccess,
                error -> callback.onError(error.getMessage() != null ? error.getMessage() : "Gagal memuat halaman komik")
        );

        addToRequestQueue(request);
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public interface ApiCallback {
        void onSuccess(JSONObject response);
        void onError(String error);
    }
}
