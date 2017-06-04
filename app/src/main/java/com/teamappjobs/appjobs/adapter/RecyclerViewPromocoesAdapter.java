package com.teamappjobs.appjobs.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.teamappjobs.appjobs.R;
import com.teamappjobs.appjobs.activity.MinhaVitrineActivity;
import com.teamappjobs.appjobs.activity.PublicarPromocaoActivity;
import com.teamappjobs.appjobs.activity.VitrineActivity;
import com.teamappjobs.appjobs.asyncTask.AtivaPromocaoTask;
import com.teamappjobs.appjobs.asyncTask.CancelaPromocaoTask;
import com.teamappjobs.appjobs.modelo.Promocao;
import com.teamappjobs.appjobs.modelo.Vitrine;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Lucas on 10/05/2016.
 */
public class RecyclerViewPromocoesAdapter extends RecyclerView.Adapter<RecyclerViewPromocoesAdapter.MyViewHolder> {
    private Context context;
<<<<<<< HEAD
    private List<Promocao> promocoes;
=======
    private static List<Promocao> promocoes;
    private  List<Vitrine> vitrines;
    private LayoutInflater layoutInflater;
>>>>>>> GitHub/Monica
    private boolean criador=false;
    private static Fragment fragment;
    private DateFormat dtFormat = new SimpleDateFormat("dd/MM/yyyy");
    private DateFormat dthrFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private Vitrine vitrine;
    private SharedPreferences prefs ;


    public RecyclerViewPromocoesAdapter(Context context, List<Promocao> promocoes, List<Vitrine> vitrines, Fragment fragment) {
        this.context = context;
        this.promocoes = promocoes;
        this.vitrines=vitrines;
        this.fragment = fragment;

    }

