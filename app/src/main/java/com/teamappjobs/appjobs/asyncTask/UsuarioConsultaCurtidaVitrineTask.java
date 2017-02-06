package com.teamappjobs.appjobs.asyncTask;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.teamappjobs.appjobs.activity.MinhaVitrineActivity;
import com.teamappjobs.appjobs.activity.VitrineActivity;
import com.teamappjobs.appjobs.modelo.Vitrine;
import com.teamappjobs.appjobs.web.HttpConnection;
import com.teamappjobs.appjobs.web.VitrineJson;

/**
 * Created by Lucas on 12/05/2016.
 */
public class UsuarioConsultaCurtidaVitrineTask extends AsyncTask {
    private final String url = "http://www.4runnerapp.com.br/AppJobs/Ctrl/curtir_seguir_vitrine_json.php";
    private String method = "verifica_curtida_vitrine-json";


    private Activity activity;
    private int codVitrine;
    private String usuario;
    private String tipo = "minhaVitrine";

    public UsuarioConsultaCurtidaVitrineTask(VitrineActivity activity, int codVitrine,String usuario){
        this.codVitrine = codVitrine;
        this.usuario=usuario;
        this.activity = activity;

    }


    @Override
    protected Object doInBackground(Object[] params) {


        VitrineJson json = new VitrineJson();
        String data = json.CurtirVitrineToJson(codVitrine, usuario);

        String answer = HttpConnection.getSetDataWeb(this.url, this.method, data);
        Log.i("Resp", answer);

        VitrineJson vitrineJson = new VitrineJson();

        Bundle bundle = vitrineJson.VerificaCurtidaVitrineJsonToDetalhesVitrine(answer);

        return bundle;
    }


    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);


        try {
            ((VitrineActivity)activity).atualizaCurtidaAtual((Bundle)o);
        } catch (Throwable e) {
          Log.e("","Erro:",e);
        }


    }
}
