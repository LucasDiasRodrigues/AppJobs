package com.teamappjobs.appjobs.asyncTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.teamappjobs.appjobs.R;
import com.teamappjobs.appjobs.fragment.MinhaVitrinePromocoesFragment;
import com.teamappjobs.appjobs.modelo.Promocao;
import com.teamappjobs.appjobs.modelo.Vitrine;
import com.teamappjobs.appjobs.web.HttpConnection;
import com.teamappjobs.appjobs.web.VitrineJson;

/**
 * Created by Lucas on 17/05/2016.
 */
public class InativaVitrineTask extends AsyncTask {

    private final String url = "http://www.4runnerapp.com.br/AppJobs/Ctrl/cadastra_vitrine_json.php";
    private final String method = "cancela_vitrine-json";
    private ProgressDialog progress;

    private Context context;
    private Vitrine vitrine;
    private Fragment fragment;

    public InativaVitrineTask(Context context, Vitrine vitrine) {
        this.context = context;
        this.vitrine = vitrine;
    }

    public InativaVitrineTask(Context context, Vitrine vitrine, Fragment fragment) {
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
            Toast.makeText(context, R.string.vitrineInativada, Toast.LENGTH_SHORT).show();
            ((Activity)context).finish();

        } else {

            Toast.makeText(context, R.string.opserro, Toast.LENGTH_SHORT).show();
        }

    }
}
