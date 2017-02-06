package com.teamappjobs.appjobs.asyncTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;


import com.teamappjobs.appjobs.R;
import com.teamappjobs.appjobs.activity.ConfiguracaoContaActivity;
import com.teamappjobs.appjobs.modelo.Usuario;
import com.teamappjobs.appjobs.web.HttpConnection;
import com.teamappjobs.appjobs.web.UsuarioJson;


public class ConfiguraContaTask extends AsyncTask <Object, String, String> {
    private final String url = "http://www.4runnerapp.com.br/AppJobs/Ctrl/configura_conta_json.php";
    private String method;
    private String data;

    private Context context;
    private Usuario usuario;


    private ProgressDialog progress;

    public ConfiguraContaTask(Context context, Usuario usuario, String metodo){
        this.context = context;
        this.usuario = usuario;

        switch (metodo){
            case "alterarSenha":
                method = "altera_senha-json";
                break;

        }
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progress = ProgressDialog.show(context, "Aguarde...", "Envio de dados para web", true, true);

    }


    @Override
    protected String doInBackground(Object... params) {



            UsuarioJson usuarioJson = new UsuarioJson();
            data = usuarioJson.UsuarioToJson(usuario);


        String answer = HttpConnection.getSetDataWeb(url, method, data);
        Log.i("HttpConn", answer);


        return answer;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        progress.dismiss();

        switch (s){
            case "senhasucesso":
                Toast.makeText(context, context.getText(R.string.senhaalterada),Toast.LENGTH_SHORT).show();
                ((ConfiguracaoContaActivity)context).limparCampos();
                break;
            case "senhaincorreta":
                Toast.makeText(context, context.getText(R.string.senhaincorreta),Toast.LENGTH_SHORT).show();
                ((ConfiguracaoContaActivity)context).senhaincorreta();
                break;

            default:
                Toast.makeText(context, context.getText(R.string.opserro),Toast.LENGTH_SHORT).show();
                break;
        }



    }
}
