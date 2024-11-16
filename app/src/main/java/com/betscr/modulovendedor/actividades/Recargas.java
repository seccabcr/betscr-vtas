package com.betscr.modulovendedor.actividades;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.betscr.modulovendedor.R;
import com.betscr.modulovendedor.adaptadores.AdapterRecargas;
import com.betscr.modulovendedor.interfaces.IResult;
import com.betscr.modulovendedor.modelo.DAO.UsuarioDAO;
import com.betscr.modulovendedor.modelo.DAO.VentaDAO;
import com.betscr.modulovendedor.modelo.VO.Recarga;
import com.betscr.modulovendedor.modelo.Variables;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Recargas extends AppCompatActivity {

    RecyclerView recyclerRecargas;
    ProgressBar progressBar;
    TextView tvNomCliente, tvNuevoMov;
    EditText txtMonRecarga, txtIdCliente;
    ImageButton btnVerificaCta;

    ArrayList<Recarga> listaRecargas;

    VentaDAO ventaDAO;
    UsuarioDAO usuarioDAO;


    private int mMonRecarga;
    private boolean nuevaRecarga;
    private int mCodVendedor, mNumRecarga, mCodCliente;

    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat dfDMY = new SimpleDateFormat("dd/MM/yyyy");
    DecimalFormat fEntero = new DecimalFormat("###,###,###");
    DecimalFormat fCeros = new DecimalFormat("00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recargas);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        iniciaUI();

        limpiaCampos();
        inactivaCampos();
        llenarListaRecargas();


    }


    private void iniciaUI() {

        ventaDAO = new VentaDAO();
        usuarioDAO = new UsuarioDAO();

        progressBar = findViewById(R.id.rec_progressBar);

        txtIdCliente = findViewById(R.id.rec_idCliente);
        txtIdCliente.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){

                    limpiaCampos();
                    txtMonRecarga.setEnabled(false);
                }
            }
        });
        txtIdCliente.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if (txtIdCliente.getText().toString().length() == 8) {
                        consultaNumeroCta();
                        return true;
                    }
                }
                return false;
            }
        });

        tvNomCliente = findViewById(R.id.rec_nomCliente);

        btnVerificaCta = findViewById(R.id.rec_verificaCta);
        btnVerificaCta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                consultaNumeroCta();

            }
        });

        txtMonRecarga = findViewById(R.id.rec_monto);
        txtMonRecarga.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    if (txtMonRecarga.getText().toString().length() == 0) {
                        txtMonRecarga.setText("0");
                    }

                    int monto = Integer.parseInt(txtMonRecarga.getText().toString().replace(",", ""));
                    txtMonRecarga.setText(fEntero.format(monto));
                    return true;
                }
                return false;
            }
        });

        recyclerRecargas = findViewById(R.id.rec_recycler);
        recyclerRecargas.setLayoutManager(new LinearLayoutManager(this));

        tvNuevoMov = findViewById(R.id.rec_nueva);
        tvNuevoMov.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limpiaCampos();
                nuevaRecarga = true;
                mNumRecarga = 0;
                txtIdCliente.setEnabled(true);
                btnVerificaCta.setEnabled(true);
                txtIdCliente.requestFocus();

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_recargas, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_rec_guardar) {
            actualizarRecarga();
            return true;
        } else if (item.getItemId() == R.id.menu_rec_Anular) {
            anularRecarga();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void limpiaCampos() {

        txtMonRecarga.setText("0");
        tvNomCliente.setText("");
        txtIdCliente.setText("");
        mNumRecarga = 0;
        nuevaRecarga = true;

    }

    private void inactivaCampos() {

        txtIdCliente.setEnabled(false);
        txtMonRecarga.setEnabled(false);
        btnVerificaCta.setEnabled(false);
    }

    private void consultaNumeroCta() {


        if (txtIdCliente.getText().toString().length() != 8) {
            Toast.makeText(this, "Se requiere un numero de cuenta válido", Toast.LENGTH_SHORT).show();
            txtIdCliente.requestFocus();
            return;
        }

        JSONObject jsonSend = new JSONObject();
        try {

            jsonSend.put("id_usuario", txtIdCliente.getText().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        progressBar.setVisibility(View.VISIBLE);
        usuarioDAO.consultaUsuarioOnLine(this, jsonSend, new IResult() {
            @Override
            public void onResponse(JSONObject object) {
                progressBar.setVisibility(View.GONE);

                JSONObject resp = object.optJSONObject("resp");

                if (resp.optString("status").equals("error")) {

                    Toast.makeText(Recargas.this, resp.optString("msg"), Toast.LENGTH_SHORT).show();
                    return;
                }

                String nombre = "Cliente: " + resp.optString("nom_cliente");
                tvNomCliente.setText(nombre);
                mCodCliente = resp.optInt("cod_cliente");
                txtMonRecarga.setEnabled(true);
                txtMonRecarga.requestFocus();

            }

            @Override
            public void onError(VolleyError error) {
                progressBar.setVisibility(View.GONE);
            }
        });


    }

    private void consultaRecarga() {

        JSONObject jsonSend = new JSONObject();
        try {

            jsonSend.put("num_recarga", mNumRecarga);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        progressBar.setVisibility(View.VISIBLE);
        ventaDAO.consultaRecarga(this, jsonSend, new IResult() {
            @Override
            public void onResponse(JSONObject object) {
                progressBar.setVisibility(View.GONE);

                JSONObject resp = object.optJSONObject("resp");

                if (resp.optString("status").equals("error")) {
                    Toast.makeText(Recargas.this, resp.optString("msg"), Toast.LENGTH_SHORT).show();
                    return;
                }

                nuevaRecarga = false;
                JSONObject datos = resp.optJSONObject("datos_recarga");
                txtIdCliente.setText(datos.optString("id_cliente"));
                txtIdCliente.setEnabled(false);

                String nombre = "Cliente: " + datos.optString("nom_usuario");
                tvNomCliente.setText(nombre);
                //mCodCliente = resp.optInt("cod_cliente");

                int monto = datos.optInt("mon_recarga");
                txtMonRecarga.setText(fEntero.format(monto));

                txtMonRecarga.setEnabled(true);
                txtMonRecarga.requestFocus();

            }

            @Override
            public void onError(VolleyError error) {
                progressBar.setVisibility(View.GONE);
            }
        });


    }


    private void actualizarRecarga() {

        mMonRecarga = Integer.parseInt(txtMonRecarga.getText().toString().replace(",", ""));
        if (mMonRecarga <= 0) {
            Toast.makeText(this, "NO se puede registrar una recarga menor o igual a cero", Toast.LENGTH_SHORT).show();
            txtMonRecarga.requestFocus();
            return;
        }

        JSONObject jsonSend = new JSONObject();
        try {
            jsonSend.put("nueva", nuevaRecarga);
            jsonSend.put("num_recarga", mNumRecarga);
            jsonSend.put("id_cliente", txtIdCliente.getText().toString());
            jsonSend.put("cod_cliente", mCodCliente);
            jsonSend.put("id_vendedor", Variables.ID_USUARIO);
            jsonSend.put("cod_vendedor", Variables.COD_USUARIO);
            jsonSend.put("cod_suc", Variables.COD_AGENCIA);
            jsonSend.put("mon_recarga", mMonRecarga);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        progressBar.setVisibility(View.VISIBLE);
        ventaDAO.actualizaRecarga(this, jsonSend, new IResult() {
            @Override
            public void onResponse(JSONObject object) {
                progressBar.setVisibility(View.GONE);

                Toast.makeText(Recargas.this, object.optJSONObject("resp").optString("msg"), Toast.LENGTH_SHORT).show();
                llenarListaRecargas();
                limpiaCampos();
                inactivaCampos();

            }

            @Override
            public void onError(VolleyError error) {
                progressBar.setVisibility(View.GONE);
            }
        });


    }

    private void anularRecarga() {

        if (mNumRecarga == 0) {
            Toast.makeText(this, "Seleccione un movimiento válido", Toast.LENGTH_SHORT).show();
            return;
        }
        dialogoAnulaRecarga().show();

    }


    private void llenarListaRecargas() {
        progressBar.setVisibility(View.VISIBLE);
        listaRecargas = new ArrayList<>();
        JSONObject jsonSend = new JSONObject();
        try {
            jsonSend.put("id_vendedor", Variables.ID_USUARIO);
            jsonSend.put("tipo_usuario", Variables.TIPO_USU);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        ventaDAO.listaRecargas(this, jsonSend, new IResult() {
            @Override
            public void onResponse(JSONObject object) {
                progressBar.setVisibility(View.GONE);


                JSONArray movimientos = object.optJSONObject("resp").optJSONArray("lista_recargas");

                if (movimientos.length() > 0) {
                    for (int i = 0; i < movimientos.length(); i++) {
                        Recarga mov = new Recarga();
                        mov.setFec_recarga(movimientos.optJSONObject(i).optString("fec_recarga"));
                        mov.setNum_recarga(movimientos.optJSONObject(i).optInt("num_recarga"));
                        mov.setId_cliente(movimientos.optJSONObject(i).optString("id_cliente"));
                        int monMov = movimientos.optJSONObject(i).optInt("mon_recarga");
                        mov.setMon_recarga(monMov);
                        listaRecargas.add(mov);
                    }
                } else {
                    Toast.makeText(Recargas.this, "NO hay movimientos registrados", Toast.LENGTH_SHORT).show();
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

        AdapterRecargas adapter = new AdapterRecargas(listaRecargas);
        recyclerRecargas.setAdapter(adapter);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mNumRecarga = listaRecargas.get(recyclerRecargas.getChildAdapterPosition(v)).getNum_recarga();

                consultaRecarga();

            }
        });


    }

    private AlertDialog dialogoAnulaRecarga() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Anulación Recargas").setMessage("Esta seguro de anular la recarga?");
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                limpiaCampos();
                inactivaCampos();

            }
        }).setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                anulaRecarga();
            }
        });

        return builder.create();
    }

    private void anulaRecarga() {
        JSONObject jsonSend = new JSONObject();
        try {
            jsonSend.put("num_recarga", mNumRecarga);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        progressBar.setVisibility(View.VISIBLE);
        ventaDAO.anulaRecarga(this, jsonSend, new IResult() {
            @Override
            public void onResponse(JSONObject object) {
                progressBar.setVisibility(View.GONE);


                Toast.makeText(Recargas.this, object.optJSONObject("resp").optString("msg"), Toast.LENGTH_SHORT).show();
                llenarListaRecargas();
                limpiaCampos();
                inactivaCampos();

            }

            @Override
            public void onError(VolleyError error) {
                progressBar.setVisibility(View.GONE);

            }
        });

    }


}