package com.betscr.modulovendedor.actividades;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.betscr.modulovendedor.R;
import com.betscr.modulovendedor.interfaces.IResult;
import com.betscr.modulovendedor.modelo.DAO.UsuarioDAO;
import com.betscr.modulovendedor.modelo.Variables;

import org.json.JSONException;
import org.json.JSONObject;


public class CambiaPin extends AppCompatActivity implements View.OnClickListener {

    Button btnCambiaPin;
    EditText edPinActual, edPinNuevo, edPinNuevo2;
    TextView tvNomUsuario;
    ProgressBar progressBar;
    UsuarioDAO usuarioDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambia_pin);

        usuarioDAO = new UsuarioDAO();

        progressBar = findViewById(R.id.cp_progressBar);
        btnCambiaPin = findViewById(R.id.cp_btnCambiar);
        edPinActual = findViewById(R.id.cp_PinActual);
        edPinNuevo = findViewById(R.id.cp_PinNuevo);
        edPinNuevo2 = findViewById(R.id.cp_PinNuevo2);
        tvNomUsuario = findViewById(R.id.cp_nomUsuario);
        tvNomUsuario.setText(Variables.NOMBRE_USUARIO);

        btnCambiaPin.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        String pinNuevo = edPinNuevo.getText().toString();
        String pinNuevo2 = edPinNuevo2.getText().toString();

        if (!pinNuevo.equals(pinNuevo2)) {
            Toast.makeText(this, "Los campos del nuevo PIN no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        JSONObject jsonSend = new JSONObject();
        try {
            jsonSend.put("id_usuario", Variables.ID_USUARIO);
            jsonSend.put("pin_pass", edPinActual.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        usuarioDAO.login(this, jsonSend, new IResult() {
            @Override
            public void onResponse(JSONObject object) {
                progressBar.setVisibility(View.GONE);

                JSONObject jsonUsuario = object.optJSONObject("resp");

                if (jsonUsuario.optInt("estadoRes") == 0) {
                    Toast.makeText(CambiaPin.this, jsonUsuario.optString("msg"), Toast.LENGTH_SHORT).show();
                    edPinActual.requestFocus();
                    return;
                }

                /*if (!edPinActual.getText().toString().trim().equals(jsonUsuario.optString("pin_pass").trim())) {
                    Toast.makeText(getApplicationContext(), "Password Actual Incorrecto", Toast.LENGTH_SHORT).show();
                    edPinActual.requestFocus();
                    return;
                }*/

                cambiarpin();
                edPinActual.setText("");
                edPinNuevo.setText("");
                edPinNuevo2.setText("");
                edPinActual.requestFocus();

            }

            @Override
            public void onError(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(com.betscr.modulovendedor.actividades.CambiaPin.this, "Error al consultar Usuario", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void cambiarpin() {
        progressBar.setVisibility(View.VISIBLE);
        String nuevoPin = edPinNuevo.getText().toString().trim();

        usuarioDAO.cambiaPin(Variables.ID_USUARIO, nuevoPin, getApplicationContext(), new IResult() {
            @Override
            public void onResponse(JSONObject object) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(com.betscr.modulovendedor.actividades.CambiaPin.this, object.optString("resp"), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Error al cambiar el PIN", Toast.LENGTH_SHORT).show();
                //Log.d("Error: ", error.toString());

            }
        });

    }
}