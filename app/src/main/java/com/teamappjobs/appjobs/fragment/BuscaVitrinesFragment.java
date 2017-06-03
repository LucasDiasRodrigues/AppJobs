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

import com.google.android.gms.maps.model.LatLng;
import com.teamappjobs.appjobs.R;
import com.teamappjobs.appjobs.activity.MainActivity;
import com.teamappjobs.appjobs.adapter.RecyclerViewHomeTimeLineAdapter;
import com.teamappjobs.appjobs.asyncTask.ListaNovidadesTask;
import com.teamappjobs.appjobs.asyncTask.ListaResultadoBuscaTask;
import com.teamappjobs.appjobs.modelo.Vitrine;
import com.teamappjobs.appjobs.util.BuscarEventBus;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lucas on 17/05/2016.
 */
public class BuscaVitrinesFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private ProgressBar progressBar;
    private ProgressBar progressBarUpdate;
    private RecyclerViewHomeTimeLineAdapter adapter;
    private List<Vitrine> vitrines= new ArrayList<Vitrine>();
    private RecyclerView listVitrines;
    private TextView txtSemVitrines;

    private String palavras;

    private SwipeRefreshLayout mSwipeRefreshLayout;

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
        mSwipeRefreshLayout.setActivated(false);
        mSwipeRefreshLayout.setEnabled(false);

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
                pastVisiblesItems = mLayoutManager
                        .findFirstVisibleItemPosition();

                if (userScrolled
                        && (visibleItemCount + pastVisiblesItems) == totalItemCount) {
                    userScrolled = false;

                    updateRecyclerView();

                }
            }
        });

        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(BuscarEventBus event){

        if(event.getVitrines().size() > 0){
            Log.i("atualizaListaVitr","");

            adapter = new RecyclerViewHomeTimeLineAdapter(getActivity(),event.getVitrines(),"Vitrines encontradas","");
            mLayoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(adapter);
        } else {
            txtSemVitrines.setText("Nenhum resultado encontrado");
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
