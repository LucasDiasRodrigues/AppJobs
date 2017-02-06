package com.teamappjobs.appjobs.asyncTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.teamappjobs.appjobs.R;
import com.teamappjobs.appjobs.fragment.MinhaVitrinePromocoesFragment;
import com.teamappjobs.appjobs.modelo.Promocao;
import com.teamappjobs.appjobs.web.HttpConnection;
import com.teamappjobs.appjobs.web.VitrineJson;

/**
 * Created by Lucas on 12/05/2016.
 */
public class CancelaPromocaoTask extends AsyncTask {
    private final String url = "http://www.4runnerapp.com.br/AppJobs/Ctrl/cadastra_promocao_json.php";
    private final String method = "cancela_promocao-json";
    private ProgressDialog progress;

    private Context context;
    private Promocao promocao;
    private Fragment fragment;

    public CancelaPromocaoTask(Context context, Promocao promocao) {
        this.context = context;
        this.promocao = promocao;
    }

    public CancelaPromocaoTask(Context context, Promocao promocao, Fragment fragment) {
        this.context = context;
        this.promocao = promocao;
        this.fragment = fragment;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = ProgressDialog.show(context, "Aguarde...", "Envio de dados para web", true, true);
    }

    @Override
    protected Object doInBackground(Object[] params) {

        VitrineJson json = new VitrineJson();
        String jsonPromocao = json.PromocaoToJson(promocao);

        String answer = HttpConnection.getSetDataWeb(this.url, this.method, jsonPromocao);
        Log.i("RespCancPromocao", answer);
        return answer;
    }


    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        progress.dismiss();

        if(o.equals("Sucesso")){

            Toast.makeText(context, R.string.promoCancelada,Toast.LENGTH_SHORT).show();
            if(fragment != null){
                ((MinhaVitrinePromocoesFragment)fragment).listaPromocoes();
            }

        } else {

            Toast.makeText(context,R.string.opserro,Toast.LENGTH_SHORT).show();
        }



    }
}
