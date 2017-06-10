package com.teamappjobs.appjobs.fragment;

import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.teamappjobs.appjobs.R;
import com.teamappjobs.appjobs.activity.ChatActivity;
import com.teamappjobs.appjobs.activity.LoginActivity;
import com.teamappjobs.appjobs.activity.MainActivity;
import com.teamappjobs.appjobs.activity.MinhaVitrineActivity;
import com.teamappjobs.appjobs.activity.VitrineActivity;
import com.teamappjobs.appjobs.modelo.Vitrine;
import com.teamappjobs.appjobs.util.GeocodeAddressIntentService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by Lucas on 03/05/2016.
 */
public class VitrineSobreFragment extends Fragment {
    private Vitrine vitrine;
    private TextView txtDescricao;
    private TextView txtFaixaPreco;
    private TextView txtDataCriacao;
    private TextView txtLinks;
    private TextView txtTags;
    private TextView txtCategoria;
    private TextView txtLocalizacao;
    private TextView txtNomeAnunciante;
    private TextView txtEmailAnunciante;
    private TextView txtTelefoneAnunciante;
    private TextView tvTelefoneAnunciante;
    private Button btnChat;

    AddressResultReceiver mResultReceiver;

    private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            vitrine = ((MinhaVitrineActivity) getActivity()).getVitrine();
        } catch (Throwable e) {
            vitrine = ((VitrineActivity) getActivity()).getVitrine();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_vitrine_sobre, container, false);

        txtDescricao = (TextView) fragment.findViewById(R.id.txtDescricao);
        txtFaixaPreco = (TextView) fragment.findViewById(R.id.txtFaixaPreco);
        txtDataCriacao = (TextView) fragment.findViewById(R.id.txtDataCriacao);
        txtLinks = (TextView) fragment.findViewById(R.id.txtLinks);
        txtTags = (TextView) fragment.findViewById(R.id.txtTags);
        txtCategoria = (TextView) fragment.findViewById(R.id.txtCategoria);
        txtLocalizacao = (TextView) fragment.findViewById(R.id.txtLocalizacao);
        txtNomeAnunciante = (TextView) fragment.findViewById(R.id.txtNomeAnunciante);
        txtEmailAnunciante = (TextView) fragment.findViewById(R.id.txtEmailAnunciante);
        txtTelefoneAnunciante = (TextView) fragment.findViewById(R.id.txtTelefone);
        tvTelefoneAnunciante = (TextView) fragment.findViewById(R.id.tvTelefone);
        txtLocalizacao.setText("Localização indisponível");
        //mONICA
        mResultReceiver = new AddressResultReceiver(null);
        Intent intent = new Intent(getActivity(), GeocodeAddressIntentService.class);
        intent.putExtra(GeocodeAddressIntentService.RECEIVER, mResultReceiver);
        intent.putExtra(GeocodeAddressIntentService.FETCH_TYPE_EXTRA, GeocodeAddressIntentService.USE_ADDRESS_LOCATION);

        try {
            if (vitrine.getLocalizacao().equals("") || vitrine.getLocalizacao().isEmpty()) {
                txtLocalizacao.setText("Localização indisponível");
            } else {
                String auxlocalizacao[] = vitrine.getLocalizacao().split(",");
                String auxLat = auxlocalizacao[0];
                String auxLon = auxlocalizacao[1];

                intent.putExtra(GeocodeAddressIntentService.LOCATION_LATITUDE_DATA_EXTRA,
                        Double.parseDouble(auxLat));
                intent.putExtra(GeocodeAddressIntentService.LOCATION_LONGITUDE_DATA_EXTRA,
                        Double.parseDouble(auxLon));
                getActivity().startService(intent);
                //FIM MONICA
            }
        } catch (Exception e) {
            Log.i("Erro:", e.toString());
        }
        ;

        btnChat = (Button) fragment.findViewById(R.id.btnChat);
        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((VitrineActivity) getActivity()).verificaUsuarioLogado()) {
                    Intent it = new Intent(getActivity(), ChatActivity.class);
                    it.putExtra("vitrine", vitrine);
                    it.putExtra("origem", "faleComOAnunciante");
                    it.putExtra("fotoAnunciante", vitrine.getFoto());
                    it.putExtra("nomeAnunciante", vitrine.getNomeAnunciante());
                    startActivity(it);
                } else {
                    // Mandar para a tela de login/Cadastro
                    Intent it = new Intent(getActivity(), LoginActivity.class);
                    startActivity(it);
                }


            }
        });
        if (getActivity() instanceof MinhaVitrineActivity) {
            btnChat.setVisibility(View.GONE);
        }

        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();

        txtDescricao.setText(vitrine.getDescricao());
        txtFaixaPreco.setText(vitrine.getFaixaPreco());
        txtDataCriacao.setText(dateFormat.format(vitrine.getDataCriacao()));
        txtNomeAnunciante.setText(vitrine.getNomeAnunciante());
        txtEmailAnunciante.setText(vitrine.getEmailAnunciante());
        txtCategoria.setText(vitrine.getDescCategoria());

        if (vitrine.getTelefoneAnunciante() == null) {
            // tvTelefoneAnunciante.setVisibility(View.INVISIBLE);
            //txtTelefoneAnunciante.setVisibility(View.INVISIBLE);
            txtTelefoneAnunciante.setText("Não informado");
        } else {
            txtTelefoneAnunciante.setText(vitrine.getTelefoneAnunciante());
        }


        //Tags
        String auxTags = "";
        for (String tag : vitrine.getTags()) {
            auxTags = auxTags + tag + "  ";
        }
        txtTags.setText(auxTags);

        //Links
        String auxLinks = "";
        for (String link : vitrine.getLinks()) {
            auxLinks = auxLinks + link + "   ";
        }
        txtLinks.setText(auxLinks);

    }

    //Obviamente COPIEI E COLEI. PEGA EU HOTARIO!!!!!!!!!!!!!!
    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, final Bundle resultData) {
            if (resultCode == GeocodeAddressIntentService.SUCCESS_RESULT) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txtLocalizacao.setText(resultData.getString(GeocodeAddressIntentService.RESULT_DATA_KEY));
                        }
                    });
                }
            } else {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txtLocalizacao.setText("Localização indisponível");
                        }
                    });
                }
            }
        }
    }
}
