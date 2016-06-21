package com.svendroid.samplemanagerapp.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class SmInstanceIDListenerService extends FirebaseInstanceIdService {
    private static final String ACTION_TOKENREFRESH = "com.svendroid.samplemanagerapp.service.action.TOKENREFRESH";
    private String TAG = "SmInstanceIDListenerService";

    public SmInstanceIDListenerService() {
    }


    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        Intent intent = new Intent(this, SmRegistrationIntentService.class);
        intent.putExtra("token", refreshedToken);
        intent.setAction(ACTION_TOKENREFRESH);
        startService(intent);
    }
}
