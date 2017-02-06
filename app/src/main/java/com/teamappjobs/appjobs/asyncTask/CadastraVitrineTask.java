package com.teamappjobs.appjobs.asyncTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;

import com.teamappjobs.appjobs.R;
import com.teamappjobs.appjobs.activity.CadastroVitrineActivity;
import com.teamappjobs.appjobs.activity.MainActivity;
import com.teamappjobs.appjobs.modelo.Vitrine;
import com.teamappjobs.appjobs.web.HttpConnection;
import com.teamappjobs.appjobs.web.VitrineJson;

import java.io.ByteArrayOutputStream;

/**
 * Created by Lucas on 27/04/2016.
 */
public class CadastraVitrineTask extends AsyncTask {

    private final String url = "http://www.4runnerapp.com.br/AppJobs/Ctrl/cadastra_vitrine_json.php";
    private final String method = "cadastra_vitrine-json";

    private Bundle usuarioBundle;
    private ProgressDialog progress;

    private Activity activity;
    private Vitrine vitrine;
    private Bitmap imagemPerfil;
    private String imagemDecodificada = "";

    public CadastraVitrineTask(Activity activity, Vitrine vitrine, Bitmap imagemPerfil) {
        this.activity = activity;
        this.vitrine = vitrine;
        this.imagemPerfil = imagemPerfil;
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
        if (imagemPerfil != null) {
            ByteArrayOutputStream bAOS = new ByteArrayOutputStream();
            imagemPerfil.compress(Bitmap.CompressFormat.JPEG, 50, bAOS);
            byte[] imagemArrayBytes = bAOS.toByteArray();

            //decodifica com a classe BASE64 e transforma em string
            imagemDecodificada = Base64.encodeToString(imagemArrayBytes, Base64.DEFAULT);
            vitrine.setFoto(imagemDecodificada);
        } else {
            vitrine.setFoto("");
        }

        VitrineJson json = new VitrineJson();
        String jsonVitrine = json.VitrineToJson(vitrine);

        String answer = HttpConnection.getSetDataWeb(this.url, this.method, jsonVitrine);
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
                            Intent intent = new Intent(activity, MainActivity.class);
                            activity.startActivity(intent);
                            activity.finish();
                        }
                    })
                    .show();
        }else{
                ((CadastroVitrineActivity) activity).onErroCadastro();
            }


        }
    }
