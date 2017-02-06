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
import com.teamappjobs.appjobs.activity.CadastroUsuarioActivity;
import com.teamappjobs.appjobs.modelo.Usuario;
import com.teamappjobs.appjobs.web.HttpConnection;
import com.teamappjobs.appjobs.web.UsuarioJson;
import java.io.ByteArrayOutputStream;

/**
 * Created by Lucas on 05/05/2016.
 */
public class EditaUsuarioTask extends AsyncTask {

    private final String url = "http://www.4runnerapp.com.br/AppJobs/Ctrl/cadastra_usuario_json.php";
    private final String method = "edita_usuario-json";


    private ProgressDialog progress;

    private Activity activity;
    private Usuario usuario;
    private Bitmap imagem;
    private String imagemDecodificada = "";

    public EditaUsuarioTask(Activity activity, Usuario usuario, Bitmap imagem) {
        this.activity = activity;
        this.usuario = usuario;
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
            usuario.setImagemPerfil(imagemDecodificada);

        } else if(imagem == null && (!usuario.getImagemPerfil().equals(""))) {
            usuario.setImagemPerfil("naoalterada");
        } else{
            usuario.setImagemPerfil("");
        }

        UsuarioJson json = new UsuarioJson();
        String jsonUsuario = json.UsuarioToJson(usuario);

        String answer = HttpConnection.getSetDataWeb(this.url, this.method, jsonUsuario);
        Log.i("RespAltUsuario", answer);
        return answer;
    }


    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        progress.dismiss();

        Log.i("resultado Json", o.toString());

        if (o.toString().equals("Sucesso")) {

            new AlertDialog.Builder(activity).setTitle(R.string.sucesso)
                    .setMessage("Alteração concluída")
                    .setCancelable(false)
                    .setPositiveButton(R.string.continuar, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            activity.finish();
                        }
                    })
                    .show();
        } else {
            ((CadastroUsuarioActivity)activity).onErroCadastro();
        }

    }


}
