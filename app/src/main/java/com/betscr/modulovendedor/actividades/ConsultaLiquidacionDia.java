package com.betscr.modulovendedor.actividades;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.betscr.modulovendedor.R;
import com.betscr.modulovendedor.adaptadores.AdapterVentaTKT;
import com.betscr.modulovendedor.dialogos.DatePickerFragment;
import com.betscr.modulovendedor.interfaces.IResult;
import com.betscr.modulovendedor.modelo.DAO.UsuarioDAO;
import com.betscr.modulovendedor.modelo.VO.SorteoUsuVO;
import com.betscr.modulovendedor.modelo.VO.VentaTktVO;
import com.betscr.modulovendedor.modelo.Variables;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ConsultaLiquidacionDia extends AppCompatActivity {

    TextView tvFechaSorteo, tvVentaTotal, tvComision, tvPremio, tvMontoLiq;
    Spinner spSorteos;
    ProgressBar progressBar;
    RecyclerView recyclerLiqDia;
    AdapterVentaTKT adapterVentaTKT;

    UsuarioDAO usuarioDAO;

    private ArrayList<SorteoUsuVO> listaSorteosUsu;
    private ArrayList<VentaTktVO> listaVentaTkt;

    private String mFechaSorteo;
    private int mCodUsuario, codSorteo, mVentaTotal, mPremio, mComision, mMontoLiq;
    int mVentaNor = 0;
    int mVentaRev = 0;


    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat dfDMY = new SimpleDateFormat("dd/MM/yyyy");
    DecimalFormat fEntero = new DecimalFormat("###,###,###");
    DecimalFormat fCeros = new DecimalFormat("00");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_liquidacion_dia);

        iniciaUI();
        limpiaCampos();
        llenaSorteosUsu();

    }


    private void iniciaUI() {

        usuarioDAO = new UsuarioDAO();

        progressBar = findViewById(R.id.liqDia_progressBar);
        tvVentaTotal = findViewById(R.id.liqDia_ventaTotal);


        mFechaSorteo = df.format(new Date());
        tvFechaSorteo = findViewById(R.id.liqDia_fecha);
        tvFechaSorteo.setText(dfDMY.format(new Date()));
        tvFechaSorteo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtieneFechaSorteo();
            }
        });

        spSorteos = findViewById(R.id.liqDia_spSorteos);
        spSorteos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                codSorteo = listaSorteosUsu.get(position).getCodSorteo();

                //consultaSorteo();

                if (position > 0) {
                    consultaSorteo();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        recyclerLiqDia = findViewById(R.id.liqDia_recycler);
        recyclerLiqDia.setLayoutManager(new GridLayoutManager(this, 2));

    }


    private void limpiaCampos() {

        mVentaTotal = 0;
        tvVentaTotal.setText(fEntero.format(mVentaTotal));
        mComision = 0;
       // tvComision.setText(fEntero.format(mComision));
        mPremio = 0;
       // tvPremio.setText(fEntero.format(mPremio));
        mMontoLiq = 0;
       // tvMontoLiq.setText(fEntero.format(mMontoLiq));
        listaVentaTkt = new ArrayList<>();
        construirRecycler();

    }


    private void obtieneFechaSorteo() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because January is zero
                final String selectedDate = fCeros.format(day) + "/" + fCeros.format(month + 1) + " / " + year;
                mFechaSorteo = year + "-" + fCeros.format(month + 1) + "-" + fCeros.format(day);
                tvFechaSorteo.setText(selectedDate);
                consultaSorteo();
            }
        });

        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private void llenaSorteosUsu() {

        progressBar.setVisibility(View.VISIBLE);
        listaSorteosUsu = new ArrayList<>();
        SorteoUsuVO sorteo = new SorteoUsuVO();
        sorteo.setCodSorteo(0);
        sorteo.setNomSorteo("Seleccone un sorteo");
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

    private void consultaSorteo() {

        if (codSorteo == 0) {
            spSorteos.requestFocus();
            return;
        }

        listaVentaTkt = new ArrayList<>();
        JSONObject jsonSend = new JSONObject();
        try {
            jsonSend.put("fecha_sorteo", mFechaSorteo);
            jsonSend.put("cod_sorteo", codSorteo);
            jsonSend.put("cod_usuario", Variables.COD_USUARIO);
            jsonSend.put("cod_suc", Variables.COD_AGENCIA);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mVentaTotal = 0;
        mVentaNor = 0;
        mVentaRev = 0;
        mPremio = 0;
        mComision = 0;
        mMontoLiq = 0;

        progressBar.setVisibility(View.VISIBLE);
        usuarioDAO.consultaLiqDiaria(this, jsonSend, new IResult() {
            @Override
            public void onResponse(JSONObject object) {
                progressBar.setVisibility(View.GONE);

                if (object.optJSONObject("resp") != null) {

                    JSONArray liqSorteo = object.optJSONObject("resp").optJSONArray("liqSorteo");
                    for (int i = 0; i < liqSorteo.length(); i++) {
                        VentaTktVO ventaTktVO = new VentaTktVO();
                        ventaTktVO.setNumero(liqSorteo.optJSONObject(i).optString("num_jugado"));
                        ventaTktVO.setMonto(liqSorteo.optJSONObject(i).optInt("mon_venta"));
                        ventaTktVO.setMontoRev(liqSorteo.optJSONObject(i).optInt("mon_venta_rev"));
                        mVentaNor += ventaTktVO.getMonto();
                        mVentaRev += ventaTktVO.getMontoRev();
                        mVentaTotal = mVentaNor+mVentaRev;
                        mPremio += liqSorteo.optJSONObject(i).optInt("premio");
                        mComision += liqSorteo.optJSONObject(i).optInt("comision");
                        listaVentaTkt.add(ventaTktVO);
                    }

                }

                tvVentaTotal.setText(fEntero.format(mVentaTotal));
                //tvComision.setText(fEntero.format(mComision));
                //tvPremio.setText(fEntero.format(mPremio));
                //mMontoLiq = mVentaTotal - mComision - mPremio;
                //tvMontoLiq.setText(fEntero.format(mMontoLiq));

                construirRecycler();

            }

            @Override
            public void onError(VolleyError error) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void construirRecycler() {
        adapterVentaTKT = new AdapterVentaTKT(listaVentaTkt);
        recyclerLiqDia.setAdapter(adapterVentaTKT);
    }


}