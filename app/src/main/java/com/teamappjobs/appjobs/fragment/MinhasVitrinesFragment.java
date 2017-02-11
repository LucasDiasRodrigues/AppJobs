package com.teamappjobs.appjobs.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.teamappjobs.appjobs.R;
import com.teamappjobs.appjobs.adapter.RecyclerViewMinhasVitrinesAdapter;
import com.teamappjobs.appjobs.asyncTask.ListaVitrinesTask;
import com.teamappjobs.appjobs.modelo.Vitrine;

import java.util.List;

/**
 * Created by Lucas on 28/04/2016.
 */
public class MinhasVitrinesFragment extends Fragment{

    private RecyclerView listVitrines;
    private TextView txtSemVitrines;
    private RecyclerView.LayoutManager mLayoutManager;
    private SharedPreferences prefs ;

    private List<Vitrine> vitrines;
    private ProgressBar progressBar;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_lista_minhas_vitrines,container,false);

        listVitrines = (RecyclerView) fragment.findViewById(R.id.recycler_view_vitrines);
        txtSemVitrines = (TextView) fragment.findViewById(R.id.txtSemVitrines);
        prefs=  getActivity().getSharedPreferences("Configuracoes", Context.MODE_PRIVATE);
        progressBar = (ProgressBar) fragment.findViewById(R.id.progress);
        mSwipeRefreshLayout = (SwipeRefreshLayout) fragment.findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent,R.color.colorPrimary,R.color.colorPrimaryDark);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i("AtualizandoLista", "onRefresh called from SwipeRefreshLayout");
                listaVitrines();
            }
        });
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

    public void listaVitrines(){

        ListaVitrinesTask task = new ListaVitrinesTask(getActivity(),this,"minhasVitrines",prefs.getString("email", ""));
        task.execute();
    }

    public void atualizaListaVitrines(List<Vitrine> vitrines){
        this.vitrines = vitrines;
        mSwipeRefreshLayout.setRefreshing(false);
        progressBar.setVisibility(View.GONE);
        if(vitrines.size() > 0){
            Log.i("atualizaListaVitr","");
            txtSemVitrines.setVisibility(View.GONE);

            RecyclerViewMinhasVitrinesAdapter adapter = new RecyclerViewMinhasVitrinesAdapter(getActivity(),vitrines);
            mLayoutManager = new LinearLayoutManager(getActivity());
            listVitrines.setLayoutManager(mLayoutManager);
            listVitrines.setAdapter(adapter);
        } else {
            txtSemVitrines.setVisibility(View.VISIBLE);
        }

    }


}
