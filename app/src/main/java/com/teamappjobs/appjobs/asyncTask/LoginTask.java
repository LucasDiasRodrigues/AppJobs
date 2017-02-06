package com.teamappjobs.appjobs.asyncTask;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.teamappjobs.appjobs.R;
import com.teamappjobs.appjobs.activity.LoginActivity;
import com.teamappjobs.appjobs.activity.MainActivity;
import com.teamappjobs.appjobs.modelo.Usuario;
import com.teamappjobs.appjobs.web.HttpConnection;
import com.teamappjobs.appjobs.web.LoginJson;

/**
 * Created by Lucas on 28/04/2016.
 */
public class LoginTask extends AsyncTask {
    private final String url = "http://www.4runnerapp.com.br/AppJobs/Ctrl/login_json.php";
    private final String method = "login-json";
    private final LoginActivity activity;
    private String data;
    private ProgressDialog progress;
    public LoginTask(String data, LoginActivity activity) {
        this.data = data;
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = ProgressDialog.show(activity, "Aguarde...", "Envio de dados para web", true, true);
    }

    @Override
    protected Object doInBackground(Object... params) {

        if (HttpConnection.isNetworkAvailable(activity)) {
            Log.i("CONEXAO", "CONECTADO");

            String answer = HttpConnection.getSetDataWeb(this.url, this.method, this.data);
            Log.i("Script", "ANSWER: " + answer);

            if (answer.contains("senhaincorreta")) {
                String resposta = "senhaIncorreta";
                return resposta;
            } else if (answer.contains("naolocalizado")) {
                String resposta2 = "naoLocalizado";
                return resposta2;
            } else if (!answer.equals("")) {
                Object resposta = new LoginJson().loginToString(answer);


                Usuario usuarioLogado;
                usuarioLogado = (Usuario) resposta;

                if (!usuarioLogado.getEmail().equals("")) {

                    SharedPreferences prefs = activity.getSharedPreferences("Configuracoes", activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("logado", true);
                    editor.putString("nome", usuarioLogado.getNome());
                    editor.putString("email", usuarioLogado.getEmail());
                    editor.putString("imagemperfil", usuarioLogado.getImagemPerfil());

                    editor.commit();
                    Log.i("preferencias ==== ", prefs.getString("perfil", ""));

                    return usuarioLogado;
                } else {
                    return "erro";
                }


            } else {
                Log.i("CONECXAO", "DESCONECTADO");
                return "erroConexao";
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

        if (resultado.equals("erroConexao")) {
            Toast.makeText(activity, R.string.conexaoIndosponivel, Toast.LENGTH_LONG).show();

        } else if (resultado.equals("senhaIncorreta") || resultado.equals("naoLocalizado")) {
            Toast.makeText(activity, R.string.dadosInvalidos, Toast.LENGTH_SHORT).show();

        } else if (resultado instanceof Usuario) {
            Intent intent = new Intent(activity, MainActivity.class);
            activity.startActivity(intent);
            activity.finish();
        }


    }
}
