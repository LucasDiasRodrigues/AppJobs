package com.teamappjobs.appjobs.fragment;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.teamappjobs.appjobs.adapter.RecyclerViewPromocoesAdapter;
import com.teamappjobs.appjobs.adapter.RecyclerViewThumbSigoAdapter;
import com.teamappjobs.appjobs.asyncTask.ListaVitrinesSigoTask;
import com.teamappjobs.appjobs.modelo.Promocao;
import com.teamappjobs.appjobs.modelo.Vitrine;

import java.util.List;

/**
 * Created by Lucas on 28/04/2016.
 */
public class VitrinesSigoFragment extends Fragment {

    private RecyclerView listVitrines;
    private RecyclerView listThumbSigo;
    private TextView txtSubTitulo;
    private TextView txtSemVitrines;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.LayoutManager mLayoutManagerThumb;
    private SharedPreferences prefs;

    private List Lpromocoes;
    private List Lvitrines;
    private List<Vitrine> vitrines;
    private List<Promocao> promocoes;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_lista_vitrines_sigo, container, false);

        listVitrines = (RecyclerView) fragment.findViewById(R.id.recycler_view_vitrines);
        listThumbSigo = (RecyclerView) fragment.findViewById(R.id.recycler_view_sigo);
        txtSubTitulo = (TextView) fragment.findViewById(R.id.txtSubtitulo);
        txtSemVitrines = (TextView) fragment.findViewById(R.id.txtSemVitrines);
        prefs = getActivity().getSharedPreferences("Configuracoes", Context.MODE_PRIVATE);
        progressBar = (ProgressBar) fragment.findViewById(R.id.progress);


        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listaVitrines();
    }

    @Override
    public void onResume() {
        super.onResume();
        listaVitrines();
    }

    public void listaVitrines() {

        ListaVitrinesSigoTask task = new ListaVitrinesSigoTask(getActivity(), this, "vitrinesSigo", prefs.getString("email", ""));
        task.execute();
    }

    public void atualizaListaVitrines(List Lpromocoes, List Lvitrines) {
        this.Lpromocoes = Lpromocoes;
        this.Lvitrines = Lvitrines;

        vitrines = Lvitrines;
        promocoes = Lpromocoes;

        progressBar.setVisibility(View.GONE);

        if (vitrines.size() > 0) {
            Log.i("atualizaListaVitr", "");


            txtSemVitrines.setVisibility(View.GONE);

            RecyclerViewPromocoesAdapter adapter = new RecyclerViewPromocoesAdapter(getActivity(), promocoes, this);
            RecyclerViewThumbSigoAdapter adapterThumb = new RecyclerViewThumbSigoAdapter(getActivity(), vitrines);
            mLayoutManager = new LinearLayoutManager(getActivity());
            mLayoutManagerThumb = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);


            listVitrines.setLayoutManager(mLayoutManager);
            listThumbSigo.setLayoutManager(mLayoutManagerThumb);

            listVitrines.setAdapter(adapter);
            listThumbSigo.setAdapter(adapterThumb);
        } else {

            txtSemVitrines.setVisibility(View.VISIBLE);
        }

    }


}
