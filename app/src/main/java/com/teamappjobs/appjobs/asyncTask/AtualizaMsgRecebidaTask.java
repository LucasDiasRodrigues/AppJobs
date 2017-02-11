package com.teamappjobs.appjobs.asyncTask;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.teamappjobs.appjobs.modelo.Usuario;
import com.teamappjobs.appjobs.web.HttpConnection;
import com.teamappjobs.appjobs.web.UsuarioJson;


public class AtualizaMsgRecebidaTask extends AsyncTask {


    private final String url = "http://www.4runnerapp.com.br/AppJobs/Ctrl/chat_json.php";
    private final String method = "AtualizaMsgRecebida-json";

    private Context context;
    private String email;
    private String origem;
    private boolean chatAtivo;

    private String data;

    public AtualizaMsgRecebidaTask(Context context, String email, String origem, boolean chatAtivo) {
        this.context = context;
        this.email = email;
        this.origem = origem;
        this.chatAtivo = chatAtivo;
    }


    @Override
    protected Object doInBackground(Object[] params) {



            Usuario usuario = new Usuario();
            usuario.setEmail(email);

            UsuarioJson json = new UsuarioJson();
            data = json.UsuarioToJson(usuario);

            String answer = HttpConnection.getSetDataWeb(this.url, this.method, this.data);

            Log.i("consultaMensagens", answer);


            return null;




    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        if (chatAtivo) {
            Intent intent = new Intent("chat");
            //send broadcast
            context.sendBroadcast(intent);


        }

    }
}
