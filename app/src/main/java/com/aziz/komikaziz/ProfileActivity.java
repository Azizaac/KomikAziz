package com.aziz.komikaziz;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

public class ProfileActivity extends AppCompatActivity {
    private TextView tvUserName, tvUserEmail, tvMemberSince, tvFavoriteCount, tvReadingCount;
    private Button btnEditProfile, btnLogout;
    private CardView cardStats;
    private SharedPreferences sharedPreferences;
    private DatabaseHelper databaseHelper;
    private static final String PREFS_NAME = "ComicPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        databaseHelper = new DatabaseHelper(this);

        setupToolbar();
        initViews();
        loadUserData();
        setupClickListeners();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Profil Pengguna");
        }
    }

    private void initViews() {
        tvUserName = findViewById(R.id.tvUserName);
        tvUserEmail = findViewById(R.id.tvUserEmail);
        tvMemberSince = findViewById(R.id.tvMemberSince);
        tvFavoriteCount = findViewById(R.id.tvFavoriteCount);
        tvReadingCount = findViewById(R.id.tvReadingCount);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnLogout = findViewById(R.id.btnLogout);
        cardStats = findViewById(R.id.cardStats);
    }

    private void loadUserData() {
        String userName = sharedPreferences.getString("userName", "User");
        String userEmail = sharedPreferences.getString("userEmail", "user@example.com");
        long registrationTime = sharedPreferences.getLong("registration_time", System.currentTimeMillis());

        tvUserName.setText(userName);
        tvUserEmail.setText(userEmail);

        // Format member since date
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMMM yyyy",
                java.util.Locale.getDefault());
        tvMemberSince.setText("Bergabung " + sdf.format(new java.util.Date(registrationTime)));

        // Load statistics
        int favoriteCount = databaseHelper.getAllFavorites().size();
        tvFavoriteCount.setText(String.valueOf(favoriteCount));
        tvReadingCount.setText("0"); // TODO: Implement reading history
    }

    private void setupClickListeners() {
        btnEditProfile.setOnClickListener(v -> {
            // TODO: Implement edit profile
            Toast.makeText(this, "Fitur edit profil akan segera tersedia", Toast.LENGTH_SHORT).show();
        });

        btnLogout.setOnClickListener(v -> {
            logout();
        });

        cardStats.setOnClickListener(v -> {
            startActivity(new Intent(this, FavoritesActivity.class));
        });
    }

    private void logout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

        Toast.makeText(this, "Berhasil logout", Toast.LENGTH_SHORT).show();
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
