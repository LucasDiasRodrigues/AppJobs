package com.teamappjobs.appjobs.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.teamappjobs.appjobs.R;
import com.teamappjobs.appjobs.activity.MinhaVitrineActivity;
import com.teamappjobs.appjobs.activity.VitrineActivity;
import com.teamappjobs.appjobs.modelo.Vitrine;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class RecyclerViewHomeTimeLineAdapter extends RecyclerView.Adapter<RecyclerViewHomeTimeLineAdapter.MyViewHolder> {

    private static Activity activity;
    private static List<Vitrine> vitrines = new ArrayList<Vitrine>();
    private LayoutInflater layoutInflater;
    private DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private  boolean GEO;

    public RecyclerViewHomeTimeLineAdapter(Activity activity, List<Vitrine> vitrines) {
        this.activity = activity;
        this.vitrines = vitrines;
        this.layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.GEO = GEO;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_list_todas_vitrines, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Picasso.with(activity).load(activity.getResources().getString(R.string.imageserver) + vitrines.get(position).getFoto()).into(holder.imageVitrine);
        holder.txtNomeVitrine.setText(vitrines.get(position).getNome());
        holder.txtCategoriaVitrine.setText(vitrines.get(position).getDescCategoria());
        holder.txtDtCriacao.setText(format.format(vitrines.get(position).getDataCriacao()));

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
        private Button btnVerVitrine;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            imageVitrine = (ImageView) itemView.findViewById(R.id.imageViewCapa);
            txtNomeVitrine = (TextView) itemView.findViewById(R.id.txtNomeVitrine);
            txtCategoriaVitrine = (TextView) itemView.findViewById(R.id.categoriaVitrine);
          //  txtSubcatVitrine = (TextView) itemView.findViewById(R.id.subcategoriaVitrine);
            txtDtCriacao = (TextView) itemView.findViewById(R.id.dtCriacaoVitrine);
            btnVerVitrine=(Button) itemView.findViewById(R.id.btnVerMais);
        }


        @Override
        public void onClick(View v) {
            Vitrine vitrineSelecionada = vitrines.get(getPosition());
            Intent intent = new Intent(activity, VitrineActivity.class);
            intent.putExtra("vitrine", vitrineSelecionada);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptions options = ActivityOptions
                        .makeSceneTransitionAnimation(activity, imageVitrine, "capaVitrine");
                activity.startActivity(intent, options.toBundle());
            } else {
                activity.startActivity(intent);
            }
        }
    }
}
