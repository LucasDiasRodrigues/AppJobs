package com.teamappjobs.appjobs.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.teamappjobs.appjobs.R;
import com.teamappjobs.appjobs.fragment.MinhaVitrinePortifolioFragment;
import com.teamappjobs.appjobs.fragment.MinhaVitrinePromocoesFragment;
import com.teamappjobs.appjobs.fragment.VitrineSobreFragment;

/**
 * Created by Lucas on 03/05/2016.
 */
public class PagerAdapterMinhaVitrine extends FragmentStatePagerAdapter {
    private Context context;
    private int numTabs;
    private FragmentManager fm;


    public PagerAdapterMinhaVitrine(FragmentManager fm, Context context, int numTabs) {
        super(fm);
        this.fm = fm;
        this.context = context;
        this.numTabs = numTabs;
    }

    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            //MinhaVitrineSobreFragment fragment = new MinhaVitrineSobreFragment();
           VitrineSobreFragment fragment = new VitrineSobreFragment();
            return fragment;
        } else if (position == 1) {
            MinhaVitrinePortifolioFragment fragment = new MinhaVitrinePortifolioFragment();
            return fragment;
        } else {
            MinhaVitrinePromocoesFragment fragment = new MinhaVitrinePromocoesFragment();
            return fragment;
        }

    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {

             return context.getResources().getString(R.string.sobre);
        } else if (position == 1) {
             return context.getResources().getString(R.string.portifolio);
        } else {
                return context.getResources().getString(R.string.promocoes);
        }

    }

    @Override
    public int getCount() {
        return numTabs;
    }
}
