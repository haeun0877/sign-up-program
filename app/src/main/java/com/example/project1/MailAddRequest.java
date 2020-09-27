package com.example.project1;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class MailAddRequest extends StringRequest {
    final static private String URL = "http://haeunhosting.dothome.co.kr/PersonManagement/MailAdd.php";
    private Map<String, String> parameters;
    public MailAddRequest(String receiveID, String sendID, String title, String content, String time, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("RegisterRequest", error.getMessage());
            }
        });
        parameters = new HashMap<>();
        parameters.put("receiveID", receiveID);
        parameters.put("sendID", sendID);
        parameters.put("title", title);
        parameters.put("content", content);
        parameters.put("time", time);
    }
    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}