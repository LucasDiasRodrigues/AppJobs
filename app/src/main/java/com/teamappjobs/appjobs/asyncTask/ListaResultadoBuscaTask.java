package com.teamappjobs.appjobs.asyncTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.teamappjobs.appjobs.activity.BuscaActivity;
import com.teamappjobs.appjobs.activity.MainActivity;
import com.teamappjobs.appjobs.modelo.Vitrine;
import com.teamappjobs.appjobs.web.HttpConnection;
import com.teamappjobs.appjobs.web.PesquisaJson;
import com.teamappjobs.appjobs.web.VitrineJson;

import java.util.List;


/**
 * Created by Mônica on 21/07/2016.
 */
public class ListaResultadoBuscaTask extends AsyncTask {

    private final String url = "http://www.4runnerapp.com.br/AppJobs/Ctrl/pesquisa_json.php";
    private String method = "";
    private ProgressDialog progress;

    private Activity activity;
    private String palavras;

    private int codCategoria = 0;
    private boolean isByCategoria;

    public ListaResultadoBuscaTask(Activity activity, String palavras) {
        this.activity = activity;
        this.palavras = palavras;
    }

    //Por Categoria
    public ListaResultadoBuscaTask(Activity activity, int codCategoria) {
        this.activity = activity;
        this.codCategoria = codCategoria;
        this.isByCategoria = true;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = ProgressDialog.show(activity, "Pesquisando...", "", true, true);
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        String answer;

        String data;
        if (!isByCategoria) {
            method = "lista_resultado_pesquisa-json";
            PesquisaJson json = new PesquisaJson();
            data = json.PesquisaToJson(palavras.trim());
        } else {
            method = "lista_resultado_pesquisa_categoria-json";
            PesquisaJson json = new PesquisaJson();
            data = json.PesquisaCatToJson(codCategoria);
        }

        answer = HttpConnection.getSetDataWeb(this.url, this.method, data);
        Log.i("RespostaPopulares", answer);


        VitrineJson vitrineJson = new VitrineJson();
        List<Vitrine> vitrines = vitrineJson.JsonArrayToListaVitrines(answer);

        return vitrines;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        progress.dismiss();

        if (activity instanceof MainActivity) {
            ((MainActivity) activity).RecebeLista((List<Vitrine>) o);
        }

        if (activity instanceof BuscaActivity) {
            ((BuscaActivity) activity).RecebeLista((List<Vitrine>) o);
        }
    }
}
