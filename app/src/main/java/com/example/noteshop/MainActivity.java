package com.example.noteshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

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

        // Firebase Database reference
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("barang").child("data_barang");

        // Check if user is logged in
        if (mAuth.getCurrentUser() == null) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        // RecyclerView setup and data load
        setupRecyclerView();

        // Set up Floating Action Button (FAB) to add new item
        findViewById(R.id.tambah_barang).setOnClickListener(v -> {
            // Open dialog to add new item (implement dialog functionality here)
            dialogTambahBarang();
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
                ArrayList<ModelBarang> daftarBarang = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ModelBarang barang = snapshot.getValue(ModelBarang.class);
                    barang.setKey(snapshot.getKey());
                    daftarBarang.add(barang);
                }

                // Initialize and set adapter for RecyclerView
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
        // Implement dialog to add new item to Firebase
        // Example:
        // AlertDialog dialog = new AlertDialog.Builder(this).setTitle("Add Item").create();
        // dialog.show();
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
