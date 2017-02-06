package com.teamappjobs.appjobs.asyncTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.teamappjobs.appjobs.fragment.MinhaVitrinePortifolioFragment;
import com.teamappjobs.appjobs.fragment.MinhaVitrinePromocoesFragment;
import com.teamappjobs.appjobs.fragment.MinhasVitrinesFragment;
import com.teamappjobs.appjobs.modelo.Portifolio;
import com.teamappjobs.appjobs.modelo.Promocao;
import com.teamappjobs.appjobs.modelo.Usuario;
import com.teamappjobs.appjobs.modelo.Vitrine;
import com.teamappjobs.appjobs.web.HttpConnection;
import com.teamappjobs.appjobs.web.UsuarioJson;
import com.teamappjobs.appjobs.web.VitrineJson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lucas on 04/05/2016.
 */
public class ListaPortifolioTask extends AsyncTask {
    private final String url = "http://www.4runnerapp.com.br/AppJobs/Ctrl/lista_portifolio_json.php";
    private String method = "lista_portifolio_vitrine-json";
    private ProgressDialog progress;

    private Activity activity;
    private MinhaVitrinePortifolioFragment fragment;
    private Vitrine vitrine;

    public ListaPortifolioTask(Activity activity, MinhaVitrinePortifolioFragment fragment, Vitrine vitrine) {
        this.activity = activity;
        this.fragment = fragment;
        this.vitrine = vitrine;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected Object doInBackground(Object[] params) {

        VitrineJson json = new VitrineJson();
        String data = json.VitrineToJson(vitrine);

        String answer = HttpConnection.getSetDataWeb(this.url, this.method, data);
        Log.i("RespostaPortifolio", answer);

        VitrineJson vitrineJson = new VitrineJson();
        ArrayList<Portifolio> portifolio = vitrineJson.JsonArrayToListaPortifolio(answer);

        return portifolio;
    }


    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        if (fragment != null) {
            fragment.atualizaListaPortifolio((ArrayList<Portifolio>) o);
        }


    }
}
