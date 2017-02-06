package com.teamappjobs.appjobs.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.teamappjobs.appjobs.R;
import com.teamappjobs.appjobs.activity.CadastroUsuarioActivity;
import com.teamappjobs.appjobs.activity.ConfiguracaoContaActivity;
import com.teamappjobs.appjobs.adapter.ListViewConfiguracoesAdapter;
import com.teamappjobs.appjobs.modelo.Usuario;
import java.util.ArrayList;
import java.util.List;

public class ConfiguracoesFragment extends Fragment {
    Usuario usuario;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_configuracoes, container, false);

        SharedPreferences prefs = getActivity().getSharedPreferences("Configuracoes", Context.MODE_PRIVATE);
        usuario = new Usuario();
        usuario.setEmail(prefs.getString("email", ""));

        final List<String> mList = new ArrayList<>();
        mList.add(getString(R.string.perfil));
        mList.add(getString(R.string.conta));


        final ListView listView = (ListView)fragment.findViewById(R.id.listView);
        ListViewConfiguracoesAdapter adapter = new ListViewConfiguracoesAdapter(getActivity(),mList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selecionado = (String) listView.getItemAtPosition(position);
                switch (selecionado) {

                    case "Perfil":
                        Intent intent1 = new Intent(getActivity(), CadastroUsuarioActivity.class);
                        intent1.putExtra("editar",true);
                        startActivity(intent1);
                        break;

                    case "Conta":
                        Intent intent3 = new Intent(getActivity(), ConfiguracaoContaActivity.class);
                        startActivity(intent3);
                        break;

                }

            }
        });


        return fragment;
    }
}
