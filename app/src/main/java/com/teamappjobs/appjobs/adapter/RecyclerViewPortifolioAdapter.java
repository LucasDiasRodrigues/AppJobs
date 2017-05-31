package com.teamappjobs.appjobs.adapter;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.teamappjobs.appjobs.R;
import com.teamappjobs.appjobs.modelo.Portifolio;
import com.teamappjobs.appjobs.modelo.Vitrine;
import com.teamappjobs.appjobs.util.SlideshowDialogFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lucas on 04/05/2016.
 */
public class RecyclerViewPortifolioAdapter extends RecyclerView.Adapter<RecyclerViewPortifolioAdapter.MyViewHolder>{
    static FragmentActivity activity;
    static ArrayList<Portifolio> portifolio;
    static Vitrine vitrine;
    LayoutInflater layoutInflater;


    public RecyclerViewPortifolioAdapter(FragmentActivity activity, ArrayList<Portifolio> portifolio, Vitrine vitrine) {
        this.activity = activity;
        this.portifolio = portifolio;
        this.vitrine = vitrine;
        this.layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_list_portifolio, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Picasso.with(activity).load(activity.getResources().getString(R.string.imageservermini) + portifolio.get(position).getImagem()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return portifolio.size();
    }




    public static  class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
        }


        @Override
        public void onClick(View v) {

            Bundle bundle = new Bundle();
            bundle.putSerializable("portifolio", portifolio);
            bundle.putSerializable("vitrine", vitrine);
            bundle.putInt("position", getPosition());

            FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
            SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
            newFragment.setArguments(bundle);
            newFragment.show(ft, "slideshow");


        }
    }


}
