package com.betscr.modulovendedor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.betscr.modulovendedor.actividades.RecargasRetiros;
import com.google.android.material.navigation.NavigationView;
import com.betscr.modulovendedor.actividades.CambiaPin;
import com.betscr.modulovendedor.actividades.Configuracion;
import com.betscr.modulovendedor.actividades.ConsultaEstadoCuenta;
import com.betscr.modulovendedor.actividades.ConsultaLiqDia2;
import com.betscr.modulovendedor.actividades.ConsultaLiqDia3;
import com.betscr.modulovendedor.actividades.ConsultaLiquidacionDia;
import com.betscr.modulovendedor.actividades.ConsultaPremios;
import com.betscr.modulovendedor.actividades.LoginUsuario;
import com.betscr.modulovendedor.actividades.ConsultaVentaTkts;
import com.betscr.modulovendedor.actividades.VentaTkt;
import com.betscr.modulovendedor.interfaces.IResult;
import com.betscr.modulovendedor.modelo.DAO.UsuarioDAO;
import com.betscr.modulovendedor.modelo.Variables;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TextView tvNomAgencia, tvNomUsuario, tvSaldoUsu;

    private UsuarioDAO usuarioDAO;

    private int mSaldoUsu = 0;

    DecimalFormatSymbols simboloDec = new DecimalFormatSymbols(Locale.ENGLISH);
    DecimalFormat fDecimal = new DecimalFormat("###,###,###", simboloDec);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        usuarioDAO = new UsuarioDAO();

        //tvNomAgencia = findViewById(R.id.main_nomagencia);
        tvNomUsuario = findViewById(R.id.main_nomusuario);
        //tvSaldoUsu = findViewById(R.id.main_saldoUsu);

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarPreferencias();
    }


    @Override
    public void onBackPressed() {

    }

    private void cargarPreferencias() {
        // Busca los parametros del usuario almacenados en el movil
        SharedPreferences sharedPreferences = getSharedPreferences("userpref", Context.MODE_PRIVATE);
        Variables.ID_USUARIO = sharedPreferences.getString("user", "");
        Variables.COD_USUARIO = sharedPreferences.getInt("coduser", 0);
        Variables.NOMBRE_USUARIO = sharedPreferences.getString("nomuser", "Invalido");
        Variables.DB_USUARIO = sharedPreferences.getString("dbuser", "");
        Variables.COD_AGENCIA = sharedPreferences.getInt("codagencia", 0);
        Variables.NOMBRE_AGENCIA = sharedPreferences.getString("nomagencia", "Invalida");
        Variables.TIPO_USU = sharedPreferences.getInt("tipousu", 1);
        Variables.TIT_TKT = sharedPreferences.getString("tit_tkt", "");
        Variables.MSG_TKT = sharedPreferences.getString("msg_tkt", "");
        Variables.DIRECCION_MAC = sharedPreferences.getString("direccionMAC", "");
        //Variables.URL_API = getString(R.string.ip) + "apiRest/" + Variables.DB_USUARIO.trim() + "/api.php";
        Variables.URL_API = getString(R.string.ip) +"lotto" +Variables.DB_USUARIO.trim() + "/api.php";
        tvNomUsuario.setText("Usuario: " + Variables.NOMBRE_USUARIO);
        //tvNomAgencia.setText("Sucursal: " + Variables.NOMBRE_AGENCIA);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.main_settings) {
            Intent intent = new Intent(MainActivity.this, Configuracion.class);
            startActivity(intent);
            return true;

        } else if (id == R.id.main_login) {
            if (Variables.DB_USUARIO.equals("")) {
                Toast.makeText(MainActivity.this, "URL no configurada", Toast.LENGTH_SHORT).show();
                return false;
            }
            Intent intent = new Intent(MainActivity.this, LoginUsuario.class);
            startActivity(intent);
            return true;

        } else if (id == R.id.main_cambia_pin) {
            if (Variables.ID_USUARIO.equals("")) {
                Toast.makeText(MainActivity.this, "NO existe un usuario seleccionado", Toast.LENGTH_SHORT).show();
                return false;
            }

            Intent intent = new Intent(MainActivity.this, CambiaPin.class);
            startActivity(intent);
            return true;

        } else if (id == R.id.main_salir) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem itemNav) {

        if (Variables.ID_USUARIO.equals("")) {
            Toast.makeText(this, "Debe ingresar un Usuario", Toast.LENGTH_LONG).show();
            return false;
        }


        Intent intent = null;

        int id = itemNav.getItemId();

        if (id == R.id.nav_ventaTKT) {
            intent = new Intent(MainActivity.this, VentaTkt.class);

        } else if (id == R.id.nav_recargas) {
            intent = new Intent(MainActivity.this, RecargasRetiros.class);

        }  else if (id == R.id.nav_repVentaDia) {
            intent = new Intent(MainActivity.this, ConsultaVentaTkts.class);

        } else if (id == R.id.nav_repListas) {
            intent = new Intent(MainActivity.this, ConsultaLiquidacionDia.class);

        } else if (id == R.id.nav_repRes_sorteos) {
            intent = new Intent(MainActivity.this, ConsultaLiqDia2.class);

        } else if (id == R.id.nav_repRes_periodo) {
            intent = new Intent(MainActivity.this, ConsultaLiqDia3.class);

        } else if (id == R.id.nav_repPremios) {
            intent = new Intent(MainActivity.this, ConsultaPremios.class);

        } else if (id == R.id.nav_repEstadoCuenta) {
            intent = new Intent(MainActivity.this, ConsultaEstadoCuenta.class);
        }


        try {
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Proceso NO disponible", Toast.LENGTH_SHORT).show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void consultaSaldoUsuario() {

        Log.i("Main", Variables.URL_API);

        JSONObject jsonSend = new JSONObject();
        try {
            jsonSend.put("cod_usuario", Variables.COD_USUARIO);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //progressBar.setVisibility(View.VISIBLE);

        usuarioDAO.consultaSaldoUsuario(this, jsonSend, new IResult() {
            @Override
            public void onResponse(JSONObject object) {
                //progressBar.setVisibility(View.GONE);

                Log.i("Main", object.toString());

                mSaldoUsu = 0;

                if (object.optJSONObject("resp") != null) {

                    mSaldoUsu = object.optJSONObject("resp").optInt("saldoUsu");

                }

                //tvSaldoUsu.setText(fDecimal.format(mSaldoUsu)),

            }

            @Override
            public void onError(VolleyError error) {
                //progressBar.setVisibility(View.GONE);

                Log.e("Main", error.toString());

            }
        });


    }

}


