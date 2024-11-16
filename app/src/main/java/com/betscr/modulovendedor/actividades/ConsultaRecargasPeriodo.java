package com.betscr.modulovendedor.actividades;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.betscr.modulovendedor.R;
import com.betscr.modulovendedor.adaptadores.AdapterReporteRecargas;
import com.betscr.modulovendedor.adaptadores.AdapterResumenLiqSorteos;
import com.betscr.modulovendedor.dialogos.DatePickerFragment;
import com.betscr.modulovendedor.interfaces.IResult;
import com.betscr.modulovendedor.modelo.DAO.UsuarioDAO;
import com.betscr.modulovendedor.modelo.DAO.VentaDAO;
import com.betscr.modulovendedor.modelo.VO.LiqDiaResumenVO;
import com.betscr.modulovendedor.modelo.VO.Recarga;
import com.betscr.modulovendedor.modelo.Variables;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ConsultaRecargasPeriodo extends AppCompatActivity {

    TextView tvFechaInicial, tvFechaFinal, tvTotalRecargas, tvTotalComision,tvTotalNeto, btnCalcular;

    ProgressBar progressBar;
    RecyclerView recyclerRecargas;

    VentaDAO ventaDAO;


    private ArrayList<Recarga> lista_recargas;

    private String mFechaInicial, mFechaFinal;
    private int mTotalRecargas, mTotalComision, mMontoLiq;


    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat dfDMY = new SimpleDateFormat("dd/MM/yyyy");
    DecimalFormat fEntero = new DecimalFormat("###,###,###");
    DecimalFormat fCeros = new DecimalFormat("00");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_recargas_periodo);

        iniciaUI();
        limpiaCampos();


    }


    private void iniciaUI() {

        ventaDAO = new VentaDAO();

        progressBar = findViewById(R.id.repRec_progressBar);
        tvTotalRecargas = findViewById(R.id.repRec_totalRecargas);
        tvTotalComision = findViewById(R.id.repRec_totalComision);

        tvTotalNeto = findViewById(R.id.repRec_montoLiq);

        mFechaInicial = df.format(new Date());
        tvFechaInicial = findViewById(R.id.repRec_fechaInicial);
        tvFechaInicial.setText(dfDMY.format(new Date()));
        tvFechaInicial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtieneFechaInicial();
            }
        });

        mFechaFinal = df.format(new Date());
        tvFechaFinal = findViewById(R.id.repRec_fechaFinal);
        tvFechaFinal.setText(dfDMY.format(new Date()));
        tvFechaFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtieneFechaFinal();
            }
        });

        btnCalcular = findViewById(R.id.repRec_consultar);
        btnCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mFechaInicial.compareTo(mFechaFinal) > 0) {
                    Toast.makeText(ConsultaRecargasPeriodo.this, "Fecha Inicial no puede ser mayor que Fecha Final", Toast.LENGTH_SHORT).show();
                    return;
                }

                consultaRecargas();
            }
        });


        recyclerRecargas = findViewById(R.id.repRec_recycler);
        recyclerRecargas.setLayoutManager(new LinearLayoutManager(this));

    }


    private void limpiaCampos() {

        mTotalRecargas = 0;
        tvTotalRecargas.setText(fEntero.format(mTotalRecargas));
        mTotalComision = 0;
        tvTotalComision.setText(fEntero.format(mTotalComision));

        mMontoLiq = 0;
        tvTotalNeto.setText(fEntero.format(mMontoLiq));
        lista_recargas = new ArrayList<>();
        construirRecycler();

    }


    private void obtieneFechaInicial() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because January is zero
                final String selectedDate = fCeros.format(day) + "/" + fCeros.format(month + 1) + "/" + year;
                mFechaInicial = year + "-" + fCeros.format(month + 1) + "-" + fCeros.format(day);
                tvFechaInicial.setText(selectedDate);

            }
        });

        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private void obtieneFechaFinal() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because January is zero
                final String selectedDate = fCeros.format(day) + "/" + fCeros.format(month + 1) + "/" + year;
                mFechaFinal = year + "-" + fCeros.format(month + 1) + "-" + fCeros.format(day);
                tvFechaFinal.setText(selectedDate);

            }
        });

        newFragment.show(getSupportFragmentManager(), "datePicker");
    }



    private void consultaRecargas() {

        lista_recargas = new ArrayList<>();
        JSONObject jsonSend = new JSONObject();
        try {
            jsonSend.put("fecha_ini", mFechaInicial);
            jsonSend.put("fecha_fin", mFechaFinal);
            jsonSend.put("id_vendedor", Variables.ID_USUARIO);
            //jsonSend.put("cod_suc", Variables.COD_AGENCIA);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mTotalRecargas = 0;
        mTotalComision = 0;
        mMontoLiq = 0;

        progressBar.setVisibility(View.VISIBLE);
        ventaDAO.listaRecargasPeriodo(this, jsonSend, new IResult() {
            @Override
            public void onResponse(JSONObject object) {
                progressBar.setVisibility(View.GONE);

                JSONObject resp = object.optJSONObject("resp");



                    JSONArray recargas = resp.optJSONArray("lista_recargas");
                    for (int i = 0; i < recargas.length(); i++) {
                        Recarga recarga = new Recarga();

                        recarga.setNum_recarga(recargas.optJSONObject(i).optInt("num_recarga"));
                        recarga.setFec_recarga(recargas.optJSONObject(i).optString("fec_recarga"));
                        int mon_recarga = recargas.optJSONObject(i).optInt("mon_recarga");
                        recarga.setMon_recarga(mon_recarga);
                        int mon_comision = recargas.optJSONObject(i).optInt("mon_comision");
                        recarga.setMon_comision(mon_comision);
                        mTotalRecargas+=mon_recarga;
                        mTotalComision+=mon_comision;


                        lista_recargas.add(recarga);
                    }




                tvTotalRecargas.setText(fEntero.format(mTotalRecargas));
                tvTotalComision.setText(fEntero.format(mTotalComision));
                mMontoLiq = mTotalRecargas - mTotalComision;
                tvTotalNeto.setText(fEntero.format(mMontoLiq));

                construirRecycler();

            }

            @Override
            public void onError(VolleyError error) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void construirRecycler() {
        AdapterReporteRecargas adapter = new AdapterReporteRecargas(lista_recargas);
        recyclerRecargas.setAdapter(adapter);
    }


}