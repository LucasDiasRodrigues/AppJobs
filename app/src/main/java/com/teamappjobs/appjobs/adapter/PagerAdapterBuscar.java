package com.teamappjobs.appjobs.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.teamappjobs.appjobs.R;
import com.teamappjobs.appjobs.fragment.BuscaFragment;
import com.teamappjobs.appjobs.fragment.BuscaVitrinesFragment;
import com.teamappjobs.appjobs.fragment.HomeHomeFragment;
import com.teamappjobs.appjobs.fragment.HomeMapFragment;
import com.teamappjobs.appjobs.fragment.HomePopularesFragment;
import com.teamappjobs.appjobs.modelo.Vitrine;

import java.util.List;

public class PagerAdapterBuscar extends FragmentStatePagerAdapter{
    private Context context;
    private int numTabs;
    private String palavras;
    private FragmentManager fm;
    private BuscaFragment fragment;
    private List<Vitrine> vitrines;

    public PagerAdapterBuscar(FragmentManager fm, Context context, int numTabs,BuscaFragment fragment) {
        super(fm);
        this.fm = fm;
        this.context = context;
        this.numTabs = numTabs;
        this.fragment=fragment;
    }

    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            BuscaVitrinesFragment fragment = new BuscaVitrinesFragment();

            return fragment;
        }  else {
            HomeMapFragment fragment = new HomeMapFragment();
            return fragment;
        }

    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return context.getResources().getString(R.string.home);
        } else {
            return "Mapa";
        }

    }



    @Override
    public int getCount() {
        return numTabs;
    }



}

