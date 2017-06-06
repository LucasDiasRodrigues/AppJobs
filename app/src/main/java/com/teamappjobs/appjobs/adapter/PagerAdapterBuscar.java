package com.teamappjobs.appjobs.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.google.android.gms.maps.model.LatLng;
import com.teamappjobs.appjobs.fragment.BuscaVitrinesFragment;
import com.teamappjobs.appjobs.fragment.BuscaMapFragment;
import com.teamappjobs.appjobs.modelo.Vitrine;

import java.util.List;

public class PagerAdapterBuscar extends FragmentStatePagerAdapter{
    private Context context;
    private int numTabs;
    private FragmentManager fm;

    public PagerAdapterBuscar(FragmentManager fm, Context context, int numTabs) {
        super(fm);
        this.fm = fm;
        this.context = context;
        this.numTabs = numTabs;
    }

    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            BuscaVitrinesFragment fragment = new BuscaVitrinesFragment();

            return fragment;
        }  else {
           BuscaMapFragment fragment = new BuscaMapFragment();

            return fragment;
        }

    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "Vitrines";
        } else {
            return "Mapa";
        }

    }



    @Override
    public int getCount() {
        return numTabs;
    }



}

