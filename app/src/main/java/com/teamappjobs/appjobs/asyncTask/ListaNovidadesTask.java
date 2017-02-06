package com.teamappjobs.appjobs.asyncTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.teamappjobs.appjobs.fragment.HomeHomeFragment;
import com.teamappjobs.appjobs.modelo.Usuario;
import com.teamappjobs.appjobs.modelo.Vitrine;
import com.teamappjobs.appjobs.web.HttpConnection;
import com.teamappjobs.appjobs.web.UsuarioJson;
import com.teamappjobs.appjobs.web.VitrineJson;

import java.util.List;


/**
 * Created by Mônica on 21/07/2016.
 */
public class ListaNovidadesTask extends AsyncTask {

    private final String url = "http://www.4runnerapp.com.br/AppJobs/Ctrl/lista_novas_vitrines_json.php";
    private String method = "";
    private ProgressDialog progress;

    private Activity activity;
    private HomeHomeFragment fragment;

    public ListaNovidadesTask(Activity activity, HomeHomeFragment fragment) {
        this.activity = activity;
        this.fragment = fragment;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = ProgressDialog.show(activity, "Carregando...", "", true, true);
    }



    @Override
    protected Object doInBackground(Object[] objects) {
        String answer = "";

                method = "lista_todas_vitrines-json";

                Usuario usuario = new Usuario();
                UsuarioJson json = new UsuarioJson();
                String data = json.UsuarioToJson(usuario);

                answer = HttpConnection.getSetDataWeb(this.url, this.method, data);
                Log.i("RespostaNovidades", answer);



          VitrineJson vitrineJson = new VitrineJson();
          List<Vitrine> vitrines = vitrineJson.JsonArrayToListaVitrines(answer);

        return vitrines;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        progress.dismiss();

                fragment.mostraListaNovidades((List<Vitrine>) o);



    }


}
