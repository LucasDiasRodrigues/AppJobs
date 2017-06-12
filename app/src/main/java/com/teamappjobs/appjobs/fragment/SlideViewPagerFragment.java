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

    private TextView txt1;


    public static SlideViewPagerFragment novaInstancia(String textoMotivador) {

        Bundle params = new Bundle();
        params.putString("txt", textoMotivador);

        SlideViewPagerFragment instancia = new SlideViewPagerFragment();
        instancia.setArguments(params);

        return instancia;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_viewpager_introducao, container, false);

        Bundle params = getArguments();

        String txt = params.getString("txt");

        txt1 = (TextView) fragment.findViewById(R.id.txt1);
        txt1.setText(txt);

        return fragment;
    }

}
