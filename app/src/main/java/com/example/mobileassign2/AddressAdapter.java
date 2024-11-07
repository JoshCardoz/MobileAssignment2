package com.example.mobileassign2;


import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {
    private Cursor cursor;
    private Context context;
    private OnItemClickListener listener;

    // Interface for handling click events
    public interface OnItemClickListener {
        void onItemClick(int id, String address, double latitude, double longitude);
        void onDeleteClick(int id);
    }

    // Constructor
    public AddressAdapter(Context context, Cursor cursor, OnItemClickListener listener) {
        this.context = context;
        this.cursor = cursor;
        this.listener = listener;
    }

    // ViewHolder class to hold references to the views for each data item
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView addressText;
        public TextView coordinatesText;
        public Button deleteButton;
        public Button editButton;

        public ViewHolder(View itemView) {
            super(itemView);
            // Initialize views from the item layout
            addressText = itemView.findViewById(R.id.address_text);
            coordinatesText = itemView.findViewById(R.id.coordinates_text);
            deleteButton = itemView.findViewById(R.id.delete_button);
            editButton = itemView.findViewById(R.id.edit_button);
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a new view for each list item
        View view = LayoutInflater.from(context).inflate(R.layout.address_item, parent, false);
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Move cursor to the correct position
        if (!cursor.moveToPosition(position)) {
            return;
        }

        // Get data from cursor
        String address = cursor.getString(cursor.getColumnIndexOrThrow("ADDRESS"));
        double latitude = cursor.getDouble(cursor.getColumnIndexOrThrow("LATITUDE"));
        double longitude = cursor.getDouble(cursor.getColumnIndexOrThrow("LONGITUDE"));
        final int id = cursor.getInt(cursor.getColumnIndexOrThrow("ID"));

        // Set the text for address and coordinates
        holder.addressText.setText(address);
        holder.coordinatesText.setText(String.format("Lat: %.6f, Long: %.6f", latitude, longitude));

        // Set click listeners for buttons
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(id, address, latitude, longitude);
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDeleteClick(id);
            }
        });
    }

    // Return the size of your dataset
    @Override
    public int getItemCount() {
        return cursor == null ? 0 : cursor.getCount();
    }

    // Method to update the cursor with new data
    public void swapCursor(Cursor newCursor) {
        if (cursor != null) {
            cursor.close();
        }
        cursor = newCursor;
        notifyDataSetChanged();
    }
}
