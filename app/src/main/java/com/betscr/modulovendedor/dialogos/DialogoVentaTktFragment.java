package com.betscr.modulovendedor.dialogos;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.betscr.modulovendedor.R;

import java.text.DecimalFormat;


public class DialogoVentaTktFragment extends DialogFragment {

    Activity activity;
    private int monto, montoRev, mReventado;

    private String numero;
    EditText txtMonto, txtMontoRev;
    TextView txtNumero;
    Button btnEliminar, btnAceptar, btnCancelar;
    DecimalFormat fDecimal = new DecimalFormat("####,###");

    public DialogoVentaTktFragment() {
        // Required empty public constructor
    }


    public interface EnviaParametros {
        void onClickBtnAceptar(String numero, int monto, int montoRev);

        void onClickBtnEliminar(String numero);
    }

    EnviaParametros listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        if (getArguments() != null) {
            monto = getArguments().getInt("monto");
            montoRev = getArguments().getInt("monto_rev");
            numero = getArguments().getString("numero");
            mReventado = getArguments().getInt("reventado",0);

        }

        return crearDialogo();
    }

    private Dialog crearDialogo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.fragment_dialogo_venta_tkt, null);
        builder.setView(v);
        txtNumero = v.findViewById(R.id.df_vtkt_numero);
        txtNumero.setText(numero);
        txtMonto = v.findViewById(R.id.df_vtkt_monto);
        txtMonto.setText(fDecimal.format(monto));
        txtMontoRev = v.findViewById(R.id.df_vtkt_monto_rev);
        txtMontoRev.setText(fDecimal.format(montoRev));
        txtMontoRev.setEnabled(mReventado == 1);
        btnAceptar = v.findViewById(R.id.df_vtkt_btnAceptar);
        btnEliminar = v.findViewById(R.id.df_vtkt_btnEliminar);
        //btnCancelar = v.findViewById(R.id.df_vtkt_btnCancelar);

        creaEventos();

        return builder.create();
    }

    private void creaEventos() {
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mon = Integer.parseInt(txtMonto.getText().toString().replace(",", ""));
                int monRev = Integer.parseInt(txtMontoRev.getText().toString().replace(",", ""));
                String num = txtNumero.getText().toString();
                listener.onClickBtnAceptar(num,mon,monRev);
                dismiss();
            }
        });
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num = txtNumero.getText().toString();
                listener.onClickBtnEliminar(num);
                dismiss();
            }
        });

        /*btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });*/


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            this.activity = (Activity) context;
            listener = (EnviaParametros) context;
        } else {
            throw new RuntimeException(context.toString() + "must implement OnFragmentInterationListener");
        }
    }


}