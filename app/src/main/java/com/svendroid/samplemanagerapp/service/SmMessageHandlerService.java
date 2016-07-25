
package com.svendroid.samplemanagerapp.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.svendroid.samplemanagerapp.R;
import com.svendroid.samplemanagerapp.model.MeasureExtended;
import com.svendroid.samplemanagerapp.model.TriggerInstance;
import com.svendroid.samplemanagerapp.model.User;
import com.svendroid.samplemanagerapp.util.Config;

import java.util.Calendar;
import java.util.Random;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class SmMessageHandlerService extends IntentService {

    private static final String ACTION_PROJECT_START = "com.svendroid.samplemanagerapp.service.action.project_start";
    private static final String ACTION_PROJECT_END = "com.svendroid.samplemanagerapp.service.action.project_end";
    private static final String ACTION_MEASURE_UPDATE = "com.svendroid.samplemanagerapp.service.action.measure_update";
    private static final String ACTION_TRIGGER_UPDATE = "com.svendroid.samplemanagerapp.service.action.trigger_update";
    private static final String ACTION_PROJECT_UPDATE = "com.svendroid.samplemanagerapp.service.action.project_update";
    private static final String ACTION_MANUAL_TRIGGER = "com.svendroid.samplemanagerapp.service.action.manual_trigger";
    private static final String ACTION_EXTERNAL_TRIGGER = "com.svendroid.samplemanagerapp.service.action.external_trigger";

    // TODO: Rename parameters
    private static final String EXTRA_MEASURE_ID = "com.svendroid.samplemanagerapp.service.extra.measureId";
    private static final String EXTRA_TRIGGER_ID = "com.svendroid.samplemanagerapp.service.extra.triggerId";
    private static final String EXTRA_PROJECT_ID = "com.svendroid.samplemanagerapp.service.extra.projectId";

    public SmMessageHandlerService() {
        super("SmMessageHandlerService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            Log.i("Svendroid", "Message handled, Action: " + intent.getAction());

            switch (intent.getAction()) {
                case ACTION_PROJECT_START:
                    startProject(intent.getStringExtra("projectId"));
                    break;
                case ACTION_PROJECT_END:
                    break;
                case ACTION_MEASURE_UPDATE:
                    break;
                case ACTION_TRIGGER_UPDATE:
                    break;
                case ACTION_PROJECT_UPDATE:
                    break;
                case ACTION_MANUAL_TRIGGER:
                    triggerManual(intent.getStringExtra("measureId"));
                    break;
                case ACTION_EXTERNAL_TRIGGER:
                    break;
            }
        }
    }

    private void triggerManual(String measureId) {
        Ion.with(getApplicationContext())
                .load("GET", Config.hostUrl + "api/measures/" + measureId)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        Log.i("Svendroid", "Manual trigger");
                        if (result != null) {
                            MeasureExtended measure = new Gson().fromJson(result, MeasureExtended.class);

                            Intent intent = new Intent(getApplicationContext(), SmTriggerReceiver.class);
                            intent.setAction(measure.get_id());
                            intent.putExtra("text", measure.getText());
                            intent.putExtra("alias", measure.getAlias());
                            intent.putExtra("type", measure.getType());
                            intent.putExtra("values", measure.getValues());
                            intent.putExtra("measureId", measure.get_id());
                            intent.putExtra("hasChildren", measure.getChildren() != null);

                            sendBroadcast(intent);
                        }
                    }
                });
    }

    private AlarmManager alarmMgr;

    private void startProject(String projectId) {

        if (Config.getUserId(getApplicationContext()) == null) {
            return;
        }
        /*
        UserDbHelper userDbHelper = new UserDbHelper(getApplicationContext());
        User user = userDbHelper.getAll().get(0);
        userDbHelper.close();
*/
        // Set repeating alarm for daily project update
        Intent updateIntent = new Intent(getApplicationContext(), SmMessageHandlerService.class);
        updateIntent.setAction(projectId);
        updateIntent.putExtra("projectId", projectId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, updateIntent, 0);
        alarmMgr = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);

        // Schedule an update request between 01:00-01:59 next day
        Random r = new Random();
        int randomMinutes = r.nextInt(60);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 1);
        calendar.set(Calendar.MINUTE, randomMinutes);
        calendar.add(Calendar.DATE, 1);
        Log.d("Svendroid", "Update scheduled on " + calendar.getTime().toString());

        alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        // Get actual data of project
        Ion.with(getApplicationContext())
                .load("GET", Config.hostUrl + "api/triggerInstances/project/" + projectId)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        Log.i("Svendroid", "Start project");
                        if (result != null) {
                            TriggerInstance[] triggerInstances = new Gson().fromJson(result, TriggerInstance[].class);

                            Log.i("Svendroid", "TriggerInstances: " + triggerInstances.length);

                            for (TriggerInstance instance : triggerInstances) {

                                if (instance.getTrigger().getType().equals(getString(R.string.trigger_type_and))) {
                                    // And
                                } else if (instance.getTrigger().getType().equals(getString(R.string.trigger_type_or))) {
                                    // Or
                                } else if (instance.getTrigger().getType().equals(getString(R.string.trigger_type_timer))) {
                                    // Timer
                                    for (int i = 0; i < instance.getTrigger().getTimers().length; i++) {

                                        PendingIntent alarmIntent = getIntentBroadcast(instance, i);

                                        // Set the alarm to start
                                        int hour = instance.getTrigger().getTimers()[i].getHour();
                                        int minute = instance.getTrigger().getTimers()[i].getMinute();

                                        Calendar calendar = Calendar.getInstance();
                                        calendar.setTimeInMillis(System.currentTimeMillis());
                                        calendar.set(Calendar.HOUR_OF_DAY, hour);
                                        calendar.set(Calendar.MINUTE, minute);

                                        if (calendar.getTimeInMillis() > Calendar.getInstance().getTimeInMillis()) {
                                            Log.i("Svendroid", "Next trigger: " + calendar.getTime().toString());
                                            alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
                                        } else {
                                            Log.i("Svendroid", "Excluded trigger: " + calendar.getTime().toString());
                                        }
                                    }

                                } else if (instance.getTrigger().getType().equals(getString(R.string.trigger_type_random))) {
                                    // Random

                                    // Calculate random triggers
                                    int cronStart = Integer.parseInt(instance.getTrigger().getTimeSpan().getCronStart().split(" ")[2]);
                                    int cronEnd = Integer.parseInt(instance.getTrigger().getTimeSpan().getCronEnd().split(" ")[2]);
                                    int repeats = instance.getTrigger().getTimeSpan().getRepeats();

                                    int timeSpan = cronEnd - cronStart;
                                    Random r = new Random();

                                    for (int i = 0; i < repeats; i++) {
                                        PendingIntent alarmIntent = getIntentBroadcast(instance, i);

                                        // Set the alarm to start
                                        int minutes = timeSpan * 60;
                                        int randomMinutes = r.nextInt(minutes / repeats) + (minutes / repeats) * i;

                                        int randomHour = randomMinutes / 60;
                                        int randomMinute = randomMinutes % 60;

                                        Calendar calendar = Calendar.getInstance();
                                        calendar.setTimeInMillis(System.currentTimeMillis());
                                        calendar.set(Calendar.HOUR_OF_DAY, randomHour + cronStart);
                                        calendar.set(Calendar.MINUTE, randomMinute);

                                        if (calendar.getTimeInMillis() > Calendar.getInstance().getTimeInMillis()) {
                                            Log.i("Svendroid", "Next trigger: " + calendar.getTime().toString());
                                            alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
                                        } else {
                                            Log.i("Svendroid", "Excluded trigger: " + calendar.getTime().toString());
                                        }
                                    }


                                } else if (instance.getTrigger().getType().equals(getString(R.string.trigger_type_social))) {
                                    // Social
                                } else if (instance.getTrigger().getType().equals(getString(R.string.trigger_type_health))) {
                                    // Health
                                } else if (instance.getTrigger().getType().equals(getString(R.string.trigger_type_place))) {
                                    // Place
                                } else if (instance.getTrigger().getType().equals(getString(R.string.trigger_type_external))) {
                                    // External
                                }
                            }
                        }
                    }
                });
    }

    private PendingIntent getIntentBroadcast(TriggerInstance instance, int index) {
        // Setup of alarm manager
        Context context = getApplicationContext();

        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, SmTriggerReceiver.class);
        // Set triggerInstanceId to identify pending intent
        intent.setAction(instance.getMeasure().get_id() + "_" + index);
        if (instance.getMeasure() != null) {
            intent.putExtra("text", instance.getMeasure().getText());
            intent.putExtra("alias", instance.getMeasure().getAlias());
            intent.putExtra("type", instance.getMeasure().getType());
            intent.putExtra("values", instance.getMeasure().getValues());
            intent.putExtra("measureId", instance.getMeasure().get_id());
            intent.putExtra("hasChildren", instance.getMeasure().getChildren() != null);
        }

        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }
}
