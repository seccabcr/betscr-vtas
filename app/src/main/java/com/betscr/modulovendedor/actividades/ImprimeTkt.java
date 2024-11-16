package com.betscr.modulovendedor.actividades;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.VolleyError;
import com.betscr.modulovendedor.R;
import com.betscr.modulovendedor.interfaces.IResult;
import com.betscr.modulovendedor.metodos.BluetoothService;
import com.betscr.modulovendedor.modelo.DAO.VentaDAO;
import com.betscr.modulovendedor.modelo.VO.VentaTktVO;
import com.betscr.modulovendedor.modelo.Variables;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class ImprimeTkt extends AppCompatActivity {


    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    private static boolean is58mm = true;

    /******************************************************************************************************/
    // Name of the connected device
    private final String mConnectedDeviceName = null;
    // Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;
    // Member object for the services
    private BluetoothService mService = null;

    private TextView tvDetalleTkt;

    private ProgressBar progressBar;

    VentaDAO ventaDAO;

    DecimalFormat fMonto = new DecimalFormat("#####,##0", new DecimalFormatSymbols(Locale.ENGLISH));
    DecimalFormat fEntero = new DecimalFormat("####,##0");
    DecimalFormat fTkt = new DecimalFormat("0000000000");
    DecimalFormat fNumero = new DecimalFormat("00");

    SimpleDateFormat formatoDMY = new SimpleDateFormat("dd-MM-yyyy");
    SimpleDateFormat formatoYMD = new SimpleDateFormat("yyyy-MM-dd");

    //TextView detalleTkt;
    ArrayList<VentaTktVO> listaNumerosTkt;
    private String mFechaTkt, mHoraTkt, mNomSorteo, mNomCliente;
    private int mNumeroTkt, mFacPremio, mTotalRev, mTotalTkt, mTotalMon, mNumDigitos, mTipoJugada, mCodSorteo;
    private boolean reimprime = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imprime_tkt);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        progressBar = findViewById(R.id.impTkt_progressBar);
        tvDetalleTkt = findViewById(R.id.tv_imprimeTkt);
        ventaDAO = new VentaDAO();

        recibeExtras();
        consultaTkt();

    }

    @Override
    public void onStart() {
        super.onStart();

        // If Bluetooth is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the session
        } else {
            if (mService == null)
                KeyListenerInit();// Inicializa los campos


        }
    }

    private void recibeExtras() {

        mNumeroTkt = getIntent().getExtras().getInt("numTkt");
        reimprime = getIntent().getBooleanExtra("reimprime", false);
        mFacPremio = getIntent().getExtras().getInt("facPremio");

    }

    @Override
    public synchronized void onResume() {
        super.onResume();

        if (mService != null) {

            if (mService.getState() == BluetoothService.STATE_NONE) {
                // Start the Bluetooth services
                mService.start();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Stop the Bluetooth services
        if (mService != null)
            mService.stop();
    }


    private void KeyListenerInit() {

        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth No esta disponible", Toast.LENGTH_LONG).show();
        } else {
            mService = new BluetoothService(this);
            conectaImpresora();
        }


    }

    private void conectaImpresora() {
        if (Variables.DIRECCION_MAC.length() > 0) {
            BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            BluetoothDevice device = bluetoothManager.getAdapter().getRemoteDevice(Variables.DIRECCION_MAC);
            mService.connect(device);
        } else {
            Toast.makeText(this, "Impresora NO ha sido configurada", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.imprime_tkt, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.impTkt_imprimir) {

            imprimeTktk();

            return true;
        } else if (id == R.id.impTkt_compartir) {

            compartirTkt();

            return true;

        } else if (id == R.id.impTkt_anular) {

            anulaTkt();

            return true;

        }

        return super.onOptionsItemSelected(item);
    }


    private void consultaTkt() {

        listaNumerosTkt = new ArrayList<>();

        JSONObject jsonSend = new JSONObject();
        try {
            jsonSend.put("num_tkt", mNumeroTkt);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        progressBar.setVisibility(View.VISIBLE);
        ventaDAO.consultaVentatkt(this, jsonSend, new IResult() {
            @Override
            public void onResponse(JSONObject object) {
                progressBar.setVisibility(View.GONE);

                if (object.optJSONObject("resp") != null) {
                    mCodSorteo = object.optJSONObject("resp").optInt("cod_sorteo");
                    mNomSorteo = object.optJSONObject("resp").optString("nom_sorteo");
                    mNumDigitos = object.optJSONObject("resp").optInt("num_digitos");
                    mTipoJugada = object.optJSONObject("resp").optInt("tipo_jugada");
                    //mFechaTkt = object.optJSONObject("resp").optString("fecha_tkt");
                    mHoraTkt = object.optJSONObject("resp").optString("hora_tkt");
                    mNomCliente = object.optJSONObject("resp").optString("nom_cliente");

                    Date fechaJson = null;
                    try {
                        fechaJson = formatoYMD.parse(object.optJSONObject("resp").optString("fecha_tkt"));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    mFechaTkt = formatoDMY.format(fechaJson);

                    JSONArray listaNumeros = object.optJSONObject("resp").optJSONArray("numeros");
                    mTotalTkt = 0;
                    mTotalRev = 0;
                    mTotalMon = 0;
                    for (int i = 0; i < listaNumeros.length(); i++) {
                        VentaTktVO ventaTkt = new VentaTktVO();
                        ventaTkt.setNumero(listaNumeros.optJSONObject(i).optString("num_jugado"));
                        ventaTkt.setMonto(listaNumeros.optJSONObject(i).optInt("mon_jugado"));
                        ventaTkt.setMontoRev(listaNumeros.optJSONObject(i).optInt("mon_jugado_rev"));
                        mTotalMon += ventaTkt.getMonto();
                        mTotalRev += ventaTkt.getMontoRev();
                        mTotalTkt = mTotalMon + mTotalRev;
                        listaNumerosTkt.add(ventaTkt);
                    }

                    /*Collections.sort(listaNumerosTkt, new Comparator<VentaTktVO>() {
                        @Override
                        public int compare(VentaTktVO o1, VentaTktVO o2) {
                            String a = fEntero.format(o1.getMonto()) + o1.getNumero();
                            String b = fEntero.format(o2.getMonto()) + o2.getNumero();
                            return a.compareTo(b);
                        }
                    });*/

                    if (mNumDigitos == 2) {

                        tvDetalleTkt.setText(llenaDetalleTkt());

                    } else {

                        tvDetalleTkt.setText(llenaDetalleTkt4());

                    }


                } else {
                    tvDetalleTkt.setText("Tiquete erroneo");
                }

            }

            @Override
            public void onError(VolleyError error) {
                progressBar.setVisibility(View.GONE);
            }
        });


    }

    private String llenaDetalleTkt() {

        if (mTotalRev == 0) {
            Collections.sort(listaNumerosTkt, new Comparator<VentaTktVO>() {
                @Override
                public int compare(VentaTktVO o1, VentaTktVO o2) {
                    String a = fEntero.format(o1.getMonto()) + o1.getNumero();
                    String b = fEntero.format(o2.getMonto()) + o2.getNumero();
                    return a.compareTo(b);
                }
            });
        }

        String sNum, sMon;

        StringBuilder builder = new StringBuilder();

        builder.append(Variables.TIT_TKT).append("\r\n");
        builder.append("Tiquete de compra").append("\r\n");
        builder.append("Numero Tiquete: ").append(fTkt.format(mNumeroTkt)).append("\n");
        builder.append("\r\n");
        builder.append("Fecha: " + mFechaTkt).append(String.format("%12s", mHoraTkt)).append("\n");
        builder.append(("Sorteo: ") + mNomSorteo).append("\r\n");

        String nomJugada = "Numero Exacto";
        if (mTipoJugada==1){
            nomJugada="Combinado / Desorden";
        }

        builder.append(("Tipo Jugada: ") + nomJugada).append("\r\n");

        if (mNomCliente.length() > 0) {
            builder.append(("Cliente: ") + mNomCliente).append("\r\n");
        }


        if (mTotalRev == 0) {

            builder.append("Detalle\r\n");
            builder.append("==============================\r\n");
            int montoGrupo = 0;
            int numLinea = 1;

            for (int i = 0; i < listaNumerosTkt.size(); i++) {
                int mon_jugado = listaNumerosTkt.get(i).getMonto();
                String num_jugado = listaNumerosTkt.get(i).getNumero();

                if (mon_jugado != montoGrupo) {
                    if (montoGrupo > 0) {
                        builder.append("\r\n");
                    }
                    montoGrupo = mon_jugado;
                    numLinea = 1;
                    builder.append(String.format("%9s", fMonto.format(montoGrupo))).append(" x ");
                } else {
                    if (numLinea == 1) {
                        //builder.append("####,###").append("   ");
                        builder.append(String.format("%12s", " "));
                    }
                }
                builder.append(num_jugado.trim());
                numLinea += 1;

                if (numLinea < 9) {
                    builder.append(", ");
                }
                if (numLinea == 9) {
                    numLinea = 1;
                    builder.append("\n");
                }
            }
            builder.append("\n");

        } else {

            builder.append("Detalle\n");

            builder.append(String.format("%4s", "Num"));
            builder.append(String.format("%11s", "  Apuesta"));
            builder.append(String.format("%11s", "Reventado")).append("\n");
            builder.append("==============================\n");

            for (int i = 0; i < listaNumerosTkt.size(); i++) {

                int mon_jugado = listaNumerosTkt.get(i).getMonto();
                int mon_jugado_rev = listaNumerosTkt.get(i).getMontoRev();
                String num_jugado = listaNumerosTkt.get(i).getNumero();

                builder.append(String.format("%4s", num_jugado.trim()));
                builder.append(String.format("%11s", fMonto.format(mon_jugado)));
                builder.append(String.format("%11s", fMonto.format(mon_jugado_rev))).append("\n");

            }

        }

        builder.append("==============================\n");
        builder.append("Total Tiquete: ").append(String.format("%10s", fMonto.format(mTotalTkt))).append("\n").append("\n");

        builder.append("PAGAMOS ").append(String.valueOf(mFacPremio)).append(" veces").append("\n").append("\n");


        builder.append(Variables.MSG_TKT).append("\n");
        if (reimprime) {
            builder.append("REIMPRESION").append("\n");
        }

        //builder.append("Emitido por: ").append(Variables.NOMBRE_USUARIO).append("\n\n");

        builder.append("Generado por LOTTOBANCACR").append("\n");

        return builder.toString();

    }


    private String llenaDetalleTkt4() {


        Collections.sort(listaNumerosTkt, new Comparator<VentaTktVO>() {
            @Override
            public int compare(VentaTktVO o1, VentaTktVO o2) {
                String a = fEntero.format(o1.getMonto()) + o1.getNumero();
                String b = fEntero.format(o2.getMonto()) + o2.getNumero();
                return a.compareTo(b);
            }
        });

        String sNum, sMon;

        StringBuilder builder = new StringBuilder();

        builder.append(Variables.TIT_TKT).append("\r\n");
        builder.append("Tiquete de compra").append("\r\n");
        builder.append("Numero Tiquete: ").append(fTkt.format(mNumeroTkt)).append("\n");
        builder.append("\r\n");
        builder.append("Fecha: " + mFechaTkt).append(String.format("%12s", mHoraTkt)).append("\n");
        builder.append(("Sorteo: ") + mNomSorteo).append("\r\n");

        String nomJugada = "Numero Exacto";
        if (mTipoJugada==1){
            nomJugada="Combinado / Desorden";
        }

        builder.append(("Tipo Jugada: ") + nomJugada).append("\r\n");

        if (mNomCliente.length() > 0) {
            builder.append(("Cliente: ") + mNomCliente).append("\r\n");
        }


        builder.append("Detalle\r\n");
        builder.append("==============================\r\n");
        int montoGrupo = 0;
        int numLinea = 1;

        for (int i = 0; i < listaNumerosTkt.size(); i++) {
            int mon_jugado = listaNumerosTkt.get(i).getMonto();
            String num_jugado = listaNumerosTkt.get(i).getNumero();

            if (mon_jugado != montoGrupo) {
                if (montoGrupo > 0) {
                    builder.append("\r\n");
                }
                montoGrupo = mon_jugado;
                numLinea = 1;
                builder.append(String.format("%9s", fMonto.format(montoGrupo))).append(" x ");
            } else {
                if (numLinea == 1) {
                    //builder.append("####,###").append("   ");
                    builder.append(String.format("%12s", " "));
                }
            }
            builder.append(num_jugado.trim());
            numLinea += 1;

            if (numLinea < 5) {
                builder.append(", ");
            }
            if (numLinea == 5) {
                numLinea = 1;
                builder.append("\n");
            }
        }
        builder.append("\n");


        builder.append("==============================\n");
        builder.append("Total Tiquete: ").append(String.format("%10s", fMonto.format(mTotalTkt))).append("\n").append("\n");

        builder.append("PAGAMOS ").append(String.valueOf(mFacPremio)).append(" veces").append("\n").append("\n");


        builder.append(Variables.MSG_TKT).append("\n");
        if (reimprime) {
            builder.append("REIMPRESION").append("\n");
        }

        //builder.append("Emitido por: ").append(Variables.NOMBRE_USUARIO).append("\n\n");

        builder.append("Generado por LOTTOBANCACR").append("\n");

        return builder.toString();

    }


    private void compartirTkt() {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        //intent.setPackage("com.whatsapp");
        intent.putExtra(Intent.EXTRA_TEXT, tvDetalleTkt.getText());

        startActivity(Intent.createChooser(intent, "Seleccione"));


    }


    private void imprimeTktk() {

        String data = "";
        if (mNumDigitos == 2) {
            data = crearTktImpreso();

        } else {
            data = crearTktImpreso4();
        }


        SendDataString(data);

    }

    private void anulaTkt() {

        JSONObject jsonSend = new JSONObject();
        try {
            jsonSend.put("num_tkt", mNumeroTkt);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        progressBar.setVisibility(View.VISIBLE);
        ventaDAO.anulaTkts(this, jsonSend, new IResult() {
            @Override
            public void onResponse(JSONObject object) {
                progressBar.setVisibility(View.GONE);

                if (object.optJSONObject("resp") != null) {
                    if (object.optJSONObject("resp").optInt("estadoRes") == 0) {
                        Toast.makeText(ImprimeTkt.this, object.optJSONObject("resp").optString("msg"), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Toast.makeText(ImprimeTkt.this, object.optJSONObject("resp").optString("msg"), Toast.LENGTH_SHORT).show();
                    finish();
                }

            }

            @Override
            public void onError(VolleyError error) {
                progressBar.setVisibility(View.GONE);
            }
        });


    }


    private void SendDataString(String data) {

        if (mService.getState() != BluetoothService.STATE_CONNECTED) {

            conectaImpresora();
        }
        if (data.length() > 0) {
            mService.write(data.getBytes(StandardCharsets.UTF_8));
        }
    }


    @SuppressLint("MissingSuperCall")
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        //super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {// When the request to enable Bluetooth returns
            if (resultCode == AppCompatActivity.RESULT_OK) {
                // Bluetooth is now enabled, so set up a session
                KeyListenerInit();
            } else {
                // User did not enable Bluetooth or an error occured

                Toast.makeText(this, R.string.bt_not_enabled_leaving,
                        Toast.LENGTH_SHORT).show();
                //finish();
            }
        }
    }


    private String crearTktImpreso() {

        if (mTotalRev == 0) {

            Collections.sort(listaNumerosTkt, new Comparator<VentaTktVO>() {
                @Override
                public int compare(VentaTktVO o1, VentaTktVO o2) {
                    String a = fEntero.format(o1.getMonto()) + o1.getNumero();
                    String b = fEntero.format(o2.getMonto()) + o2.getNumero();
                    return a.compareTo(b);
                }
            });
        }

        String sNum, sMon;

        StringBuilder builder = new StringBuilder();
        builder.append(new char[]{27, '@'});

        //builder.append(new char[]{27, 't', 2}); // Set caractereres PC-850

        builder.append(new char[]{27, 'd', 1}); // Avance de 3 lineas
        builder.append(new char[]{27, '!', 24}); // doble altura

        builder.append(new char[]{27, 'a', 1}); // Alineacion centrada
        builder.append(Variables.TIT_TKT).append("\r\n");


        builder.append(new char[]{27, '!', 8}); // Negrita, 10cpp

        builder.append("Tiquete de compra").append("\r\n");
        builder.append("Numero Tiquete: ").append(fTkt.format(mNumeroTkt)).append("\r\n");
        if (reimprime) {
            builder.append("*** REIMPRESION ***").append("\r\n");
        }

        builder.append(new char[]{27, '!', 0}); // 10cpp
        builder.append(new char[]{27, 'a', 0}); // Alineacion izquierda

        builder.append("\r\n");
        builder.append("Fecha: " + mFechaTkt).append(String.format("%12s", mHoraTkt)).append("\r\n");
        builder.append(("Sorteo: ") + mNomSorteo).append("\r\n");

        String nomJugada = "Numero Exacto";
        if (mTipoJugada==1){
            nomJugada="Combinado/Desorden";
        }

        builder.append(("Tipo Jugada: ") + nomJugada).append("\r\n");

        if (mNomCliente.length() > 0) {
            builder.append("Ref: ").append(mNomCliente).append("\r\n");
        }

        if (mTotalRev == 0) {

            builder.append("\r\n");
            builder.append("Detalle de la compra\r\n");
            builder.append("==============================\r\n");

            int montoGrupo = 0;
            int numLinea = 1;

            for (int i = 0; i < listaNumerosTkt.size(); i++) {

                int mon_jugado = listaNumerosTkt.get(i).getMonto();
                String num_jugado = listaNumerosTkt.get(i).getNumero();


                if (mon_jugado != montoGrupo) {
                    if (montoGrupo > 0) {
                        builder.append("\r\n");
                    }
                    montoGrupo = mon_jugado;
                    numLinea = 1;
                    builder.append(String.format("%9s", fMonto.format(montoGrupo))).append(" x ");
                } else {
                    if (numLinea == 1) {
                        //builder.append("####,###").append("   ");
                        builder.append(String.format("%12s", " "));
                    }
                }
                builder.append(num_jugado.trim());
                //builder.append(", ");
                numLinea += 1;

                if (numLinea < 6) {
                    builder.append(", ");
                }
                if (numLinea == 6) {
                    numLinea = 1;
                    builder.append("\r\n");
                }
            }
            builder.append("\r\n");

        } else {

            builder.append("Detalle\r\n");

            builder.append(String.format("%4s", "Num"));
            builder.append(String.format("%11s", "  Apuesta"));
            builder.append(String.format("%11s", "Reventado")).append("\r\n");
            builder.append("==============================\r\n");

            for (int i = 0; i < listaNumerosTkt.size(); i++) {

                int mon_jugado = listaNumerosTkt.get(i).getMonto();
                int mon_jugado_rev = listaNumerosTkt.get(i).getMontoRev();
                String num_jugado = listaNumerosTkt.get(i).getNumero();

                builder.append(String.format("%4s", num_jugado.trim()));
                builder.append(String.format("%11s", fMonto.format(mon_jugado)));
                builder.append(String.format("%11s", fMonto.format(mon_jugado_rev))).append("\r\n");

            }
        }


        builder.append("==============================\r\n");
        builder.append("Total Tiquete: ").append(String.format("%10s", fMonto.format(mTotalTkt))).append("\r\n").append("\r\n");

        builder.append(new char[]{27, '!', 8}); // Negrita, 10cpp
        builder.append(new char[]{27, 'a', 1}); // Alineacion centrada
        builder.append("PAGAMOS ").append(String.valueOf(mFacPremio)).append(" veces").append("\r\n").append("\r\n");
        builder.append(new char[]{27, '!', 0}); // 10cpp
        builder.append(Variables.MSG_TKT).append("\r\n").append("\r\n");

        builder.append(new char[]{27, '!', 1}); // 12cpp
        //builder.append("Emitido por: ").append(Variables.NOMBRE_USUARIO).append("\r\n");
        builder.append("*** Generado por LOTTOBANCACR ***").append("\r\n");

        builder.append(new char[]{27, '!', 0}); // 10 CPP
        builder.append(new char[]{27, 'd', 5}); // Avance de lineas

        return builder.toString();
    }


    private String crearTktImpreso4() {


        Collections.sort(listaNumerosTkt, new Comparator<VentaTktVO>() {
            @Override
            public int compare(VentaTktVO o1, VentaTktVO o2) {
                String a = fEntero.format(o1.getMonto()) + o1.getNumero();
                String b = fEntero.format(o2.getMonto()) + o2.getNumero();
                return a.compareTo(b);
            }
        });

        String sNum, sMon;

        StringBuilder builder = new StringBuilder();
        builder.append(new char[]{27, '@'});

        //builder.append(new char[]{27, 't', 2}); // Set caractereres PC-850

        builder.append(new char[]{27, 'd', 1}); // Avance de 3 lineas
        builder.append(new char[]{27, '!', 24}); // doble altura

        builder.append(new char[]{27, 'a', 1}); // Alineacion centrada
        builder.append(Variables.TIT_TKT).append("\r\n");


        builder.append(new char[]{27, '!', 8}); // Negrita, 10cpp

        builder.append("Tiquete de compra").append("\r\n");
        builder.append("Numero Tiquete: ").append(fTkt.format(mNumeroTkt)).append("\r\n");
        if (reimprime) {
            builder.append("*** REIMPRESION ***").append("\r\n");
        }

        builder.append(new char[]{27, '!', 0}); // 10cpp
        builder.append(new char[]{27, 'a', 0}); // Alineacion izquierda

        builder.append("\r\n");
        builder.append("Fecha: " + mFechaTkt).append(String.format("%12s", mHoraTkt)).append("\r\n");
        builder.append(("Sorteo: ") + mNomSorteo).append("\r\n");

        String nomJugada = "Numero Exacto";
        if (mTipoJugada==1){
            nomJugada="Combinado/Desorden";
        }

        builder.append(("Tipo Jugada: ") + nomJugada).append("\r\n");

        if (mNomCliente.length() > 0) {
            builder.append("Ref: ").append(mNomCliente).append("\r\n");
        }


        builder.append("\r\n");
        builder.append("Detalle de la compra\r\n");
        builder.append("==============================\r\n");

        int montoGrupo = 0;
        int numLinea = 1;

        for (int i = 0; i < listaNumerosTkt.size(); i++) {

            int mon_jugado = listaNumerosTkt.get(i).getMonto();
            String num_jugado = listaNumerosTkt.get(i).getNumero();


            if (mon_jugado != montoGrupo) {
                if (montoGrupo > 0) {
                    builder.append("\r\n");
                }
                montoGrupo = mon_jugado;
                numLinea = 1;
                builder.append(String.format("%9s", fMonto.format(montoGrupo))).append(" x ");
            } else {
                if (numLinea == 1) {
                    //builder.append("####,###").append("   ");
                    builder.append(String.format("%12s", " "));
                }
            }
            builder.append(num_jugado.trim());
            //builder.append(", ");
            numLinea += 1;

            if (numLinea < 4) {
                builder.append(", ");
            }
            if (numLinea == 4) {
                numLinea = 1;
                builder.append("\r\n");
            }
        }
        builder.append("\r\n");


        builder.append("==============================\r\n");
        builder.append("Total Tiquete: ").append(String.format("%10s", fMonto.format(mTotalTkt))).append("\r\n").append("\r\n");

        builder.append(new char[]{27, '!', 8}); // Negrita, 10cpp
        builder.append(new char[]{27, 'a', 1}); // Alineacion centrada
        builder.append("PAGAMOS ").append(String.valueOf(mFacPremio)).append(" veces").append("\r\n").append("\r\n");
        builder.append(new char[]{27, '!', 0}); // 10cpp
        builder.append(Variables.MSG_TKT).append("\r\n").append("\r\n");

        builder.append(new char[]{27, '!', 1}); // 12cpp
        //builder.append("Emitido por: ").append(Variables.NOMBRE_USUARIO).append("\r\n");
        builder.append("*** Generado por LOTTOBANCACR ***").append("\r\n");

        builder.append(new char[]{27, '!', 0}); // 10 CPP
        builder.append(new char[]{27, 'd', 5}); // Avance de lineas

        return builder.toString();
    }


}
