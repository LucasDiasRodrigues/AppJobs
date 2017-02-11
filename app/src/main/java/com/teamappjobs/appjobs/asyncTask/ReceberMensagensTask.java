package com.teamappjobs.appjobs.asyncTask;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.teamappjobs.appjobs.activity.ChatActivity;
import com.teamappjobs.appjobs.modelo.Mensagem;
import com.teamappjobs.appjobs.web.HttpConnection;
import com.teamappjobs.appjobs.web.MensagemJson;

import java.util.List;


public class ReceberMensagensTask extends AsyncTask {

    private final String url = "http://www.4runnerapp.com.br/AppJobs/Ctrl/chat_json.php";
    private final String method = "SolicitaMensagensNew-json";

    private String emailRemetente;
    private String emailDestinatario;
    private String leitor;
    private Context context;

    private int page = 1;

    List<Mensagem> mensagens;


    //OLD
    public ReceberMensagensTask(String emailRemetente, String emailDestinatario, Context context){
        this.emailRemetente = emailRemetente;
        this.emailDestinatario = emailDestinatario;
        this.context = context;
    }

    public ReceberMensagensTask(String emailRemetente, String emailDestinatario, Context context, int page){
        this.emailRemetente = emailDestinatario;
        this.emailDestinatario = emailRemetente;
        this.context = context;
        this.page = page;
    }

    @Override
    protected Object doInBackground(Object[] params) {

        SharedPreferences prefs = context.getSharedPreferences("Configuracoes", Context.MODE_PRIVATE);
        leitor = prefs.getString("email","");

        MensagemJson mensagemJson = new MensagemJson();
        String data = mensagemJson.SolicitaMensagem(emailRemetente, emailDestinatario,leitor, page);

        String answer = HttpConnection.getSetDataWeb(this.url, this.method, data);

        Log.i("testeMensagem",answer);

        mensagens = mensagemJson.JsonArrayToListaMensagem(answer);


        return mensagens;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        if(page > 1)
            ((ChatActivity)context).updateRecyclerView(mensagens);
        else
            ((ChatActivity)context).atualizarTela(mensagens);


    }
}
