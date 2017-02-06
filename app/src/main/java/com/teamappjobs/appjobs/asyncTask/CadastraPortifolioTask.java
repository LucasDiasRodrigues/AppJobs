package com.teamappjobs.appjobs.asyncTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;

import com.teamappjobs.appjobs.R;
import com.teamappjobs.appjobs.modelo.Portifolio;
import com.teamappjobs.appjobs.web.HttpConnection;
import com.teamappjobs.appjobs.web.VitrineJson;

import java.io.ByteArrayOutputStream;

/**
 * Created by Lucas on 05/05/2016.
 */
public class CadastraPortifolioTask extends AsyncTask {


    private final String url = "http://www.4runnerapp.com.br/AppJobs/Ctrl/cadastra_portifolio_json.php";
    private final String method = "cadastra_portifolio-json";


    private ProgressDialog progress;

    private Activity activity;
    private Portifolio portifolio;
    private Bitmap imagem;
    private String imagemDecodificada = "";

    public CadastraPortifolioTask(Activity activity, Portifolio portifolio, Bitmap imagem) {
        this.activity = activity;
        this.portifolio = portifolio;
        this.imagem = imagem;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = ProgressDialog.show(activity, "Aguarde...", "Envio de dados para web", true, true);
    }


    @Override
    protected Object doInBackground(Object[] params) {

        //Tratar ImageViwew
        //localiza e transforma em um array de bytes
        if (imagem != null) {
            ByteArrayOutputStream bAOS = new ByteArrayOutputStream();
            imagem.compress(Bitmap.CompressFormat.JPEG, 50, bAOS);
            byte[] imagemArrayBytes = bAOS.toByteArray();

            //decodifica com a classe BASE64 e transforma em string
            imagemDecodificada = Base64.encodeToString(imagemArrayBytes, Base64.DEFAULT);
            portifolio.setImagem(imagemDecodificada);
        } else {
            portifolio.setImagem("");
        }

        VitrineJson json = new VitrineJson();
        String jsonPortifolio = json.PortifolioToJson(portifolio);

        String answer = HttpConnection.getSetDataWeb(this.url, this.method, jsonPortifolio);
        Log.i("RespCadVitrine",answer);
        return answer;

    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        progress.dismiss();

        Log.i("resultado Json", o.toString());

        if (o.toString().equals("Sucesso")) {

            new AlertDialog.Builder(activity).setTitle(R.string.sucesso)
                    .setMessage(R.string.cadastrado)
                    .setCancelable(false)
                    .setPositiveButton(R.string.continuar, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            activity.finish();
                        }
                    })
                    .show();
        }else{

        }



    }
}
