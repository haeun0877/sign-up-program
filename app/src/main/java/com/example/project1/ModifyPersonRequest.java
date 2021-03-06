package com.example.project1;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ModifyPersonRequest extends StringRequest {
    final static private String URL = "http://haeunhosting.dothome.co.kr/PersonManagement/ModifyPerson.php";
    private Map<String, String> parameters;
    public ModifyPersonRequest(String userPassword, String userName, String userMajor, String userID, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("RegisterRequest", error.getMessage());
            }
        });
        parameters = new HashMap<>();
        parameters.put("userPassword", userPassword);
        parameters.put("userName", userName);
        parameters.put("userMajor", userMajor);
        parameters.put("userID", userID);
    }
    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}