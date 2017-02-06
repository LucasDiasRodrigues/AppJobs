package com.teamappjobs.appjobs.asyncTask;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.teamappjobs.appjobs.activity.CadastroVitrineActivity;
import com.teamappjobs.appjobs.fragment.MinhasVitrinesFragment;
import com.teamappjobs.appjobs.modelo.Categoria;
import com.teamappjobs.appjobs.modelo.Usuario;
import com.teamappjobs.appjobs.modelo.Vitrine;
import com.teamappjobs.appjobs.web.HttpConnection;
import com.teamappjobs.appjobs.web.UsuarioJson;
import com.teamappjobs.appjobs.web.VitrineJson;

import java.util.List;


public class ListaVitrinesTask extends AsyncTask {

    private final String url = "http://www.4runnerapp.com.br/AppJobs/Ctrl/lista_vitrines_json.php";
    private String method = "";
    private ProgressDialog progress;

    private Activity activity;
    private MinhasVitrinesFragment fragment;
    private String tipo;
    private String email;


    public ListaVitrinesTask(Activity activity, MinhasVitrinesFragment fragment, String tipo, String email) {
        this.activity = activity;
        this.fragment = fragment;
        this.tipo = tipo;
        this.email = email;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progress = ProgressDialog.show(activity, "Aguarde...", "", true, true);

    }

    @Override
    protected Object doInBackground(Object[] params) {
        String answer = "";
        Usuario usuario = new Usuario();
        UsuarioJson json = new UsuarioJson();

        switch (tipo) {
            case "minhasVitrines":
                method = "lista_minhas_vitrines-json";


                usuario.setEmail(email);

                String data = json.UsuarioToJson(usuario);

                answer = HttpConnection.getSetDataWeb(this.url, this.method, data);
                Log.i("RespostaMinhasVitrines", answer);
                break;

            default:
                break;
        }

        VitrineJson vitrineJson = new VitrineJson();
        List<Vitrine> vitrines = vitrineJson.JsonArrayToListaVitrines(answer);

        return vitrines;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        progress.dismiss();

        switch (tipo) {
            case "minhasVitrines":

                fragment.atualizaListaVitrines((List<Vitrine>) o);
                break;
            default:
                break;
        }

    }
}
