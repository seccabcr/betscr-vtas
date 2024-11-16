package com.betscr.modulovendedor.actividades;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.google.android.material.snackbar.Snackbar;
import com.betscr.modulovendedor.R;
import com.betscr.modulovendedor.adaptadores.AdapterVentaTKT;
import com.betscr.modulovendedor.dialogos.DialogoClonaTktFragment;
import com.betscr.modulovendedor.dialogos.DialogoVentaTktFragment;
import com.betscr.modulovendedor.interfaces.IResult;
import com.betscr.modulovendedor.modelo.DAO.SucursalDAO;
import com.betscr.modulovendedor.modelo.DAO.UsuarioDAO;
import com.betscr.modulovendedor.modelo.DAO.VentaDAO;
import com.betscr.modulovendedor.modelo.VO.SorteoUsuVO;
import com.betscr.modulovendedor.modelo.VO.VentaTktVO;
import com.betscr.modulovendedor.modelo.Variables;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class VentaTkt extends AppCompatActivity implements DialogoVentaTktFragment.EnviaParametros, DialogoClonaTktFragment.RetornaTkt {

    private ArrayList<SorteoUsuVO> listaSorteosUsu;
    private ArrayList<VentaTktVO> listaVentaTkt;
    private ArrayList<VentaTktVO> listaVentaAcumula;
    private static final int IMPRIME_TKT = 100;

    private ProgressBar progressBar;
    private EditText txtNombreCli, txtMonto, txtMontoRev, txtNumero;
    private TextView txtVentaTot, txtDisVta;
    private Spinner spSorteos, spTipoApu;
    private Button btnGT, btnNuevoTkt;
    private RecyclerView rvDetalleTKT;
    private AdapterVentaTKT adapterVentaTKT;

    DecimalFormat fDecimal;
    DecimalFormat fNum2d;
    DecimalFormat fNum3d;
    DecimalFormat fNum4d;
    DecimalFormatSymbols simboloDec;
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat tf = new SimpleDateFormat("HH:mm:ss");


    private SucursalDAO sucursalDAO;
    private UsuarioDAO usuarioDAO;
    private VentaDAO ventaDAO;
    private int codSucursal; // Almacena el codigo de la Sucursal seleccionado en el Spinner
    private int codSorteo; // Almacena el codigo del sorteo seleccionado en el Spinner
    private int mVentaTotal = 0;
    private int mDisVta = 0;
    private int mTotalNor = 0;
    private int mTotalRev = 0;
    private int mNumTkt = 0;
    private int mFacPremio;
    private int mFacPremio2;
    private int mNumDigitos = 2;
    private int mReventado = 0;
    private int mTipoApu = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venta_tkt);

        iniUI();
        limpiaCampos();
        llenaSorteosUsu();
        consultaDisVta();

    }


    private void iniUI() {

        sucursalDAO = new SucursalDAO();
        usuarioDAO = new UsuarioDAO();
        ventaDAO = new VentaDAO();
        listaVentaTkt = new ArrayList<>();
        simboloDec = new DecimalFormatSymbols(Locale.ENGLISH);
        fDecimal = new DecimalFormat("###,###,###", simboloDec);
        fNum2d = new DecimalFormat("00");
        fNum3d = new DecimalFormat("000");
        fNum4d = new DecimalFormat("0000");

        progressBar = findViewById(R.id.vtkt_progressBar);
        txtNombreCli = findViewById(R.id.vtkt_nombreCli);
        txtNombreCli.setEnabled(false);
        txtVentaTot = findViewById(R.id.vtkt_total);
        txtVentaTot.setText(fDecimal.format(mVentaTotal));

        txtDisVta = findViewById(R.id.vtkt_disVta);
        txtDisVta.setText(fDecimal.format(mDisVta));

        rvDetalleTKT = findViewById(R.id.vtkt_recycler);
        //rvDetalleTKT.setLayoutManager(new LinearLayoutManager(this));
        rvDetalleTKT.setLayoutManager(new GridLayoutManager(this, 2));
        rvDetalleTKT.setEnabled(false);


        txtMonto = findViewById(R.id.vtkt_monto);
        txtMonto.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    if (txtMonto.getText().toString().length() == 0) {
                        txtMonto.setText("0");
                    }

                    int monto = Integer.parseInt(txtMonto.getText().toString().replace(",", ""));
                    txtMonto.setText(fDecimal.format(monto));
                    return true;

                }
                return false;
            }
        });

        txtMontoRev = findViewById(R.id.vtkt_monto_rev);
        txtMontoRev.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    if (txtMontoRev.getText().toString().length() == 0) {
                        txtMontoRev.setText("0");
                    }

                    int montoRev = Integer.parseInt(txtMontoRev.getText().toString().replace(",", ""));
                    txtMontoRev.setText(fDecimal.format(montoRev));
                    return true;


                }
                return false;
            }
        });

        txtNumero = findViewById(R.id.vtkt_numero);
        txtNumero.setEnabled(false);
        txtNumero.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    agregaNumero();
                    //txtNumero.requestFocus();
                    return true;
                }

                return false;
            }
        });

        txtNumero.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String dato = s.toString();
                if (dato.length() >= mNumDigitos) {
                    agregaNumero();
                    //txtNumero.requestFocus();
                }

            }
        });

        spSorteos = findViewById(R.id.vtkt_spSorteos);
        spSorteos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                codSorteo = listaSorteosUsu.get(position).getCodSorteo();
                mFacPremio = listaSorteosUsu.get(position).getFac_premio_usu();
                mFacPremio2 = listaSorteosUsu.get(position).getFac_premio2();
                if (listaSorteosUsu.get(position).getNum_digitos() != mNumDigitos) {
                    limpiaCampos();
                }
                mNumDigitos = listaSorteosUsu.get(position).getNum_digitos();
                mReventado = listaSorteosUsu.get(position).getReventado();
                mTipoApu = 0;
                spTipoApu.setSelection(0);

                txtMonto.setEnabled(false);
                txtMontoRev.setEnabled(false);
                txtNumero.setEnabled(false);
                btnGT.setEnabled(false);
                rvDetalleTKT.setEnabled(false);
                txtNombreCli.setEnabled(false);

                if (position > 0) {
                    //llenarListaVentaAcumulada();
                    consultaEstadoSorteo();

                } else {

                    spSorteos.requestFocus();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spTipoApu = findViewById(R.id.vtkt_spTipoApu);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this,
                R.array.tipoApuesta, R.layout.texto_spinner);
        spTipoApu.setAdapter(arrayAdapter);
        spTipoApu.setEnabled(false);
        spTipoApu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mTipoApu = position;
                if (mTipoApu == 1) {
                    borraListaNumeros();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnGT = findViewById(R.id.vtkt_GT);
        btnGT.setEnabled(false);
        btnGT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mNumDigitos == 2) {
                    calculaGT();
                } else if (mNumDigitos == 3) {
                    calculaGT3();
                } else {
                    calculaGT4();
                }

                txtNumero.requestFocus();

            }
        });

        btnNuevoTkt = findViewById(R.id.vtkt_nuevoTkt);
        btnNuevoTkt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (codSorteo == 0) {
                    Toast.makeText(VentaTkt.this, "Debe seleccionar un sorteo", Toast.LENGTH_SHORT).show();
                    spSorteos.requestFocus();
                    return;
                }

                limpiaCampos();
                txtMonto.requestFocus();
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.venta_tkt, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.enviar_venta_tkt) {
            enviarTkt();
            return true;
        } else if (id == R.id.clona_tkt) {


            dialogoClonaTkt();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void dialogoClonaTkt() {

        if (codSorteo == 0) {
            Toast.makeText(this, "Debe seleccione un sorteo", Toast.LENGTH_SHORT).show();
            return;
        }


        DialogoClonaTktFragment dialogoClonaTktFragment = new DialogoClonaTktFragment();
        dialogoClonaTktFragment.show(getSupportFragmentManager(), "DialogoClonaTkt");

    }


    private void consultaEstadoSorteo() {

        JSONObject jsonSend = new JSONObject();
        try {
            jsonSend.put("cod_usuario", Variables.COD_USUARIO);
            jsonSend.put("cod_sorteo", codSorteo);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        progressBar.setVisibility(View.VISIBLE);
        ventaDAO.consultaEstadoSorteo(this, jsonSend, new IResult() {
            @Override
            public void onResponse(JSONObject object) {
                progressBar.setVisibility(View.GONE);

                if (object.optJSONObject("resp").optInt("estadoRes") == 0) {
                    Toast.makeText(VentaTkt.this, object.optJSONObject("resp").optString("msg"), Toast.LENGTH_SHORT).show();
                    codSorteo = 0;
                    spSorteos.setSelection(0);
                    return;
                }

                txtMonto.setEnabled(true);
                txtMontoRev.setEnabled(mReventado > 0);
                spTipoApu.setEnabled(mNumDigitos >= 3 && mFacPremio2 > 0);
                txtNumero.setEnabled(true);
                btnGT.setEnabled(true);
                rvDetalleTKT.setEnabled(true);
                txtNombreCli.setEnabled(true);
                txtMonto.requestFocus();

            }

            @Override
            public void onError(VolleyError error) {
                progressBar.setVisibility(View.GONE);

                Toast.makeText(VentaTkt.this, "Error: " + error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });


    }


    private void construirSpinnerSorteos() {
        ArrayAdapter<String> adapter = new ArrayAdapter(this, R.layout.item_spinner, listaSorteosUsu);
        spSorteos.setAdapter(adapter);
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

        usuarioDAO.listaSorteosUsuVta(this, jsonSend, new IResult() {
            @Override
            public void onResponse(JSONObject object) {
                progressBar.setVisibility(View.GONE);

                if (object.optJSONObject("resp") != null) {

                    JSONArray lista = object.optJSONObject("resp").optJSONArray("sorteosUsu");

                    for (int i = 0; i < lista.length(); i++) {
                        SorteoUsuVO sorteo = new SorteoUsuVO();
                        sorteo.setCodSorteo(lista.optJSONObject(i).optInt("cod_sorteo"));
                        sorteo.setNomSorteo(lista.optJSONObject(i).optString("nom_sorteo"));
                        sorteo.setPor_comision_usu(lista.optJSONObject(i).optDouble("por_comision_usu"));
                        sorteo.setFac_premio_usu(lista.optJSONObject(i).optInt("fac_premio_usu"));
                        sorteo.setFac_premio2(lista.optJSONObject(i).optInt("fac_premio_comb_usu"));
                        sorteo.setReventado(lista.optJSONObject(i).optInt("reventado"));
                        sorteo.setNum_digitos(lista.optJSONObject(i).optInt("num_digitos"));
                        listaSorteosUsu.add(sorteo);
                    }
                } else {
                    Toast.makeText(VentaTkt.this, "No hay sorteos disponibles para venta", Toast.LENGTH_SHORT).show();
                }

                construirSpinnerSorteos();

            }

            @Override
            public void onError(VolleyError error) {
                progressBar.setVisibility(View.GONE);
            }
        });


    }


    private void consultaDisVta() {

        JSONObject jsonSend = new JSONObject();
        try {
            jsonSend.put("cod_usuario", Variables.COD_USUARIO);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        progressBar.setVisibility(View.VISIBLE);
        ventaDAO.consultaDisVtaUsu(this, jsonSend, new IResult() {
            @Override
            public void onResponse(JSONObject object) {
                progressBar.setVisibility(View.GONE);

                mDisVta = object.optJSONObject("resp").optInt("dis_vta");
                if (mDisVta < 0) {
                    txtDisVta.setText("Ilimitado");
                } else {
                    txtDisVta.setText(fDecimal.format(mDisVta));
                }

            }

            @Override
            public void onError(VolleyError error) {
                progressBar.setVisibility(View.GONE);

            }
        });


    }


    private void agregaNumero() {
        if (txtNumero.getText().toString().length() == 0) {
            return;
        }

        if (txtMontoRev.getText().toString().length() == 0) {
            txtMontoRev.setText("0");
        }

        //String num = fNumero.format(Integer.valueOf(txtNumero.getText().toString()));
        String num = txtNumero.getText().toString();
        int mon = Integer.parseInt(txtMonto.getText().toString().replace(",", ""));
        int monRev = Integer.parseInt(txtMontoRev.getText().toString().replace(",", ""));

        if (mon <= 0) {
            Toast.makeText(this, "Monto Apuesta NO puede ser CERO", Toast.LENGTH_SHORT).show();
            txtMonto.setText("");
            txtMonto.requestFocus();
            return;

        } else if (!esMultiplo(mon)) {
            Toast.makeText(this, "Monto apuesta NO es multiplo de 50", Toast.LENGTH_SHORT).show();
            txtMonto.requestFocus();
            return;
        }

        if(monRev<0){
            Toast.makeText(this, "Monto reventado NO puede ser menor a CERO", Toast.LENGTH_SHORT).show();
            txtMontoRev.setText("");
            txtMontoRev.requestFocus();
            return;

        }else if (monRev > 0 && !esMultiplo(monRev)) {
            Toast.makeText(this, "Monto reventado NO es multiplo de 50", Toast.LENGTH_SHORT).show();
            txtMontoRev.requestFocus();
            return;
        }

        if (!numeroValido(num)) {
            Toast.makeText(this, "Número digitado NO es valido", Toast.LENGTH_SHORT).show();
            txtNumero.requestFocus();
            return;

        }


        // Verifica que apuesta reventado no sea mayor que apuesta normal
        if (monRev > mon) {
            Toast.makeText(this, "Apuesta Reventado NO puede ser mayor que apuesta normal", Toast.LENGTH_SHORT).show();
            txtMontoRev.setText(fDecimal.format(mon));
            txtMontoRev.requestFocus();
            return;
        }


        //Busca numero en el arrayList
        boolean nuevoNum = true;
        for (int i = 0; i < listaVentaTkt.size(); i++) {
            if (listaVentaTkt.get(i).getNumero().equals(num)) {
                nuevoNum = false;
                int nuevoMonto = listaVentaTkt.get(i).getMonto() + mon;
                int nuevoMontoRev = listaVentaTkt.get(i).getMontoRev() + monRev;
                listaVentaTkt.get(i).setMonto(nuevoMonto);
                listaVentaTkt.get(i).setMontoRev(nuevoMontoRev);
                break;
            }
        }
        if (nuevoNum) {
            VentaTktVO ventaTktVO = new VentaTktVO();
            ventaTktVO.setNumero(num);
            ventaTktVO.setMonto(mon);
            ventaTktVO.setMontoRev(monRev);
            listaVentaTkt.add(0, ventaTktVO);

        }
        calculaMontos();
        construirRecycler();

    }

    private boolean numeroValido(String num) {

        boolean numVal = true;

        if (num.length() != mNumDigitos) {
            numVal = false;
        } else if (mNumDigitos == 3 && mTipoApu == 1) {

            char[] aDigitos = num.toCharArray();
            if (aDigitos[0] == aDigitos[1] || aDigitos[0] == aDigitos[2] || aDigitos[1] == aDigitos[2]) {
                numVal = false;
            }
        }

        return numVal;

    }

    private boolean esMultiplo(int monto) {

        return monto % 50 == 0;

    }


    private void borraListaNumeros() {

        listaVentaTkt = new ArrayList<>();
        calculaMontos();
        construirRecycler();
    }


    private void construirRecycler() {
        adapterVentaTKT = new AdapterVentaTKT(listaVentaTkt);
        rvDetalleTKT.setAdapter(adapterVentaTKT);
        adapterVentaTKT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int mon = listaVentaTkt.get(rvDetalleTKT.getChildAdapterPosition(v)).getMonto();
                int monRev = listaVentaTkt.get(rvDetalleTKT.getChildAdapterPosition(v)).getMontoRev();
                String num = listaVentaTkt.get(rvDetalleTKT.getChildAdapterPosition(v)).getNumero();
                DialogoVentaTktFragment dialogoVentaTktFragment = new DialogoVentaTktFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("monto", mon);
                bundle.putInt("montoRev", monRev);
                bundle.putString("numero", num);
                bundle.putInt("reventado", mReventado);
                dialogoVentaTktFragment.setArguments(bundle);
                dialogoVentaTktFragment.show(getSupportFragmentManager(), "DialogoVentaTkt");

            }
        });

    }

    private void calculaGT() {

        if (txtMontoRev.getText().toString().length() == 0) {
            txtMontoRev.setText("0");
        }

        boolean recalcular = true;
        int numero;
        do {

            numero = (int) Math.floor(Math.random() * 100);
            recalcular = false;

            for (int i = 0; i < listaVentaTkt.size(); i++) {
                if (listaVentaTkt.get(i).getNumero().equals(fNum2d.format(numero))) {
                    recalcular = true;
                    break;
                }
            }

        } while (recalcular);

        String num = fNum2d.format(numero);
        int mon = Integer.parseInt(txtMonto.getText().toString().replace(",", ""));
        int monRev = Integer.parseInt(txtMontoRev.getText().toString().replace(",", ""));
        VentaTktVO ventaTktVO = new VentaTktVO();
        ventaTktVO.setNumero(num);
        ventaTktVO.setMonto(mon);
        ventaTktVO.setMontoRev(monRev);
        listaVentaTkt.add(0, ventaTktVO);

        calculaMontos();

        construirRecycler();

        txtNumero.setText("");
        txtNumero.requestFocus();

    }


    private void calculaGT3() {

        if (txtMontoRev.getText().toString().length() == 0) {
            txtMontoRev.setText("0");
        }

        boolean recalcular = true;
        int numero;
        do {

            numero = (int) Math.floor(Math.random() * 1000);
            recalcular = false;

            if (numeroValido(fNum3d.format(numero))) {
                // Busca numero en la lista
                for (int i = 0; i < listaVentaTkt.size(); i++) {
                    if (listaVentaTkt.get(i).getNumero().equals(fNum3d.format(numero))) {
                        recalcular = true;
                        break;
                    }
                }
            }

        } while (recalcular);

        String num = fNum3d.format(numero);
        int mon = Integer.parseInt(txtMonto.getText().toString().replace(",", ""));
        int monRev = Integer.parseInt(txtMontoRev.getText().toString().replace(",", ""));
        VentaTktVO ventaTktVO = new VentaTktVO();
        ventaTktVO.setNumero(num);
        ventaTktVO.setMonto(mon);
        ventaTktVO.setMontoRev(monRev);
        listaVentaTkt.add(0, ventaTktVO);

        calculaMontos();

        construirRecycler();

        txtNumero.setText("");
        txtNumero.requestFocus();

    }

    private void calculaGT4() {

        if (txtMontoRev.getText().toString().length() == 0) {
            txtMontoRev.setText("0");
        }

        boolean recalcular = true;
        int numero;
        do {

            numero = (int) Math.floor(Math.random() * 10000);
            recalcular = false;

            for (int i = 0; i < listaVentaTkt.size(); i++) {
                if (listaVentaTkt.get(i).getNumero().equals(fNum4d.format(numero))) {
                    recalcular = true;
                    break;
                }
            }

        } while (recalcular);

        String num = fNum4d.format(numero);
        int mon = Integer.parseInt(txtMonto.getText().toString().replace(",", ""));
        int monRev = Integer.parseInt(txtMontoRev.getText().toString().replace(",", ""));
        VentaTktVO ventaTktVO = new VentaTktVO();
        ventaTktVO.setNumero(num);
        ventaTktVO.setMonto(mon);
        ventaTktVO.setMontoRev(monRev);
        listaVentaTkt.add(0, ventaTktVO);

        calculaMontos();

        construirRecycler();

        txtNumero.setText("");
        txtNumero.requestFocus();

    }

    // @SuppressLint("MissingSuperCall")
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMPRIME_TKT) {

            // dialogoRepiteJugada().show();
            //limpiaCampos();
            //txtMonto.requestFocus();

            consultaDisVta();
            mNumTkt = 0;
            spSorteos.requestFocus();
        }
    }


    private void enviarTkt() {

        if (mVentaTotal == 0) {
            Toast.makeText(this, "NO se puede registrar un tiquete en CERO", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONArray detalleVenta = new JSONArray();
        for (int i = 0; i < listaVentaTkt.size(); i++) {
            JSONObject numVendido = new JSONObject();
            try {
                numVendido.put("num_jugado", listaVentaTkt.get(i).getNumero());
                numVendido.put("mon_jugado", listaVentaTkt.get(i).getMonto());
                numVendido.put("mon_jugado_rev", listaVentaTkt.get(i).getMontoRev());
                detalleVenta.put(numVendido);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        String nombre = "";

        if (txtNombreCli.getText().toString().length() > 0) {

            if (txtNombreCli.getText().toString().length() > 20) {
                nombre = txtNombreCli.getText().toString().substring(0, 20);

            } else {
                nombre = txtNombreCli.getText().toString();
            }


        }


        JSONObject jsonSend = new JSONObject();
        try {
            jsonSend.put("cod_usuario", Variables.COD_USUARIO);
            jsonSend.put("cod_sorteo", codSorteo);
            jsonSend.put("num_digitos", mNumDigitos);
            jsonSend.put("hora_envio", tf.format(new Date()));
            jsonSend.put("cod_suc", Variables.COD_AGENCIA);
            jsonSend.put("nom_cliente", nombre);
            jsonSend.put("tipo_jugada", mTipoApu);
            jsonSend.put("numeros", detalleVenta);
        } catch (
                JSONException e) {
            e.printStackTrace();
        }
        progressBar.setVisibility(View.VISIBLE);
        ventaDAO.registraVentatkt(this, jsonSend, new

                IResult() {
                    @Override
                    public void onResponse(JSONObject object) {
                        progressBar.setVisibility(View.GONE);

                        if (object.optJSONObject("resp").optInt("estadoRes") == 0) {
                            Toast.makeText(VentaTkt.this, object.optJSONObject("resp").optString("msg"), Toast.LENGTH_SHORT).show();
                            return;
                        }


                        mNumTkt = object.optJSONObject("resp").optInt("num_tkt");

                        //Snackbar.make(getCurrentFocus(), "Tiquete registrado: " + mNumTkt, Snackbar.LENGTH_LONG).show();

                        Toast.makeText(VentaTkt.this, "Tiquete registrado: " + mNumTkt, Toast.LENGTH_SHORT).show();

                        Intent intentImprime = new Intent(VentaTkt.this, ImprimeTkt.class);
                        intentImprime.putExtra("numTkt", mNumTkt);
                        intentImprime.putExtra("reimprime", false);
                        if (mTipoApu == 0) {
                            intentImprime.putExtra("facPremio", mFacPremio);
                        } else {
                            intentImprime.putExtra("facPremio", mFacPremio2);
                        }

                        startActivityForResult(intentImprime, IMPRIME_TKT);

                    }

                    @Override
                    public void onError(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                    }
                });


    }

    private void clonaTkt(int numTkt) {

        progressBar.setVisibility(View.VISIBLE);
        JSONObject jsonSend = new JSONObject();
        try {
            jsonSend.put("cod_usuario", Variables.COD_USUARIO);
            jsonSend.put("num_tkt", numTkt);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        listaVentaTkt = new ArrayList<>();
        ventaDAO.clonarTkt(this, jsonSend, new IResult() {
            @Override
            public void onResponse(JSONObject object) {
                progressBar.setVisibility(View.GONE);

                if (object.optJSONObject("resp") == null) {
                    Toast.makeText(VentaTkt.this, "Tiquete NO existe o NO fue emitido por este usuario", Toast.LENGTH_SHORT).show();
                    return;
                }

                txtNombreCli.setText(object.optJSONObject("resp").optString("nom_cliente"));

                JSONArray lista = object.optJSONObject("resp").optJSONArray("numeros");
                for (int i = 0; i < lista.length(); i++) {
                    String num = lista.optJSONObject(i).optString("num_jugado");
                    int mon = lista.optJSONObject(i).optInt("mon_jugado");

                    int monRev = 0;
                    if (mReventado == 1) {
                        monRev = lista.optJSONObject(i).optInt("mon_jugado_rev");
                    }

                    if (num.length() == mNumDigitos && numeroValido(num)) {
                        VentaTktVO ventaTktVO = new VentaTktVO();
                        ventaTktVO.setNumero(num);
                        ventaTktVO.setMonto(mon);
                        ventaTktVO.setMontoRev(monRev);
                        listaVentaTkt.add(0, ventaTktVO);
                    }


                }
                calculaMontos();
                if (listaVentaTkt.size() == 0) {

                    Snackbar.make(getCurrentFocus(), "Los numeros del tiquete clonado no son compatibles con el sorteo actual", Snackbar.LENGTH_INDEFINITE)
                            .setAction("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            }).show();
                }

                construirRecycler();

            }

            @Override
            public void onError(VolleyError error) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }


    private void limpiaCampos() {
        mNumTkt = 0;
        mVentaTotal = 0;
        txtMonto.setText("0");
        txtMontoRev.setText("0");
        txtNumero.setText("");
        txtNombreCli.setText("");
        txtVentaTot.setText(fDecimal.format(mVentaTotal));
        spTipoApu.setSelection(0);
        listaVentaTkt = new ArrayList<>();
        construirRecycler();

        //spSorteos.setSelection(0);
        //spSorteos.requestFocus();
    }

    @SuppressLint("NewApi")
    private void calculaMontos() {

        //Calcula el total
        mVentaTotal = 0;
        mTotalNor = 0;
        mTotalRev = 0;
        for (int i = 0; i < listaVentaTkt.size(); i++) {
            mVentaTotal += listaVentaTkt.get(i).getMonto();
            mVentaTotal += listaVentaTkt.get(i).getMontoRev();
        }

        txtVentaTot.setText(fDecimal.format(mVentaTotal));
        txtNumero.setText("");

    }

    public AlertDialog dialogoRepiteJugada() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Repite Jugada").setMessage("Desea repetir la jugada?");
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                limpiaCampos();
                txtMonto.requestFocus();
                //spSorteos.requestFocus();
            }
        }).setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                spSorteos.requestFocus();
            }
        });

        return builder.create();
    }


    @Override
    public void onClickBtnAceptar(String num, int monto, int montoRev) {

        for (int i = 0; i < listaVentaTkt.size(); i++) {
            if (listaVentaTkt.get(i).getNumero().equals(num)) {
                listaVentaTkt.get(i).setMonto(monto);
                listaVentaTkt.get(i).setMontoRev(montoRev);
                break;
            }
        }

        calculaMontos();

        construirRecycler();

    }

    @Override
    public void onClickBtnEliminar(String num) {

        for (int i = 0; i < listaVentaTkt.size(); i++) {
            if (listaVentaTkt.get(i).getNumero().equals(num)) {
                listaVentaTkt.remove(i);
                break;
            }
        }

        calculaMontos();

        construirRecycler();
    }


    @Override
    public void btnAceptarClonaTkt(int numTkt) {
        clonaTkt(numTkt);
    }
}