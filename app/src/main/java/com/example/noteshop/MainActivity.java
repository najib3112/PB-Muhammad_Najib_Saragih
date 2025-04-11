package com.example.noteshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View; // Import View
import android.widget.EditText; // Import EditText
import android.widget.Button; // Import Button
import android.util.Log; // Import Log for debugging

import androidx.appcompat.app.AlertDialog; // Import AlertDialog
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noteshop.adapter.MainAdapter;
import com.example.noteshop.model.ModelBarang;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MainAdapter.FirebaseDataListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseReference;
    private RecyclerView mRecyclerView;
    private MainAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        // Set up Toolbar (for drawer toggle)
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up Navigation Drawer
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);

        // Update user info in the navigation header
        View headerView = navigationView.getHeaderView(0);
        TextView usernameTextView = headerView.findViewById(R.id.user_name);
        TextView emailTextView = headerView.findViewById(R.id.user_email);
        if (mAuth.getCurrentUser() != null) {
            usernameTextView.setText(mAuth.getCurrentUser().getDisplayName());
            emailTextView.setText(mAuth.getCurrentUser().getEmail());
        }

        // Firebase Database reference
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("barang").child("data_barang"); // Update to correct Firebase URL

        // Check if user is logged in and redirect to LoginActivity if not
        if (mAuth.getCurrentUser() == null) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        // RecyclerView setup and data load
        setupRecyclerView();

        // Set up Floating Action Button (FAB) to add new item
        findViewById(R.id.tambah_barang).setOnClickListener(v -> {
            dialogTambahBarang(); // Open dialog to add new item
        });

        // Add a toggle to open/close the navigation drawer
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);  // Set your menu icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupRecyclerView() {
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<ModelBarang> daftarBarang = new ArrayList<>(); // Initialize the list to hold barang items
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ModelBarang barang = snapshot.getValue(ModelBarang.class);
                    barang.setKey(snapshot.getKey());
                    daftarBarang.add(barang);
                }

                // Initialize and set adapter for RecyclerView
                Log.d("MainActivity", "Daftar barang: " + daftarBarang.toString()); // Log the list of barang items
                if (mAdapter == null) {
                    mAdapter = new MainAdapter(MainActivity.this, daftarBarang);
                    mRecyclerView.setAdapter(mAdapter);
                } else {
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Already in MainActivity (no need to navigate)
            Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_profile) {
            // Navigate to ProfileActivity
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_settings) {
            // Navigate to SettingsActivity
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            // Logout and redirect to LoginActivity
            mAuth.signOut();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        drawerLayout.closeDrawer(navigationView);
        return true;
    }

    private void dialogTambahBarang() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_tambah_barang, null);
        EditText editNama = view.findViewById(R.id.edit_nama);
        EditText editMerk = view.findViewById(R.id.edit_merk);
        EditText editHarga = view.findViewById(R.id.edit_harga);
        Button btnSimpan = view.findViewById(R.id.btn_simpan);

        builder.setView(view);
        AlertDialog dialog = builder.create();

        btnSimpan.setOnClickListener(v -> {
            String nama = editNama.getText().toString();
            String merk = editMerk.getText().toString();
            String harga = editHarga.getText().toString();

            if (!nama.isEmpty() && !merk.isEmpty() && !harga.isEmpty()) {
                String key = mDatabaseReference.push().getKey(); // Generate a unique key for the new item
                Log.d("MainActivity", "Generated key: " + key); // Log the generated key for debugging
                ModelBarang barang = new ModelBarang(nama, merk, harga);
                barang.setKey(key);
                mDatabaseReference.child(key).setValue(barang);
                Toast.makeText(MainActivity.this, "Barang berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } else {
                Toast.makeText(MainActivity.this, "Semua field harus diisi", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    @Override
    public void onDataClick(ModelBarang barang, int position) {
        // Handle item click in RecyclerView (e.g., show update/delete options)
        Toast.makeText(this, "Item clicked: " + barang.getNama(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Open Navigation Drawer when menu icon is clicked
            drawerLayout.openDrawer(navigationView);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
