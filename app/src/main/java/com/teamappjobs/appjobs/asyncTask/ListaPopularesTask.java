package com.teamappjobs.appjobs.asyncTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.teamappjobs.appjobs.fragment.HomeHomeFragment;
import com.teamappjobs.appjobs.fragment.HomePopularesFragment;
import com.teamappjobs.appjobs.modelo.Usuario;
import com.teamappjobs.appjobs.modelo.Vitrine;
import com.teamappjobs.appjobs.web.HttpConnection;
import com.teamappjobs.appjobs.web.UsuarioJson;
import com.teamappjobs.appjobs.web.VitrineJson;

import java.util.List;


/**
 * Created by Mônica on 21/07/2016.
 */
public class ListaPopularesTask extends AsyncTask {

    private final String url = "http://www.4runnerapp.com.br/AppJobs/Ctrl/lista_vitrines_populares_json.php";
    private String method = "";
    private ProgressDialog progress;
    private int page = 1;
    private Activity activity;
    private HomePopularesFragment fragment;
    List<Vitrine> vitrines;

    public ListaPopularesTask(Activity activity, HomePopularesFragment fragment, int page) {
        this.activity = activity;
        this.fragment = fragment;
        this.page=page;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected Object doInBackground(Object[] objects) {
        String answer = "";

        method = "lista_todas_vitrines_populares-json";

        Usuario usuario = new Usuario();
        UsuarioJson json = new UsuarioJson();
        String data = json.UsuarioToJson(usuario,page);
        answer = HttpConnection.getSetDataWeb(this.url, this.method, data);
        Log.i("RespostaPopulares", answer);

        VitrineJson vitrineJson = new VitrineJson();
        vitrines = vitrineJson.JsonArrayToListaVitrines(answer);

        return vitrines;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        if(page>1){
            fragment.updateRecyclerView(vitrines);
        }else{
            fragment.updateScreen(vitrines);
        }
    }
}
