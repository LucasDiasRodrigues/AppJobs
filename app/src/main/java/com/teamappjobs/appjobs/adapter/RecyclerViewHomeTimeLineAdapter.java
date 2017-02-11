package com.teamappjobs.appjobs.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class RecyclerViewHomeTimeLineAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static Activity activity;
    private static List<Vitrine> vitrines = new ArrayList<Vitrine>();
    private LayoutInflater layoutInflater;
    private DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private boolean GEO;

    private String headerTitulo;
    private String headerSubtitulo;

    private static final int tipo_header = 0;
    private static final int tipo_item = 1;
    private static SharedPreferences prefs;
    private static String user;

    public RecyclerViewHomeTimeLineAdapter(Activity activity, List<Vitrine> vitrines, String headerTitulo, String headerSub) {
        this.activity = activity;
        this.vitrines = vitrines;
        this.layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.GEO = GEO;
        this.headerTitulo = headerTitulo;
        this.headerSubtitulo = headerSub;
        prefs = activity.getSharedPreferences("Configuracoes", Context.MODE_PRIVATE);
        user = prefs.getString("email", "");
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == tipo_header) {
            View view = layoutInflater.inflate(R.layout.item_header_recyclerview, parent, false);
            HeaderViewHolder headerViewHolder = new HeaderViewHolder(view);
            return headerViewHolder;
        } else {
            View view = layoutInflater.inflate(R.layout.item_list_todas_vitrines, parent, false);
            ItemViewHolder itemViewHolder = new ItemViewHolder(view);
            return itemViewHolder;
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            headerViewHolder.txtTitulo.setText(headerTitulo);
            if (!headerSubtitulo.equals("")) {
                headerViewHolder.txtSubtitulo.setVisibility(View.VISIBLE);
                headerViewHolder.txtSubtitulo.setText(headerSubtitulo);
            } else {
                headerViewHolder.txtSubtitulo.setVisibility(View.GONE);
            }
        } else {
            int currentPosition = position - 1;
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            Picasso.with(activity).load(activity.getResources().getString(R.string.imageserver) + vitrines.get(currentPosition).getFoto()).into(itemViewHolder.imageVitrine);
            itemViewHolder.txtNomeVitrine.setText(vitrines.get(currentPosition).getNome());
            itemViewHolder.txtCategoriaVitrine.setText(vitrines.get(currentPosition).getDescCategoria());
            itemViewHolder.txtDtCriacao.setText(format.format(vitrines.get(currentPosition).getDataCriacao()));
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return tipo_header;
        else
            return tipo_item;
    }

    @Override
    public int getItemCount() {
        return vitrines.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imageVitrine;
        private TextView txtNomeVitrine;
        private TextView txtCategoriaVitrine;
        private TextView txtSubcatVitrine;
        private TextView txtDtCriacao;
        private Button btnVerVitrine;

        public ItemViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            imageVitrine = (ImageView) itemView.findViewById(R.id.imageViewCapa);
            txtNomeVitrine = (TextView) itemView.findViewById(R.id.txtNomeVitrine);
            txtCategoriaVitrine = (TextView) itemView.findViewById(R.id.categoriaVitrine);
            //  txtSubcatVitrine = (TextView) itemView.findViewById(R.id.subcategoriaVitrine);
            txtDtCriacao = (TextView) itemView.findViewById(R.id.dtCriacaoVitrine);
            btnVerVitrine = (Button) itemView.findViewById(R.id.btnVerMais);
        }


        @Override
        public void onClick(View v) {
            int currentPosition = getAdapterPosition() - 1;
            Vitrine vitrineSelecionada = vitrines.get(currentPosition);

            //verifica se o usuario eh o dono da vitrine e o direciona para a activity correta
            if(vitrineSelecionada.getEmailAnunciante().equals(user)){
                Intent intent = new Intent(activity, MinhaVitrineActivity.class);
                intent.putExtra("vitrine", vitrineSelecionada);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions
                            .makeSceneTransitionAnimation(activity, imageVitrine, "capaVitrine");
                    activity.startActivity(intent, options.toBundle());
                } else {
                    activity.startActivity(intent);
                }
            } else {
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

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTitulo;
        private TextView txtSubtitulo;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            txtTitulo = (TextView) itemView.findViewById(R.id.txtTitulo);
            txtSubtitulo = (TextView) itemView.findViewById(R.id.txtSubtitulo);
        }
    }
}
