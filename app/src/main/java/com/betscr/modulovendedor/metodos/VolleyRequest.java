package com.betscr.modulovendedor.metodos;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.betscr.modulovendedor.interfaces.IResult;
import com.betscr.modulovendedor.modelo.Variables;
import org.json.JSONObject;

public class VolleyRequest implements Response.Listener, Response.ErrorListener {
    private IResult iResult;
    private String url= Variables.URL_API;

    public void sendRequest(JSONObject jsonSend, Context context, IResult iResult) {
        this.iResult = iResult;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonSend, this, this);
        //jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getIntanciaVolley(context.getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        iResult.onError(error);
    }

    @Override
    public void onResponse(Object response) {
        iResult.onResponse((JSONObject) response);
    }
}
