package com.teamappjobs.appjobs.web;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.teamappjobs.appjobs.modelo.Usuario;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Lucas on 28/04/2016.
 */
public class LoginJson {

    private String resposta = "";

    public String loginToJson(String email, String senha, String gcmId) {
        JSONObject jo = new JSONObject();
        try {
            jo.put("email", email);
            jo.put("senha", senha);
            jo.put("gcmId", gcmId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jo.toString();
    }

    public Object loginToString(String data) {

        Usuario usuario = new Usuario();

        try {
            JSONObject jo = new JSONObject(data);


            usuario.setEmail(jo.getString("email"));
            usuario.setNome(jo.getString("nome"));
            usuario.setImagemPerfil(jo.getString("foto"));


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return usuario;


    }

    public String logoffToJson(String email, String gcmId) {
        JSONObject jo = new JSONObject();
        try {
            jo.put("email", email);
            jo.put("gcmId", gcmId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jo.toString();
    }


}
