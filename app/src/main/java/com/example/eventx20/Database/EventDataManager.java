package com.example.eventx20.Database;

import androidx.annotation.NonNull;

import com.example.eventx20.Database.Callback.FindByModel;
import com.example.eventx20.Database.Callback.GroupToEvent;
import com.example.eventx20.Database.Callback.InsertModel;
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

    public static void InsertGroupInEvent(@NotNull String EventName, Group group, String key, GroupToEvent callback){
        Query query = databaseReference.orderByChild("Name").equalTo(EventName);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Event event = snapshot.getValue(Event.class);
                if (event != null) {
                    if (event.getGroupId() == null) {
                        event.setGroupId(new ArrayList<>());
                    }
                    event.getGroupId().add(key);
                    databaseReference.child(Objects.requireNonNull(snapshot.getKey())).setValue(event);
                    callback.onSuccess(group, snapshot.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
                    allUser.add(userData);
                }
                callback.onDataLoaded(Collections.singletonList(allUser));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onError(error);
            }
        });
    }

    public static void InsertEvent(Event event, InsertModel callback){
        HashMap<String, Object> dataset = new HashMap<>();

        FindByName(event.getName(), new FindByModel() {

            @Override
            public void onSuccess(Object model) {

            }

            @Override
            public void onFailure() {

            }
        });


    }

    public  static void FindByName(String name, FindByModel callback){
        Query query = databaseReference.orderByChild("Name").equalTo(name);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean successful = false;
                if (dataSnapshot.exists()) {
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
}
