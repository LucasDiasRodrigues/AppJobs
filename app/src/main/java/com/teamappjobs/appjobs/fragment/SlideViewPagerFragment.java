package com.teamappjobs.appjobs.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.teamappjobs.appjobs.R;

/**
 * Created by Lucas on 11/06/2017.
 */

public class SlideViewPagerFragment extends Fragment {

    private int page;

    private TextView title;
    private TextView txt;



    public static SlideViewPagerFragment novaInstancia(int page) {

        Bundle params = new Bundle();
        params.putInt("page", page);

        SlideViewPagerFragment instancia = new SlideViewPagerFragment();
        instancia.setArguments(params);

        return instancia;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle params = getArguments();
        int page = params.getInt("page");
        View fragment;
        if(page == 1){
            fragment = inflater.inflate(R.layout.fragment_viewpager_introducao_page1, container, false);


        } else {
            fragment = inflater.inflate(R.layout.fragment_viewpager_introducao, container, false);

            title = (TextView) fragment.findViewById(R.id.txtTitulo);
            txt = (TextView) fragment.findViewById(R.id.txt1);

            if(page == 2) {
                txt.setText(getString(R.string.txtPag2));
                title.setText(getString(R.string.usuario));
            } else {
                txt.setText(getString(R.string.txtPag3));
                title.setText(getString(R.string.profissional));
            }
        }
        return fragment;
    }
}
