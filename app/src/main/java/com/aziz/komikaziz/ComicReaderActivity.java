package com.aziz.komikaziz;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ComicReaderActivity extends AppCompatActivity {
    private ViewPager2 viewPagerPages;
    private TextView tvPageInfo;
    private ImageButton btnPrevious, btnNext;
    private ComicPageAdapter pageAdapter;
    private List<String> pageUrls;
    private int currentPage = 0;
    private String comicTitle;
    private String chapterEndpoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comic_reader);

        // Terima data dari Intent
        chapterEndpoint = getIntent().getStringExtra("chapter_endpoint");
        comicTitle = getIntent().getStringExtra("comic_title");

        Log.d("ComicReaderActivity", "chapter_endpoint received: " + chapterEndpoint);

        setupToolbar();
        initViews();

        if (chapterEndpoint != null && !chapterEndpoint.isEmpty()) {
            loadComicPagesFromApi(chapterEndpoint);
        } else {
            Toast.makeText(this, "Endpoint chapter tidak ditemukan!", Toast.LENGTH_SHORT).show();
            finish();
        }

        setupClickListeners();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(comicTitle != null ? comicTitle : "Comic Reader");
        }
    }

    private void initViews() {
        viewPagerPages = findViewById(R.id.viewPagerPages);
        tvPageInfo = findViewById(R.id.tvPageInfo);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnNext = findViewById(R.id.btnNext);

        pageUrls = new ArrayList<>();
    }

    private void loadComicPagesFromApi(String endpoint) {
        String url = "https://komiku-api.fly.dev/api/comic/chapter" + endpoint;

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONObject dataObj = response.getJSONObject("data");

                        // Jika image == null, tampilkan pesan
                        if (dataObj.isNull("image")) {
                            Toast.makeText(this, "Tidak ada halaman gambar!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        JSONArray imagesArray = dataObj.getJSONArray("image");

                        pageUrls.clear();
                        for (int i = 0; i < imagesArray.length(); i++) {
                            String imageUrl = imagesArray.getString(i);
                            pageUrls.add(imageUrl);
                        }

                        pageAdapter = new ComicPageAdapter(this, pageUrls);
                        viewPagerPages.setAdapter(pageAdapter);

                        updatePageInfo();

                        viewPagerPages.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                            @Override
                            public void onPageSelected(int position) {
                                currentPage = position;
                                updatePageInfo();
                                updateNavigationButtons();
                            }
                        });

                    } catch (JSONException e) {
                        Toast.makeText(this, "Format JSON salah!", Toast.LENGTH_SHORT).show();
                        Log.e("ComicReaderActivity", "JSONException: " + e.getMessage());
                    }
                },
                error -> {
                    Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                    Log.e("ComicReaderActivity", "Volley error: " + error.getMessage());
                });

        queue.add(request);
    }

    private void setupClickListeners() {
        btnPrevious.setOnClickListener(v -> {
            if (currentPage > 0) {
                viewPagerPages.setCurrentItem(currentPage - 1, true);
            }
        });

        btnNext.setOnClickListener(v -> {
            if (currentPage < pageUrls.size() - 1) {
                viewPagerPages.setCurrentItem(currentPage + 1, true);
            } else {
                Toast.makeText(this, "Anda telah mencapai halaman terakhir", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updatePageInfo() {
        tvPageInfo.setText((currentPage + 1) + " / " + pageUrls.size());
    }

    private void updateNavigationButtons() {
        btnPrevious.setEnabled(currentPage > 0);
        btnNext.setEnabled(currentPage < pageUrls.size() - 1);

        btnPrevious.setAlpha(currentPage > 0 ? 1.0f : 0.5f);
        btnNext.setAlpha(currentPage < pageUrls.size() - 1 ? 1.0f : 0.5f);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
