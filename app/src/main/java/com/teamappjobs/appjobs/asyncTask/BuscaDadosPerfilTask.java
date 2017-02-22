package com.teamappjobs.appjobs.asyncTask;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.teamappjobs.appjobs.activity.CadastroUsuarioActivity;
import com.teamappjobs.appjobs.activity.MainActivity;
import com.teamappjobs.appjobs.activity.MinhaVitrineActivity;
import com.teamappjobs.appjobs.activity.VitrineActivity;
import com.teamappjobs.appjobs.modelo.Usuario;
import com.teamappjobs.appjobs.modelo.Vitrine;
import com.teamappjobs.appjobs.web.HttpConnection;
import com.teamappjobs.appjobs.web.UsuarioJson;
import com.teamappjobs.appjobs.web.VitrineJson;

public class BuscaDadosPerfilTask extends AsyncTask {
    private final String url = "http://www.4runnerapp.com.br/AppJobs/Ctrl/busca_dados_usuario_json.php";
    private String method = "busca_dados-json";


    private Activity activity;
    private Usuario usuario;
    private String tipo = "minhaVitrine";


    public BuscaDadosPerfilTask(CadastroUsuarioActivity activity, Usuario usuario) {
        this.usuario = usuario;
        this.activity = activity;
    }

    public BuscaDadosPerfilTask(MainActivity activity, Usuario usuario) {
        this.usuario = usuario;
        this.activity = activity;
    }

    @Override
    protected Object doInBackground(Object[] params) {


        UsuarioJson json = new UsuarioJson();
        String data = json.UsuarioToJson(usuario);

        String answer = HttpConnection.getSetDataWeb(this.url, this.method, data);
        Log.i("RespDadosUsuario", answer);

        UsuarioJson usuarioJson = new UsuarioJson();
        Usuario usuarioresp = usuarioJson.JsonToUsuario(answer);

        return usuarioresp;
    }


    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        Usuario usuarioresp = (Usuario) o;
        if (activity instanceof CadastroUsuarioActivity) {
            ((CadastroUsuarioActivity) activity).PreencheCampos(usuarioresp);
        } else if (activity instanceof MainActivity) {
            ((MainActivity) activity).recebeDadosPerfil(usuarioresp);
        }
    }
}
