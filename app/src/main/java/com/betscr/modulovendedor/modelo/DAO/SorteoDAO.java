package com.betscr.modulovendedor.modelo.DAO;

import android.content.Context;

import com.betscr.modulovendedor.interfaces.IResult;
import com.betscr.modulovendedor.metodos.VolleyRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class SorteoDAO extends VolleyRequest {

    public void listaSorteosUsu(int codigoUsuario, Context ctx, IResult iResult){

        JSONObject jsonSend = new JSONObject();
        try {
            jsonSend.put("w", "apiLotto");
            jsonSend.put("r", "lista_sorteos_usu");
            jsonSend.put("cod_usuario", codigoUsuario);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.sendRequest(jsonSend, ctx, iResult);

    }

}
