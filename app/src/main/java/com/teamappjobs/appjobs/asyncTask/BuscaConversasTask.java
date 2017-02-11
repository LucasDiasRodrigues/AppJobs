package com.teamappjobs.appjobs.asyncTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.teamappjobs.appjobs.fragment.ConversasFragment;
import com.teamappjobs.appjobs.modelo.Usuario;
import com.teamappjobs.appjobs.web.HttpConnection;
import com.teamappjobs.appjobs.web.MensagemJson;
import com.teamappjobs.appjobs.web.UsuarioJson;

import java.util.List;


public class BuscaConversasTask extends AsyncTask<Object, Object, Object> {
    private final String url = "http://www.4runnerapp.com.br/AppJobs/Ctrl/chat_json.php";
    private String method;
    private String data;
    private Activity activity;
    private Usuario usuario;
    private Fragment fragment;

    public BuscaConversasTask(Activity activity, Usuario usuario, Fragment fragment) {
        this.activity = activity;
        this.usuario = usuario;
        this.fragment = fragment;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<Bundle> doInBackground(Object... params) {
        UsuarioJson usuarioJson = new UsuarioJson();
        data = usuarioJson.UsuarioToJson(usuario);
        method = "busca_conversas-json";

        String answer = HttpConnection.getSetDataWeb(this.url, this.method, this.data);
        Log.i("Resposta ===", answer);
        MensagemJson msgJson = new MensagemJson();
        List<Bundle> bundles = msgJson.JsonToListaConversas(answer);

        return bundles;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        if(fragment instanceof ConversasFragment){
            ((ConversasFragment) fragment).atualizaLista((List<Bundle>)o);
        }
    }
}
