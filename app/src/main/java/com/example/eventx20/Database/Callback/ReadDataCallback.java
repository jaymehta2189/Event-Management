package com.example.eventx20.Database.Callback;

import com.example.eventx20.Database.DataModel.User;
import com.google.firebase.database.DatabaseError;
import java.util.List;
import java.util.Objects;

public interface ReadDataCallback {
    void onDataLoaded(List<Object> users);
    void onError(DatabaseError error);
}

