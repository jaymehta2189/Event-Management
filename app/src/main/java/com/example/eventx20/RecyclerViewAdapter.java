package com.example.eventx20;
// RecyclerViewAdapter.java
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import android.content.Context;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private final List<Event> eventList;
    private final Context context; // Add this context

    // Constructor
    public RecyclerViewAdapter(Context context, List<Event> eventList) {
        this.context = context; // Initialize context
        this.eventList = eventList;
    }

    // ViewHolder class
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView eventName, eventDate, eventTime, eventLocation;

        public ViewHolder(View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.event_name);
            eventDate = itemView.findViewById(R.id.event_date);
            eventTime = itemView.findViewById(R.id.event_time);
            eventLocation = itemView.findViewById(R.id.event_location);
        }

        public void bind(Event event, Context context) {
            eventName.setText(event.getName());
            eventDate.setText("Date: " + event.getDate());
            eventTime.setText("Time: " + event.getTime());
            eventLocation.setText("Location: " + event.getLocation());

            // Set click listener to open the new activity with the event name
            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, Requirement_collection.class); // Replace with your new activity name
                intent.putExtra("EVENT_NAME", event.getName()); // Pass the event name
                context.startActivity(intent);
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(eventList.get(position), context);
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }
}
