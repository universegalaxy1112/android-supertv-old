package com.livetv.normal.utils.networking.services;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import java.io.UnsupportedEncodingException;

public class UTF8StringRequest extends StringRequest {
    public UTF8StringRequest(int type, String url, Listener<String> listener, ErrorListener errorListener) {
        super(type, url, listener, errorListener);
    }

    /* access modifiers changed from: protected */
    public Response<String> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        try {
            parsed = new String(response.data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }
        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
    }
}
