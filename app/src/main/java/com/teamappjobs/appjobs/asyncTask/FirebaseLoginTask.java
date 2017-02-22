package com.teamappjobs.appjobs.asyncTask;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.teamappjobs.appjobs.R;
import com.teamappjobs.appjobs.activity.LoginActivity;
import com.teamappjobs.appjobs.activity.MainActivity;
import com.teamappjobs.appjobs.modelo.Usuario;
import com.teamappjobs.appjobs.web.HttpConnection;
import com.teamappjobs.appjobs.web.LoginJson;
import com.teamappjobs.appjobs.web.UsuarioJson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by Lucas on 14/02/2017.
 */

public class FirebaseLoginTask extends AsyncTask<Object, Object, Object> {

    private final String url = "http://www.4runnerapp.com.br/AppJobs/Ctrl/login_json.php";
    private final String method = "firebaseLogin-json";
    private final LoginActivity activity;
    private String data;
    private ProgressDialog progress;
    private Bitmap imagemPerfil;
    private String imagemDecodificada = "";
    private Usuario usuario;
    private Uri profilePic;

    //apos o login
    private Usuario usuarioLogado;

    public FirebaseLoginTask(Usuario usuario, LoginActivity activity, Uri profilePic) {
        this.usuario = usuario;
        this.activity = activity;
        this.profilePic = profilePic;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = ProgressDialog.show(activity, "Aguarde...", "Envio de dados para web", true, true);
    }

    @Override
    protected Object doInBackground(Object... objects) {

        if (HttpConnection.isNetworkAvailable(activity)) {
            Log.i("CONEXAO", "CONECTADO");

            //Tratar ImageViwew
            try {
                imagemPerfil = Picasso.with(activity).load(profilePic).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //localiza e transforma em um array de bytes
            if (imagemPerfil != null) {
                ByteArrayOutputStream bAOS = new ByteArrayOutputStream();
                imagemPerfil.compress(Bitmap.CompressFormat.JPEG, 50, bAOS);
                byte[] imagemArrayBytes = bAOS.toByteArray();

                //decodifica com a classe BASE64 e transforma em string
                imagemDecodificada = Base64.encodeToString(imagemArrayBytes, Base64.DEFAULT);
                usuario.setImagemPerfil(imagemDecodificada);
            } else {
                usuario.setImagemPerfil("");
            }

            UsuarioJson json = new UsuarioJson();
            data = json.UsuarioToJson(usuario);


            String answer = HttpConnection.getSetDataWeb(this.url, this.method, this.data);
            Log.i("Script", "ANSWER: " + answer);

            if (!answer.equals("")) {
                Object resposta = new LoginJson().loginToString(answer);


                usuarioLogado = (Usuario) resposta;
                if (!usuarioLogado.getEmail().equals("")) {

                    SharedPreferences prefs = activity.getSharedPreferences("Configuracoes", activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("logado", true);
                    editor.putString("nome", usuarioLogado.getNome());
                    editor.putString("sobrenome", usuarioLogado.getSobreNome());
                    editor.putString("email", usuarioLogado.getEmail());
                    editor.putString("imagemperfil", usuarioLogado.getImagemPerfil());
                    editor.putString("sexo",usuarioLogado.getSexo());
                    editor.apply();
                    Log.i("preferencias ==== ", prefs.getString("perfil", ""));

                    return usuarioLogado;
                } else {
                    return "erroLogin";
                }

            } else {
                Log.i("ErroLogin", "answer == vazio ");
                return "erroLogin";
            }

        } else {
            Log.i("CONECXAO", "DESCONECTADO");
            return "erroConexao";
        }
    }

    @Override
    protected void onPostExecute(Object resultado) {
        super.onPostExecute(resultado);
        progress.dismiss();
        SharedPreferences prefs = activity.getSharedPreferences("Configuracoes", activity.MODE_PRIVATE);
        if (resultado.equals("erroConexao")) {
            Toast.makeText(activity, R.string.conexaoIndosponivel, Toast.LENGTH_LONG).show();

        } else if (resultado.equals("erroLogin")) {
            Toast.makeText(activity, R.string.opserro, Toast.LENGTH_SHORT).show();

        } else if (resultado instanceof Usuario) {

            Usuario user = (Usuario)resultado;
            if(user.getSexo().equals("")){
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("logado", false);
                editor.commit();
                activity.CadastroComplementarFirebase(usuarioLogado);
            }else {
                Intent intent = new Intent(activity, MainActivity.class);
                activity.startActivity(intent);
                activity.finish();
            }
        }
    }
}
