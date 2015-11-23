package com.barantech.sayaword.web;

import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * Created by mary on 11/23/15.
 */
public class ErrorListener implements Response.ErrorListener {

    private ErrorHandler handler;
    public ErrorListener(ErrorHandler handler) {
        this.handler = handler;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        // TODO: 11/23/15 add error handling here
        if(handler != null)
            handler.onErrorResponse(error);
    }

    public interface ErrorHandler{
        public void onErrorResponse(VolleyError error);
    }

}
