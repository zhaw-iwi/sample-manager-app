package com.svendroid.samplemanagerapp;

import android.app.NotificationManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.svendroid.samplemanagerapp.data.UserDbHelper;
import com.svendroid.samplemanagerapp.model.User;
import com.svendroid.samplemanagerapp.service.SmRegistrationIntentService;
import com.svendroid.samplemanagerapp.util.Config;

import java.util.ArrayList;

public class MeasureActivity extends AppCompatActivity {

    private String text;
    private String alias;
    private String type;
    private String[] values;
    private String measureId;
    private boolean hasChildren;
    private Toolbar mActionBarToolbar;
    private int notificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure);

        // Set support toolbar
        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setTitle("Ich habe eine Frage...");


        Intent intent = getIntent();
        text = intent.getStringExtra("text");
        alias = intent.getStringExtra("alias");
        type = intent.getStringExtra("type");
        values = intent.getStringArrayExtra("values");
        measureId = intent.getStringExtra("measureId");
        hasChildren = intent.getBooleanExtra("hasChildren", false);
        notificationId = intent.getIntExtra("notificationId", 0);

        if (type.equals(getString(R.string.measure_type_select_one))) {
            final RadioGroup rg = (RadioGroup) findViewById(R.id.radioGroupSelectOne);

            for(int j = 0; j < values.length; j++)
            {
                // Create Radio Button
                RadioButton radioButton = new RadioButton(this);
                radioButton.setText(values[j]);
                rg.addView(radioButton);

            }
            findViewById(R.id.select_one).setVisibility(View.VISIBLE);
        }
        if (type.equals(getString(R.string.measure_type_select_many))) {
            final LinearLayout lm = (LinearLayout) findViewById(R.id.select_many);

            LinearLayout ll = (LinearLayout) findViewById(R.id.containerSelectMany);

            for (int j = 0; j < values.length; j++) {
                // Create Checkboxes
                CheckBox checkBox = new CheckBox(this);
                checkBox.setText(values[j]);
                ll.addView(checkBox);
            }

            findViewById(R.id.select_many).setVisibility(View.VISIBLE);
        }
        else if (type.equals(getString(R.string.measure_type_yes_no))) {
            // Yes/no
            findViewById(R.id.yes_no).setVisibility(View.VISIBLE);

        } else if (type.equals(getString(R.string.measure_type_rating))) {
            // Rating
            findViewById(R.id.rating).setVisibility(View.VISIBLE);
            RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
            ratingBar.setNumStars(values.length);
            ratingBar.setMax(values.length);
            ratingBar.setStepSize(1);
        } else if (type.equals(getString(R.string.measure_type_frequency))) {
            // Frequency
            findViewById(R.id.frequency).setVisibility(View.VISIBLE);
            NumberPicker numberPicker = (NumberPicker) findViewById(R.id.numberPicker);
            numberPicker.setMinValue(Integer.parseInt(values[0]));
            numberPicker.setMaxValue(Integer.parseInt(values[values.length - 1]));

        } else if (type.equals(getString(R.string.measure_type_free_text))) {
            // Free text
            findViewById(R.id.free_text).setVisibility(View.VISIBLE);

        } else if (type.equals(getString(R.string.measure_type_option))) {
            // Option
            findViewById(R.id.option).setVisibility(View.VISIBLE);

        }
        TextView measureText =  (TextView) findViewById(R.id.measureText);
        measureText.setText(text);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void onConfirmed(final View view) {
        String value = "";
        if (type.equals(getString(R.string.measure_type_select_one))) {
            // Select one
            RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroupSelectOne);
            int count = radioGroup.getChildCount();
            for (int i = 0; i < count; i++) {
                View o = radioGroup.getChildAt(i);
                if (o instanceof RadioButton) {
                    RadioButton rb = (RadioButton)o;
                    if (rb.isChecked()) {
                        value = rb.getText().toString();
                    }
                }
            }

        }
        else if (type.equals(getString(R.string.measure_type_select_many))) {
            // Select many
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.containerSelectMany);
            int count = linearLayout.getChildCount();
            for (int i = 0; i < count; i++) {
                View o = linearLayout.getChildAt(i);
                if (o instanceof CheckBox) {
                    CheckBox cb = (CheckBox)o;
                    if (cb.isChecked()) {
                        value += cb.getText().toString() + ',';
                    }
                }
            }
            if (value.length() > 0) {
                value = value.substring(0, value.length()-1);
            }
        }
        else if (type.equals(getString(R.string.measure_type_yes_no))) {
            // Yes/no
            RadioButton rbYes = (RadioButton) findViewById(R.id.radioButtonYes);
            RadioButton rbNo = (RadioButton) findViewById(R.id.radioButtonNo);
            value = rbYes.isChecked() ? getString(R.string.value_yes) : rbNo.isChecked() ? getString(R.string.value_no) : "";

            findViewById(R.id.yes_no).setVisibility(View.GONE);

        } else if (type.equals(getString(R.string.measure_type_rating))) {
            // Rating

            RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
            value = ratingBar.getRating() + "";
            findViewById(R.id.rating).setVisibility(View.GONE);
        } else if (type.equals(getString(R.string.measure_type_frequency))) {
            // Frequency

            NumberPicker numberPicker = (NumberPicker) findViewById(R.id.numberPicker);
            value = numberPicker.getValue() + "";
            findViewById(R.id.frequency).setVisibility(View.GONE);
        } else if (type.equals(getString(R.string.measure_type_free_text))) {
            // Free text
            EditText freeText = (EditText) findViewById(R.id.freeText);
            value = freeText.getText().toString();
            findViewById(R.id.free_text).setVisibility(View.GONE);

        } else if (type.equals(getString(R.string.measure_type_option))) {
            // Option
            findViewById(R.id.option).setVisibility(View.GONE);

        }


        findViewById(R.id.answerPanel).setVisibility(View.GONE);

        // Validation
        if (value.equals("")) {
            Snackbar.make(view, "Nichts ausgewählt!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }

        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        findViewById(R.id.confirmButton).setVisibility(View.GONE);


        UserDbHelper userDbHelper = new UserDbHelper(this);
        User user = userDbHelper.getAll().get(0);
        userDbHelper.close();

        JsonObject json = new JsonObject();
        json.addProperty("value", value);
        json.addProperty("measure", measureId);
        json.addProperty("user", user.get_id());
        Ion.with(this)
                .load("POST" , Config.hostUrl + "api/records/")
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        if (result != null) {

                            NotificationManager notifyManager =
                                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                            notifyManager.cancel(notificationId);

                            findViewById(R.id.progressBar).setVisibility(View.GONE);
                            findViewById(R.id.questionLayout).setVisibility(View.VISIBLE);

                            new Thread(new Runnable() {
                                public void run() {

                                    findViewById(R.id.measureText).post(new Runnable() {
                                        public void run() {
                                            ((TextView)findViewById(R.id.measureText)).setText("Vielen Dank für deine Antwort.");
                                        }
                                    });

                                    try {
                                        Thread.sleep(2500);
                                    } catch (InterruptedException e1) {
                                        e1.printStackTrace();
                                    }

                                    finish();
                                }
                            }).start();
                        } else {
                            findViewById(R.id.progressBar).setVisibility(View.GONE);
                            findViewById(R.id.questionLayout).setVisibility(View.VISIBLE);
                            findViewById(R.id.confirmButton).setVisibility(View.VISIBLE);
                            findViewById(R.id.answerPanel).setVisibility(View.VISIBLE);
                            findViewById(R.id.measureText).post(new Runnable() {
                                public void run() {
                                    ((TextView)findViewById(R.id.measureText)).setText("Ooops, da ist was schief gelaufen.\nBitte versuche, deine Antwort nochmals zu bestätigen.");
                                }
                            });
                        }
                    }
                });
    }
}
