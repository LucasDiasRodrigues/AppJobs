package com.teamappjobs.appjobs.activity;

import android.animation.Animator;
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

        mWallpaper = (ImageView) findViewById(R.id.imageWallpaper);
        //Slides
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        ArrayList<String> txts = new ArrayList<>();
        txts.add("Textinho motivador 1");
        txts.add("Textinho motivador 2");
        txts.add("Textinho motivador 3");
        PagerAdapterIntroducao adapter = new PagerAdapterIntroducao(getSupportFragmentManager(), this, txts);
        mViewPager.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        mIndicator1.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                        mIndicator1.animate().scaleX(1.5f).scaleY(1.5f);

                        mIndicator2.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                        mIndicator2.animate().scaleX(1).scaleY(1);

                        mIndicator3.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                        mIndicator3.animate().scaleX(1).scaleY(1);

                        mWallpaper.animate().alpha(0);

                        btnContinuar.setVisibility(View.GONE);

                        break;
                    case 1:
                        mIndicator1.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                        mIndicator1.animate().scaleX(1).scaleY(1);

                        mIndicator2.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                        mIndicator2.animate().scaleX(1.5f).scaleY(1.5f);

                        mIndicator3.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                        mIndicator3.animate().scaleX(1).scaleY(1);

                        mWallpaper.setAlpha(0f);
                        mWallpaper.setImageBitmap(loadImageBg(2));
                        mWallpaper.animate().alpha(1);

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
                        mIndicator1.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                        mIndicator1.animate().scaleX(1).scaleY(1);

                        mIndicator2.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                        mIndicator2.animate().scaleX(1).scaleY(1);

                        mIndicator3.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                        mIndicator3.animate().scaleX(1.5f).scaleY(1.5f);

                        mWallpaper.setAlpha(0f);
                        mWallpaper.setImageBitmap(loadImageBg(3));
                        mWallpaper.animate().alpha(1);

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

    private Bitmap loadImageBg(int page) {
        int resource;
        Bitmap bitmap;

        if (page == 2) {
            resource = R.drawable.bg_intro_user;
        } else {
            resource = R.drawable.bg_intro_prof;
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), resource, options);

        options.inSampleSize = calculateInSampleSize(options, mWallpaper.getWidth(), mWallpaper.getHeight());

        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeResource(getResources(), resource, options);

        return bitmap;
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

}
