package com.teamappjobs.appjobs.asyncTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.teamappjobs.appjobs.fragment.MinhasVitrinesFragment;
import com.teamappjobs.appjobs.fragment.VitrinesSigoFragment;
import com.teamappjobs.appjobs.modelo.Promocao;
import com.teamappjobs.appjobs.modelo.Usuario;
import com.teamappjobs.appjobs.modelo.Vitrine;
import com.teamappjobs.appjobs.web.HttpConnection;
import com.teamappjobs.appjobs.web.UsuarioJson;
import com.teamappjobs.appjobs.web.VitrineJson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ListaVitrinesSigoTask extends AsyncTask {

    private final String url = "http://www.4runnerapp.com.br/AppJobs/Ctrl/lista_vitrines_json.php";
    private final String url2="http://www.4runnerapp.com.br/AppJobs/Ctrl/lista_promocao_json.php";
    private String method = "";
    private String method2 = "";
    private ProgressDialog progress;

    private Activity activity;
    private VitrinesSigoFragment fragment;
    private String tipo;
    private String email;

    Map<String, List> mapSigo = new HashMap<String, List>();
    List[] lista;

    public ListaVitrinesSigoTask(Activity activity, VitrinesSigoFragment fragment, String tipo, String email) {
        this.activity = activity;
        this.fragment = fragment;
        this.tipo = tipo;
        this.email = email;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected Object doInBackground(Object[] params) {
        String answer = "";
        String answer2 = "";
        Usuario usuario = new Usuario();
        UsuarioJson json = new UsuarioJson();

        switch (tipo) {
            case "vitrinesSigo":
                method = "lista_vitrines_sigo-json";
                method2="lista_promocoes_sigo-json";
                usuario.setEmail(email);
                String data = json.UsuarioToJson(usuario);
                answer2 =HttpConnection.getSetDataWeb(this.url2, this.method2, data);
                Log.i("RespostaMinhasPromocoes", answer2);
                answer = HttpConnection.getSetDataWeb(this.url, this.method, data);
                Log.i("RespostaMinhasVitrines", answer);

                break;

            default:
                break;
        }

        VitrineJson vitrineJson = new VitrineJson();
        List<Promocao> promocoes =  vitrineJson.JsonArrayToListaPromocoes(answer2);
        List<Vitrine> vitrines = vitrineJson.JsonArrayToListaVitrines(answer);



        mapSigo.put("promocoes", promocoes);
        mapSigo.put("vitrines", vitrines);

        lista = new List[] { promocoes, vitrines };

        return lista;

    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        switch (tipo) {
            case "vitrinesSigo":
                fragment.atualizaListaVitrines(lista[0],lista[1]);
                break;
            default:
                break;
        }

    }
}
