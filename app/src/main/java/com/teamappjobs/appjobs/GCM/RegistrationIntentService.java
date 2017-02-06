package com.teamappjobs.appjobs.GCM;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.teamappjobs.appjobs.R;

import java.io.IOException;

/**
 * Created by Lucas on 04/12/2015.
 */
public class RegistrationIntentService extends IntentService {


    private static final String TAG = "RegIntentService";
    private static final String[] TOPICS = {"global"};


    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        InstanceID InstanceID = com.google.android.gms.iid.InstanceID.getInstance(this);
        try {
            String token = InstanceID.getToken(getResources().getString(R.string.GCMSenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Log.i(TAG, "TOKEN: " + token);

            SharedPreferences prefs = this.getSharedPreferences("Configuracoes", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("gcmId",token);
            editor.commit();

        } catch (IOException e) {
            e.printStackTrace();
        }
        //  }


    }

}
