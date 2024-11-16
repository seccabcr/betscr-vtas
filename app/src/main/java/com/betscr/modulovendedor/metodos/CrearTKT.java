package com.betscr.modulovendedor.metodos;

import com.betscr.modulovendedor.modelo.Variables;

import java.text.DecimalFormat;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author Gonzalo Cabrera
 */
public class CrearTKT {

    public static String tkt80mm(JSONObject datos) {

        DecimalFormat fMonto = new DecimalFormat("###,###,##0");

        String sNum, sMon;
        String mNomSorteo = datos.optString("nom_sorteo");
        int mFacPremio = datos.optInt("fac_premio");
        String mNumTkt = datos.optString("num_tkt");
        String mFechaTkt = datos.optString("fecha_tkt");
        String mHoraTkt = datos.optString("hora_tkt");
        String mReferencia = datos.optString("referencia");
        int mTotalTkt = datos.optInt("total_tkt");
        JSONArray listaVentaTkt = datos.optJSONArray("detalle");

        StringBuilder builder = new StringBuilder();
        builder.append(new char[]{27, '@'});
        builder.append(new char[]{27, 't', 2}); // Set de caracateres PC-850
        builder.append(new char[]{27, 'd', 3}); // Avance de 3 lineas

        if (Variables.TIT_TKT.length() > 0) {
            builder.append(new char[]{27, '!', 24}); // doble altura
            builder.append(new char[]{27, 'a', 1}); // Alineacion centrada
            builder.append(Variables.TIT_TKT).append("\r\n");
        }

        builder.append(new char[]{27, '!', 8}); // Negrita, 10cpp
        builder.append("Tiquete de compra").append("\r\n");
        builder.append("Numero Tiquete: ").append(mNumTkt).append("\r\n");

        if (datos.optBoolean("reimprime")) {
            builder.append("*** REIMPRESION ***").append("\r\n");
        }

        builder.append(new char[]{27, '!', 0}); // 10cpp
        builder.append(new char[]{27, 'a', 0}); // Alineacion izquierda

        builder.append("\r\n");
        builder.append("Fecha.: ").append(mFechaTkt).append(String.format("%12s", mHoraTkt)).append("\r\n");
        builder.append("Sorteo: ").append(mNomSorteo).append("\r\n");

        if (mReferencia.length() > 0) {
            builder.append("Cliente: ").append(mReferencia).append("\r\n");
        }

        builder.append("Detalle\r\n");
        builder.append("==========================================\r\n");

        int montoGrupo = 0;
        int numLinea = 1;

        for (int i = 0; i < listaVentaTkt.length(); i++) {

            int mon_jugado = listaVentaTkt.optJSONObject(i).optInt("monto");
            String num_jugado = listaVentaTkt.optJSONObject(i).optString("numero");

            if (mon_jugado != montoGrupo) {
                if (montoGrupo > 0) {
                    builder.append("\r\n");
                }
                montoGrupo = mon_jugado;
                numLinea = 1;
                builder.append(String.format("%9s", fMonto.format(montoGrupo))).append(" x ");
            } else {
                if (numLinea == 1) {
                    //builder.append("####,###").append("   ");
                    builder.append(String.format("%12s", " "));
                }
            }
            builder.append(num_jugado.trim());
            numLinea += 1;

            if (numLinea < 9) {
                builder.append(", ");
            }
            if (numLinea == 9) {
                numLinea = 1;
                builder.append("\r\n");
            }
        }
        builder.append("\r\n");
        builder.append("==========================================\r\n");
        builder.append("Total Tiquete: ").append(String.format("%10s", fMonto.format(mTotalTkt))).append("\r\n").append("\r\n");

        builder.append(new char[]{27, '!', 8}); // Negrita, 10cpp
        builder.append(new char[]{27, 'a', 1}); // Alineacion centrada
        builder.append("PAGAMOS AL  ").append(String.valueOf(mFacPremio)).append("\r\n").append("\r\n");

        if (Variables.MSG_TKT.length() > 0) {
            builder.append(new char[]{27, '!', 0}); // 10cpp
            builder.append(Variables.MSG_TKT).append("\r\n").append("\r\n");
        }

        builder.append(new char[]{27, '!', 1}); // 12cpp
        //builder.append("Emitido por: ").append(Variables.mNOMUSU).append("\r\n");
        builder.append("Generado por LOTTOBANCACR").append("\r\n");

        builder.append(new char[]{27, '!', 0}); // 10 CPP
        builder.append(new char[]{27, 'd', 5}); // Avance de lineas
        builder.append(new char[]{27, 'm'}); // Corte parcial papel
        return builder.toString();

    }

