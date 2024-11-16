package com.betscr.modulovendedor.interfaces;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface IResult {

    void onResponse(JSONObject object);

    void onError(VolleyError error);

}
