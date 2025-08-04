package com.aziz.komikaziz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ComicDetailActivity extends AppCompatActivity {
    private ImageView ivCover;
    private TextView tvTitle, tvAuthor, tvGenre, tvRating, tvDescription, tvStatus, tvChapters;
    private Button btnReadNow, btnAddToFavorite;
    private CollapsingToolbarLayout collapsingToolbar;
    private Comic comic;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "ComicPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comic_detail);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        initViews();
        setupToolbar();
        loadComicData();
        setupClickListeners();
    }

    private void initViews() {
        ivCover = findViewById(R.id.ivComicCover);
        tvTitle = findViewById(R.id.tvComicTitle);
        tvAuthor = findViewById(R.id.tvComicAuthor);
        tvGenre = findViewById(R.id.tvComicGenre);
        tvRating = findViewById(R.id.tvComicRating);
        tvDescription = findViewById(R.id.tvComicDescription);
        tvStatus = findViewById(R.id.tvComicStatus);
        tvChapters = findViewById(R.id.tvComicChapters);
        btnReadNow = findViewById(R.id.btnReadNow);
        btnAddToFavorite = findViewById(R.id.btnAddToFavorite);
        collapsingToolbar = findViewById(R.id.collapsingToolbar);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void loadComicData() {
        comic = (Comic) getIntent().getSerializableExtra("comic");

        if (comic != null) {
            collapsingToolbar.setTitle(comic.getTitle());
            tvTitle.setText(comic.getTitle());
            tvAuthor.setText("Oleh: " + comic.getAuthor());
            tvGenre.setText(comic.getGenre());
            tvRating.setText(String.format("%.1f", comic.getRating()));
            tvDescription.setText(comic.getDescription());
            tvStatus.setText(comic.getStatus());
            tvChapters.setText(comic.getChapters() + " Chapter");

            // Load cover image
            Picasso.get()
                    .load(comic.getImageUrl())
                    .placeholder(R.drawable.placeholder_comic)
                    .error(R.drawable.placeholder_comic)
                    .into(ivCover);

            // Check if comic is in favorites
            updateFavoriteButton();
        }
    }

    private void setupClickListeners() {
        btnReadNow.setOnClickListener(v -> {
            if (comic != null && comic.getChaptersList() != null && !comic.getChaptersList().isEmpty()) {
                String chapterEndpoint = comic.getChaptersList().get(0).getEndpoint(); // chapter pertama
                Intent intent = new Intent(this, ComicReaderActivity.class);
                intent.putExtra("comic_title", comic.getTitle());
                intent.putExtra("chapter_endpoint", chapterEndpoint); // 👈 pastikan key ini dipakai di ComicReaderActivity
                startActivity(intent);
            } else {
                Toast.makeText(this, "Tidak ada chapter untuk dibaca!", Toast.LENGTH_SHORT).show();
            }
        });







        btnAddToFavorite.setOnClickListener(v -> {
            toggleFavorite();
        });
    }

    private void toggleFavorite() {
        if (comic != null) {
            boolean isFavorite = sharedPreferences.getBoolean("fav_" + comic.getEndpoint(), false);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            if (isFavorite) {
                editor.remove("fav_" + comic.getEndpoint());
                editor.remove("fav_data_" + comic.getEndpoint());
                Toast.makeText(this, "Dihapus dari favorit", Toast.LENGTH_SHORT).show();
            } else {
                editor.putBoolean("fav_" + comic.getEndpoint(), true);
                editor.putString("fav_data_" + comic.getEndpoint(), serializeComic(comic));
                Toast.makeText(this, "Ditambahkan ke favorit", Toast.LENGTH_SHORT).show();
            }
            editor.apply();


            updateFavoriteButton();
        }
    }

    private void updateFavoriteButton() {
        if (comic != null) {
            boolean isFavorite = sharedPreferences.getBoolean("fav_" + comic.getEndpoint(), false);
            btnAddToFavorite.setText(isFavorite ? "HAPUS FAVORIT" : "TAMBAH FAVORIT");

        }
    }

    private String serializeComic(Comic comic) {
        // Simple serialization - in real app, use JSON or Gson
        return comic.getEndpoint() + "|" + comic.getTitle() + "|" + comic.getAuthor() +
                "|" + comic.getImageUrl() + "|" + comic.getRating();

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
