package com.teamappjobs.appjobs.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.teamappjobs.appjobs.R;
import com.teamappjobs.appjobs.activity.LoginActivity;
import com.teamappjobs.appjobs.asyncTask.RecuperarSenhaTask;
import com.teamappjobs.appjobs.util.Mask;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Lucas on 28/04/2016.
 */
public class RecuperarSenhaFragment extends Fragment {

    EditText etEmail;
    EditText etDataNasc;
    Button btnRecuperar;
    String dataNasc;

    DateFormat brDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    DateFormat sqlDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_recuperar_senha, container, false);

        etEmail = (EditText) fragment.findViewById(R.id.etEmail);
        etDataNasc = (EditText) fragment.findViewById(R.id.etDataNasc);
        etDataNasc.addTextChangedListener(Mask.insert("##/##/####", etDataNasc));
        btnRecuperar = (Button) fragment.findViewById(R.id.btnRecuperar);
        btnRecuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validaCampos()) {
                    recuperarSenha();
                }
            }
        });


        return fragment;
    }

    public void recuperarSenha() {

        RecuperarSenhaTask task = new RecuperarSenhaTask(getActivity(), this, etEmail.getText().toString(), dataNasc);
        task.execute();


    }

    public boolean validaCampos() {
        Boolean valida = true;

        if (etEmail.getText().toString().equals("")) {
            valida = false;
            etEmail.setError(getResources().getString(R.string.preenchacampo));
        } else {
            String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

            Pattern pattern = Pattern.compile(EMAIL_PATTERN);
            Matcher matcher = pattern.matcher(etEmail.getText().toString());
            valida = matcher.matches();
            if (!matcher.matches()) {
                etEmail.setError(getResources().getString(R.string.emailInvalido));
            }
        }

        if (etDataNasc.getText().toString().equals("")) {
            valida = false;
            etDataNasc.setError(getResources().getString(R.string.preenchacampo));
        }

        try{
            Date auxdata = brDateFormat.parse(etDataNasc.getText().toString());
            dataNasc = sqlDateFormat.format(auxdata);
        }catch (ParseException e) {
            e.printStackTrace();
            valida = false;
        }

        return valida;
    }

    public void emailEnviado() {

        new AlertDialog.Builder(getActivity()).setTitle(R.string.emailEnviado)
                .setMessage(R.string.acesseemailparasenha)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((LoginActivity) getActivity()).Entrar();
                    }
                })
                .show();

    }

    public void dadosInvalidaos() {

        new AlertDialog.Builder(getActivity()).setTitle(R.string.dadosInvalidos)
                .setMessage(R.string.informacoesInvalidas)
                .setPositiveButton(R.string.ok, null)
                .show();

    }


}
