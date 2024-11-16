package com.betscr.modulovendedor.actividades;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.betscr.modulovendedor.R;
import com.betscr.modulovendedor.interfaces.IResult;
import com.betscr.modulovendedor.metodos.Internet;
import com.betscr.modulovendedor.modelo.DAO.UsuarioDAO;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginUsuario extends AppCompatActivity {

    EditText txtCodUsuario, txtPassUsuario;
    Button btnAceptar;
    ProgressBar progressBar;
    UsuarioDAO usuarioDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_usuario);

        usuarioDAO = new UsuarioDAO();
        progressBar = findViewById(R.id.login_progressBar);
        txtCodUsuario = findViewById(R.id.login_edUsuario);
        txtPassUsuario = findViewById(R.id.login_edPass);
        btnAceptar = findViewById(R.id.login_btnAceptar);


    }

    public void btnAceptar(View view) {

        if (!Internet.isOnLine(this)) {
            Toast.makeText(this, "Compruebe la conección a internet e intente de nuevo", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        JSONObject jsonSend = new JSONObject();
        try {
            jsonSend.put("id_usuario", txtCodUsuario.getText().toString().trim());
            jsonSend.put("pin_pass", txtPassUsuario.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        usuarioDAO.login(this, jsonSend, new IResult() {
            @Override
            public void onResponse(JSONObject object) {
                progressBar.setVisibility(View.GONE);

                JSONObject jsonUsuario = object.optJSONObject("resp");

                if (jsonUsuario == null) {
                    Toast.makeText(LoginUsuario.this, "Usuario NO existe", Toast.LENGTH_SHORT).show();
                    txtCodUsuario.requestFocus();
                    return;
                }

                if (jsonUsuario.optInt("estadoRes") == 0) {
                    Toast.makeText(LoginUsuario.this, jsonUsuario.optString("msg"), Toast.LENGTH_SHORT).show();
                    txtPassUsuario.requestFocus();
                    return;
                }

                if (jsonUsuario.optInt("est_usuario") == 0) {
                    Toast.makeText(LoginUsuario.this, "Usuario Inactivo", Toast.LENGTH_SHORT).show();
                    txtCodUsuario.requestFocus();
                    return;
                }

                SharedPreferences sharedPreferences = getSharedPreferences("userpref", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("user", txtCodUsuario.getText().toString());
                editor.putInt("coduser", jsonUsuario.optInt("cod_usuario"));
                editor.putString("nomuser", jsonUsuario.optString("nom_usuario"));
                editor.putInt("codagencia", jsonUsuario.optInt("cod_suc"));
                editor.putString("nomagencia", jsonUsuario.optString("nom_suc"));
                editor.putInt("tipousu", jsonUsuario.optInt("tipo_usuario"));
                //editor.putString("tit_tkt", jsonUsuario.optString("tit_tkt"));
                //editor.putString("msg_tkt", jsonUsuario.optString("msg_tkt"));
                editor.apply();

                Toast.makeText(getApplicationContext(), "Acceso al sistema validado", Toast.LENGTH_SHORT).show();
                finish();


            }

            @Override
            public void onError(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Error de conexion con el servidor" + error.toString(), Toast.LENGTH_LONG).show();
            }
        });

    }


}