    public static String tkt57mm(JSONObject datos) {

        DecimalFormat fMonto = new DecimalFormat("###,###,##0");

        String sNum, sMon;
        String mNomSorteo = datos.optString("nom_sorteo");
        int mFacPremio = datos.optInt("fac_premio");
        String mNumTkt = datos.optString("num_tkt");
        String mFechaTkt = datos.optString("fecha_tkt");
        String mHoraTkt = datos.optString("hora_tkt");
        String mReferencia = datos.optString("referencia");
        int mTotalTkt = datos.optInt("total_tkt");
        JSONArray listaVentaTkt = datos.optJSONArray("detalle");

        StringBuilder builder = new StringBuilder();
        builder.append(new char[]{27, '@'});
        builder.append(new char[]{27, 't', 2}); // Set de caracateres PC-850
        builder.append(new char[]{27, 'd', 3}); // Avance de 3 lineas

        if (Variables.TIT_TKT.length() > 0) {
            builder.append(new char[]{27, '!', 24}); // doble altura
            builder.append(new char[]{27, 'a', 1}); // Alineacion centrada
            builder.append(Variables.TIT_TKT).append("\r\n");
        }

        builder.append(new char[]{27, '!', 8}); // Negrita, 10cpp
        builder.append("Tiquete de compra").append("\r\n");
        builder.append("Numero Tiquete: ").append(mNumTkt).append("\r\n");

        if (datos.optBoolean("reimprime")) {
            builder.append("*** REIMPRESION ***").append("\r\n");
        }

        builder.append(new char[]{27, '!', 0}); // 10cpp
        builder.append(new char[]{27, 'a', 0}); // Alineacion izquierda

        builder.append("\r\n");
        builder.append("Fecha : ").append(mFechaTkt).append(String.format("%12s", mHoraTkt)).append("\r\n");
        builder.append("Sorteo: ").append(mNomSorteo).append("\r\n");

        if (mReferencia.length() > 0) {
            builder.append("Cliente: ").append(mReferencia).append("\r\n");
        }

        builder.append("Detalle\r\n");
        builder.append("==============================\r\n");

        int montoGrupo = 0;
        int numLinea = 1;

        for (int i = 0; i < listaVentaTkt.length(); i++) {

            int mon_jugado = listaVentaTkt.optJSONObject(i).optInt("monto");
            String num_jugado = listaVentaTkt.optJSONObject(i).optString("numero");

            if (mon_jugado != montoGrupo) {
                if (montoGrupo > 0) {
                    builder.append("\r\n");
                }
                montoGrupo = mon_jugado;
                numLinea = 1;
                builder.append(String.format("%9s", fMonto.format(montoGrupo))).append(" x ");
            } else {
                if (numLinea == 1) {
                    //builder.append("####,###").append("   ");
                    builder.append(String.format("%12s", " "));
                }
            }
            builder.append(num_jugado.trim());
            numLinea += 1;

            if (numLinea < 6) {
                builder.append(", ");
            }
            if (numLinea == 6) {
                numLinea = 1;
                builder.append("\r\n");
            }
        }
        builder.append("\r\n");
        builder.append("==============================\r\n");
        builder.append("Total Tiquete: ").append(String.format("%10s", fMonto.format(mTotalTkt))).append("\r\n").append("\r\n");

        builder.append(new char[]{27, '!', 8}); // Negrita, 10cpp
        builder.append(new char[]{27, 'a', 1}); // Alineacion centrada
        builder.append("PAGAMOS AL  ").append(String.valueOf(mFacPremio)).append("\r\n").append("\r\n");

        if (Variables.MSG_TKT.length() > 0) {
            builder.append(new char[]{27, '!', 0}); // 10cpp
            builder.append(Variables.MSG_TKT).append("\r\n").append("\r\n");
        }

        builder.append(new char[]{27, '!', 1}); // 12cpp
        //builder.append("Emitido por: ").append(Variables.mNOMUSU).append("\r\n");
        builder.append("Generado por LOTTOBANCACR").append("\r\n");

        builder.append(new char[]{27, '!', 0}); // 10 CPP
        builder.append(new char[]{27, 'd', 5}); // Avance de lineas
        builder.append(new char[]{27, 'm'}); // Corte parcial papel
        return builder.toString();

    }

