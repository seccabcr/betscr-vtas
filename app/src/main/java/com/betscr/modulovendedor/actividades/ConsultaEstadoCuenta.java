package com.betscr.modulovendedor.actividades;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.betscr.modulovendedor.R;
import com.betscr.modulovendedor.adaptadores.AdapterMovUsu;
import com.betscr.modulovendedor.dialogos.DatePickerFragment;
import com.betscr.modulovendedor.interfaces.IResult;
import com.betscr.modulovendedor.modelo.DAO.UsuarioDAO;
import com.betscr.modulovendedor.modelo.VO.MovCtaUsuVO;
import com.betscr.modulovendedor.modelo.Variables;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ConsultaEstadoCuenta extends AppCompatActivity {


    TextView tvFechaInicial, tvFechaFinal, tvSaldoInicial, tvMontoMov, tvSaldoFinal, btnRefrescar;
    RecyclerView recyclerMov;

    ArrayList<MovCtaUsuVO> listaMovimientos;
    AdapterMovUsu adapterMovUsu;
    ProgressBar progressBar;
    UsuarioDAO usuarioDAO;

    String mFechaInicial, mFechaFinal;
    int mSaldoInicial, mMontoMov, mSaldoFinal;

    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat dfDMY = new SimpleDateFormat("dd/MM/yyyy");
    DecimalFormat fEntero = new DecimalFormat("###,###,###");
    DecimalFormat fCeros = new DecimalFormat("00");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_estado_cuenta);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        iniciaUI();
        consultarEstadoCuenta();


    }


    private void iniciaUI() {

        usuarioDAO = new UsuarioDAO();
        listaMovimientos = new ArrayList<>();
        mSaldoInicial = 0;
        mSaldoFinal = 0;
        mMontoMov = 0;

        progressBar = findViewById(R.id.conEst_progressBar);
        tvFechaInicial = findViewById(R.id.conEst_fechaInicial);
        tvFechaInicial.setText(dfDMY.format(new Date()));
        mFechaInicial = df.format(new Date());
        tvFechaInicial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtieneFechaInicial();
            }
        });
        tvFechaFinal = findViewById(R.id.conEst_fechaFinal);
        tvFechaFinal.setText(dfDMY.format(new Date()));
        mFechaFinal = df.format(new Date());
        tvFechaFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtieneFechaFinal();
            }
        });
        tvSaldoInicial = findViewById(R.id.conEst_saldoInicial);
        tvSaldoInicial.setText(fEntero.format(mSaldoInicial));
        tvSaldoFinal = findViewById(R.id.conEst_saldoFinal);
        tvSaldoFinal.setText(fEntero.format(mSaldoFinal));
        tvMontoMov = findViewById(R.id.conEst_montoMov);
        tvMontoMov.setText(fEntero.format(mMontoMov));
        recyclerMov = findViewById(R.id.conEst_recycler);
        recyclerMov.setLayoutManager(new LinearLayoutManager(this));
        btnRefrescar = findViewById(R.id.conEst_refrescar);
        btnRefrescar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFechaInicial.compareTo(mFechaFinal) > 0) {
                    Toast.makeText(ConsultaEstadoCuenta.this, "Fecha Inicial no puede ser mayor que Fecha Final", Toast.LENGTH_SHORT).show();
                    return;
                }

                consultarEstadoCuenta();
            }
        });

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

    private void consultarEstadoCuenta() {

        listaMovimientos = new ArrayList<>();
        JSONObject jsonSend = new JSONObject();
        try {
            jsonSend.put("fecha_inicial", mFechaInicial);
            jsonSend.put("fecha_final", mFechaFinal);
            jsonSend.put("cod_usuario", Variables.COD_USUARIO);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSaldoInicial = 0;
        mMontoMov = 0;
        mSaldoFinal = 0;

        progressBar.setVisibility(View.VISIBLE);
        usuarioDAO.consultaEstadoCuenta(this, jsonSend, new IResult() {
            @Override
            public void onResponse(JSONObject object) {
                progressBar.setVisibility(View.GONE);
                mSaldoInicial = object.optJSONObject("resp").optInt("saldo_inicial");
                tvSaldoInicial.setText(fEntero.format(mSaldoInicial));
                if (mSaldoInicial < 0) {
                    tvSaldoInicial.setTextColor(Color.RED);
                }else{
                    tvSaldoInicial.setTextColor(Color.GRAY);
                }
                JSONArray movimientos = object.optJSONObject("resp").optJSONArray("movimientos");
                if (movimientos.length() > 0) {
                    for (int i = 0; i < movimientos.length(); i++) {
                        MovCtaUsuVO mov = new MovCtaUsuVO();
                        mov.setConse_mov(movimientos.optJSONObject(i).optInt("conse_mov"));
                        mov.setFechaMov(movimientos.optJSONObject(i).optString("fec_mov"));
                        mov.setDocRefe(movimientos.optJSONObject(i).optString("doc_refe"));
                        mov.setDetMov(movimientos.optJSONObject(i).optString("detalle"));
                        int monto = movimientos.optJSONObject(i).optInt("mon_mov");
                        mMontoMov += monto;
                        mov.setMontoMov(monto);
                        listaMovimientos.add(mov);
                    }

                } else {
                    Toast.makeText(ConsultaEstadoCuenta.this, "NO hay movimientos en el periodo seleccionado", Toast.LENGTH_SHORT).show();
                }
                tvMontoMov.setText(fEntero.format(mMontoMov));
                mSaldoFinal = mSaldoInicial + mMontoMov;
                tvSaldoFinal.setText(fEntero.format(mSaldoFinal));
                if (mMontoMov < 0) {
                    tvMontoMov.setTextColor(Color.RED);
                }else{
                    tvMontoMov.setTextColor(Color.GRAY);
                }
                if (mSaldoFinal < 0) {
                    tvSaldoFinal.setTextColor(Color.RED);
                }else{
                    tvSaldoFinal.setTextColor(Color.GRAY);
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
        adapterMovUsu = new AdapterMovUsu(listaMovimientos);
        recyclerMov.setAdapter(adapterMovUsu);
    }
}