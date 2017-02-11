package com.teamappjobs.appjobs.fragment;

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
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.teamappjobs.appjobs.R;
import com.teamappjobs.appjobs.adapter.RecyclerViewHomeTimeLineAdapter;
import com.teamappjobs.appjobs.adapter.RecyclerViewMinhasVitrinesAdapter;
import com.teamappjobs.appjobs.asyncTask.ListaNovidadesTask;
import com.teamappjobs.appjobs.modelo.Vitrine;

import java.util.List;

/**
 * Created by Lucas on 17/05/2016.
 */
public class HomeHomeFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private ProgressBar progressBar;
    private ProgressBar progressBarUpdate;
    private RecyclerViewHomeTimeLineAdapter adapter;
    private List<Vitrine> vitrines;
    private SwipeRefreshLayout mSwipeRefreshLayout;


    private RecyclerView listVitrines;
    private TextView txtSemVitrines;


    // Variaveis para o scroll listener
    private boolean userScrolled = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_home_home, container, false);

        listVitrines = (RecyclerView) fragment.findViewById(R.id.recycler_view_vitrines);
        txtSemVitrines = (TextView) fragment.findViewById(R.id.txtSemVitrines);

        progressBar = (ProgressBar) fragment.findViewById(R.id.progress);
        progressBarUpdate = (ProgressBar) fragment.findViewById(R.id.progressUpdate);
        mRecyclerView = (RecyclerView) fragment.findViewById(R.id.recyclerview);

        mSwipeRefreshLayout = (SwipeRefreshLayout) fragment.findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent,R.color.colorPrimary,R.color.colorPrimaryDark);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i("AtualizandoLista", "onRefresh called from SwipeRefreshLayout");
                listaNovidades();
            }
        });

        listaNovidades();

        //Controle de scroll
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            boolean userScrolled;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    userScrolled = true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = mLayoutManager.getChildCount();
                totalItemCount = mLayoutManager.getItemCount();
                pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();
                if (userScrolled
                        && (visibleItemCount + pastVisiblesItems) == totalItemCount) {
                    userScrolled = false;
                    updateRecyclerView();
                }
            }
        });
        return fragment;
    }

    public void listaNovidades() {
        //Task para baixar mais itens
        ListaNovidadesTask task = new ListaNovidadesTask(getActivity(), this);
        task.execute();
    }

    public void mostraListaNovidades(List<Vitrine> vitrines) {
        this.vitrines = vitrines;
        mSwipeRefreshLayout.setRefreshing(false);
        if (vitrines.size() > 0) {
            Log.i("atualizaListaVitr", "");
            txtSemVitrines.setVisibility(View.GONE);
            adapter = new RecyclerViewHomeTimeLineAdapter(getActivity(), vitrines, "Novos anúncios","");
            mLayoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(adapter);
        } else {
            txtSemVitrines.setVisibility(View.VISIBLE);
        }
    }

    public void updateRecyclerView() {
        progressBarUpdate.setVisibility(View.VISIBLE);
        //Task para baixar mais itens
        adapter.notifyDataSetChanged();
        progressBarUpdate.setVisibility(View.GONE);
    }
}
