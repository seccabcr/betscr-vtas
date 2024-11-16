package com.betscr.modulovendedor.actividades;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.betscr.modulovendedor.R;
import com.betscr.modulovendedor.adaptadores.AdapterConsultaPremios;
import com.betscr.modulovendedor.dialogos.DatePickerFragment;
import com.betscr.modulovendedor.interfaces.IResult;
import com.betscr.modulovendedor.modelo.DAO.UsuarioDAO;
import com.betscr.modulovendedor.modelo.DAO.VentaDAO;
import com.betscr.modulovendedor.modelo.VO.PremiosVO;
import com.betscr.modulovendedor.modelo.VO.SorteoUsuVO;
import com.betscr.modulovendedor.modelo.Variables;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ConsultaPremios extends AppCompatActivity {

    TextView tvFechaSorteo, tvNumPremiado, tvTotalPremios;
    Spinner spSorteos;
    RecyclerView recyclerPremios;
    ProgressBar progressBar;
    AdapterConsultaPremios adapterConsultaPremios;

    VentaDAO ventaDAO;
    UsuarioDAO usuarioDAO;
    private ArrayList<PremiosVO> listaPremios;
    private ArrayList<SorteoUsuVO> listaSorteosUsu;

    private String mFechaSorteo;
    private int mTotalPremios, mCodSorteo;
    int mTotalPremioNor = 0;
    int mTotalPremioRev = 0;

    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat dfDMY = new SimpleDateFormat("dd/MM/yyyy");
    DecimalFormat fEntero = new DecimalFormat("###,###,###");
    DecimalFormat fCeros = new DecimalFormat("00");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_premios);

        iniciaUI();
        llenaSorteosUsu();

    }

    private void iniciaUI() {

        ventaDAO = new VentaDAO();
        usuarioDAO = new UsuarioDAO();

        progressBar = findViewById(R.id.conPre_progressBar);
        recyclerPremios = findViewById(R.id.conPre_recycler);
        recyclerPremios.setLayoutManager(new LinearLayoutManager(this));
        tvNumPremiado = findViewById(R.id.conPre_numero);
        tvTotalPremios = findViewById(R.id.conPre_totalPremios);
        tvFechaSorteo = findViewById(R.id.conPre_fecha);
        tvFechaSorteo.setText(dfDMY.format(new Date()));
        mFechaSorteo = df.format(new Date());
        tvFechaSorteo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtieneFechaSorteo();
                consultaPremios();
            }
        });


        spSorteos = findViewById(R.id.conPre_spSorteos);
        spSorteos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mCodSorteo = listaSorteosUsu.get(position).getCodSorteo();

                if (position > 0) {
                    consultaPremios();
                } else {
                    tvNumPremiado.setText("");
                    tvTotalPremios.setText("0");
                    listaPremios = new ArrayList<>();
                    construirRecycler();
                    spSorteos.requestFocus();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void obtieneFechaSorteo() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because January is zero
                final String selectedDate = fCeros.format(day) + "/" + fCeros.format(month + 1) + "/" + year;
                mFechaSorteo = year + "-" + fCeros.format(month + 1) + "-" + fCeros.format(day);
                tvFechaSorteo.setText(selectedDate);
            }
        });

        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private void llenaSorteosUsu() {

        progressBar.setVisibility(View.VISIBLE);
        listaSorteosUsu = new ArrayList<>();
        SorteoUsuVO sorteo = new SorteoUsuVO();
        sorteo.setCodSorteo(0);
        sorteo.setNomSorteo("Seleccione un sorteo");
        listaSorteosUsu.add(sorteo);

        JSONObject jsonSend = new JSONObject();
        try {
            jsonSend.put("cod_usuario", Variables.COD_USUARIO);
            jsonSend.put("activas", true);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        usuarioDAO.listaSorteosUsu(this, jsonSend, new IResult() {
            @Override
            public void onResponse(JSONObject object) {
                progressBar.setVisibility(View.GONE);


                JSONArray lista = object.optJSONObject("resp").optJSONArray("sorteosUsu");
                for (int i = 0; i < lista.length(); i++) {
                    SorteoUsuVO sorteo = new SorteoUsuVO();
                    sorteo.setCodSorteo(lista.optJSONObject(i).optInt("cod_sorteo"));
                    sorteo.setNomSorteo(lista.optJSONObject(i).optString("nom_sorteo"));
                    sorteo.setPor_comision_usu(lista.optJSONObject(i).optDouble("por_comision_usu"));
                    sorteo.setFac_premio_usu(lista.optJSONObject(i).optInt("fac_premio_usu"));
                    listaSorteosUsu.add(sorteo);
                }
                construirSpinnerSorteos();

            }

            @Override
            public void onError(VolleyError error) {
                progressBar.setVisibility(View.GONE);
            }
        });


    }

    private void construirSpinnerSorteos() {
        ArrayAdapter<String> adapter = new ArrayAdapter(this, R.layout.item_spinner, listaSorteosUsu);
        spSorteos.setAdapter(adapter);
    }


    private void consultaPremios() {

        if (mCodSorteo == 0) {
            return;
        }

        listaPremios = new ArrayList<>();
        JSONObject jsonSend = new JSONObject();
        try {
            jsonSend.put("fecha_sorteo", mFechaSorteo);
            jsonSend.put("cod_sorteo", mCodSorteo);
            jsonSend.put("cod_usuario", Variables.COD_USUARIO);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mTotalPremios = 0;
        mTotalPremioNor = 0;
        mTotalPremioRev = 0;
        progressBar.setVisibility(View.VISIBLE);
        usuarioDAO.consultaTktsPremiados(this, jsonSend, new IResult() {
            @Override
            public void onResponse(JSONObject object) {
                progressBar.setVisibility(View.GONE);
                if (object.optJSONObject("resp") != null) {
                    tvNumPremiado.setText(object.optJSONObject("resp").optString("num_premiado"));
                    JSONArray lista_premios = object.optJSONObject("resp").optJSONArray("tkts_premiados");
                    if (lista_premios.length() > 0) {
                        for (int i = 0; i < lista_premios.length(); i++) {
                            PremiosVO premio = new PremiosVO();
                            premio.setNum_tkt(lista_premios.optJSONObject(i).optInt("num_tkt"));
                            premio.setMon_jugado(lista_premios.optJSONObject(i).optInt("mon_jugado"));
                            premio.setMon_jugado_rev(lista_premios.optJSONObject(i).optInt("mon_jugado_rev"));
                            premio.setMon_premio(lista_premios.optJSONObject(i).optInt("mon_premio"));
                            premio.setMon_premio_rev(lista_premios.optJSONObject(i).optInt("mon_premio_rev"));
                            premio.setReferencia(lista_premios.optJSONObject(i).optString("nom_cliente"));
                            mTotalPremioNor += lista_premios.optJSONObject(i).optInt("mon_premio");
                            mTotalPremioRev += lista_premios.optJSONObject(i).optInt("mon_premio_rev");
                            mTotalPremios = mTotalPremioNor +mTotalPremioRev;
                            listaPremios.add(premio);
                        }
                    } else {
                        Toast.makeText(ConsultaPremios.this, "No hay tiquetes premiados para este sorteo", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    tvNumPremiado.setText("");
                    Toast.makeText(ConsultaPremios.this, "No hay tiquetes premiados para este sorteo", Toast.LENGTH_SHORT).show();
                }
                tvTotalPremios.setText(fEntero.format(mTotalPremios));
                construirRecycler();
            }

            @Override
            public void onError(VolleyError error) {
                progressBar.setVisibility(View.GONE);
            }
        });


    }

    private void construirRecycler() {
        adapterConsultaPremios = new AdapterConsultaPremios(listaPremios);
        recyclerPremios.setAdapter(adapterConsultaPremios);
    }


}