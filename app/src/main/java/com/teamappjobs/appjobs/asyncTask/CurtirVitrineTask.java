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
public class CurtirVitrineTask extends AsyncTask {


    private final String url = "http://www.4runnerapp.com.br/AppJobs/Ctrl/curtir_seguir_vitrine_json.php";

    private ProgressDialog progress;

    private Context context;
    private  String  method;
    private String usuario;
    private int codVitrine;
    //Criar um if para o nome do metodo;
    public CurtirVitrineTask(Context context, String usuario, int codVitrine, String metodo) {
        this.context = context;
        this.method= metodo;
        this.usuario=usuario;
        this.codVitrine=codVitrine;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = ProgressDialog.show(context, "Aguarde...", "Envio de dados para web", true, true);
    }

    @Override
    protected Object doInBackground(Object[] params) {

        VitrineJson json = new VitrineJson();
        String jsonVitrine = json.CurtirVitrineToJson(codVitrine,usuario);

        String answer = HttpConnection.getSetDataWeb(this.url, this.method, jsonVitrine);
        Log.i("RespInatVitrine", answer);
        return answer;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        progress.dismiss();

        if (o.equals("Sucesso")) {

        } else {
            Toast.makeText(context, R.string.opserro, Toast.LENGTH_SHORT).show();
        }

    }


}
