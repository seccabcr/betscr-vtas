package com.betscr.modulovendedor.modelo.DAO;

import android.content.Context;

import com.betscr.modulovendedor.interfaces.IResult;
import com.betscr.modulovendedor.metodos.VolleyRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class VentaDAO extends VolleyRequest {

    public void registraVentatkt(Context ctx, JSONObject jsonSend, IResult iResult) {

        try {
            jsonSend.put("w", "apiLotto");
            jsonSend.put("r", "registra_venta_tkt");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.sendRequest(jsonSend, ctx, iResult);

    }

    public void consultaVentatkt(Context ctx, JSONObject jsonSend, IResult iResult) {

        try {
            jsonSend.put("w", "apiLotto");
            jsonSend.put("r", "consulta_venta_tkt");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.sendRequest(jsonSend, ctx, iResult);

    }

    public void listaVentaTkts(Context ctx, JSONObject jsonSend, IResult iResult) {

        try {
            jsonSend.put("w", "apiLotto");
            jsonSend.put("r", "lista_venta_tkts_usu");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.sendRequest(jsonSend, ctx, iResult);

    }

    public void anulaTkts(Context ctx, JSONObject jsonSend, IResult iResult) {

        try {
            jsonSend.put("w", "apiLotto");
            jsonSend.put("r", "anula_tkt");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.sendRequest(jsonSend, ctx, iResult);

    }

    public void consultaEstadoSorteo(Context ctx, JSONObject jsonSend, IResult iResult) {

        try {
            jsonSend.put("w", "apiLotto");
            jsonSend.put("r", "consulta_estado_sorteo");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.sendRequest(jsonSend, ctx, iResult);

    }

    public void clonarTkt(Context ctx, JSONObject jsonSend, IResult iResult) {

        try {
            jsonSend.put("w", "apiLotto");
            jsonSend.put("r", "clonar_tkt");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.sendRequest(jsonSend, ctx, iResult);

    }

    public void consultaDisVtaUsu(Context ctx, JSONObject jsonSend, IResult iResult) {

        try {
            jsonSend.put("w", "apiLotto");
            jsonSend.put("r", "consulta_dis_vta_usu");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.sendRequest(jsonSend, ctx, iResult);

    }

    public void actualizaRecarga(Context ctx, JSONObject jsonSend, IResult iResult) {

        try {
            jsonSend.put("w", "apiLotto");
            jsonSend.put("r", "actualiza_recarga");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.sendRequest(jsonSend, ctx, iResult);

    }

    public void consultaRecarga(Context ctx, JSONObject jsonSend, IResult iResult) {

        try {
            jsonSend.put("w", "apiLotto");
            jsonSend.put("r", "consulta_recarga");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.sendRequest(jsonSend, ctx, iResult);

    }

    public void anulaRecarga(Context ctx, JSONObject jsonSend, IResult iResult) {

        try {
            jsonSend.put("w", "apiLotto");
            jsonSend.put("r", "anula_recarga");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.sendRequest(jsonSend, ctx, iResult);

    }

    public void listaRecargas(Context ctx, JSONObject jsonSend, IResult iResult) {

        try {
            jsonSend.put("w", "apiLotto");
            jsonSend.put("r", "lista_recargas");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.sendRequest(jsonSend, ctx, iResult);

    }

    public void listaRecargasPeriodo(Context ctx, JSONObject jsonSend, IResult iResult) {

        try {
            jsonSend.put("w", "apiLotto");
            jsonSend.put("r", "lista_recargas_periodo_usu");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.sendRequest(jsonSend, ctx, iResult);

    }
}
