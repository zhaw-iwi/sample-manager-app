package com.svendroid.samplemanagerapp.util;

import android.content.Context;

import com.svendroid.samplemanagerapp.data.UserDbHelper;
import com.svendroid.samplemanagerapp.model.User;

/**
 * Created by svhe on 11.04.2016.
 */
public class Config {
    //public static final String hostUrl = "https://mighty-everglades-95815.herokuapp.com/";
    public static final String hostUrl = "http://192.168.1.149:3001/";

    public static String userId = null;

    public static String getUserId(Context context) {
        if (userId == null) {
            UserDbHelper userDbHelper = new UserDbHelper(context);

            if (userDbHelper.getAll().size() == 0) {
                return null;
            }
            User user = userDbHelper.getAll().get(0);
            userDbHelper.close();
            userId = user.get_id();
        }
        return userId;
    }
}
