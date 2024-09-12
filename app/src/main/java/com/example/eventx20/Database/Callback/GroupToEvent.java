package com.example.eventx20.Database.Callback;

import com.example.eventx20.Database.DataModel.Group;

public interface GroupToEvent {
    void onSuccess(Group group,String key);
    void onFailed();
}
