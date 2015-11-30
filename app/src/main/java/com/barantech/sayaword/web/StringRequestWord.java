package com.barantech.sayaword.web;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.Map;

/**
 * Created by mary on 11/30/15.
 */
public class StringRequestWord extends StringRequest {
    private final Map<String, String> param;
    public StringRequestWord(int method, String url, Map<String, String> param,
                             Response.Listener<String> listener,
                             Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        this.param = param;
    }

    @Override
    public Map<String, String> getParams() throws AuthFailureError {
        return param;
    }
}
