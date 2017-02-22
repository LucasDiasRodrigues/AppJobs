package com.teamappjobs.appjobs.asyncTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FacebookAuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.teamappjobs.appjobs.R;
import com.teamappjobs.appjobs.activity.MainActivity;
import com.teamappjobs.appjobs.web.HttpConnection;
import com.teamappjobs.appjobs.web.LoginJson;

/**
 * Created by Lucas on 28/04/2016.
 */
public class LogOutTask extends AsyncTask {

    private final String url = "http://www.4runnerapp.com.br/AppJobs/Ctrl/login_json.php";
    private final String method = "logoff-json";
    private final Context context;
    private String email;
    private ProgressDialog progress;

    public LogOutTask (String email, Context context) {
        this.email = email;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progress = ProgressDialog.show(context, "Aguarde...", "Saindo", true, true);
    }

    @Override
    protected Object doInBackground(Object[] params) {
        LoginJson json = new LoginJson();

        SharedPreferences prefs = context.getSharedPreferences("Configuracoes",Context.MODE_PRIVATE);
        String gcmId = prefs.getString("gcmId", "");

        String data = json.logoffToJson(email,gcmId);



        String answer = HttpConnection.getSetDataWeb(this.url, this.method, data);
        Log.i("LogoffTask", answer+"");

        return answer;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        progress.dismiss();

        if(o.equals("Sucesso")){
            SharedPreferences prefs = context.getSharedPreferences("Configuracoes", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.commit();

            FirebaseAuth.getInstance().signOut();
            LoginManager.getInstance().logOut();

            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);
            ((Activity)context).finish();
        } else {
            Toast.makeText(context, R.string.conexaoIndosponivel,Toast.LENGTH_SHORT).show();

        }
    }




}
