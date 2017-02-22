package com.teamappjobs.appjobs.web;


import com.teamappjobs.appjobs.modelo.Usuario;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by Lucas on 25/08/2016.
 */

//Para pegar dados complementares do facebook
public class FacebookJson {


    DateFormat dateBirthdayFormat = new SimpleDateFormat("MM/dd/yyyy");
    DateFormat sqlDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    DateFormat sqlDateHrFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public Usuario jsonToUsuario(String data) {
        // Log.i("corredor json", corredor.toString());

        Usuario usuario = new Usuario();

        try {
            JSONObject jo = new JSONObject(data);


            if (jo.has("name"))
                usuario.setNome(jo.getString("name"));
            else
                usuario.setNome("");

            if (jo.has("birthday")) {
                String birthday = jo.getString("birthday");
                usuario.setDataNasc(dateBirthdayFormat.parse(birthday));
            }


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return usuario;

    }

    public String jsonToCoverPic(String data) {
        // Log.i("corredor json", corredor.toString());
        String coverUrl = "";

        try {

            JSONObject jo = new JSONObject(data);

            if (!(jo.getJSONObject("cover")).equals("null")) {
                JSONObject joCover = jo.getJSONObject("cover");
                coverUrl = joCover.getString("source");

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return coverUrl;


    }

}
