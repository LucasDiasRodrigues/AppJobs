package com.teamappjobs.appjobs.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.teamappjobs.appjobs.R;
import com.teamappjobs.appjobs.fragment.HomeHomeFragment;
import com.teamappjobs.appjobs.fragment.HomePopularesFragment;


/**
 * Created by Lucas on 17/05/2016.
 */
public class PagerAdapterHome extends FragmentStatePagerAdapter{
    private Context context;
    private int numTabs;
    private FragmentManager fm;

    public PagerAdapterHome(FragmentManager fm, Context context, int numTabs) {
        super(fm);
        this.fm = fm;
        this.context = context;
        this.numTabs = numTabs;
    }

    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            HomeHomeFragment fragment = new HomeHomeFragment();
            return fragment;
        } else {
            HomePopularesFragment fragment = new HomePopularesFragment();
            return fragment;
        }

    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return context.getResources().getString(R.string.home);
        }else {
            return context.getResources().getString(R.string.melhorAvaliados);
        }

    }



    @Override
    public int getCount() {
        return numTabs;
    }
}
