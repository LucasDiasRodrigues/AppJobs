package com.teamappjobs.appjobs.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.teamappjobs.appjobs.R;
import com.teamappjobs.appjobs.activity.MinhaVitrineActivity;
import com.teamappjobs.appjobs.activity.VitrineActivity;
import com.teamappjobs.appjobs.adapter.RecyclerViewPortifolioAdapter;
import com.teamappjobs.appjobs.asyncTask.ListaPortifolioTask;
import com.teamappjobs.appjobs.modelo.Portifolio;
import com.teamappjobs.appjobs.modelo.Vitrine;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lucas on 03/05/2016.
 */
public class MinhaVitrinePortifolioFragment extends Fragment {
    private Vitrine vitrine;
    private RecyclerView recyclerView;
    private ArrayList<Portifolio> portifolio;
    private TextView txtSemPortifolio;
    private ProgressBar progressBar;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_minha_vitrine_portifolio, container, false);


        try {
            vitrine = ((MinhaVitrineActivity) getActivity()).getVitrine();
        } catch (Throwable e) {
            vitrine = ((VitrineActivity) getActivity()).getVitrine();
        }

        recyclerView = (RecyclerView) fragment.findViewById(R.id.recycler_view);
        progressBar = (ProgressBar) fragment.findViewById(R.id.progress);
        txtSemPortifolio = (TextView) fragment.findViewById(R.id.txtSemPortifolio);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        listaPortifolio();

    }

    public void listaPortifolio() {

        ListaPortifolioTask task = new ListaPortifolioTask(getActivity(), this, vitrine);
        task.execute();

    }

    public void atualizaListaPortifolio(ArrayList<Portifolio> portifolio) {
        progressBar.setVisibility(View.GONE);

        if(portifolio.size() > 0){
            txtSemPortifolio.setVisibility(View.GONE);
            RecyclerViewPortifolioAdapter adapter = new RecyclerViewPortifolioAdapter(getActivity(), portifolio,vitrine);
            recyclerView.setAdapter(adapter);
        } else {
            txtSemPortifolio.setVisibility(View.VISIBLE);
        }

    }


}
