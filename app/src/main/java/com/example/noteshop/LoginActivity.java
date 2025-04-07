package com.example.noteshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private CheckBox rememberMeCheckBox;
    private Button loginButton;
    private TextView signUpTextView;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inisialisasi FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Cek apakah user sudah login
        if (mAuth.getCurrentUser() != null) {
            // Jika sudah login, langsung buka MainActivity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Tutup LoginActivity
        }

        // Referensi ke komponen UI
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        rememberMeCheckBox = findViewById(R.id.checkboxes);
        loginButton = findViewById(R.id.btnLogin);
        signUpTextView = findViewById(R.id.signUp);  // Pastikan ini sesuai dengan ID di XML

        // Listener untuk tombol login
        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            // Validasi login
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Email dan password tidak boleh kosong!", Toast.LENGTH_SHORT).show();
            } else {
                // Login pengguna menggunakan Firebase Authentication
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, task -> {
                            if (task.isSuccessful()) {
                                // Login berhasil, pindah ke MainActivity
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish(); // Tutup LoginActivity
                            } else {
                                // Jika login gagal
                                Toast.makeText(LoginActivity.this, "Login gagal: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });

        // Listener untuk tombol "Daftar Sekarang"
        signUpTextView.setOnClickListener(v -> {
            // Pindah ke SignUpActivity ketika "Daftar Sekarang" diklik
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }
}
