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


public class DialogoClonaTktFragment extends DialogFragment {

    Activity activity;
    private int numero;

    EditText numTkt;
    TextView txtNumero;
    Button btnEliminar, btnAceptar, btnCancelar;
    DecimalFormat fDecimal = new DecimalFormat("####,###");


    public DialogoClonaTktFragment() {
        // Required empty public constructor
    }


    public interface RetornaTkt {
        void btnAceptarClonaTkt(int num);


    }

    RetornaTkt listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        return crearDialogo();
    }

    private Dialog crearDialogo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.fragment_dialogo_clona_tkt, null);
        builder.setView(v);

        numTkt = v.findViewById(R.id.df_clona_numeroTkt);
        btnAceptar = v.findViewById(R.id.df_clonaTkt_btnAceptar);
        btnCancelar = v.findViewById(R.id.df_clonaTkt_btnCancelar);

        creaEventos();

        return builder.create();
    }

    private void creaEventos() {
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = Integer.parseInt(numTkt.getText().toString().replace(",", ""));
                listener.btnAceptarClonaTkt(num);
                dismiss();
            }
        });


        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            this.activity = (Activity) context;
            listener = (RetornaTkt) context;
        } else {
            throw new RuntimeException(context.toString() + "must implement OnFragmentInterationListener");
        }
    }


}