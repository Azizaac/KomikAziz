package com.aziz.komikaziz;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity implements ComicAdapter.OnComicClickListener {
    private RecyclerView recyclerView;
    private ComicAdapter comicAdapter;
    private TextView tvEmptyState;
    private DatabaseHelper databaseHelper;
    private List<Comic> favoriteComics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        setupToolbar();
        initViews();
        loadFavorites();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Komik Favorit");
        }
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerViewFavorites);
        tvEmptyState = findViewById(R.id.tvEmptyState);
        databaseHelper = new DatabaseHelper(this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    private void loadFavorites() {
        favoriteComics = databaseHelper.getAllFavorites();

        if (favoriteComics.isEmpty()) {
            showEmptyState();
        } else {
            showFavorites();
        }
    }

    private void showEmptyState() {
        recyclerView.setVisibility(View.GONE);
        tvEmptyState.setVisibility(View.VISIBLE);
    }

    private void showFavorites() {
        recyclerView.setVisibility(View.VISIBLE);
        tvEmptyState.setVisibility(View.GONE);

        comicAdapter = new ComicAdapter(this, favoriteComics, this);
        recyclerView.setAdapter(comicAdapter);
    }

    @Override
    public void onComicClick(Comic comic) {
        Intent intent = new Intent(this, ComicDetailActivity.class);
        intent.putExtra("comic", comic);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFavorites(); // Refresh favorites when returning to activity
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
