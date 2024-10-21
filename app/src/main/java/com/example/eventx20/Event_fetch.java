package com.example.eventx20;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventx20.Database.Callback.ReadDataCallback;
import com.example.eventx20.Database.DataModel.Event;
import com.example.eventx20.Database.EventDataManager;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

public class Event_fetch extends AppCompatActivity {
    private List<Event> eventList; // Define the event list as a class member
    private RecyclerViewAdapter adapter; // Define the adapter as a class member

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currentevents);

        // Initialize the event list
        eventList = new ArrayList<>();

        // Find RecyclerView and set its layout manager
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the adapter with the event list
        adapter = new RecyclerViewAdapter(this, eventList);
        recyclerView.setAdapter(adapter);

        // Fetch events from the data manager
        fetchEvents();
    }

    private void fetchEvents() {
        // Read events from the data manager
        EventDataManager.ReadEvent(new ReadDataCallback() {
            @Override
            public void onDataLoaded(List<Object> events) {
                // Clear the existing list to avoid duplication
                eventList.clear();

                // Iterate through the received data and cast each object to the Event type
                if (!events.isEmpty() && events.get(0) instanceof List<?>) {
                    List<?> eventObjects = (List<?>) events.get(0);

                    for (Object eventObj : eventObjects) {
                        if (eventObj instanceof Event) {
                            Event event = (Event) eventObj;
                            eventList.add(event);
                        }
                    }
                }

                // Notify the adapter that the data set has changed
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(DatabaseError error) {
                // Handle the error, e.g., show a message to the user
                Toast.makeText(Event_fetch.this, "Failed to load events: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("EventDataManager", "Error loading events", error.toException());
            }
        });
    }
}