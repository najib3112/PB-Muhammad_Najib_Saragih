package com.example.noteshop;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView profileName, profileEmail, profileNim ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();

        // Inisialisasi TextViews untuk menampilkan nama dan email pengguna
        profileName = findViewById(R.id.profile_name);
        profileEmail = findViewById(R.id.profile_email);
        profileNim = findViewById(R.id.profile_nim);

        // Periksa apakah pengguna sudah login
        if (mAuth.getCurrentUser() != null) {
            // Ambil informasi pengguna dari FirebaseAuth
            String userName = mAuth.getCurrentUser().getDisplayName();
            String userEmail = mAuth.getCurrentUser().getEmail();
            String userNim = "12350111357";

            // Tampilkan data pengguna pada TextView
            profileName.setText(userName);
            profileEmail.setText(userEmail);
            profileNim.setText(userNim);
        }
    }
}
