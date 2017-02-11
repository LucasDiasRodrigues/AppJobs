package com.teamappjobs.appjobs.web;

import android.os.Bundle;
import android.util.Log;

import com.teamappjobs.appjobs.modelo.Mensagem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MensagemJson {


    public String MensagemToJson(Mensagem mensagem) {

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");

        JSONStringer jsonStringer = new JSONStringer();
        try {
            jsonStringer.object()

                    .key("remetente").value(mensagem.getRemetente())
                    .key("destinatario").value(mensagem.getDestinatario())
                    .key("mensagem").value(mensagem.getMsg())
                    .key("dthrEnviada").value(dateFormat.format(mensagem.getDthrEnviada()))
                    .key("situacao").value(mensagem.getSituacao())

                    .endObject();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Json = ", jsonStringer.toString());
        return jsonStringer.toString();
    }

    public String SolicitaMensagem(String emailRemetente, String emailDestinatario, String leitor, int page) {


        JSONStringer jsonStringer = new JSONStringer();
        try {
            jsonStringer.object()

                    .key("remetente").value(emailRemetente)
                    .key("destinatario").value(emailDestinatario)
                    .key("leitor").value(leitor)
                    .key("page").value(page)

                    .endObject();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Json = ", jsonStringer.toString());
        return jsonStringer.toString();

    }

    public Mensagem JsonToMensagem(String data) {
        Mensagem mensagem = new Mensagem();

        try {
            JSONObject jo = new JSONObject(data);


            mensagem.setRemetente(jo.getString("remetente"));
            mensagem.setDestinatario(jo.getString("destinatario"));
            mensagem.setCodMsg(jo.getInt("codMsg"));
            mensagem.setMsg(jo.getString("msg"));

            DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            mensagem.setDthrEnviada(format.parse(jo.getString("dthrEnviada")));
            if (!(jo.getString("dthrRecebida").equals("null"))) {
                mensagem.setDthrEnviada(format.parse(jo.getString("dthrRecebida")));
            }
            if (!(jo.getString("dthrVisualizada").equals("null"))) {
                mensagem.setDthrEnviada(format.parse(jo.getString("dthrVisualizada")));
            }
            mensagem.setSituacao(jo.getString("situacao"));


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return mensagem;
    }


    public List<Mensagem> JsonArrayToListaMensagem(String data) {
        List<Mensagem> list = new ArrayList<>();
        try {
            JSONArray json = new JSONArray(data);

            for (int i = 0; i < json.length(); i++) {
                Mensagem mensagem = JsonToMensagem(json.getJSONObject(i).toString());
                list.add(mensagem);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return list;
    }



    public List<Bundle> JsonToNotificacoesCorredor(String data) {


        List<Bundle> bundles = new ArrayList<Bundle>();


        try {
            JSONArray json = new JSONArray(data);

            for (int i = 0; i < json.length(); i++) {

                Bundle bundle = new Bundle();
                String nome = json.getJSONObject(i).getString("nome");
                String email = json.getJSONObject(i).getString("email");
                String profPic = json.getJSONObject(i).getString("imagemperfil");
                String qtdMsg = json.getJSONObject(i).getString("MSG_NAO_LIDAS");

                bundle.putString("nome",nome);
                bundle.putString("email",email);
                bundle.putString("profPic", profPic);
                bundle.putString("qtdMsg", qtdMsg);


                bundles.add(bundle);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return bundles;



    }

    public List<Bundle> JsonToNotificacoes(String data) {

        List<Bundle> bundles = new ArrayList<Bundle>();


        try {
            JSONArray json = new JSONArray(data);

            for (int i = 0; i < json.length(); i++) {

                Bundle bundle = new Bundle();
                String nome = json.getJSONObject(i).getString("nome");
                String email = json.getJSONObject(i).getString("email");
                String profPic = json.getJSONObject(i).getString("imagemperfil");
                String qtdMsg = json.getJSONObject(i).getString("MSG_NAO_LIDAS");

                bundle.putString("nome",nome);
                bundle.putString("email",email);
                bundle.putString("foto", profPic);
                bundle.putString("qtdMsg", qtdMsg);


                bundles.add(bundle);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return bundles;

    }

    public List<Bundle> JsonToListaConversas(String data) {

        List<Bundle> bundles = new ArrayList<Bundle>();


        try {
            JSONArray json = new JSONArray(data);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            for (int i = 0; i < json.length(); i++) {

                Bundle bundle = new Bundle();
                String nome = json.getJSONObject(i).getString("nome");
                String email = json.getJSONObject(i).getString("email");
                String profPic = json.getJSONObject(i).getString("imagemperfil");
                Date dataUltimaMsg = dateFormat.parse(json.getJSONObject(i).getString("data"));
                int qtdMsg = json.getJSONObject(i).getInt("MSG_NAO_LIDAS");

                bundle.putString("nome",nome);
                bundle.putString("email",email);
                bundle.putString("foto", profPic);
                bundle.putSerializable("data",dataUltimaMsg);
                bundle.putInt("qtdMsg", qtdMsg);


                bundles.add(bundle);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return bundles;

    }


}
