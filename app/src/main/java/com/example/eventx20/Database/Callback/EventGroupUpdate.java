package com.example.eventx20.Database.Callback;

import com.example.eventx20.Database.DataModel.Group;

public interface EventGroupUpdate {
    void onSuccess(Group group);
    void onFailed();
}
