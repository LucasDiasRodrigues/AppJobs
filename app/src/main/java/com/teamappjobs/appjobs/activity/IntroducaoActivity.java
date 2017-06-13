package com.teamappjobs.appjobs.activity;

import android.animation.Animator;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.teamappjobs.appjobs.R;
import com.teamappjobs.appjobs.adapter.PagerAdapterIntroducao;

import java.util.ArrayList;

public class IntroducaoActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private ImageView mWallpaper;
    private ImageView mWallpaper2;
    private View mIndicator1;
    private View mIndicator2;
    private View mIndicator3;

    private Button btnContinuar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introducao);

        mIndicator1 = findViewById(R.id.indicator1);
        mIndicator2 = findViewById(R.id.indicator2);
        mIndicator3 = findViewById(R.id.indicator3);

        btnContinuar = (Button) findViewById(R.id.btnContinuar);
        btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mWallpaper = (ImageView) findViewById(R.id. imageWallpaperUser);
        mWallpaper2 = (ImageView) findViewById(R.id. imageWallpaperProf);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = 2;

        mWallpaper.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bg_intro_user,options));
        mWallpaper2.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bg_intro_prof,options));

        //Slides
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        PagerAdapterIntroducao adapter = new PagerAdapterIntroducao(getSupportFragmentManager(), this);
        mViewPager.setAdapter(adapter);
        savePreferences();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mIndicator1.setDrawingCacheBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
        mIndicator1.animate().scaleX(1.5f).scaleY(1.5f);
        
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        mIndicator1.setDrawingCacheBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                        mIndicator1.animate().scaleX(1.5f).scaleY(1.5f);

                        mIndicator2.setDrawingCacheBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                        mIndicator2.animate().scaleX(1).scaleY(1);

                        mIndicator3.setDrawingCacheBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                        mIndicator3.animate().scaleX(1).scaleY(1);

                        mWallpaper.animate().alpha(0).setStartDelay(0);

                        btnContinuar.setVisibility(View.GONE);

                        break;
                    case 1:
                        mIndicator1.setDrawingCacheBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                        mIndicator1.animate().scaleX(1).scaleY(1);

                        mIndicator2.setDrawingCacheBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                        mIndicator2.animate().scaleX(1.5f).scaleY(1.5f);

                        mIndicator3.setDrawingCacheBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                        mIndicator3.animate().scaleX(1).scaleY(1);


                        mWallpaper.setImageResource(R.drawable.bg_intro_user);
                        mWallpaper.animate().alpha(1);
                        mWallpaper2.animate().alpha(0);

                        btnContinuar.animate().alpha(0).setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                btnContinuar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        });

                        break;
                    case 2:
                        mIndicator1.setDrawingCacheBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                        mIndicator1.animate().scaleX(1).scaleY(1);

                        mIndicator2.setDrawingCacheBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                        mIndicator2.animate().scaleX(1).scaleY(1);

                        mIndicator3.setDrawingCacheBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                        mIndicator3.animate().scaleX(1.5f).scaleY(1.5f);

                        mWallpaper2.animate().alpha(1);

                        btnContinuar.animate().alpha(1).setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                btnContinuar.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {

                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        });
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void savePreferences(){
        SharedPreferences preferences = getSharedPreferences("config", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("introduction", true);
        editor.commit();
    }


}
