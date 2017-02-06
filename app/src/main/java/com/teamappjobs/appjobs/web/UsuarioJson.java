package com.teamappjobs.appjobs.web;

import android.os.Bundle;
import android.util.Log;

import com.teamappjobs.appjobs.modelo.Usuario;

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
 * Created by Lucas on 17/06/2015.
 */
public class UsuarioJson {

    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
    DateFormat sqlDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public String UsuarioToJson(Usuario usuario) {
        // Log.i("corredor json", corredor.toString());


        JSONStringer jsonStringer = new JSONStringer();
        try {
            jsonStringer.object()

                    .key("nome").value(usuario.getNome())
                    .key("sobrenome").value(usuario.getSobreNome())
                    .key("email").value(usuario.getEmail())
                    .key("gcmIdAtual").value(usuario.getGcmIdAtual())
                    .key("senha").value(usuario.getSenha())
                    .key("dataNasc").value(sqlDateFormat.format(usuario.getDataNasc()))
                    .key("sexo").value(usuario.getSexo())
                    .key("imagemPerfil").value(usuario.getImagemPerfil())
                    .key("situacao").value(usuario.getSituacao())
                    .key("dataCadastro").value(dateFormat.format(usuario.getDataCadastro()))
                    .key("telefone").array();

            List<String> telefones = usuario.getTelefones();

            for (String item : telefones) {

                jsonStringer.object()
                        .key("numTelefone").value(item)
                        .endObject();
            }

            jsonStringer.endArray()
                    .key("gcmId").array();


            List<String> gcmIds = usuario.getGcmIds();

            for (String item : gcmIds) {

                jsonStringer.object()
                        .key("numGcm").value(item)
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


    public Usuario JsonToUsuario(String data) {
        Usuario usuario = new Usuario();

        try {
            JSONObject jo = new JSONObject(data);

            usuario.setNome(jo.getString("nome"));
            usuario.setSobreNome(jo.getString("sobrenome"));
            usuario.setDataNasc(sqlDateFormat.parse(jo.getString("dataNasc")));
            usuario.setEmail(jo.getString("email"));
            usuario.setSexo(jo.getString("sexo"));
            usuario.setImagemPerfil(jo.getString("foto"));
            usuario.setSenha(jo.getString("senha"));
            usuario.setSituacao(jo.getString("situacao"));

            ArrayList<String> telefones = new ArrayList<String>();
            JSONObject jsonData = new JSONObject();
            jsonData = jo.optJSONObject("telefone");
            String aux=jsonData.getString("numTelefone");
            telefones.add(aux);


            usuario.setTelefone(telefones);

/*
            if (!(jo.getString("dataCadastro")).equals("null")) {
                DateFormat format = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
                usuario.setDataCadastro(format.parse(jo.getString("dataCadastro")));
            }
*/
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return usuario;
    }

    public List JsonArrayToListaUsuario(String data) {
        List<Usuario> list = new ArrayList<>();
        try {
            JSONArray json = new JSONArray(data);

            for (int i = 0; i < json.length(); i++) {
                Usuario corredor = JsonToUsuario(json.getJSONObject(i).toString());
                list.add(corredor);
                Log.i("Corredor" + i + " ", corredor.getNome());
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return list;
    }
}
