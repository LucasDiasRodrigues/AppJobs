package com.teamappjobs.appjobs.asyncTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.teamappjobs.appjobs.R;
import com.teamappjobs.appjobs.activity.MainActivity;
import com.teamappjobs.appjobs.modelo.Usuario;
import com.teamappjobs.appjobs.web.HttpConnection;
import com.teamappjobs.appjobs.web.UsuarioJson;


public class DesativaContaTask extends AsyncTask <Object,String,String>{
    private final String url = "http://www.4runnerapp.com.br/AppJobs/Ctrl/desativa_conta_json.php";
    private String method;
    private String data;

    private Usuario usuario;
    private Context context;


    private ProgressDialog progress;

    public DesativaContaTask(Usuario usuario, Context context){
        this.usuario = usuario;
        this.context = context;
        method = "desativa_conta-json";

    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progress = ProgressDialog.show(context, "Aguarde...", "Envio de dados para web", true, true);

    }

    @Override
    protected String doInBackground(Object[] params) {



            UsuarioJson usuarioJson = new UsuarioJson();
            data = usuarioJson.UsuarioToJson(usuario);



        String answer = HttpConnection.getSetDataWeb(url, method, data);
        Log.i("HttpConn", answer);


        return answer;
    }

    @Override
    protected void onPostExecute(String o) {
        super.onPostExecute(o);
        progress.dismiss();

        if(o.equals("sucesso")){
            Toast.makeText(context, R.string.contadesativada,Toast.LENGTH_SHORT);

            SharedPreferences prefs = context.getSharedPreferences("Configuracoes", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.commit();
            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);

        } else {
            Toast.makeText(context, R.string.opserro,Toast.LENGTH_SHORT);
        }


    }
}
