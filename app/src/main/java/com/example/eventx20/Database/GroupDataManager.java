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
        UserDataManager.FindByListOfEmailOnly(group.getStudentEmailId(), new FindListOfEmail() {
            @Override
            public void onSuccess(List<String> users) {
                dataset.put("ProjectName",group.getProjectName());
                dataset.put("GroupName",group.getGroupName());
                dataset.put("EventName",group.getEventName());
                dataset.put("ContactNumber",group.getContactNumber());
                dataset.put("StudentsKey",users);

                String key = databaseReference.push().getKey();
                dataset.put("Key", key);

                assert key != null;
                databaseReference.child(key).push().setValue(dataset)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                UserDataManager.FindByListSet(users,new UpdateOrViewData(){
                                    @Override
                                    public void onSuccess(Object data) {
                                        User user = (User) data;
                                        user.setPresent(false);
                                        user.setFood(false);
                                        user.setJoinEvent(true);
                                        user.setGroupId(key);
                                    }
                                });
                                EventDataManager.InsertGroupInEvent(group.getEventName(), group, key, callback);
                            } else {
                                callback.onFailed();
                            }
                        });
            }

            @Override
            public void onFailed(List<String> allSuccessuser) {

            }
        });
    }
//public static void InsertUserInGroup(Group group, GroupToEvent callback){
//    HashMap<String, Object> dataset = new HashMap<>();
//    UserDataManager.FindByListOfEmailOnly(group.getStudentEmailId(), new FindListOfEmail() {
//        @Override
//        public void onSuccess(List<String> users) {
//            dataset.put("ProjectName", group.getProjectName());
//            dataset.put("GroupName", group.getGroupName());
//            dataset.put("EventName", group.getEventName());
//            dataset.put("ContactNumber", group.getContactNumber());
//            dataset.put("StudentsKey", users);
//
//            String key = databaseReference.push().getKey();  // Generate a single key
//            if (key == null) {
//                callback.onFailed();  // Handle failure if key generation fails
//                return;
//            }
//            dataset.put("Key", key);
//
//            databaseReference.child(key).setValue(dataset)  // Set value without another push
//                    .addOnCompleteListener(task -> {
//                        if (task.isSuccessful()) {
//                            UserDataManager.FindByListSet(users, new UpdateOrViewData() {
//                                @Override
//                                public void onSuccess(Object data) {
//                                    User user = (User) data;
//                                    user.setPresent(false);
//                                    user.setFood(false);
//                                    user.setJoinEvent(true);
//                                    user.setGroupId(key);
//                                }
//                            });
//                            EventDataManager.InsertGroupInEvent(group.getEventName(), group, key, callback);
//                        } else {
//                            callback.onFailed();
//                        }
//                    });
//        }
//
//        @Override
//        public void onFailed(List<String> allSuccessuser) {
//            callback.onFailed();  // Handle case when emails aren't found
//        }
//    });
//}

    public static void FindGroup(String Studentkey, UpdateOrViewData callback){

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
                                callback.onSuccess(group);
//                                UserDataManager.FindByListSet(group.getStudentEmailId(), callback);
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
}
