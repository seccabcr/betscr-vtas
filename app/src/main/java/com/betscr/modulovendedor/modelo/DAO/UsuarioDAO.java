package com.betscr.modulovendedor.modelo.DAO;

import android.content.Context;
import com.betscr.modulovendedor.interfaces.IResult;
import com.betscr.modulovendedor.metodos.VolleyRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class UsuarioDAO extends VolleyRequest {

    public void login(Context ctx,JSONObject jsonSend,  IResult iResult){


        try {
            jsonSend.put("w", "apiLotto");
            jsonSend.put("r", "login_usuario");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.sendRequest(jsonSend, ctx, iResult);

    }

    public void cambiaPin(String idUsuario, String nuevoPin, Context ctx, IResult iResult){

        //String url = ctx.getString(R.string.ip);

        JSONObject jsonSend = new JSONObject();
        try {
            jsonSend.put("w", "apiLotto");
            jsonSend.put("r", "cambia_pin");
            jsonSend.put("id_usuario", idUsuario);
            jsonSend.put("nuevopin", nuevoPin);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.sendRequest(jsonSend, ctx, iResult);


    }

    public void consultaUsuarioOnLine(Context ctx,JSONObject jsonSend, IResult iResult) {

        try {
            jsonSend.put("w", "apiLotto");
            jsonSend.put("r", "consulta_usuario_online");


        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.sendRequest(jsonSend, ctx, iResult);

    }


    public void listaSorteosUsu(Context ctx, JSONObject jsonSend, IResult iResult) {

        try {
            jsonSend.put("w", "apiLotto");
            jsonSend.put("r", "lista_sorteos_usu");


        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.sendRequest(jsonSend, ctx, iResult);

    }

    public void listaSorteosUsuVta(Context ctx, JSONObject jsonSend, IResult iResult) {

        try {
            jsonSend.put("w", "apiLotto");
            jsonSend.put("r", "lista_sorteos_usu_vta");


        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.sendRequest(jsonSend, ctx, iResult);

    }


    public void consultaLiqDiaria(Context ctx, JSONObject jsonSend, IResult iResult) {

        try {
            jsonSend.put("w", "apiLotto");
            jsonSend.put("r", "consulta_liq_sorteo_usu");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.sendRequest(jsonSend, ctx, iResult);

    }
    public void consultaResumenDiaSorteos(Context ctx, JSONObject jsonSend, IResult iResult) {

        try {
            jsonSend.put("w", "apiLotto");
            jsonSend.put("r", "consulta_resumen_sorteos_usu");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.sendRequest(jsonSend, ctx, iResult);

    }

    public void consultaResumenPeriodo(Context ctx, JSONObject jsonSend, IResult iResult) {

        try {
            jsonSend.put("w", "apiLotto");
            jsonSend.put("r", "consulta_resumen_periodo_usu");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.sendRequest(jsonSend, ctx, iResult);

    }


    public void consultaTktsPremiados(Context ctx, JSONObject jsonSend, IResult iResult) {

        try {
            jsonSend.put("w", "apiLotto");
            jsonSend.put("r", "consulta_tkts_premiados");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.sendRequest(jsonSend, ctx, iResult);

    }


    public void consultaEstadoCuenta(Context ctx, JSONObject jsonSend, IResult iResult) {

        try {
            jsonSend.put("w", "apiLotto");
            jsonSend.put("r", "consulta_estado_cuenta_usu");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.sendRequest(jsonSend, ctx, iResult);

    }

    public void consultaSaldoUsuario(Context ctx, JSONObject jsonSend, IResult iResult) {

        try {
            jsonSend.put("w", "apiLotto");
            jsonSend.put("r", "consulta_saldo_usuario");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.sendRequest(jsonSend, ctx, iResult);

    }

}
