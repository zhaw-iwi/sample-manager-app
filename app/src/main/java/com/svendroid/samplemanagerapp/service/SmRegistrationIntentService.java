package com.svendroid.samplemanagerapp.service;

import android.app.IntentService;
import android.content.Intent;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.svendroid.samplemanagerapp.LoginActivity;
import com.svendroid.samplemanagerapp.RegisterActivity;
import com.svendroid.samplemanagerapp.data.UserDbHelper;
import com.svendroid.samplemanagerapp.model.User;
import com.svendroid.samplemanagerapp.util.Config;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class SmRegistrationIntentService extends IntentService {

    private static final String ACTION_TOKENREFRESH = "com.svendroid.samplemanagerapp.service.action.TOKENREFRESH";
    private static final String ACTION_TOKENCHECK = "com.svendroid.samplemanagerapp.service.action.TOKENCHECK";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.svendroid.samplemanagerapp.service.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.svendroid.samplemanagerapp.service.extra.PARAM2";

    public SmRegistrationIntentService() {
        super("SmRegistrationIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {

            if (Config.getUserId(getApplicationContext()) == null) {
                Intent registerIntent = new Intent(getApplicationContext(), RegisterActivity.class);
                registerIntent.putExtra("token", intent.getStringExtra("token"));
                registerIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                registerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(registerIntent);
            } else {

                String token = intent.getStringExtra("token");
                String userId = Config.getUserId(getApplicationContext());
                JsonObject json = new JsonObject();
                json.addProperty("token", token);
                json.addProperty("userId", userId);
                Ion.with(getApplicationContext())
                        .load("POST", Config.hostUrl + "api/users/token")
                        .setJsonObjectBody(json)
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {

                                if (result != null) {
                                    User user = new Gson().fromJson(result, User.class);

                                    UserDbHelper userDbHelper = new UserDbHelper(getApplicationContext());
                                    userDbHelper.tabulaRasa();
                                    userDbHelper.insertUser(user.getEmail(), user.get_id(), user.getGcmToken());
                                    userDbHelper.close();

                                /*
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                */
                                }

                                // do stuff with the result or error
                            }
                        });

            }
        }
    }
}
