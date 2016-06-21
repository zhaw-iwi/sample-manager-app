package com.svendroid.samplemanagerapp.service;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.Console;
import java.util.Iterator;
import java.util.Map;

public class SmFcmListenerService extends FirebaseMessagingService {
    private static final String actionPrefix = "com.svendroid.samplemanagerapp.service.action.";
    public SmFcmListenerService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage message) {

        Log.i("Svendroid", "Message received");
        Map data = message.getData();

        Intent intent = new Intent(this, SmMessageHandlerService.class);
        intent.setAction(actionPrefix + data.get("action"));

        Iterator it = data.keySet().iterator();

        while (it.hasNext()) {
            String key = (String) it.next();
            intent.putExtra(key, (String) data.get(key));
        }

        startService(intent);
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    @Override
    public void onMessageSent(String msgId) {
        super.onMessageSent(msgId);
    }

}
