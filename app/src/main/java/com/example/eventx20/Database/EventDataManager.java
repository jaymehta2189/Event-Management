package com.example.eventx20.Database;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.eventx20.Database.Callback.FindByModel;
import com.example.eventx20.Database.Callback.GroupToEvent;
import com.example.eventx20.Database.Callback.InsertModel;
import com.example.eventx20.Database.Callback.InsertUpdateModel;
import com.example.eventx20.Database.Callback.ReadDataCallback;
import com.example.eventx20.Database.DataModel.Event;
import com.example.eventx20.Database.DataModel.Group;
import com.example.eventx20.Database.DataModel.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class EventDataManager {
    private static final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private static final DatabaseReference databaseReference = firebaseDatabase.getReference("Event");

    public static void InsertGroupInEvent(@NotNull String EventName, Group group, String key, GroupToEvent callback) {
        Query query = databaseReference.orderByChild("name").equalTo(EventName);
        Log.d("Tag",EventName);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Event found, update its groupId
                    Log.d("Tag",EventName+"3");
                    for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                        Event event = eventSnapshot.getValue(Event.class);
                        Log.d("Tag",EventName+"4");
                        if (event != null) {
                            // Check if groupId is null, initialize it if needed
                            if (event.getGroupId() == null) {
                                event.setGroupId(new ArrayList<>());
                            }
                            if (!event.getGroupId().contains(key)) {
                                event.getGroupId().add(key);
                            }

                            // Update the event in the database
                            Log.d("Tag",EventName+"1");
                            databaseReference.child(Objects.requireNonNull(eventSnapshot.getKey())).setValue(event)
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            callback.onSuccess(group, eventSnapshot.getKey());
                                        } else {
                                            Log.d("Tag",EventName+"2");
                                            callback.onFailed();
                                        }
                                    });

                            return;
                        }
                    }
                } else {
                    // Event does not exist, handle the case (optional)
                    callback.onFailed();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onFailed();
            }
        });
    }

    public static void ReadEvent(ReadDataCallback callback){
        List<Event> allUser = new ArrayList<>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                for (DataSnapshot snapshot : datasnapshot.getChildren()) {
                    Event userData = snapshot.getValue(Event.class);
                    if(userData != null) allUser.add(userData);
                }
                callback.onDataLoaded(Collections.singletonList(allUser));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onError(error);
            }
        });
    }


    public static void InsertEvent(@NotNull Event event, @NotNull InsertUpdateModel callback){
        HashMap<String, Object> dataset = new HashMap<>();

        dataset.put("name",event.getName());
        dataset.put("date",event.getDate());
        dataset.put("time",event.getTime());
        dataset.put("location",event.getLocation());

        FindByName(event.getName(), new FindByModel() {

            @Override
            public void onSuccess(Object model) {
//                Toast.makeText()
            }

            @Override
            public void onFailure() {
                String key = EventDataManager.databaseReference.push().getKey();
                dataset.put("key",key);
                assert key != null;
                EventDataManager.databaseReference.child(key).setValue(dataset).
                        addOnCompleteListener(task->{
                            if (task.isSuccessful()) {
                                callback.onComplete(key);
                            }else{
                                callback.onError(key);
                            }
                        });
            }
        });


    }


    public  static void FindByName(String name, FindByModel callback){
        Query query = databaseReference.orderByChild("name").equalTo(name);
        Log.d("TAG", "FindByName: "+"first");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean successful = false;
                Log.d("TAG", "FindByName: "+"first2");
                if (dataSnapshot.exists()) {
                    Log.d("TAG", "FindByName: "+"first1");
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Event userData = snapshot.getValue(Event.class);
                        if (userData != null && name.equals(userData.getName())) {
                            successful = true;
                            callback.onSuccess(userData);
                            break;
                        }
                    }
                }
                if (!successful) {
                    callback.onFailure();  // Notify the caller that no matching user was found
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public static void FindByKey(@NonNull String key ,@NonNull FindByModel callback){
        EventDataManager.databaseReference.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Event event = null;
                if (snapshot.exists()) {
                    event = snapshot.getValue(Event.class);
                }
                callback.onSuccess(event);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
