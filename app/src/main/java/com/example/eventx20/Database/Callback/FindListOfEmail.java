package com.example.eventx20.Database.Callback;

import java.util.List;

public interface FindListOfEmail {
    void onSuccess(List<String> alluser);
    void onFailed(List<String> allSuccessuser);
}
