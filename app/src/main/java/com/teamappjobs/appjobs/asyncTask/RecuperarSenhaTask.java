package com.teamappjobs.appjobs.asyncTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.teamappjobs.appjobs.fragment.RecuperarSenhaFragment;
import com.teamappjobs.appjobs.web.HttpConnection;
import com.teamappjobs.appjobs.web.LoginJson;

/**
 * Created by Lucas on 28/04/2016.
 */
public class RecuperarSenhaTask extends AsyncTask {
    private final String url = "http://www.4runnerapp.com.br/AppJobs/Ctrl/mail_json.php";
    private final String method = "recupera_senha-json";

    private String email;
    private String data;
    private Activity activity;
    private Fragment fragment;

    private ProgressDialog progress;

    public RecuperarSenhaTask(Activity activity, Fragment fragment, String email, String data){
        this.activity = activity;
        this.fragment = fragment;
        this.email = email;
        this.data = data;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = ProgressDialog.show(activity,"Aguarde","enviando dados...", true, true);
    }

    @Override
    protected Object doInBackground(Object[] params) {

        LoginJson json = new LoginJson();

        String dados = json.loginToJson(email,data,"");

        String answer = HttpConnection.getSetDataWeb(this.url, this.method, dados);

        Log.i("Resposta ===", answer);


        return answer;

    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        progress.dismiss();

        if (((String)o).equals("sucesso")){

            ((RecuperarSenhaFragment)fragment).emailEnviado();


        } else if(((String)o).equals("dadosInvalidos")){

            ((RecuperarSenhaFragment)fragment).dadosInvalidaos();

        }

    }
}
