package com.teamappjobs.appjobs.asyncTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.teamappjobs.appjobs.activity.CadastroVitrineActivity;
import com.teamappjobs.appjobs.modelo.Categoria;
import com.teamappjobs.appjobs.web.CategoriaJson;
import com.teamappjobs.appjobs.web.HttpConnection;

import java.util.List;

/**
 * Created by Lucas on 27/04/2016.
 */
public class ListaCategoriasTask extends AsyncTask {

    private final String url = "http://www.4runnerapp.com.br/AppJobs/Ctrl/consulta_categorias_json.php";
    private final String method = "consulta_categorias-json";

    private ProgressDialog progress;

    private Context context;

    public ListaCategoriasTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = ProgressDialog.show(context, "Aguarde...", "Envio de dados para web", true, true);
    }

    @Override
    protected Object doInBackground(Object[] params) {

        String answer = HttpConnection.getSetDataWeb(this.url, this.method, "");
        Log.i("RespostaCategorias", answer);

        CategoriaJson json = new CategoriaJson();
        List<Categoria> categorias = json.JsonArrayToListaCategorias(answer);


        return categorias;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        progress.dismiss();

        try {
            List<Categoria> categorias = (List<Categoria>) o;
            ((CadastroVitrineActivity) context).AtualizaListaCategoria(categorias);

        } catch (Exception ex) {
            Log.i("erro", ex.toString());
        }


    }
}
