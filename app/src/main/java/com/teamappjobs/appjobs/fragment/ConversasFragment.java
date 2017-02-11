package com.teamappjobs.appjobs.fragment;


import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.teamappjobs.appjobs.R;
import com.teamappjobs.appjobs.activity.ChatActivity;
import com.teamappjobs.appjobs.adapter.ListViewConversasAdapter;
import com.teamappjobs.appjobs.modelo.Usuario;

import java.util.List;

/**
 * Created by Mônica on 05/10/2016.
 */

public class ConversasFragment extends Fragment{

    ListView mListView;
    ListViewConversasAdapter adapter;
    private TextView txtSemConversas;
    private Usuario usuario;
    //listas
    List<Bundle> mList;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View fragment = inflater.inflate(R.layout.fragment_conversas, container, false);

        SharedPreferences prefs = getActivity().getSharedPreferences("Configuracoes", Context.MODE_PRIVATE);

        usuario = new Usuario();
        usuario.setEmail(prefs.getString("email", ""));

        mListView = (ListView)fragment.findViewById(R.id.listViewConversas);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Usuario usuarioSelec = new Usuario();
                usuarioSelec.setEmail(mList.get(position).getString("email",""));
                usuarioSelec.setNome(mList.get(position).getString("nome", ""));
                usuarioSelec.setImagemPerfil(mList.get(position).getString("foto",""));

                Intent intent= new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("origem","icNav");
                intent.putExtra("emailRemetente",usuarioSelec.getEmail());
                intent.putExtra("profPicRemetente",usuarioSelec.getImagemPerfil());
                intent.putExtra("nomeRemetente",usuarioSelec.getNome());
                startActivity(intent);
/*
                Intent it = new Intent(getActivity(), ChatActivity.class);
                it.putExtra("vitrine", vitrine);
                it.putExtra("origem","faleComOAnunciante");
                it.putExtra("fotoAnunciante",vitrine.getFoto());
                it.putExtra("nomeAnunciante",vitrine.getNomeAnunciante());
                startActivity(it); */


            }
        });

        txtSemConversas = (TextView) fragment.findViewById(R.id.txtSemConversas);
        progressBar = (ProgressBar) fragment.findViewById(R.id.progress);

        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        listaConversas();

    }

    public void atualizaLista(List<Bundle> list){
        mList = list;
        adapter = new ListViewConversasAdapter(mList,getActivity());
        mListView.setAdapter(adapter);

        if(mList.size() < 1){
            txtSemConversas.setVisibility(View.VISIBLE);
        } else {
            txtSemConversas.setVisibility(View.GONE);
        }

        progressBar.setVisibility(View.GONE);

    }

    public void listaConversas(){
        usuario.buscaConversas(getActivity(), this);
    }
}
