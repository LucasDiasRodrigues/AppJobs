package com.teamappjobs.appjobs.asyncTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.teamappjobs.appjobs.R;
import com.teamappjobs.appjobs.activity.MinhaVitrineActivity;
import com.teamappjobs.appjobs.modelo.Vitrine;
import com.teamappjobs.appjobs.web.HttpConnection;
import com.teamappjobs.appjobs.web.VitrineJson;

/**
 * Created by Lucas on 17/05/2016.
 */
public class AtivaVitrineTask extends AsyncTask {


    private final String url = "http://www.4runnerapp.com.br/AppJobs/Ctrl/cadastra_vitrine_json.php";
    private final String method = "reativa_vitrine-json";
    private ProgressDialog progress;

    private Context context;
    private Vitrine vitrine;
    private Fragment fragment;

    public AtivaVitrineTask(Context context, Vitrine vitrine) {
        this.context = context;
        this.vitrine = vitrine;
    }

    public AtivaVitrineTask(Context context, Vitrine vitrine, Fragment fragment) {
        this.context = context;
        this.vitrine = vitrine;
        this.fragment = fragment;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = ProgressDialog.show(context, "Aguarde...", "Envio de dados para web", true, true);
    }

    @Override
    protected Object doInBackground(Object[] params) {

        VitrineJson json = new VitrineJson();
        String jsonVitrine = json.VitrineToJson(vitrine);

        String answer = HttpConnection.getSetDataWeb(this.url, this.method, jsonVitrine);
        Log.i("RespInatVitrine", answer);
        return answer;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        progress.dismiss();

        if (o.equals("Sucesso")) {
            Toast.makeText(context, R.string.vitrineAtivada, Toast.LENGTH_SHORT).show();

            vitrine.setSituacao("ativa");
            Intent intent = new Intent(context, MinhaVitrineActivity.class);
            intent.putExtra("vitrine", vitrine);
            ((Activity) context).finish();
            context.startActivity(intent);
        } else {
            Toast.makeText(context, R.string.opserro, Toast.LENGTH_SHORT).show();
        }

    }


}
