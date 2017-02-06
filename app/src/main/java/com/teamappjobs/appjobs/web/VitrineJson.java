package com.teamappjobs.appjobs.web;

import android.os.Bundle;
import android.util.Log;

import com.teamappjobs.appjobs.modelo.Portifolio;
import com.teamappjobs.appjobs.modelo.Promocao;
import com.teamappjobs.appjobs.modelo.Usuario;
import com.teamappjobs.appjobs.modelo.Vitrine;

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

/**
 * Created by Lucas on 27/04/2016.
 */
public class VitrineJson {

    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    DateFormat sqlDateHrFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    DateFormat sqlDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public String VitrineToJson(Vitrine vitrine) {

        JSONStringer jsonStringer = new JSONStringer();
        try {
            jsonStringer.object()

                    .key("cod_vitrine").value(vitrine.getCodVitrine())
                    .key("nome").value(vitrine.getNome())
                    .key("descricao").value(vitrine.getDescricao())
                    .key("faixa_preco").value(vitrine.getFaixaPreco())
                    .key("unidades").value(vitrine.getUnidades())
                    .key("localizacao").value(vitrine.getLocalizacao())
                    .key("parametro_preco").value(vitrine.getParametroPreco())
                    .key("situacao").value(vitrine.getSituacao())
                    .key("email_anunciante").value(vitrine.getEmailAnunciante())
                    .key("nome_anunciante").value(vitrine.getNomeAnunciante())
                    .key("telefone_anunciante").value(vitrine.getTelefoneAnunciante())
                    .key("dthr_criacao").value(dateFormat.format(vitrine.getDataCriacao()))
                    .key("cod_subcat").value(vitrine.getSubCategoria())
                    .key("desc_cat").value(vitrine.getDescCategoria())
                    .key("foto").value(vitrine.getFoto())
                    .key("link").array();

            List<String> links = vitrine.getLinks();
            for (String item : links) {
                jsonStringer.object()
                        .key("link").value(item)
                        .endObject();
            }

            jsonStringer.endArray()
                    .key("tag").array();


            List<String> tags = vitrine.getTags();

            for (String item : tags) {
                jsonStringer.object()
                        .key("tag").value(item)
                        .endObject();
            }

            jsonStringer.endArray()

                    .endObject();


        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Json = ", jsonStringer.toString());
        return jsonStringer.toString();
    }

    public String CurtirVitrineToJson(int codVitrine, String email) {
        JSONStringer jsonStringer = new JSONStringer();

        try {
            jsonStringer.object()
                    .key("cod_vitrine").value(codVitrine)
                    .key("email").value(email)
                    .endObject();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("JsonCurtirVitrine = ", jsonStringer.toString());
        return jsonStringer.toString();
    }






    public Bundle DetalhesVitrineJsonToDetalhesVitrine(String data) {
        Bundle bundle = new Bundle();
        int curtidas;
        int seguidores;
        String foto;
        try {
            JSONObject jo = new JSONObject(data);

            curtidas = jo.getInt("curtidas");
            seguidores = jo.getInt("seguidores");
            foto = jo.getString("foto");

            bundle.putInt("curtidas", curtidas);
            bundle.putInt("seguidores", seguidores);
            bundle.putString("foto", foto);

            return bundle;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Bundle VerificaCurtidaVitrineJsonToDetalhesVitrine(String data) {
        Bundle bundle = new Bundle();
        String RespostaCurte;
        String RespostaSegue;
        try {
            JSONObject jo = new JSONObject(data);
            RespostaCurte = jo.getString("resposta_curte");
            RespostaSegue= jo.getString("resposta_segue");


            bundle.putString("RespostaCurte", RespostaCurte);
            bundle.putString("RespostaSegue", RespostaSegue);
            return bundle;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Vitrine JsonToVitrine(String data) {
        Vitrine vitrine = new Vitrine();
        try {
            JSONObject jo = new JSONObject(data);

            vitrine.setCodVitrine(jo.getInt("cod_vitrine"));
            vitrine.setNome(jo.getString("nome"));
            vitrine.setDescricao(jo.getString("descricao"));
            vitrine.setNomeAnunciante(jo.getString("nome_anunciante"));

            if(!(jo.getString("telefone_anunciante")).equals("null")) {
                vitrine.setTelefoneAnunciante(jo.getString("telefone_anunciante"));
            }

            if (!(jo.getString("faixa_preco")).equals("null")) {
                vitrine.setFaixaPreco(jo.getString("faixa_preco"));
            }

            if (!(jo.getString("curtidas")).equals("null")) {
                vitrine.setCurtidas((jo.getString("curtidas")+" curtidas"));
            }

            if ((jo.getString("curtidas")).equals("null")) {
                vitrine.setCurtidas("0 curtidas");
            }


            if (!(jo.getString("seguidores")).equals("null")) {
                vitrine.setSeguidores(jo.getString("seguidores"));
            }
            if ((jo.getString("seguidores")).equals("null")) {
                vitrine.setSeguidores("0");
            }

            if (!(jo.getString("parametro_preco")).equals("null")) {
                vitrine.setParametroPreco(jo.getString("parametro_preco"));
            }

            if (!(jo.getString("unidades")).equals("null")) {
                vitrine.setUnidades(jo.getInt("unidades"));
            }

            if (!(jo.getString("foto")).equals("null")) {
                vitrine.setFoto(jo.getString("foto"));
            }

            if (!(jo.getString("localizacao")).equals("null")) {
                vitrine.setLocalizacao(jo.getString("localizacao"));
            }

            if (!(jo.getString("situacao")).equals("null")) {
                vitrine.setSituacao(jo.getString("situacao"));
            }
            if (!(jo.getString("email_anunciante")).equals("null")) {
                vitrine.setEmailAnunciante(jo.getString("email_anunciante"));
            }
            if (!(jo.getString("cod_subcat")).equals("null")) {
                vitrine.setSubCategoria(jo.getInt("cod_subcat"));
            }

            if (!(jo.getString("desc_cat")).equals("null")) {
                vitrine.setDescCategoria(jo.getString("desc_cat"));
            }

            JSONArray arrayLink = new JSONArray(jo.getString("link"));
            List<String> listLink = new ArrayList<>();
            for (int i = 0; i < arrayLink.length(); i++) {
                JSONObject jsonObjectLink = arrayLink.getJSONObject(i);
                listLink.add(jsonObjectLink.getString("link"));

            }
            vitrine.setLinks(listLink);

            JSONArray arrayTag = new JSONArray(jo.getString("tag"));
            List<String> listTag = new ArrayList<>();
            for (int i = 0; i < arrayTag.length(); i++) {
                JSONObject jsonObjectTag = arrayTag.getJSONObject(i);
                listTag.add(jsonObjectTag.getString("tag"));

            }
            vitrine.setTags(listTag);

            if (!(jo.getString("dthr_criacao")).equals("null")) {
                vitrine.setDataCriacao(sqlDateHrFormat.parse(jo.getString("dthr_criacao")));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return vitrine;
    }

    public List<Vitrine> JsonArrayToListaVitrines(String data) {
        List<Vitrine> list = new ArrayList<>();
        try {
            JSONArray json = new JSONArray(data);
            for (int i = 0; i < json.length(); i++) {
                Vitrine vitrine = JsonToVitrine(json.getJSONObject(i).toString());
                list.add(vitrine);
                Log.i("Vitrine" + i + " ", vitrine.getNome());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public String PortifolioToJson(Portifolio portifolio) {

        JSONStringer jsonStringer = new JSONStringer();
        try {
            jsonStringer.object()

                    .key("cod_vitrine").value(portifolio.getCodVitrine())
                    .key("foto").value(portifolio.getImagem())
                    .key("descricao").value(portifolio.getDescricao())
                    .key("dthr_upload").value(sqlDateHrFormat.format(portifolio.getDataCadastro()))

                    .endObject();


        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Json = ", jsonStringer.toString());
        return jsonStringer.toString();
    }


    public Portifolio JsonToPortifolio(String data) {
        Portifolio portifolio = new Portifolio();
        try {
            JSONObject jo = new JSONObject(data);

            portifolio.setCodVitrine(jo.getInt("cod_vitrine"));
            portifolio.setImagem(jo.getString("foto"));
            if (!(jo.getString("descricao")).equals("null")) {
                portifolio.setDescricao(jo.getString("descricao"));
            }
            if (!(jo.getString("dthr_upload")).equals("null")) {
                portifolio.setDataCadastro(sqlDateHrFormat.parse(jo.getString("dthr_upload")));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return portifolio;
    }

    public ArrayList<Portifolio> JsonArrayToListaPortifolio(String data) {
        ArrayList<Portifolio> list = new ArrayList<>();
        try {
            JSONArray json = new JSONArray(data);
            for (int i = 0; i < json.length(); i++) {
                Portifolio portifolio = JsonToPortifolio(json.getJSONObject(i).toString());
                list.add(portifolio);
                Log.i("Vitrine" + i + " ", portifolio.getDescricao());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public String PromocaoToJson(Promocao promocao) {

        JSONStringer jsonStringer = new JSONStringer();
        try {
            jsonStringer.object()

                    .key("cod_vitrine").value(promocao.getCodVitrine())
                    .key("cod_promo").value(promocao.getCodPromocao())
                    .key("foto").value(promocao.getFoto())
                    .key("descricao").value(promocao.getDescricao())
                    .key("nome").value(promocao.getNome())
                    .key("regras").value(promocao.getRegras())
                    .key("situacao").value(promocao.getSituacao())
                    .key("dt_inicio").value(sqlDateFormat.format(promocao.getDataInicio()))
                    .key("dt_fim").value(sqlDateFormat.format(promocao.getDataFim()))
                    .key("dthr_criacao").value(sqlDateHrFormat.format(promocao.getDataHrCriacao()))

                    .endObject();


        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Json = ", jsonStringer.toString());
        return jsonStringer.toString();
    }


    public Promocao JsonToPromocao(String data) {
        Promocao promocao = new Promocao();
        try {
            JSONObject jo = new JSONObject(data);

            promocao.setCodVitrine(jo.getInt("cod_vitrine"));
            promocao.setCodPromocao(jo.getInt("cod_promo"));
            promocao.setDescricao(jo.getString("descricao"));
            promocao.setNome(jo.getString("nome"));
            promocao.setSituacao(jo.getString("situacao"));
            promocao.setFotoVitrine(jo.getString("foto_vitrine"));

            if (!(jo.getString("foto")).equals("null")) {
                promocao.setFoto(jo.getString("foto"));
            } else{
                promocao.setFoto("");
            }
            if (!(jo.getString("regras")).equals("null")) {
                promocao.setRegras(jo.getString("regras"));
            }

            if (!(jo.getString("dt_inicio")).equals("null")) {
                promocao.setDataInicio(sqlDateFormat.parse(jo.getString("dt_inicio")));
            }
            if (!(jo.getString("dt_fim")).equals("null")) {
                promocao.setDataFim(sqlDateFormat.parse(jo.getString("dt_fim")));
            }
            if (!(jo.getString("dthr_criacao")).equals("null")) {
                promocao.setDataHrCriacao(sqlDateHrFormat.parse(jo.getString("dthr_criacao")));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return promocao;
    }

    public ArrayList<Promocao> JsonArrayToListaPromocoes(String data) {
        ArrayList<Promocao> list = new ArrayList<>();
        try {
            JSONArray json = new JSONArray(data);
            for (int i = 0; i < json.length(); i++) {
                Promocao promocao = JsonToPromocao(json.getJSONObject(i).toString());
                list.add(promocao);
                Log.i("Promocao" + i + " ", promocao.getDescricao());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }


}
