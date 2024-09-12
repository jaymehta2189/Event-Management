package com.example.eventx20.Database.Callback;

import com.example.eventx20.Database.DataModel.User;

public interface QrStudent {
    void onSuccess(User user);
    void onAllReadyPresent(User user);
    void onFailed();
}
