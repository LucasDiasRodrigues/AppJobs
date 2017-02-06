package com.teamappjobs.appjobs.asyncTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.teamappjobs.appjobs.R;
import com.teamappjobs.appjobs.activity.MinhaVitrineActivity;
import com.teamappjobs.appjobs.fragment.MinhaVitrinePromocoesFragment;
import com.teamappjobs.appjobs.modelo.Promocao;
import com.teamappjobs.appjobs.modelo.Vitrine;
import com.teamappjobs.appjobs.web.HttpConnection;
import com.teamappjobs.appjobs.web.VitrineJson;

/**
 * Created by Lucas on 17/05/2016.
 */
public class AtivaPromocaoTask extends AsyncTask {


    private final String url = "http://www.4runnerapp.com.br/AppJobs/Ctrl/cadastra_promocao_json.php";
    private final String method = "reativa_promocao-json";
    private ProgressDialog progress;

    private Context context;
    private Promocao promocao;
    private Fragment fragment;

    public AtivaPromocaoTask(Context context, Promocao promocao) {
        this.context = context;
        this.promocao = promocao;
    }

    public AtivaPromocaoTask(Context context, Promocao promocao, Fragment fragment) {
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
        String jsonVitrine = json.PromocaoToJson(promocao);

        String answer = HttpConnection.getSetDataWeb(this.url, this.method, jsonVitrine);
        Log.i("RespInatPromo", answer);
        return answer;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        progress.dismiss();

        if (o.equals("Sucesso")) {
            Toast.makeText(context, R.string.promocaoReativada, Toast.LENGTH_SHORT).show();

          //  promocao.setSituacao("ativa");
            if(fragment != null){
                ((MinhaVitrinePromocoesFragment)fragment).listaPromocoes();
            }



        } else {
            Toast.makeText(context, R.string.opserro, Toast.LENGTH_SHORT).show();
        }

    }


}
