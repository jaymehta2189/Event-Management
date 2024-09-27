package com.example.eventx20.Database;

import androidx.annotation.NonNull;

import com.example.eventx20.Database.Callback.FindByModel;
import com.example.eventx20.Database.Callback.FindListOfEmail;
import com.example.eventx20.Database.Callback.GroupToEvent;
import com.example.eventx20.Database.Callback.UpdateOrViewData;
import com.example.eventx20.Database.DataModel.Group;
import com.example.eventx20.Database.DataModel.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

public class GroupDataManager {
    private static final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private static final DatabaseReference databaseReference = firebaseDatabase.getReference("Group");

    public static void InsertUserInGroup(Group group, GroupToEvent callback){

        HashMap<String, Object> dataset = new HashMap<>();
        UserDataManager.FindByListOfEmailOnly(group.getStudentId(), new FindListOfEmail() {
            @Override
            public void onSuccess(List<String> users) {
                dataset.put("projectName", group.getProjectName());
                dataset.put("groupName", group.getGroupName());
                dataset.put("eventName", group.getEventName());
                dataset.put("contactNumber", group.getContactNumber());
                dataset.put("studentId", users);

                // Generate a new unique key
                String key = databaseReference.push().getKey();
                group.setKey(key);
                dataset.put("key", group.getKey());

                assert key != null;
                databaseReference.child(key).setValue(dataset)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                UserDataManager.FindByListSet(users, new UpdateOrViewData() {
                                    @Override
                                    public void onSuccess(Object data) {
                                        User user = (User) data;
                                        user.setPresent(false);
                                        user.setFood(false);
                                        user.setJoinEvent(true);
                                        user.setGroupId(key);

//                                        databaseReference.child(user.getKey()).setValue(user)
//                                                .addOnCompleteListener(updateTask -> {
//                                                    if (updateTask.isSuccessful()) {
//                                                        // GOOD WORK :)
//                                                        // Optionally handle success
//                                                    }
//                                                });
                                    }
                                });
                                EventDataManager.InsertGroupInEvent(group.getEventName(), group, group.getKey(), callback);
                            } else {
                                callback.onFailed();
                            }
                        });
            }

            @Override
            public void onFailed(List<String> allSuccessuser) {
                // Handle failure
            }
        });
    }

    public static void FindGroup(String Studentkey, FindByModel callback){

        UserDataManager.FindByKey(Studentkey, new FindByModel() {
            @Override
            public void onSuccess(Object model) {
                User user = (User) model;
                databaseReference.child(user.getGroupId()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            Group group = snapshot.getValue(Group.class);
                            if(group != null){
                                UserDataManager.FindByGroup(group,callback);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
            @Override
            public void onFailure() {

            }
        });

    }
    public static  void FindByKey(String key,FindByModel callback){
        databaseReference.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Group group = null;
                if (snapshot.exists()) {
                    group = snapshot.getValue(Group.class);
                }
                callback.onSuccess(group);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
