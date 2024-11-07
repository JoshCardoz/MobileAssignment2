package com.example.mobileassign2;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements AddressAdapter.OnItemClickListener {
    private DatabaseHelper dbHelper;
    private AddressAdapter adapter;
    private EditText searchInput;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize database helper
        dbHelper = new DatabaseHelper(this);

        // Initialize views
        searchInput = findViewById(R.id.searchInput);
        recyclerView = findViewById(R.id.recyclerView);
        Button searchButton = findViewById(R.id.search_button);
        Button newAddressButton = findViewById(R.id.new_address);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get all entries from database and create adapter
        Cursor cursor = dbHelper.getEntries();
        adapter = new AddressAdapter(this, cursor, this);
        recyclerView.setAdapter(adapter);

        // Set up button click listeners
        searchButton.setOnClickListener(v -> performSearch());
        newAddressButton.setOnClickListener(v -> showAddAddressDialog());
    }

    private void performSearch() {
        String searchTerm = searchInput.getText().toString().trim();
        if (searchTerm.isEmpty()) {
            // If search is empty, show all entries
            adapter.swapCursor(dbHelper.getEntries());
        } else {
            // Otherwise, show search results
            adapter.swapCursor(dbHelper.search(searchTerm));
        }
    }

    // Make sure to refresh the RecyclerView after adding new entry
    private void showAddAddressDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_add_address, null);

        EditText addressInput = view.findViewById(R.id.address_input);
        EditText latitudeInput = view.findViewById(R.id.latitude_input);
        EditText longitudeInput = view.findViewById(R.id.longitude_input);

        builder.setView(view)
                .setTitle("Add New Address")
                .setPositiveButton("Add", (dialog, which) -> {
                    try {
                        String address = addressInput.getText().toString().trim();
                        double latitude = Double.parseDouble(latitudeInput.getText().toString());
                        double longitude = Double.parseDouble(longitudeInput.getText().toString());

                        dbHelper.addEntry(address, longitude, latitude);
                        // Refresh the RecyclerView with all entries
                        adapter.swapCursor(dbHelper.getEntries());
                    } catch (NumberFormatException e) {
                        Toast.makeText(this, "Please enter valid coordinates", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onItemClick(int id, String address, double latitude, double longitude) {
        showEditDialog(id, address, latitude, longitude);
    }

    @Override
    public void onDeleteClick(int id) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Address")
                .setMessage("Are you sure you want to delete this address?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    dbHelper.deleteEntry(id);
                    // Refresh the RecyclerView with all entries after deletion
                    adapter.swapCursor(dbHelper.getEntries());
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void showEditDialog(int id, String address, double latitude, double longitude) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_add_address, null);

        EditText addressInput = view.findViewById(R.id.address_input);
        EditText latitudeInput = view.findViewById(R.id.latitude_input);
        EditText longitudeInput = view.findViewById(R.id.longitude_input);

        // Pre-fill the current values
        addressInput.setText(address);
        latitudeInput.setText(String.valueOf(latitude));
        longitudeInput.setText(String.valueOf(longitude));

        builder.setView(view)
                .setTitle("Edit Address")
                .setPositiveButton("Update", (dialog, which) -> {
                    try {
                        String newAddress = addressInput.getText().toString().trim();
                        double newLatitude = Double.parseDouble(latitudeInput.getText().toString());
                        double newLongitude = Double.parseDouble(longitudeInput.getText().toString());

                        dbHelper.updateEntry(id, newAddress, newLatitude, newLongitude);
                        // Refresh the RecyclerView with all entries after update
                        adapter.swapCursor(dbHelper.getEntries());
                    } catch (NumberFormatException e) {
                        Toast.makeText(this, "Please enter valid coordinates", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close the cursor when the activity is destroyed
        if (adapter != null) {
            adapter.swapCursor(null);
        }
    }
}