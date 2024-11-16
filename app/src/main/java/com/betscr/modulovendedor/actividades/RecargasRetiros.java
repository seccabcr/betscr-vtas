package com.betscr.modulovendedor.actividades;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.betscr.modulovendedor.R;

public class RecargasRetiros extends AppCompatActivity implements View.OnClickListener {

    Button btnRecargas, btnRepRecargas, btnRetiros, btnRepRetiros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recargas_retiros);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        iniciaUI();

    }


    private void iniciaUI() {

        btnRecargas = findViewById(R.id.recargas);
        btnRecargas.setOnClickListener(this);
        btnRepRecargas = findViewById(R.id.rep_recargas);
        btnRepRecargas.setOnClickListener(this);
        btnRetiros = findViewById(R.id.pago_retiros);
        btnRetiros.setOnClickListener(this);
        btnRepRetiros = findViewById(R.id.rep_pago_retiros);
        btnRepRetiros.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        Intent i = null;

        switch (v.getId()) {
            case R.id.recargas:
                i = new Intent(this, Recargas.class);
                break;

            case R.id.rep_recargas:
                i = new Intent(this, ConsultaRecargasPeriodo.class);
                break;

            case R.id.pago_retiros:
                //i = new Intent(this, ConsultaLiqDiaUsu.class);
                break;

            case R.id.rep_pago_retiros:
                //i = new Intent(this, ConsultaLiqDiaUsu.class);
                break;

        }

        try {
            startActivity(i);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Proceso NO disponible", Toast.LENGTH_SHORT).show();

        }

    }
}