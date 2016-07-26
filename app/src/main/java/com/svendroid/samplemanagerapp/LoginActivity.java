package com.svendroid.samplemanagerapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import android.database.Cursor;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.svendroid.samplemanagerapp.data.UserDbHelper;
import com.svendroid.samplemanagerapp.model.ProjectBasic;
import com.svendroid.samplemanagerapp.model.User;
import com.svendroid.samplemanagerapp.service.SmMessageHandlerService;
import com.svendroid.samplemanagerapp.service.SmRegistrationIntentService;
import com.svendroid.samplemanagerapp.util.Config;
import com.svendroid.samplemanagerapp.util.ProjectAdapter;

import java.util.ArrayList;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * Action for token check
     */
    private static final String ACTION_TOKENCHECK = "com.svendroid.samplemanagerapp.service.action.TOKENCHECK";

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View welcomeSplash;
    public ProjectBasic[] projectItems;

    @Override
    protected void onPause() {

        String userId = Config.getUserId(this);
        JsonArray jsonArray = new JsonArray();

        if (this.projectItems != null) {
            for (int i = 0; i < this.projectItems.length; i++) {
                ProjectBasic project = projectItems[i];

                if (project.isDirty()) {

                    // Subscribe/unsubscribe for topics (firebase cloud messages)
                    if (project.isCheckedIn()) {
                        FirebaseMessaging.getInstance().subscribeToTopic(project.get_id());
                    } else {
                        FirebaseMessaging.getInstance().unsubscribeFromTopic(project.get_id());
                    }
                    JsonObject projectJson = new JsonObject();
                    projectJson.addProperty("projectId", project.get_id());
                    projectJson.addProperty("checkedIn", project.isCheckedIn());
                    jsonArray.add(projectJson);

                    project.setDirty(false);
                }
            }
        }


        if (jsonArray.size() > 0) {

            JsonObject json = new JsonObject();
            json.add("projects", jsonArray);
            json.addProperty("userId", userId);
            Ion.with(this)
                    .load("PUT", Config.hostUrl + "api/projects/subscribe")
                    .setJsonObjectBody(json)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {

                            if (result != null) {

                            }
                        }
                    });

        }

        for (int i = 0; i < jsonArray.size(); i++) {
            Intent startProjectsIntent = new Intent(this, SmMessageHandlerService.class);
            startProjectsIntent.setAction("com.svendroid.samplemanagerapp.service.action.project_start");
            startProjectsIntent.putExtra("projectId", jsonArray.get(i).getAsJsonObject().get("projectId").getAsString());
            startService(startProjectsIntent);
        }
        // Hack to make it work


        super.onPause();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        refreshProjects();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int errorCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (errorCode != ConnectionResult.SUCCESS) {
            GooglePlayServicesUtil.getErrorDialog(errorCode, this, 0);
        }
        setContentView(R.layout.activity_main);
        //getSupportActionBar().setIcon(R.mipmap.svendroid_small);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        // Set up the login form.

        welcomeSplash = findViewById(R.id.welcome_splash);
        mProgressView = findViewById(R.id.login_progress);


        //attemptLogin();

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {

                switch (menu.getViewType()) {
                    case 0:
                        // create "join" item
                        SwipeMenuItem joinItem = new SwipeMenuItem(getApplicationContext());
                        joinItem.setBackground(new ColorDrawable(Color.rgb(233, 30, 99)));
                        joinItem.setWidth(250);
                        joinItem.setIcon(R.drawable.ic_favorite_border_white_48dp);
                        menu.addMenuItem(joinItem);
                        break;
                    case 1:
                        // create "leave" item
                        SwipeMenuItem leaveItem = new SwipeMenuItem(getApplicationContext());
                        leaveItem.setTitle("Dies ist eine exemplarischer Text. Hier sollen Informationen über das Projekt stehen. Zum Beispiel, dass es um das Untersuchen des Narzissmus' geht.");
                        leaveItem.setTitleColor(Color.GRAY);
                        leaveItem.setTitleSize(14);
                        //leaveItem.setBackground(new ColorDrawable(Color.rgb(233, 30, 99)));
                        leaveItem.setBackground(new ColorDrawable(Color.rgb(238, 238, 238)));
                        leaveItem.setWidth(830);

                        //leaveItem.setIcon(R.drawable.ic_info_white_48dp);
                        menu.addMenuItem(leaveItem);

                        joinItem = new SwipeMenuItem(getApplicationContext());
                        joinItem.setBackground(new ColorDrawable(Color.BLUE));
                        joinItem.setWidth(240);
                        joinItem.setIcon(R.drawable.ic_info_white_48dp);
                        menu.addMenuItem(joinItem);
                        break;
                }
            }
        };

        // set creator
        SwipeMenuListView listView = (SwipeMenuListView) findViewById(R.id.listView);
        listView.setMenuCreator(creator);

        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // open
                        break;
                    case 1:
                        // delete
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });

        refreshProjects();
    }

    private void refreshProjects() {
        showProgress(true);
        if (Config.getUserId(this) != null) {
            Ion.with(this)
                    .load("GET" ,Config.hostUrl + "api/projects/mobile/" + Config.getUserId(getApplicationContext()))
                    .asJsonArray()
                    .setCallback(new FutureCallback<JsonArray>() {

                        @Override
                        public void onCompleted(Exception e, JsonArray result) {

                            if (result != null) {
                                ProjectBasic[] projects = new Gson().fromJson(result, ProjectBasic[].class);

                                projectItems = projects;
                                ProjectAdapter projectAdapter = new ProjectAdapter(getApplicationContext(), projects);
                                SwipeMenuListView listView = (SwipeMenuListView) findViewById(R.id.listView);
                                listView.setAdapter(projectAdapter);
                                showProgress(false);
                            }

                        }
                    });
        }

    }

    public void onHelp(View view) {
        UserDbHelper userDbHelper = new UserDbHelper(getApplicationContext());
        User user = userDbHelper.getAll().get(0);
        Snackbar.make(view, "User: " + user.getEmail(), Snackbar.LENGTH_LONG)
                .setAction("LOGOUT", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        UserDbHelper userDbHelper1 = new UserDbHelper(getApplicationContext());
                        String token = userDbHelper1.getAll().get(0).getGcmToken();
                        userDbHelper1.tabulaRasa();
                        userDbHelper1.close();

                        Intent registerIntent = new Intent(getApplicationContext(), RegisterActivity.class);
                        registerIntent.putExtra("token", token);
                        registerIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        registerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(registerIntent);
                    }
                }).show();
    }
    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {


        // Store values at the time of the login attempt.
        String email = "deine@email.ch";
        String password = "123456";

        boolean cancel = false;
        View focusView = null;
/*
        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.

        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }
*/
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            JsonObject json = new JsonObject();
            json.addProperty("login", email);
            json.addProperty("password", password);
            Ion.with(this)
                    .load("POST" ,Config.hostUrl + "api/users/login")
                    .setJsonObjectBody(json)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {

                            if (result != null) {
                                User user = new Gson().fromJson(result, User.class);

                                // Insert user in local db

                                UserDbHelper userDbHelper = new UserDbHelper(getApplicationContext());
                                Cursor res = userDbHelper.getData(user.get_id());
                                res.moveToFirst();

                                // Ugly hack to have only 1 row in user table
                                if (res.isAfterLast()) {
                                    if (userDbHelper.numberOfRows() > 0) {
                                        userDbHelper.tabulaRasa();
                                    }
                                    userDbHelper.insertUser(user.getEmail(), user.get_id(), user.getGcmToken());
                                }
                                userDbHelper.close();

                                // Start GCM registration service
                                Intent intent = new Intent(getApplicationContext(), SmRegistrationIntentService.class);
                                intent.setAction(ACTION_TOKENCHECK);
                                startService(intent);
                            }

                            showProgress(false);
                        }
                    });

            showProgress(true);
            //mAuthTask = new UserLoginTask(email, password);
            //mAuthTask.execute((Void) null);
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            welcomeSplash.setVisibility(show ? View.GONE : View.VISIBLE);
            welcomeSplash.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    welcomeSplash.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            welcomeSplash.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }


}