    public RecyclerViewPromocoesAdapter(Context context, List<Promocao> promocoes,Vitrine vitrine, Fragment fragment) {
        this.context = context;
        this.promocoes = promocoes;
        this.vitrine=vitrine;
        this.fragment = fragment;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = ((Activity)context).getLayoutInflater().inflate(R.layout.item_list_promocoes, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        prefs=  context.getSharedPreferences("Configuracoes", Context.MODE_PRIVATE);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.txtNome.setText(promocoes.get(position).getNome());
        holder.txtDescricao.setText(promocoes.get(position).getDescricao());
        holder.txtRegras.setText(promocoes.get(position).getRegras());
        holder.txtDataInicio.setText(dtFormat.format(promocoes.get(position).getDataInicio()));
        holder.txtDataFim.setText(dtFormat.format(promocoes.get(position).getDataFim()));
        holder.txtNomeVitrine.setText(promocoes.get(position).getVitrine().getNome());

        //Gambis!!!!!!!!!!!!!!!!!
        try {
            if(vitrine.getEmailAnunciante().equals(prefs.getString("email",""))){
                criador=true;
            }
            else
            {
                criador=false;
            }
        }
        catch (Exception ex){
            criador=false;
        }


        if (criador) {


            holder.toolbar.setVisibility(View.VISIBLE);
            holder.llCriador.setVisibility(View.VISIBLE);
            holder.txtDataCriacao.setText(dthrFormat.format(promocoes.get(position).getDataHrCriacao()));
            holder.txtSituacao.setText(promocoes.get(position).getSituacao());
             holder.toolbar.getMenu().clear();

            if(promocoes.get(position).getSituacao().equals("ativa")){
                holder.toolbar.inflateMenu(R.menu.menu_item_promocao);
                holder.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.cancelarPromocao:
                                new AlertDialog.Builder(context).setTitle(R.string.cancelarPromocao)
                                        .setMessage(R.string.desejaCancelarPromocao)
                                        .setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                CancelaPromocaoTask task = new CancelaPromocaoTask(context, promocoes.get(position), fragment);
                                                task.execute();
                                            }
                                        })
                                        .setNegativeButton(R.string.nao, null).show();
                                break;
                            case R.id.editarPromocao:
                                //TESTE

                                Intent intentEditar = new Intent(context, PublicarPromocaoActivity.class);
                                intentEditar.putExtra("vitrine",vitrine);
                                intentEditar.putExtra("promocao", promocoes.get(position));
                                intentEditar.putExtra("editar",true);
                                context.startActivity(intentEditar);


                                break;
                        }
                        return false;
                    }
                });
            }
            else {
                //Vitrine inativa
                holder.toolbar.inflateMenu(R.menu.menu_item_promocao_reativar);
                holder.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.reativarPromocao:
                                new AlertDialog.Builder(context).setTitle(R.string.ativarPromocao)
                                        .setMessage(R.string.desejaReativarPromocao)
                                        .setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                AtivaPromocaoTask task = new AtivaPromocaoTask(context, promocoes.get(position), fragment);
                                                task.execute();
                                            }
                                        })
                                        .setNegativeButton(R.string.nao, null).show();
                                break;
                            case R.id.editarPromocao:
                                //TESTE

                                Intent intentEditar = new Intent(context, PublicarPromocaoActivity.class);
                                intentEditar.putExtra("vitrine", vitrine);
                                intentEditar.putExtra("promocao", promocoes.get(position));
                                intentEditar.putExtra("editar", true);
                                context.startActivity(intentEditar);

                                break;
                        }
                        return false;
                    }
                });


            }

        } else {
            holder.toolbar.setVisibility(View.GONE);
            holder.llCriador.setVisibility(View.GONE);
        }

        if (!(promocoes.get(position).getFoto().equals(""))) {
            holder.imageViewCapaVitrine.setVisibility(View.VISIBLE);
            Picasso.with(context).load(context.getResources().getString(R.string.imageserver) + promocoes.get(position).getFoto()).into(holder.imageViewCapaVitrine);
        } else {
            holder.imageViewCapaVitrine.setVisibility(View.GONE);
        }

        if(promocoes.get(position).getFotoVitrine().isEmpty()){
            Picasso.with(context).load(R.drawable.nopic_thumb)
                    .into(holder.imageViewThumb);
        }
        else {

            Picasso.with(context).load(context.getResources().getString(R.string.imageservermini) + promocoes.get(position).getFotoVitrine())
                    .into(holder.imageViewThumb);
        }




    }


    @Override
    public int getItemCount() {
        return promocoes.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtNome;
        private TextView txtDescricao;
        private TextView txtRegras;
        private TextView txtDataInicio;
        private TextView txtDataFim;
        private LinearLayout llCriador;
        private TextView txtDataCriacao;
        private TextView txtSituacao;
        private ImageView imageViewCapaVitrine;
        private Toolbar toolbar;
        private CircleImageView imageViewThumb;
        private TextView txtNomeVitrine;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            txtNome=(TextView) itemView.findViewById(R.id.txtNome);
            txtDescricao = (TextView) itemView.findViewById(R.id.txtDescricao);
            txtRegras = (TextView) itemView.findViewById(R.id.txtRegras);
            txtDataInicio = (TextView) itemView.findViewById(R.id.txtDataInicio);
            txtDataFim = (TextView) itemView.findViewById(R.id.txtDataFim);
            txtNomeVitrine=(TextView) itemView.findViewById(R.id.txtNomeVitrine);

            //Imagem
            imageViewThumb = (CircleImageView) itemView.findViewById(R.id.imageViewThumb);
            imageViewCapaVitrine=(ImageView) itemView.findViewById(R.id.imageVCapaPromo);

            //Criador
            llCriador = (LinearLayout) itemView.findViewById(R.id.llCriadorPromocao);
            txtDataCriacao = (TextView) itemView.findViewById(R.id.txtDataCriacao);
            txtSituacao = (TextView) itemView.findViewById(R.id.txtSituacao);
            toolbar = (Toolbar) itemView.findViewById(R.id.toolbar);
        }


        @Override
        public void onClick(View view) {
            Vitrine vitrineSelecionada = promocoes.get(getPosition()).getVitrine();
            Intent intent = new Intent(fragment.getActivity(), VitrineActivity.class);
            intent.putExtra("vitrine", vitrineSelecionada);
            fragment.getActivity().startActivity(intent);
        }
    }
}
