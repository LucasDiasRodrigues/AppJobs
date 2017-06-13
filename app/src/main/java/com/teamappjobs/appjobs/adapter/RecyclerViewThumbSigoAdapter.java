package com.teamappjobs.appjobs.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.teamappjobs.appjobs.R;
import com.teamappjobs.appjobs.activity.VitrineActivity;
import com.teamappjobs.appjobs.modelo.Vitrine;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Lucas on 02/05/2016.
 */
public class RecyclerViewThumbSigoAdapter extends RecyclerView.Adapter<RecyclerViewThumbSigoAdapter.MyViewHolder> {
    private static Activity activity;
    private static List<Vitrine> vitrines;

    public RecyclerViewThumbSigoAdapter(Activity activity, List<Vitrine> vitrines) {
        this.activity = activity;
        this.vitrines = vitrines;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = activity.getLayoutInflater().inflate(R.layout.item_list_sigo, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if(vitrines.get(position).getFoto().isEmpty()){
            Picasso.with(activity).load(R.drawable.nopic_thumb)
                    .into(holder.imageThumb);
        }
        else {
            Picasso.with(activity).load(activity.getResources().getString(R.string.imageservermini) + vitrines.get(position).getFoto())
                    .into(holder.imageThumb);
        }
    }

    @Override
    public int getItemCount() {
        return vitrines.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imageThumb;
        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            imageThumb = (CircleImageView) itemView.findViewById(R.id.imageThumb);
        }


        @Override
        public void onClick(View v) {
            Vitrine vitrineSelecionada = vitrines.get(getPosition());
            Intent intent = new Intent(activity, VitrineActivity.class);
            intent.putExtra("vitrine", vitrineSelecionada);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptions options = ActivityOptions
                        .makeSceneTransitionAnimation(activity,imageThumb,"capaVitrine");
                activity.startActivity(intent,options.toBundle());
            } else {
                activity.startActivity(intent);
            }
        }
    }
}
