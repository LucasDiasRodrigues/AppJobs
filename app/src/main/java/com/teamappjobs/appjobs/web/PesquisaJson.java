package com.teamappjobs.appjobs.web;

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
import java.util.List;

public class PesquisaJson {

    public String PesquisaToJson(String palavras) {
        // Log.i("corredor json", corredor.toString());

        JSONObject jo = new JSONObject();
        try {
            jo.put("palavras", palavras);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Json PESQUISA= ", jo.toString());
        return jo.toString();
    }

    public String PesquisaCatToJson(int codSubCat) {
        // Log.i("corredor json", corredor.toString());

        JSONObject jo = new JSONObject();
        try {
            jo.put("codSubCat", codSubCat);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Json PESQUISA= ", jo.toString());
        return jo.toString();
    }

}
