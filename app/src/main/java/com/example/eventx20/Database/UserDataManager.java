package com.example.eventx20.Database;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.eventx20.Database.Callback.FindByModel;
import com.example.eventx20.Database.Callback.FindListOfEmail;
import com.example.eventx20.Database.Callback.InsertModel;
import com.example.eventx20.Database.Callback.QrChangeData;
import com.example.eventx20.Database.Callback.QrStudent;
import com.example.eventx20.Database.Callback.ReadDataCallback;
import com.example.eventx20.Database.Callback.UpdateOrViewData;
import com.example.eventx20.Database.DataModel.Group;
import com.example.eventx20.Database.DataModel.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class UserDataManager {
    private static final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private static final DatabaseReference databaseReference = firebaseDatabase.getReference("User");

    public static void InsertData(@NonNull User user, @NonNull InsertModel callback) {
        HashMap<String, Object> dataset = new HashMap<>();

        dataset.put("name", user.getName());
        dataset.put("email", user.getEmail());
        dataset.put("password", user.getPassword());
        dataset.put("isJoinEvent",user.isJoinEvent());
        dataset.put("isPresent",user.isPresent());
        dataset.put("isFood",user.isFood());
        dataset.put("groupId",user.getGroupId());

        String key = databaseReference.push().getKey();
        user.setKey(key);
        dataset.put("key", user.getKey());

        if (key != null) {
            databaseReference.child(key).setValue(dataset).addOnCompleteListener(task -> {
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

    public static void ReadData(@NonNull ReadDataCallback callback) {
        List<User> allUser = new ArrayList<>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                for (DataSnapshot snapshot : datasnapshot.getChildren()) {
                    // Convert the snapshot into a user object
                    User userData = snapshot.getValue(User.class);
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

    public static void FindByEmail(@NonNull String email, @NonNull String password, @NonNull FindByModel callback) {
        Query query = databaseReference.orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean successful = false;
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        User userData = snapshot.getValue(User.class);
                        if (userData != null && email.equals(userData.getEmail()) && password.equals(userData.getPassword())) {
                            successful = true;
                            callback.onSuccess(userData);  // Notify the caller with the user data
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

    public static void FindByListOfEmailOnly(@NonNull List<String> studentEmails, @NonNull FindListOfEmail callback){
        List<String> matchedUsers = new ArrayList<>();

        for (String email : studentEmails) {
            Query query = databaseReference.orderByChild("email").equalTo(email);

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            matchedUsers.add(userSnapshot.getKey());
                        }
                    }
                    if (matchedUsers.size() == studentEmails.size()) {
                        callback.onSuccess(matchedUsers); // Call the callback once all users are found
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    callback.onFailed(matchedUsers);
                }
            });
        }
    }

    public static void FindByName(@NonNull String name, @NonNull String password, @NonNull FindByModel callback) {
        Query query = databaseReference.orderByChild("name").equalTo(name);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean successful = false;
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        User userData = snapshot.getValue(User.class);
                        if (userData != null && name.equals(userData.getName()) && password.equals(userData.getPassword())) {
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

    public  static  void QrFindByKey(@NonNull String Key, QrChangeData ChangeCallback, QrStudent callback){
        databaseReference.child(Key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        if(user.isPresent()){
                            callback.onAllReadyPresent(user);
                            return;
                        }
//                        user.setPresent(true);
                        ChangeCallback.onChange(user);
                        databaseReference.child(Key).setValue(user);
                        callback.onSuccess(user);
                    }
                }else {
                    callback.onFailed();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onFailed();
            }
        });
    }

    public static  void FindByListSet(List<String> keys, UpdateOrViewData callback){
        databaseReference.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                for (String key : keys) {
                    User user = mutableData.child(key).getValue(User.class);
                    if (user != null) {
                        callback.onSuccess(user);
                        mutableData.child(key).setValue(user);
                    }
                }
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean committed, DataSnapshot dataSnapshot) {
            }
        });

    }

    public static void FindByGroup(Group group, FindByModel callback) {
        List<User> students = new ArrayList<>();
        List<String> studentIds = group.getStudentId();

        if (studentIds == null || studentIds.isEmpty()) {
            callback.onFailure();
            return;
        }

        final int totalStudents = studentIds.size();
        final AtomicInteger processedCount = new AtomicInteger(0);

        // For each student ID, retrieve the corresponding user data
        for (String key : studentIds) {
            FindByKey(key, new FindByModel() {
                @Override
                public void onSuccess(Object model) {
                    if (model != null) {
                        students.add((User) model);
                    }

                    // Increment the processed count and check if all are processed
                    if (processedCount.incrementAndGet() == totalStudents) {
                        Object[] listOfGroup = new Object[2];
                        listOfGroup[0] = students;
                        listOfGroup[1] = group;
                        callback.onSuccess(listOfGroup);
                    }
                }

                @Override
                public void onFailure() {
                    // Handle failure (e.g., if a user is not found or the request fails)
                    if (processedCount.incrementAndGet() == totalStudents) {
                        Object[] listOfGroup = new Object[2];
                        listOfGroup[0] = students;
                        listOfGroup[1] = group;
                        callback.onSuccess(listOfGroup);  // Still return collected users, even if some fail
                    }
                }
            });
        }
    }

    public static void FindByKey(String Key , FindByModel callback){
        databaseReference.child(Key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = null;
                if (dataSnapshot.exists()) {
                    user = dataSnapshot.getValue(User.class);
                }
                callback.onSuccess(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onFailure();  // Return null in case of an error
            }
        });
    }
    public static  void BindUserToEvent(String key,FindByModel callback){
        UserDataManager.FindByKey(key, new FindByModel() {
            @Override
            public void onSuccess(Object model) {
                User user = (User) model;
                if(!Objects.equals(user.getGroupId(), "")) {
                    GroupDataManager.FindByKey(user.getGroupId(), new FindByModel() {
                        @Override
                        public void onSuccess(Object model) {
                            Group group = (Group) model;
                            EventDataManager.FindByName(group.getEventName(), callback);
                        }

                        @Override
                        public void onFailure() {

                        }
                    });
                }else {
                    callback.onFailure();
                }
            }

            @Override
            public void onFailure() {

            }
        });
    }
}
