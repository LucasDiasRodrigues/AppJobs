package com.teamappjobs.appjobs;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Lucas on 28/05/2017.
 */

public class Application extends android.app.Application {
    public static String user;
    public static SharedPreferences prefs;
    private static LatLng userLocation;


    @Override
    public void onCreate() {
        super.onCreate();
    }

    public String getUserFromPreferences(){
        prefs = getSharedPreferences("Configuracoes", Context.MODE_PRIVATE);
        user = prefs.getString("email", "");
        return user;
    }

    public static void saveUserLocation(LatLng mLatlang){
        userLocation = mLatlang;
    }

    public static LatLng getUserLastLocation(){
        return userLocation;
    }


}
