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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.teamappjobs.appjobs.R;
import com.teamappjobs.appjobs.adapter.RecyclerViewMinhasVitrinesAdapter;
import com.teamappjobs.appjobs.asyncTask.ListaVitrinesTask;
import com.teamappjobs.appjobs.modelo.Vitrine;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Lucas on 28/04/2016.
 */
public class MinhasVitrinesFragment extends Fragment{

    private RecyclerView listVitrines;
    private TextView txtTitulo;
    private TextView txtSubTitulo;
    private TextView txtSemVitrines;
    private RecyclerView.LayoutManager mLayoutManager;
    private SharedPreferences prefs ;

    private List<Vitrine> vitrines;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_lista_vitrines,container,false);

        listVitrines = (RecyclerView) fragment.findViewById(R.id.recycler_view_vitrines);
        txtTitulo = (TextView) fragment.findViewById(R.id.txtTitulo);
        txtSubTitulo = (TextView) fragment.findViewById(R.id.txtSubtitulo);
        txtSemVitrines = (TextView) fragment.findViewById(R.id.txtSemVitrines);
        prefs=  getActivity().getSharedPreferences("Configuracoes", Context.MODE_PRIVATE);


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

        if(vitrines.size() > 0){
            Log.i("atualizaListaVitr","");
            txtTitulo.setVisibility(View.VISIBLE);
            txtSemVitrines.setVisibility(View.GONE);

            RecyclerViewMinhasVitrinesAdapter adapter = new RecyclerViewMinhasVitrinesAdapter(getActivity(),vitrines);
            mLayoutManager = new LinearLayoutManager(getActivity());
            listVitrines.setLayoutManager(mLayoutManager);
            listVitrines.setAdapter(adapter);
        } else {
            txtTitulo.setVisibility(View.GONE);
            txtSemVitrines.setVisibility(View.VISIBLE);
        }

    }


}
