package com.example.noteshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    private EditText usernameEditText, emailEditText, passwordEditText, nimEditText;
    private Button signUpButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Inisialisasi FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Referensi ke komponen UI
        usernameEditText = findViewById(R.id.usernameSignUp);
        emailEditText = findViewById(R.id.emailPengguna);
        passwordEditText = findViewById(R.id.passwordSingUp);
        nimEditText = findViewById(R.id.nimPengguna);
        signUpButton = findViewById(R.id.signUpBtn);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String nim = nimEditText.getText().toString();

                // Validasi input
                if (username.isEmpty() || email.isEmpty() || password.isEmpty() || nim.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Semua kolom harus diisi!", Toast.LENGTH_SHORT).show();
                } else {
                    // Mendaftar pengguna baru menggunakan Firebase Authentication
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(SignUpActivity.this, task -> {
                                if (task.isSuccessful()) {
                                    // Menampilkan Toast dan pindah ke LoginActivity
                                    Toast.makeText(SignUpActivity.this, "Pendaftaran berhasil!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish(); // Menutup SignUpActivity
                                } else {
                                    // Jika ada error saat mendaftar
                                    Toast.makeText(SignUpActivity.this, "Pendaftaran gagal: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                }
            }
        });
    }
}
