package com.betscr.modulovendedor.actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.betscr.modulovendedor.R;
import com.betscr.modulovendedor.metodos.DeviceListActivity;
import com.betscr.modulovendedor.modelo.Variables;

public class Configuracion extends AppCompatActivity implements View.OnClickListener {

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;


    EditText txtNombreDB, txtTituloTkt, txtMsgTkt;
    TextView txtDireccionMAC;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        txtNombreDB = findViewById(R.id.conf_nombreDB);
        txtDireccionMAC = findViewById(R.id.conf_direccion_MAC);
        txtTituloTkt = findViewById(R.id.conf_tituloTkt);
        txtMsgTkt = findViewById(R.id.conf_msgTkt);
        txtDireccionMAC.setOnClickListener(this);


        cargaPreferencias();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.configuracion, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.conf_guardar) {

            guardarConfiguracion();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void cargaPreferencias() {
        SharedPreferences sharedPreferences = getSharedPreferences("userpref", Context.MODE_PRIVATE);
        txtNombreDB.setText(sharedPreferences.getString("dbuser", ""));
        String direccionMAC = sharedPreferences.getString("direccionMAC", "");
        txtDireccionMAC.setText(!direccionMAC.equals("") ? direccionMAC : "02:00:00:00:00:00");
        txtTituloTkt.setText(sharedPreferences.getString("tit_tkt", ""));
        txtMsgTkt.setText(sharedPreferences.getString("msg_tkt", ""));

    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {

       super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CONNECT_DEVICE) {// When DeviceListActivity returns with a device to connect
            if (resultCode == AppCompatActivity.RESULT_OK) {
                // Get the device MAC address
                String direccionMAC = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                txtDireccionMAC.setText(direccionMAC);
            }
        }
    }


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.conf_direccion_MAC) {
            Intent serverIntent = new Intent(Configuracion.this, DeviceListActivity.class);
            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
        }
    }

    private void guardarConfiguracion() {

        SharedPreferences sharedPreferences = getSharedPreferences("userpref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("dbuser", txtNombreDB.getText().toString().trim());
        editor.putString("direccionMAC", txtDireccionMAC.getText().toString().trim());
        editor.putString("tit_tkt", txtTituloTkt.getText().toString());
        editor.putString("msg_tkt", txtMsgTkt.getText().toString());
        if (!Variables.DB_USUARIO.equals(txtNombreDB.getText().toString())){
            editor.putString("user", "");
            editor.putInt("coduser", 0);
            editor.putString("nomuser", "Invalido");
            editor.putInt("codagencia", 0);
            editor.putString("nomagencia", "Invalida");
            editor.putInt("tipousu", 1);
        }

        editor.apply();
        finish();
    }

}