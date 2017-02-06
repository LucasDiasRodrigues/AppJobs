package com.teamappjobs.appjobs.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.teamappjobs.appjobs.R;
import com.teamappjobs.appjobs.activity.MinhaVitrineActivity;
import com.teamappjobs.appjobs.activity.VitrineActivity;
import com.teamappjobs.appjobs.adapter.RecyclerViewMinhasVitrinesAdapter;
import com.teamappjobs.appjobs.adapter.RecyclerViewPromocoesAdapter;
import com.teamappjobs.appjobs.asyncTask.ListaPromocoesTask;
import com.teamappjobs.appjobs.modelo.Promocao;
import com.teamappjobs.appjobs.modelo.Vitrine;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lucas on 03/05/2016.
 */
public class MinhaVitrinePromocoesFragment extends Fragment {
    private Vitrine vitrine;

    private List<Promocao> promocoes = new ArrayList<>();

    private RecyclerView listPromocoes;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView txtSemPromocoes;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_minha_vitrine_promocoes, container, false);

        try {
            vitrine = ((MinhaVitrineActivity) getActivity()).getVitrine();
        } catch (Throwable e) {
            vitrine = ((VitrineActivity) getActivity()).getVitrine();
        }

        listPromocoes = (RecyclerView) fragment.findViewById(R.id.recycler_view_promocoes);
        progressBar = (ProgressBar) fragment.findViewById(R.id.progress);
        txtSemPromocoes = (TextView) fragment.findViewById(R.id.txtSemPromocoes);


        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        listaPromocoes();
    }

    public void listaPromocoes(){

        ListaPromocoesTask task = new ListaPromocoesTask(getActivity(), this,vitrine);
        task.execute();

    }

    public void atualizaListaPromocoes(List<Promocao> promocoes){
        this.promocoes = promocoes;
        progressBar.setVisibility(View.GONE);

        if(promocoes.size() > 0){
            Log.i("atualizaListaPromo","");
            txtSemPromocoes.setVisibility(View.GONE);
            RecyclerViewPromocoesAdapter adapter = new RecyclerViewPromocoesAdapter(getActivity(),promocoes, vitrine, this);
            mLayoutManager = new LinearLayoutManager(getActivity());
            listPromocoes.setLayoutManager(mLayoutManager);
            listPromocoes.setAdapter(adapter);
        } else {
            txtSemPromocoes.setVisibility(View.VISIBLE);
        }

    }

}
