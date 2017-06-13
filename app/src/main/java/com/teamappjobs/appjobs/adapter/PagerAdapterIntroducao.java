package com.teamappjobs.appjobs.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.teamappjobs.appjobs.fragment.SlideViewPagerFragment;

import java.util.ArrayList;

/**
 * Created by Lucas on 11/06/2017.
 */

public class PagerAdapterIntroducao extends FragmentPagerAdapter {


    private ArrayList<String> txts;
    private Context context;


    public PagerAdapterIntroducao(FragmentManager fm, Context context) {
        super(fm);
        this.txts = txts;
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {

        SlideViewPagerFragment fragment = SlideViewPagerFragment.novaInstancia(position + 1);
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
