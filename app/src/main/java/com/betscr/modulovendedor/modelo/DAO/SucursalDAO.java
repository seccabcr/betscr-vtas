package com.betscr.modulovendedor.modelo.DAO;

import android.content.Context;

import com.betscr.modulovendedor.interfaces.IResult;
import com.betscr.modulovendedor.metodos.VolleyRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class SucursalDAO extends VolleyRequest {

    public void listaSucursales(Context ctx, IResult iResult) {

        JSONObject jsonSend = new JSONObject();
        try {
            jsonSend.put("w", "apiLotto");
            jsonSend.put("r", "lista_sucursales");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.sendRequest(jsonSend, ctx, iResult);

    }

}
