package com.aziz.komikaziz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import androidx.appcompat.widget.SearchView;


public class MainActivity extends AppCompatActivity implements ComicAdapter.OnComicClickListener {
    private RecyclerView recyclerView;
    private ComicAdapter comicAdapter;
    private List<Comic> comicList;
    private List<Comic> filteredComicList;
    private RequestQueue requestQueue;
    private SharedPreferences sharedPreferences;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;
    private SearchView searchView;
    public static final String KITSU_BASE_URL = "https://kitsu.io/api/edge/";

    // Komikku API
    private static final String MANGA_API = "https://komiku-api.fly.dev/api/comic/list";
    private static final String MANGA_SEARCH_API = "https://komiku-api.fly.dev/api/comic/search/"; // tambah query di belakang
    private static final String PREFS_NAME = "ComicPrefs";
    private int currentPage = 1;


    // Kirim endpoint bukan ID karena Komikku API pakai endpoint string
    private void openComicReader(Comic comic) {
        Intent intent = new Intent(this, ComicReaderActivity.class);
        intent.putExtra("comic_title", comic.getTitle());
        intent.putExtra("comic_endpoint", comic.getEndpoint());  // Gunakan endpoint, bukan ID
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Cek apakah user sudah login
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if (!sharedPreferences.getBoolean("isLoggedIn", false)) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        initViews();
        setupToolbar();
        setupRecyclerView();
        setupSwipeRefresh();
        loadComicsFromAPI();

        // Welcome message
        String userName = sharedPreferences.getString("userName", "User");
        Toast.makeText(this, "Selamat datang, " + userName + "!", Toast.LENGTH_SHORT).show();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerViewComics);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        progressBar = findViewById(R.id.progressBar);
        requestQueue = Volley.newRequestQueue(this);
        comicList = new ArrayList<>();
        filteredComicList = new ArrayList<>();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("ComicIndo - Manga Indonesia");
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }
    }

    private void setupRecyclerView() {
        comicAdapter = new ComicAdapter(this, filteredComicList, this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(comicAdapter);
        recyclerView.setHasFixedSize(true);
    }

    private void setupSwipeRefresh() {
        swipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage = 1;
                loadComicsFromAPI();
            }
        });
    }

    private void loadComicsFromAPI() {
        showLoading(true);

        String url = "https://komiku-api.fly.dev/api/comic/list";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                response -> {
                    try {
                        if (currentPage == 1) {
                            comicList.clear();
                            filteredComicList.clear();
                        }

                        JSONArray dataArray = response.getJSONArray("data");

                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject comicObject = dataArray.getJSONObject(i);

                            Comic comic = new Comic();
                            comic.setId(i); // Gunakan indeks sebagai ID sementara
                            comic.setTitle(comicObject.optString("title", "Tanpa Judul"));
                            comic.setDescription(comicObject.optString("desc", "Tidak ada deskripsi"));

                            String imageUrl = comicObject.optString("image", "https://via.placeholder.com/300x450");
                            comic.setImageUrl(imageUrl);

                            String type = comicObject.optString("type", "Manga");
                            comic.setGenre(type);

                            comic.setAuthor("Tidak diketahui");
                            comic.setRating(4.5f);
                            comic.setYear(2024);
                            comic.setChapters((int)(Math.random() * 50) + 10);
                            comic.setStatus("Ongoing");
                            comic.setPublisher("Komikku");
                            comic.setEndpoint(comicObject.optString("endpoint", ""));

                            comicList.add(comic);
                            filteredComicList.add(comic);
                        }

                        comicAdapter.notifyDataSetChanged();
                        showLoading(false);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        showError("Error parsing: " + e.getMessage());
                    }
                },
                error -> {
                    showError("Network error: " + error.getMessage());
                }
        );

        jsonObjectRequest.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(
                10000, 3, com.android.volley.DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        requestQueue.add(jsonObjectRequest);
    }




    private void loadOfflineData() {
        // Fallback offline data
        comicList.clear();
        filteredComicList.clear();

        String[] offlineTitles = {
                "One Piece", "Naruto", "Attack on Titan", "Death Note", "Dragon Ball",
                "My Hero Academia", "Demon Slayer", "Jujutsu Kaisen", "Chainsaw Man", "Spy x Family"
        };

        for (int i = 0; i < offlineTitles.length; i++) {
            Comic comic = new Comic();
            comic.setId(i + 1);
            comic.setTitle(offlineTitles[i]);
            comic.setDescription("Popular manga series - " + offlineTitles[i]);
            comic.setImageUrl("https://via.placeholder.com/300x450?text=" + offlineTitles[i].replace(" ", "+"));
            comic.setAuthor("Mangaka " + (i + 1));
            comic.setGenre("Action, Adventure");
            comic.setRating(4.0f + (float)(Math.random() * 1.0));
            comic.setYear(2015 + i);
            comic.setChapters(100 + (i * 50));
            comic.setStatus("Ongoing");
            comic.setPublisher("Shonen Jump");

            comicList.add(comic);
            filteredComicList.add(comic);
        }

        comicAdapter.notifyDataSetChanged();
        showLoading(false);
        Toast.makeText(this, "Loaded offline data", Toast.LENGTH_SHORT).show();
    }

    private String translateStatus(String status) {
        switch (status.toLowerCase()) {
            case "finished": return "Selesai";
            case "publishing": return "Berlangsung";
            case "on_hiatus": return "Hiatus";
            case "discontinued": return "Dihentikan";
            default: return "Berlangsung";
        }
    }

    private String translateKitsuStatus(String status) {
        switch (status.toLowerCase()) {
            case "finished": return "Selesai";
            case "current": return "Berlangsung";
            case "tba": return "Akan Datang";
            case "unreleased": return "Belum Rilis";
            default: return "Berlangsung";
        }
    }

    private void showLoading(boolean show) {
        if (progressBar != null) {
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        }
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void showError(String message) {
        showLoading(false);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchComics(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    filteredComicList.clear();
                    filteredComicList.addAll(comicList);
                    comicAdapter.notifyDataSetChanged();
                } else {
                    filterComics(newText);
                }
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            currentPage = 1;
            loadComicsFromAPI();
            return true;
        } else if (id == R.id.action_favorites) {
            startActivity(new Intent(this, FavoritesActivity.class));
            return true;
        } else if (id == R.id.action_profile) {
            startActivity(new Intent(this, ProfileActivity.class));
            return true;
        } else if (id == R.id.action_logout) {
            logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void searchComics(String query) {
        showLoading(true);
        String searchUrl = MANGA_SEARCH_API + query + "&limit=20";

        JsonObjectRequest searchRequest = new JsonObjectRequest(
                Request.Method.GET, searchUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Same parsing logic as loadComicsFromAPI
                        // Implementation similar to above
                        showLoading(false);
                        Toast.makeText(MainActivity.this, "Search completed for: " + query, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showError("Search failed: " + error.getMessage());
                    }
                }
        );

        requestQueue.add(searchRequest);
    }

    private void filterComics(String text) {
        filteredComicList.clear();
        for (Comic comic : comicList) {
            if (comic.getTitle().toLowerCase().contains(text.toLowerCase()) ||
                    comic.getAuthor().toLowerCase().contains(text.toLowerCase()) ||
                    comic.getGenre().toLowerCase().contains(text.toLowerCase())) {
                filteredComicList.add(comic);
            }
        }
        comicAdapter.notifyDataSetChanged();
    }

    @Override
    public void onComicClick(Comic comic) {
        Intent intent = new Intent(this, ComicDetailActivity.class);
        intent.putExtra("comic", comic);
        startActivity(intent);
    }

    private void logout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        startActivity(new Intent(this, LoginActivity.class));
        finish();
        Toast.makeText(this, "Berhasil logout", Toast.LENGTH_SHORT).show();
    }
}