package com.example.noteshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class SettingsActivity extends AppCompatActivity {

    private Switch notificationSwitch;
    private Button logoutButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Inisialisasi FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Referensi UI
        notificationSwitch = findViewById(R.id.notification_switch);
        logoutButton = findViewById(R.id.logout_button);

        // Cek status notifikasi dan atur sesuai dengan preferensi pengguna (simulasi)
        // Misalnya, menggunakan SharedPreferences untuk menyimpan pengaturan ini.
        // notificationSwitch.setChecked(true); // Set to checked based on user preference

        // Fungsi Logout
        logoutButton.setOnClickListener(v -> {
            mAuth.signOut(); // Log out dari Firebase
            Toast.makeText(SettingsActivity.this, "Anda telah logout", Toast.LENGTH_SHORT).show();
            // Kembali ke LoginActivity
            Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Tutup SettingsActivity
        });
    }
}
