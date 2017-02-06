package com.teamappjobs.appjobs.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.teamappjobs.appjobs.R;
import com.teamappjobs.appjobs.activity.MinhaVitrineActivity;
import com.teamappjobs.appjobs.modelo.Vitrine;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Lucas on 02/05/2016.
 */
public class RecyclerViewMinhasVitrinesAdapter extends RecyclerView.Adapter<RecyclerViewMinhasVitrinesAdapter.MyViewHolder> {
    private static Activity activity;
    private static List<Vitrine> vitrines;
    private LayoutInflater layoutInflater;

    private DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public RecyclerViewMinhasVitrinesAdapter(Activity activity, List<Vitrine> vitrines) {
        this.activity = activity;
        this.vitrines = vitrines;
        this.layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_list_minhas_vitrines, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Picasso.with(activity).load(activity.getResources().getString(R.string.imageserver) + vitrines.get(position).getFoto()).into(holder.imageVitrine);
        holder.txtNomeVitrine.setText(vitrines.get(position).getNome());
        holder.txtCategoriaVitrine.setText(vitrines.get(position).getDescCategoria());

        holder.txtDtCriacao.setText(format.format(vitrines.get(position).getDataCriacao()));

        String auxSituacao;
        if (vitrines.get(position).getSituacao().equals("ativa")){
            auxSituacao = "Ativa";
        } else {
            auxSituacao = "Inativa";
        }
        holder.txtSituacao.setText(auxSituacao);
    }

    @Override
    public int getItemCount() {
        return vitrines.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imageVitrine;
        private TextView txtNomeVitrine;
        private TextView txtCategoriaVitrine;
        private TextView txtSubcatVitrine;
        private TextView txtDtCriacao;
        private TextView txtSituacao;


        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            imageVitrine = (ImageView) itemView.findViewById(R.id.imageViewCapa);
            txtNomeVitrine = (TextView) itemView.findViewById(R.id.txtNomeVitrine);
            txtCategoriaVitrine = (TextView) itemView.findViewById(R.id.categoriaVitrine);

            txtDtCriacao = (TextView) itemView.findViewById(R.id.dtCriacaoVitrine);
            txtSituacao = (TextView) itemView.findViewById(R.id.txtSituacao);

        }


        @Override
        public void onClick(View v) {
            Vitrine vitrineSelecionada = vitrines.get(getPosition());
            Intent intent = new Intent(activity, MinhaVitrineActivity.class);
            intent.putExtra("vitrine", vitrineSelecionada);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptions options = ActivityOptions
                        .makeSceneTransitionAnimation(activity,imageVitrine,"capaVitrine");
                activity.startActivity(intent,options.toBundle());
            } else {
                activity.startActivity(intent);
            }
        }
    }
}
