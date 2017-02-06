package com.teamappjobs.appjobs.asyncTask;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.teamappjobs.appjobs.fragment.MinhaVitrinePromocoesFragment;
import com.teamappjobs.appjobs.modelo.Portifolio;
import com.teamappjobs.appjobs.modelo.Promocao;
import com.teamappjobs.appjobs.modelo.Vitrine;
import com.teamappjobs.appjobs.web.HttpConnection;
import com.teamappjobs.appjobs.web.VitrineJson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lucas on 10/05/2016.
 */
public class ListaPromocoesTask extends AsyncTask {
    private final String url = "http://www.4runnerapp.com.br/AppJobs/Ctrl/lista_promocao_json.php";
    private String method = "lista_promocoes_vitrine-json";

    private Activity activity;
    private Fragment fragment;
    private Vitrine vitrine;

    public ListaPromocoesTask(Activity activity, Fragment fragment,  Vitrine vitrine){
        this.activity = activity;
        this.fragment = fragment;
        this.vitrine = vitrine;
    }

    @Override
    protected Object doInBackground(Object[] params) {

        VitrineJson json = new VitrineJson();
        String data = json.VitrineToJson(vitrine);

        String answer = HttpConnection.getSetDataWeb(this.url, this.method, data);
        Log.i("RespostaPortifolio", answer);

        VitrineJson vitrineJson = new VitrineJson();
        ArrayList<Promocao> promocoes = vitrineJson.JsonArrayToListaPromocoes(answer);

        return promocoes;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        if(fragment != null && fragment instanceof MinhaVitrinePromocoesFragment){
            ((MinhaVitrinePromocoesFragment)fragment).atualizaListaPromocoes((List<Promocao>)o);
        }

    }
}
