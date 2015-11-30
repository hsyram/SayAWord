package com.barantech.sayaword.web;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.barantech.sayaword.util.Log;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;

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
        if (error != null) {
            if (error.networkResponse != null) {
                // TODO: 11/30/15 other errors handling
                NetworkResponse response = error.networkResponse;
                if (response.statusCode == 400) {
                    try {
                        String json = new String(
                                response.data,
                                HttpHeaderParser.parseCharset(response.headers));
                        ErrorModel errorModel = new Gson().fromJson(json, ErrorModel.class);
                        if(handler != null)
                            handler.onErrorResponse(errorModel.errors.userMessage);
                    } catch (UnsupportedEncodingException | JsonSyntaxException e) {
                        Log.e(e.toString());
                    }
                }
            }
            Log.i("onErrorResponse:"+error.toString());
        }
    }

    public interface ErrorHandler {
        void onErrorResponse(String errorMessage);
    }

    private class ErrorModel {
        public ErrorMsg errors;

        private class ErrorMsg {
            public String userMessage;
            public String internalMessage;
            public int code;
            public String moreInfo;
        }
    }

}
