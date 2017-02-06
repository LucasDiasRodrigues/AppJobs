package com.teamappjobs.appjobs.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.teamappjobs.appjobs.R;
import com.teamappjobs.appjobs.adapter.PagerAdapterHome;
import com.teamappjobs.appjobs.adapter.PagerAdapterMinhaVitrine;

/**
 * Created by Lucas on 17/05/2016.
 */
public class HomeFragment extends Fragment {

    private ViewPager mViewPager;
    private int numTabs = 2;
    private TabLayout mTabLayout;
    private RecyclerView listVitrines;
    private TextView txtTitulo;
    private TextView txtSubTitulo;
    private TextView txtSemVitrines;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_home, container, false);

        listVitrines = (RecyclerView) fragment.findViewById(R.id.recycler_view_vitrines);
        txtTitulo = (TextView) fragment.findViewById(R.id.txtTitulo);
        txtSubTitulo = (TextView) fragment.findViewById(R.id.txtSubtitulo);
        txtSemVitrines = (TextView) fragment.findViewById(R.id.txtSemVitrines);


        //Configurando as tabs e fragments
        PagerAdapterHome pagerAdapter = new PagerAdapterHome(getChildFragmentManager(), getActivity(), numTabs);
        mViewPager = (ViewPager) fragment.findViewById(R.id.pager);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mTabLayout = (TabLayout) fragment.findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);


        return fragment;
    }
}
