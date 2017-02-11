package com.teamappjobs.appjobs.asyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.teamappjobs.appjobs.activity.ChatActivity;
import com.teamappjobs.appjobs.modelo.Mensagem;
import com.teamappjobs.appjobs.web.HttpConnection;
import com.teamappjobs.appjobs.web.MensagemJson;

import java.util.Calendar;


public class EnviaMensagemTask extends AsyncTask {

    private final String url = "http://www.4runnerapp.com.br/AppJobs/Ctrl/chat_json.php";
    private final String method = "EnviaMensagem-json";

    private String emailRemetente;
    private String emailDestinatario;
    private String msg;
    private Context context;

    private String data;


    public EnviaMensagemTask(String emailRemetente, String emailDestinatario, String msg, Context context){
        this.emailRemetente = emailRemetente;
        this.emailDestinatario = emailDestinatario;
        this.msg = msg;
        this.context = context;
    }


    @Override
    protected Object doInBackground(Object[] params) {

        Mensagem mensagem = new Mensagem();
        mensagem.setRemetente(emailRemetente);
        mensagem.setDestinatario(emailDestinatario);
        mensagem.setMsg(msg);
        mensagem.setDthrEnviada(Calendar.getInstance().getTime());

        MensagemJson json = new MensagemJson();
        data = json.MensagemToJson(mensagem);

        String answer = HttpConnection.getSetDataWeb(this.url, this.method, data);

        Log.i("testechat", answer);

        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        ((ChatActivity)context).receberMensagens();


    }
}
