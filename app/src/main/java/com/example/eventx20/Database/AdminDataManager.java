package com.example.eventx20.Database;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.eventx20.Database.Callback.FindByModel;
import com.example.eventx20.Database.Callback.InsertModel;
import com.example.eventx20.Database.Callback.InsertUpdateModel;
import com.example.eventx20.Database.Callback.UpdateOrViewData;
import com.example.eventx20.Database.DataModel.Admin;
import com.example.eventx20.Database.DataModel.Event;
import com.example.eventx20.Database.DataModel.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class AdminDataManager {
    private static final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private static final DatabaseReference databaseReference = firebaseDatabase.getReference("Admin");

    public static void InsertData(@NonNull Admin user, @NonNull InsertModel callback) {
        HashMap<String, Object> dataset = new HashMap<>();

        dataset.put("name", user.getName());
        dataset.put("email", user.getEmail());
        dataset.put("password", user.getPassword());

        String key = databaseReference.push().getKey();
        user.setKey(key);
        dataset.put("key", user.getKey());

        if (key != null) {
            AdminDataManager.databaseReference.child(key).setValue(dataset).addOnCompleteListener(task -> {
                if(task.isComplete()){
                    callback.onComplete();
                } else if (task.isCanceled()) {
                    callback.onError();
                }
            });
        } else {
            callback.onError();
        }
    }

    public static void FindByName(@NonNull String name, @NonNull String password, @NonNull FindByModel callback) {
        Query query = AdminDataManager.databaseReference.orderByChild("name").equalTo(name);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean successful = false;
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Admin adminData = snapshot.getValue(Admin.class);
                        if (adminData != null && name.equals(adminData.getName()) && password.equals(adminData.getPassword())) {
                            successful = true;
                            callback.onSuccess(snapshot.getKey());  // Notify the caller with the user data
                            break; // We found a match, no need to check further
                        }
                    }
                }
                if (!successful) {
                    callback.onFailure();  // Notify the caller that no matching user was found
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onFailure();  // Notify the caller that the operation was cancelled or failed
                Log.w("Firebase", "Failed to fetch data.", databaseError.toException());
            }
        });
    }

    public static void FindByKey(@NonNull String Key ,@NonNull FindByModel callback){
        databaseReference.child(Key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Admin admin = null;
                if (dataSnapshot.exists()) {
                    admin = dataSnapshot.getValue(Admin.class);
                }
                callback.onSuccess(admin);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onFailure();  // Return null in case of an error
            }
        });
    }

    public static void UpdateAdmin(@NonNull String Key , @NonNull UpdateOrViewData callback){
        AdminDataManager.databaseReference.child(Key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Admin admin = snapshot.getValue(Admin.class);
                    if(admin != null){
                        callback.onSuccess(admin);
                        AdminDataManager.databaseReference.child(Key).setValue(admin);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void InsertEvent(@NonNull String key ,@NonNull Event event,@NonNull InsertModel callback){
        EventDataManager.InsertEvent(event, new InsertUpdateModel() {
            @Override
            public void onComplete(Object o) {
                callback.onComplete();
//                String eventKey = (String) o;
//                AdminDataManager.UpdateAdmin(key, data -> {
//                    Admin admin = (Admin) data;
//                    admin.getEventList().add(eventKey);
//                });
            }

            @Override
            public void onError(Object o) {

            }
        });
    }

    public static void FindListEvent(@NonNull String key,@NonNull FindByModel callback){
        List<Event> events = new ArrayList<>();

        AdminDataManager.FindByKey(key, new FindByModel() {
            @Override
            public void onSuccess(Object model) {
                Admin admin = (Admin) model;

                if(admin.getEventList() == null | admin.getEventList().isEmpty()){
                    callback.onFailure();
                }
                final int totalEvents = admin.getEventList().size();
                final AtomicInteger processedCount = new AtomicInteger(0);

                for(String key : admin.getEventList()){
                  EventDataManager.FindByKey(key, new FindByModel() {
                      @Override
                      public void onSuccess(Object model) {
                          if(model != null){
                              events.add((Event) model);
                          }

                          if(processedCount.incrementAndGet() == totalEvents){
                              callback.onSuccess(events);
                          }
                      }

                      @Override
                      public void onFailure() {
                          if(processedCount.incrementAndGet() == totalEvents){
                              callback.onSuccess(events);
                          }
                      }
                  });
                }
            }

            @Override
            public void onFailure() {

            }
        });
    }
}
