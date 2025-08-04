package com.aziz.komikaziz;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {
    private TextInputEditText etName, etEmail, etPassword, etConfirmPassword;
    private Button btnRegister, btnBackToLogin;
    private ProgressBar progressBar;
    private SharedPreferences sharedPreferences;
    private RequestQueue requestQueue;
    private static final String PREFS_NAME = "ComicPrefs";
    private static final String REGISTER_API = "https://reqres.in/api/register"; // Mock register API

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        requestQueue = Volley.newRequestQueue(this);

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnBackToLogin = findViewById(R.id.btnBackToLogin);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupClickListeners() {
        btnRegister.setOnClickListener(v -> performRegister());
        btnBackToLogin.setOnClickListener(v -> finish());
    }

    private void performRegister() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // Validasi input
        if (!validateInput(name, email, password, confirmPassword)) {
            return;
        }

        showLoading(true);

        // Gunakan API untuk registrasi
        JSONObject registerData = new JSONObject();
        try {
            registerData.put("email", email);
            registerData.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest registerRequest = new JsonObjectRequest(
                Request.Method.POST, REGISTER_API, registerData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int id = response.getInt("id");
                            String token = response.getString("token");

                            // Simpan data registrasi
                            saveUserData(name, email, password, token);
                            showLoading(false);

                            Toast.makeText(RegisterActivity.this,
                                    "Registrasi berhasil! Silakan login", Toast.LENGTH_SHORT).show();
                            finish();

                        } catch (JSONException e) {
                            showLoading(false);
                            Toast.makeText(RegisterActivity.this,
                                    "Registrasi error", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showLoading(false);
                        // Fallback ke penyimpanan lokal
                        saveUserData(name, email, password, "local_token");
                        Toast.makeText(RegisterActivity.this,
                                "Registrasi berhasil (offline)! Silakan login", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
        );

        requestQueue.add(registerRequest);
    }

    private boolean validateInput(String name, String email, String password, String confirmPassword) {
        if (name.isEmpty()) {
            etName.setError("Nama tidak boleh kosong");
            etName.requestFocus();
            return false;
        }

        if (email.isEmpty()) {
            etEmail.setError("Email tidak boleh kosong");
            etEmail.requestFocus();
            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Format email tidak valid");
            etEmail.requestFocus();
            return false;
        }

        if (password.isEmpty()) {
            etPassword.setError("Password tidak boleh kosong");
            etPassword.requestFocus();
            return false;
        }

        if (password.length() < 6) {
            etPassword.setError("Password minimal 6 karakter");
            etPassword.requestFocus();
            return false;
        }

        if (confirmPassword.isEmpty()) {
            etConfirmPassword.setError("Konfirmasi password tidak boleh kosong");
            etConfirmPassword.requestFocus();
            return false;
        }

        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Password tidak cocok");
            etConfirmPassword.requestFocus();
            return false;
        }

        return true;
    }

    private void saveUserData(String name, String email, String password, String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("registered_name", name);
        editor.putString("registered_email", email);
        editor.putString("registered_password", password);
        editor.putString("registered_token", token);
        editor.putLong("registration_time", System.currentTimeMillis());
        editor.apply();
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        btnRegister.setEnabled(!show);
        btnBackToLogin.setEnabled(!show);
    }
}