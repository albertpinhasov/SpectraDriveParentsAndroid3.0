package com.spectraparent.WebAPI;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.spectraparent.Activities.LoginActivity;
import com.spectraparent.Helpers.LocalStorage;
import com.spectraparent.Models.UserModel;
import com.spectraparent.SpectraDrive;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by VikasNokhwal on 03-10-2017.
 */

public class ApiRequest extends JsonRequest {

    private final Map<String, String> headers;

    public ApiRequest(int method, String url, Object requestBody,
                      Response.Listener<String> listener, Response.ErrorListener errorListener, Map<String, String> headers) {
        super(method, url, new Gson().toJson(requestBody), listener,
                errorListener);
        this.headers = headers;
        System.out.println("Request "+url+"======>" + new Gson().toJson(requestBody));
        UserModel student = LocalStorage.getStudent();
        if (student != null && student.getToken() != null) {
            headers.put("Authorization", "Bearer " + student.getToken());
        }
        headers.put("Content-Type", "application/json; charset=utf-8");
        configureRequest();
        setRetryPolicy(new DefaultRetryPolicy(10000, 2, 1));
    }

    private void configureRequest() {

    }

    @Override
    public Map<String, String> getHeaders() {
        return headers;
    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        try {
            // If it's not muted; we just need to create our POJO from the returned JSON and handle correctly the errors
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            // T parsedObject = gson.fromJson(json, clazz);
            return Response.success(json, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {

        if (volleyError != null && volleyError.networkResponse != null && volleyError.networkResponse.statusCode == 401) {
            if (LocalStorage.getStudent().getToken() != null) {
                LocalStorage.storeStudent(null);
                Intent i = new Intent(SpectraDrive.AppContext, LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                SpectraDrive.AppContext.startActivity(i);
            }
        }
        return super.parseNetworkError(volleyError);
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return 0;
    }
}
