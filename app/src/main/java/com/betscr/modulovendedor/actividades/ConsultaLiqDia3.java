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
import com.betscr.modulovendedor.adaptadores.AdapterResumenLiqSorteos;
import com.betscr.modulovendedor.dialogos.DatePickerFragment;
import com.betscr.modulovendedor.interfaces.IResult;
import com.betscr.modulovendedor.modelo.DAO.UsuarioDAO;
import com.betscr.modulovendedor.modelo.VO.LiqDiaResumenVO;
import com.betscr.modulovendedor.modelo.Variables;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ConsultaLiqDia3 extends AppCompatActivity {

    TextView tvFechaInicial, tvFechaFinal, tvVentaTotal, tvComision, tvPremio, tvMontoLiq, btnCalcular;
    Spinner spSorteos;
    ProgressBar progressBar;
    RecyclerView recyclerLiqDia;

    UsuarioDAO usuarioDAO;


    private ArrayList<LiqDiaResumenVO> listaResumenSorteo;

    private String mFechaInicial, mFechaFinal;
    private int mCodUsuario, codSorteo, mVentaTotal, mPremio, mComision, mMontoLiq;


    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat dfDMY = new SimpleDateFormat("dd/MM/yyyy");
    DecimalFormat fEntero = new DecimalFormat("###,###,###");
    DecimalFormat fCeros = new DecimalFormat("00");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_liquidacion_dia3);

        iniciaUI();
        limpiaCampos();
        //consultaSorteos();

    }


    private void iniciaUI() {

        usuarioDAO = new UsuarioDAO();

        progressBar = findViewById(R.id.liqDia3_progressBar);
        tvVentaTotal = findViewById(R.id.liqDia3_ventaTotal);
        tvComision = findViewById(R.id.liqDia3_comision);
        tvPremio = findViewById(R.id.liqDia3_premio);
        tvMontoLiq = findViewById(R.id.liqDia3_montoLiq);

        mFechaInicial = df.format(new Date());
        tvFechaInicial = findViewById(R.id.liqDia3_fechaInicial);
        tvFechaInicial.setText(dfDMY.format(new Date()));
        tvFechaInicial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtieneFechaInicial();
            }
        });

        mFechaFinal = df.format(new Date());
        tvFechaFinal = findViewById(R.id.liqDia3_fechaFinal);
        tvFechaFinal.setText(dfDMY.format(new Date()));
        tvFechaFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtieneFechaFinal();
            }
        });

        btnCalcular = findViewById(R.id.liqDia3_refrescar);
        btnCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mFechaInicial.compareTo(mFechaFinal) > 0) {
                    Toast.makeText(ConsultaLiqDia3.this, "Fecha Inicial no puede ser mayor que Fecha Final", Toast.LENGTH_SHORT).show();
                    return;
                }

                consultaSorteos();
            }
        });


        recyclerLiqDia = findViewById(R.id.liqDia3_recycler);
        //recyclerLiqDia.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerLiqDia.setLayoutManager(new LinearLayoutManager(this));

    }


    private void limpiaCampos() {

        mVentaTotal = 0;
        tvVentaTotal.setText(fEntero.format(mVentaTotal));
        mComision = 0;
        tvComision.setText(fEntero.format(mComision));
        mPremio = 0;
        tvPremio.setText(fEntero.format(mPremio));
        mMontoLiq = 0;
        tvMontoLiq.setText(fEntero.format(mMontoLiq));
        listaResumenSorteo = new ArrayList<>();
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
                //consultaSorteos();
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
                //consultaSorteos();
            }
        });

        newFragment.show(getSupportFragmentManager(), "datePicker");
    }



    private void consultaSorteos() {

        listaResumenSorteo = new ArrayList<>();
        JSONObject jsonSend = new JSONObject();
        try {
            jsonSend.put("fecha_inicial", mFechaInicial);
            jsonSend.put("fecha_final", mFechaFinal);
            jsonSend.put("cod_usuario", Variables.COD_USUARIO);
            //jsonSend.put("cod_suc", Variables.COD_AGENCIA);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mVentaTotal = 0;
        mPremio = 0;
        mComision = 0;
        mMontoLiq = 0;

        progressBar.setVisibility(View.VISIBLE);
        usuarioDAO.consultaResumenPeriodo(this, jsonSend, new IResult() {
            @Override
            public void onResponse(JSONObject object) {
                progressBar.setVisibility(View.GONE);

                if (object.optJSONObject("resp") != null) {

                    JSONArray liqSorteo = object.optJSONObject("resp").optJSONArray("liqSorteo");
                    for (int i = 0; i < liqSorteo.length(); i++) {
                        LiqDiaResumenVO resumen = new LiqDiaResumenVO();
                        resumen.setFecha_venta(liqSorteo.optJSONObject(i).optString("fecha_tkt"));
                        resumen.setMon_venta(liqSorteo.optJSONObject(i).optInt("mon_venta"));
                        resumen.setMon_comision(liqSorteo.optJSONObject(i).optInt("comision"));
                        resumen.setMon_premio(liqSorteo.optJSONObject(i).optInt("premio"));
                        mVentaTotal += liqSorteo.optJSONObject(i).optInt("mon_venta");
                        mPremio += liqSorteo.optJSONObject(i).optInt("premio");
                        mComision += liqSorteo.optJSONObject(i).optInt("comision");
                        listaResumenSorteo.add(resumen);
                    }

                }

                tvVentaTotal.setText(fEntero.format(mVentaTotal));
                tvComision.setText(fEntero.format(mComision));
                tvPremio.setText(fEntero.format(mPremio));
                mMontoLiq = mVentaTotal - mComision - mPremio;
                tvMontoLiq.setText(fEntero.format(mMontoLiq));
                if(mMontoLiq<0){
                    tvMontoLiq.setTextColor(Color.RED);
                }else{
                    tvMontoLiq.setTextColor(Color.GRAY);
                }

                construirRecycler();

            }

            @Override
            public void onError(VolleyError error) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void construirRecycler() {
        AdapterResumenLiqSorteos adapter = new AdapterResumenLiqSorteos(listaResumenSorteo);
        recyclerLiqDia.setAdapter(adapter);
    }


}