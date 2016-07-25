package com.svendroid.samplemanagerapp;

import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.svendroid.samplemanagerapp.data.UserDbHelper;
import com.svendroid.samplemanagerapp.model.User;
import com.svendroid.samplemanagerapp.util.Config;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



    }

    public void onRegister(View view) {

        String token = getIntent().getStringExtra("token");
        String userId = Config.getUserId(getApplicationContext());

        if (((RadioButton)findViewById(R.id.rbWithEmail)).isChecked()) {
            EditText emailInput = (EditText) findViewById(R.id.etEmail);
            if (emailInput.getText().toString().equals("")) {
                Snackbar.make(view, "Keine Email Adresse angegeben!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return;
            }
            register(emailInput.getText().toString());
        } else if (((RadioButton)findViewById(R.id.rbWithoutEmail)).isChecked()) {
            register("");
        } else {
            Snackbar.make(view, "Nichts ausgewählt!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    private void register(String email) {
        JsonObject json = new JsonObject();
        json.addProperty("token", getIntent().getStringExtra("token"));
        json.addProperty("email", email);
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

                            finish();
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
