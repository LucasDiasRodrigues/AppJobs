package com.teamappjobs.appjobs.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.teamappjobs.appjobs.R;
import com.teamappjobs.appjobs.fragment.LoginFragment;
import com.teamappjobs.appjobs.fragment.RecuperarSenhaFragment;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //Iniciando a primeira Fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_main, new LoginFragment());
        transaction.commit();
        //--

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void Entrar() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_main, new LoginFragment());
        //  transaction.addToBackStack(null);
        transaction.commit();
    }

    public void RecuperarSenha() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_main, new RecuperarSenhaFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }


}
