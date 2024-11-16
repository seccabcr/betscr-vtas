package com.betscr.modulovendedor.actividades;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.betscr.modulovendedor.adaptadores.AdapterReporteVentaTKT;
import com.betscr.modulovendedor.dialogos.DatePickerFragment;
import com.betscr.modulovendedor.interfaces.IResult;
import com.betscr.modulovendedor.modelo.DAO.UsuarioDAO;
import com.betscr.modulovendedor.modelo.DAO.VentaDAO;
import com.betscr.modulovendedor.modelo.VO.SorteoUsuVO;
import com.betscr.modulovendedor.modelo.VO.VentaDiariaTktVO;
import com.betscr.modulovendedor.modelo.Variables;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ConsultaVentaTkts extends AppCompatActivity {


    private ArrayList<SorteoUsuVO> listaSorteosUsu;
    private ArrayList<VentaDiariaTktVO> listaTkts;

    private VentaDAO ventaDAO;
    private UsuarioDAO usuarioDAO;
    private Spinner spSorteos;
    private ProgressBar progressBar;
    TextView tvFechaSorteo, btnCalcular;
    private RecyclerView recyclerListaTkts;

    private int mCodSorteo, mFacPremio, mFacPremio2;
    private String mFechaSorteo;

    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat dfDMY = new SimpleDateFormat("dd/MM/yyyy");
    DecimalFormat fCeros = new DecimalFormat("00");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_venta_dia);

        iniciaUI();
        llenaSorteosUsu();

    }


    private void iniciaUI() {

        ventaDAO = new VentaDAO();
        usuarioDAO = new UsuarioDAO();

        progressBar = findViewById(R.id.rptkt_progressBar);
        tvFechaSorteo = findViewById(R.id.rptkt_fecha);
        mFechaSorteo = df.format(new Date());
        tvFechaSorteo.setText(dfDMY.format(new Date()));
        tvFechaSorteo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtieneFechaSorteo();
            }
        });

        spSorteos = findViewById(R.id.rptkt_sorteo);
        spSorteos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mCodSorteo = listaSorteosUsu.get(position).getCodSorteo();
                mFacPremio = listaSorteosUsu.get(position).getFac_premio_usu();
                mFacPremio2 =listaSorteosUsu.get(position).getFac_premio2();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnCalcular = findViewById(R.id.rptkt_calcular);
        btnCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mCodSorteo > 0) {
                    llenarListaTkts();
                    recyclerListaTkts.setEnabled(true);
                } else {
                    Toast.makeText(ConsultaVentaTkts.this, "Debe seleccionar un sorteo", Toast.LENGTH_SHORT).show();
                    recyclerListaTkts.setEnabled(false);
                }

            }
        });

        recyclerListaTkts = findViewById(R.id.rptkt_recycler);
        recyclerListaTkts.setLayoutManager(new LinearLayoutManager(this));


    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mCodSorteo > 0) {
            llenarListaTkts();
        }
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
                    sorteo.setFac_premio2(lista.optJSONObject(i).optInt("fac_premio_comb_usu"));
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

    private void obtieneFechaSorteo() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because January is zero
                final String selectedDate = fCeros.format(day) + "/" + fCeros.format(month + 1) + "/" + year;
                mFechaSorteo = year + "-" + fCeros.format(month + 1) + "-" + fCeros.format(day);
                tvFechaSorteo.setText(selectedDate);
                //llenarListaTkts();
            }
        });

        newFragment.show(getSupportFragmentManager(), "datePicker");
    }


    private void llenarListaTkts() {
        listaTkts = new ArrayList<>();
        JSONObject jsonSend = new JSONObject();
        try {
            jsonSend.put("cod_usuario", Variables.COD_USUARIO);
            jsonSend.put("cod_sorteo", mCodSorteo);
            jsonSend.put("fecha_venta", mFechaSorteo);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressBar.setVisibility(View.VISIBLE);
        ventaDAO.listaVentaTkts(this, jsonSend, new IResult() {
            @Override
            public void onResponse(JSONObject object) {
                progressBar.setVisibility(View.GONE);
                if (object.optJSONObject("resp") != null) {
                    JSONArray arrayTkts = object.optJSONObject("resp").optJSONArray("tkts");
                    for (int i = 0; i < arrayTkts.length(); i++) {
                        VentaDiariaTktVO tkt = new VentaDiariaTktVO();
                        tkt.setNum_tkt(arrayTkts.optJSONObject(i).optInt("num_tkt"));
                        tkt.setMon_tkt(arrayTkts.optJSONObject(i).optInt("total_tkt"));
                        tkt.setNom_cliente(arrayTkts.optJSONObject(i).optString("nom_cliente"));
                        tkt.setTipo_jugada(arrayTkts.optJSONObject(i).optInt("tipo_jugada"));
                        listaTkts.add(tkt);
                    }

                } else {
                    Toast.makeText(ConsultaVentaTkts.this, "No hay registros", Toast.LENGTH_SHORT).show();
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
        AdapterReporteVentaTKT adapterReporteVentaTKT = new AdapterReporteVentaTKT(listaTkts);
        recyclerListaTkts.setAdapter(adapterReporteVentaTKT);
        adapterReporteVentaTKT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int numTkt = listaTkts.get(recyclerListaTkts.getChildAdapterPosition(v)).getNum_tkt();
                int tipoJugada = listaTkts.get(recyclerListaTkts.getChildAdapterPosition(v)).getTipo_jugada();


                Intent intent = new Intent(ConsultaVentaTkts.this, ImprimeTkt.class);
                intent.putExtra("numTkt", numTkt);
                intent.putExtra("reimprime", true);
                if(tipoJugada==0){
                    intent.putExtra("facPremio", mFacPremio);
                }else{
                    intent.putExtra("facPremio", mFacPremio2);
                }

                intent.putExtra("fechaSorteo", mFechaSorteo);
                startActivity(intent);

            }
        });

    }


}