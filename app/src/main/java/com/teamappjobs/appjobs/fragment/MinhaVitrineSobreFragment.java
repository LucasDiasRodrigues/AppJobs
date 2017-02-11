package com.teamappjobs.appjobs.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.teamappjobs.appjobs.R;
import com.teamappjobs.appjobs.activity.MinhaVitrineActivity;
import com.teamappjobs.appjobs.activity.VitrineActivity;
import com.teamappjobs.appjobs.modelo.Vitrine;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by Lucas on 03/05/2016.
 */

//Não Usada
public class MinhaVitrineSobreFragment extends Fragment {
    private Vitrine vitrine;
    private TextView txtDescricao;
    private TextView txtFaixaPreco;
    private TextView txtDataCriacao;
    private TextView txtLinks;
    private TextView txtTags;

    private DateFormat dateFormat =  new SimpleDateFormat("dd/MM/yyyy");


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getVitrine


        try {
            vitrine = ((MinhaVitrineActivity) getActivity()).getVitrine();
        } catch (Throwable e) {
            vitrine = ((VitrineActivity) getActivity()).getVitrine();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_minha_vitrine_sobre, container, false);

        txtDescricao = (TextView) fragment.findViewById(R.id.txtDescricao);
        txtFaixaPreco = (TextView) fragment.findViewById(R.id.txtFaixaPreco);
        txtDataCriacao = (TextView) fragment.findViewById(R.id.txtDataCriacao);
        txtLinks = (TextView) fragment.findViewById(R.id.txtLinks);
        txtTags = (TextView) fragment.findViewById(R.id.txtTags);

        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();

        txtDescricao.setText(vitrine.getDescricao());
        txtFaixaPreco.setText(vitrine.getFaixaPreco());
        txtDataCriacao.setText(dateFormat.format(vitrine.getDataCriacao()));

        //Tags
        String auxTags = "";
        for (String tag : vitrine.getTags()){
            auxTags = auxTags + tag + "  ";
        }
        txtTags.setText(auxTags);

        //Links
        String auxLinks = "";
        for (String link : vitrine.getLinks()){
            auxLinks = auxLinks + link + "   ";
        }
        txtLinks.setText(auxLinks);

    }
}