    public static String tkt_Rev(JSONObject datos) {

        DecimalFormat fMonto = new DecimalFormat("###,###,##0");

        String sNum, sMon;
        String mNomSorteo = datos.optString("nom_sorteo");
        int mFacPremio = datos.optInt("fac_premio");
        String mNumTkt = datos.optString("num_tkt");
        String mFechaTkt = datos.optString("fecha_tkt");
        String mHoraTkt = datos.optString("hora_tkt");
        String mReferencia = datos.optString("referencia");
        int mTotalTkt = datos.optInt("total_tkt");
        JSONArray listaVentaTkt = datos.optJSONArray("detalle");

        StringBuilder builder = new StringBuilder();
        builder.append(new char[]{27, '@'});
        builder.append(new char[]{27, 't', 2}); // Set de caracateres PC-850
        builder.append(new char[]{27, 'd', 3}); // Avance de 3 lineas

        if (Variables.TIT_TKT.length() > 0) {
            builder.append(new char[]{27, '!', 24}); // doble altura
            builder.append(new char[]{27, 'a', 1}); // Alineacion centrada
            builder.append(Variables.TIT_TKT).append("\r\n");
        }

        builder.append(new char[]{27, '!', 8}); // Negrita, 10cpp
        builder.append("Tiquete de compra").append("\r\n");
        builder.append("Numero Tiquete: ").append(mNumTkt).append("\r\n");

        if (datos.optBoolean("reimprime")) {
            builder.append("*** REIMPRESION ***").append("\r\n");
        }

        builder.append(new char[]{27, '!', 0}); // 10cpp
        builder.append(new char[]{27, 'a', 0}); // Alineacion izquierda

        builder.append("\r\n");
        builder.append("Fecha : ").append(mFechaTkt).append(String.format("%12s", mHoraTkt)).append("\r\n");
        builder.append("Sorteo: ").append(mNomSorteo).append("\r\n");

        if (mReferencia.length() > 0) {
            builder.append("Cliente: ").append(mReferencia).append("\r\n");
        }

        builder.append("Detalle\r\n");

        builder.append(String.format("%4s","Num"));
        builder.append(String.format("%11s","  Apuesta"));
        builder.append(String.format("%11s","Reventado")).append("\r\n");
        builder.append("==============================\r\n");

        for (int i = 0; i < listaVentaTkt.length(); i++) {

            int mon_jugado = listaVentaTkt.optJSONObject(i).optInt("monto");
            int mon_jugado_rev = listaVentaTkt.optJSONObject(i).optInt("monto_rev");
            String num_jugado = listaVentaTkt.optJSONObject(i).optString("numero");

            builder.append(String.format("%4s", num_jugado.trim()));
            builder.append(String.format("%11s", fMonto.format(mon_jugado)));
            builder.append(String.format("%11s", fMonto.format(mon_jugado_rev))).append("\r\n");


        }

        //builder.append("\r\n");
        builder.append("==============================\r\n");
        builder.append("Total Tiquete: ").append(String.format("%11s", fMonto.format(mTotalTkt))).append("\r\n").append("\r\n");

        builder.append(new char[]{27, '!', 8}); // Negrita, 10cpp
        builder.append(new char[]{27, 'a', 1}); // Alineacion centrada
        builder.append("PAGAMOS AL  ").append(String.valueOf(mFacPremio)).append("\r\n").append("\r\n");

        if (Variables.MSG_TKT.length() > 0) {
            builder.append(new char[]{27, '!', 0}); // 10cpp
            builder.append(Variables.MSG_TKT).append("\r\n").append("\r\n");
        }

        builder.append(new char[]{27, '!', 1}); // 12cpp
        //builder.append("Emitido por: ").append(Variables.mNOMUSU).append("\r\n");
        builder.append("Generado por LOTTOBANCACR").append("\r\n");

        builder.append(new char[]{27, '!', 0}); // 10 CPP
        builder.append(new char[]{27, 'd', 5}); // Avance de lineas
        builder.append(new char[]{27, 'm'}); // Corte parcial papel
        return builder.toString();

    }


}
