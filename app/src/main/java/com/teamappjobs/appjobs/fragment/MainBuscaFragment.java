package com.teamappjobs.appjobs.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.teamappjobs.appjobs.R;
import com.teamappjobs.appjobs.adapter.PagerAdapterBuscar;
import com.teamappjobs.appjobs.asyncTask.ListaResultadoBuscaTask;
import com.teamappjobs.appjobs.modelo.Vitrine;
import com.teamappjobs.appjobs.util.BuscarEventBus;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

//NãoUsado
public class MainBuscaFragment extends Fragment {

    private ViewPager mViewPager;
    private int numTabs = 2;
    private String palavras;
    private TabLayout mTabLayout;
    private RecyclerView listVitrines;
    private TextView txtTitulo;
    private TextView txtSubTitulo;
    private TextView txtSemVitrines;
    public List<Vitrine> vitrines;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_home_busca, container, false);


        listVitrines = (RecyclerView) fragment.findViewById(R.id.recycler_view_vitrines);
        txtTitulo = (TextView) fragment.findViewById(R.id.txtTitulo);
        txtSubTitulo = (TextView) fragment.findViewById(R.id.txtSubtitulo);
        txtSemVitrines = (TextView) fragment.findViewById(R.id.txtSemVitrines);
        Bundle b = getArguments();
        Pesquisa(b.getString("palavras"));


        //Configurando as tabs e fragments
      //  PagerAdapterBuscar pagerAdapter = new PagerAdapterBuscar(getChildFragmentManager(), getActivity(), numTabs,this);
        mViewPager = (ViewPager) fragment.findViewById(R.id.pager);
      //  mViewPager.setAdapter(pagerAdapter);


        mTabLayout = (TabLayout) fragment.findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);


        return fragment;
    }

    public void Pesquisa(String query){
        //ListaResultadoBuscaTask task = new ListaResultadoBuscaTask(getActivity(),this, query);
        //task.execute();

    }

    public void RecebeLista(List<Vitrine> vitrines){
        this.vitrines = vitrines;
            BuscarEventBus event = new BuscarEventBus();
            event.setVitrines(vitrines);
            EventBus.getDefault().post(event);
    }
}
