package com.teamappjobs.appjobs.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.teamappjobs.appjobs.R;
import com.teamappjobs.appjobs.asyncTask.ConfiguraContaTask;
import com.teamappjobs.appjobs.asyncTask.DesativaContaTask;
import com.teamappjobs.appjobs.modelo.Usuario;

public class ConfiguracaoContaActivity extends AppCompatActivity {
    private Usuario usuario;


    EditText txtSenhaAtual;
    EditText txtNovaSenha;
    EditText txtNovaSenha2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracao_conta);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.configConta));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        SharedPreferences prefs = getSharedPreferences("Configuracoes", Context.MODE_PRIVATE);



            usuario = new Usuario();
            usuario.setEmail(prefs.getString("email", ""));
            usuario.setGcmIdAtual(prefs.getString("gcmId",""));




        txtSenhaAtual = (EditText)findViewById(R.id.txtSenhaAtual);
        txtNovaSenha = (EditText)findViewById(R.id.txtNovaSenha);
        txtNovaSenha2 = (EditText)findViewById(R.id.txtNovaSenha2);

        Button btnAlterarSenha = (Button) findViewById(R.id.btnAlterarSenha);
        btnAlterarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //AlterarSenha

                if(verificarSenha()){


                        usuario.setSenha(txtSenhaAtual.getText().toString());
                        usuario.setNome(txtNovaSenha2.getText().toString());
                        ConfiguraContaTask contaTask = new ConfiguraContaTask(ConfiguracaoContaActivity.this, usuario,"alterarSenha");
                        contaTask.execute();

                }

            }
        });


        Button btnDesativarConta = (Button)findViewById(R.id.btnDesativarConta);
        btnDesativarConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(ConfiguracaoContaActivity.this).setTitle(R.string.desativarconta)
                        .setMessage(R.string.desejadesativarconta)
                        .setNegativeButton(R.string.cancelar, null)
                        .setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //desativar
                                    DesativaContaTask task = new DesativaContaTask(usuario, ConfiguracaoContaActivity.this);
                                    task.execute();
                            }
                        }).show();

            }
        });

    }

    public boolean verificarSenha(){

        boolean correto = true;

        if(txtSenhaAtual.getText().toString().equals("")){
            txtSenhaAtual.setError(getString(R.string.preenchacampo));
            correto = false;
        }
        if(txtNovaSenha.getText().toString().equals("")){
            txtNovaSenha.setError(getString(R.string.preenchacampo));
            correto = false;
        }
        if(txtNovaSenha2.getText().toString().equals("")){
            txtNovaSenha2.setError(getString(R.string.preenchacampo));
            correto = false;
        }
        if(!(txtNovaSenha.getText().toString()).equals(txtNovaSenha2.getText().toString())){
            txtNovaSenha2.setError(getString(R.string.senhasdiferentes));
            correto = false;
        }

        return correto;
    }

    public void senhaincorreta(){
        txtSenhaAtual.setFocusable(true);
        txtSenhaAtual.setError(getString(R.string.senhaincorreta));
    }

    public void limparCampos(){
        txtSenhaAtual.setText("");
        txtNovaSenha.setText("");
        txtNovaSenha2.setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
