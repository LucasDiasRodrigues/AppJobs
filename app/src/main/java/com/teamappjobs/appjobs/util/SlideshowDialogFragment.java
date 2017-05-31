package com.teamappjobs.appjobs.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.teamappjobs.appjobs.R;
import com.teamappjobs.appjobs.asyncTask.DeletaPortifolioTask;
import com.teamappjobs.appjobs.modelo.Portifolio;
import com.teamappjobs.appjobs.modelo.Vitrine;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lucas on 04/05/2016.
 */
public class SlideshowDialogFragment extends DialogFragment {
    private String TAG = SlideshowDialogFragment.class.getSimpleName();
    private ArrayList<Portifolio> portifolio;
    private Vitrine vitrine;
    private ViewPager viewPager;
    //private MyViewPagerAdapter myViewPagerAdapter;
    private TextView lblCount, lblTitle, lblDate;
    private ImageView btnDelete;
    private int selectedPosition = 0;
    private MyViewPagerAdapter myViewPagerAdapter;

    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public static SlideshowDialogFragment newInstance() {
        SlideshowDialogFragment f = new SlideshowDialogFragment();
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_image_slider, container, false);
        viewPager = (ViewPager) v.findViewById(R.id.viewpager);
        lblCount = (TextView) v.findViewById(R.id.lbl_count);
        lblTitle = (TextView) v.findViewById(R.id.title);
        lblDate = (TextView) v.findViewById(R.id.date);
        btnDelete = (ImageView) v.findViewById(R.id.delete);


        portifolio = (ArrayList<Portifolio>) getArguments().getSerializable("portifolio");
        vitrine = (Vitrine)getArguments().getSerializable("vitrine");
        selectedPosition = getArguments().getInt("position");

        Log.e(TAG, "position: " + selectedPosition);
        Log.e(TAG, "images size: " + portifolio.size());

        //Verifica se o usuário logado é o dono da vitrine
        try {
            SharedPreferences prefs = getActivity().getSharedPreferences("Configuracoes", getActivity().MODE_PRIVATE);
            if (!prefs.getString("email", "").equals(vitrine.getEmailAnunciante())) {
                //Oculta o botão de excluir
                btnDelete.setVisibility(View.INVISIBLE);
            }
        }catch (NullPointerException exception){
            //Oculta o botão de excluir
            btnDelete.setVisibility(View.INVISIBLE);
        }



        myViewPagerAdapter = new MyViewPagerAdapter(this);
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        setCurrentItem(selectedPosition);

        return v;
    }

    private void setCurrentItem(int position) {
        viewPager.setCurrentItem(position, false);
        displayMetaInfo(selectedPosition);
    }

    //  page change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            displayMetaInfo(position);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    private void displayMetaInfo(final int position) {
        lblCount.setText((position + 1) + " de " + portifolio.size());
        lblTitle.setText(portifolio.get(position).getDescricao());
        lblDate.setText(dateFormat.format(portifolio.get(position).getDataCadastro()));

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("clickdelete",portifolio.get(position).getDescricao());
            //    Log.i("clickdelete-1",portifolio.get(position - 1).getDescricao());

                new AlertDialog.Builder(getActivity())
                        .setTitle(getResources().getString(R.string.deletarPortifolio))
                        .setMessage(R.string.desejaDeletarPortifolip)
                        .setPositiveButton(getString(R.string.sim), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DeletaPortifolioTask task = new DeletaPortifolioTask(getActivity(), portifolio.get(position), SlideshowDialogFragment.this, position - 1);
                                task.execute();
                            }
                        })
                        .setNegativeButton(getString(R.string.nao), null)
                        .show();
            }
        });



    }



    public void recarregarLista(int newSelectedPosition, Portifolio portifolioDeletado) {

        portifolio.remove(portifolioDeletado);

        if(portifolio.size() < 1){

            this.dismiss();

        } else {

            myViewPagerAdapter = new MyViewPagerAdapter(this);
            viewPager.setAdapter(myViewPagerAdapter);
            viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

            if (newSelectedPosition <= 0) {
                newSelectedPosition = 0;
            }
          //  setCurrentItem(newSelectedPosition);
        }
    }




    //  adapter
    public class MyViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;
        private SlideshowDialogFragment fragment;

        public MyViewPagerAdapter(SlideshowDialogFragment fragment) {
            this.fragment = fragment;
        }


        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.image_fullscren_preview, container, false);

            ImageView imageViewPreview = (ImageView) view.findViewById(R.id.image_preview);

            Picasso.with(getActivity()).load(getResources().getString(R.string.imageserver) + portifolio.get(position).getImagem())
                    .into(imageViewPreview);

            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return portifolio.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == ((View) obj);
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }


}
