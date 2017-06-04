package com.teamappjobs.appjobs;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Lucas on 28/05/2017.
 */

public class Applicaton extends Application {
    public static String user;
    public static SharedPreferences prefs;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public String getUser(){
        prefs = getSharedPreferences("Configuracoes", Context.MODE_PRIVATE);
        user = prefs.getString("email", "");
        return user;
    }
}
