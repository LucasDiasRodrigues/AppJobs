package com.teamappjobs.appjobs.asyncTask;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.teamappjobs.appjobs.activity.MinhaVitrineActivity;
import com.teamappjobs.appjobs.activity.VitrineActivity;
import com.teamappjobs.appjobs.modelo.Promocao;
import com.teamappjobs.appjobs.modelo.Vitrine;
import com.teamappjobs.appjobs.web.HttpConnection;
import com.teamappjobs.appjobs.web.VitrineJson;

/**
 * Created by Lucas on 12/05/2016.
 */
public class BuscaDetalhesVitrineTask extends AsyncTask {
    private final String url = "http://www.4runnerapp.com.br/AppJobs/Ctrl/lista_vitrines_json.php";
    private String method = "lista_curtidas_seguidores-json";


    private Activity activity;
    private Vitrine vitrine;
    private String tipo = "minhaVitrine";

    public BuscaDetalhesVitrineTask(MinhaVitrineActivity activity, Vitrine vitrine){
        this.vitrine = vitrine;
        this.activity = activity;
    }

    public BuscaDetalhesVitrineTask(VitrineActivity activity, Vitrine vitrine){
        this.vitrine = vitrine;
        this.activity = activity;
    }
    @Override
    protected Object doInBackground(Object[] params) {


        VitrineJson json = new VitrineJson();
        String data = json.VitrineToJson(vitrine);

        String answer = HttpConnection.getSetDataWeb(this.url, this.method, data);
        Log.i("RespDetalhesVitrine", answer);

        VitrineJson vitrineJson = new VitrineJson();
        Bundle bundle = vitrineJson.DetalhesVitrineJsonToDetalhesVitrine(answer);

        return bundle;
    }


    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);


        try {
            ((MinhaVitrineActivity)activity).atualizaDetalhesVitrine((Bundle)o);
        } catch (Throwable e) {
            ((VitrineActivity)activity).atualizaDetalhesVitrine((Bundle)o);
        }


    }
}
