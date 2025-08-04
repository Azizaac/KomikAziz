package com.aziz.komikaziz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText etEmail, etPassword;
    private Button btnLogin, btnRegister;
    private ProgressBar progressBar;
    private SharedPreferences sharedPreferences;
    private RequestQueue requestQueue;
    private static final String PREFS_NAME = "ComicPrefs";
    private static final String AUTH_API = "https://reqres.in/api/login"; // Mock auth API

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        requestQueue = Volley.newRequestQueue(this);

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupClickListeners() {
        btnLogin.setOnClickListener(v -> performLogin());
        btnRegister.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
    }

    private void performLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Mohon isi semua field", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Format email tidak valid", Toast.LENGTH_SHORT).show();
            return;
        }

        showLoading(true);

        // Use real API for authentication
        JSONObject loginData = new JSONObject();
        try {
            loginData.put("email", email);
            loginData.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest loginRequest = new JsonObjectRequest(
                Request.Method.POST, AUTH_API, loginData,
                response -> {
                    try {
                        String token = response.getString("token");

                        // Save login state
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("isLoggedIn", true);
                        editor.putString("userEmail", email);
                        editor.putString("authToken", token);
                        editor.putString("userName", email.substring(0, email.indexOf("@")));
                        editor.apply();

                        showLoading(false);
                        startActivity(new Intent(this, MainActivity.class));
                        finish();
                        Toast.makeText(this, "Login berhasil!", Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        showLoading(false);
                        Toast.makeText(this, "Login error", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    showLoading(false);
                    // Fallback to local validation
                    if (isValidLocalUser(email, password)) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("isLoggedIn", true);
                        editor.putString("userEmail", email);
                        editor.putString("userName", email.substring(0, email.indexOf("@")));
                        editor.apply();

                        startActivity(new Intent(this, MainActivity.class));
                        finish();
                        Toast.makeText(this, "Login berhasil (offline)!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Email atau password salah", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        requestQueue.add(loginRequest);
    }

    private boolean isValidLocalUser(String email, String password) {
        String savedEmail = sharedPreferences.getString("registered_email", "");
        String savedPassword = sharedPreferences.getString("registered_password", "");

        // Default test account
        return (email.equals("test@comic.com") && password.equals("123456")) ||
                (email.equals(savedEmail) && password.equals(savedPassword));
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        btnLogin.setEnabled(!show);
        btnRegister.setEnabled(!show);
    }
}
