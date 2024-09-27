package com.example.eventx20.QrScannRes;

import com.example.eventx20.Database.Callback.QrChangeData;
import com.example.eventx20.Database.Callback.QrStudent;
import com.example.eventx20.Database.UserDataManager;

public class QrScan {
    private String[] Data;
    public QrScan(String data){
        Data = data.split(" ");
    }

    public void ProcessQrData(QrChangeData ChangeCallback, QrStudent callback){
        UserDataManager.QrFindByKey(Data[0],ChangeCallback,callback);
    }
}
