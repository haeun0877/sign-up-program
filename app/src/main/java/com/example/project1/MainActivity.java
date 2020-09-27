package com.example.project1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    EditText idEdit;
    EditText passEdit;
    CheckBox loginCheck;
    final static int REQCODE_ACTEDIT = 1001;
    Person person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        idEdit = (EditText) findViewById(R.id.idedit);
        passEdit = (EditText) findViewById(R.id.passedit);

        SharedPreferences pref = getSharedPreferences("Pref", MODE_PRIVATE);
        String id = pref.getString("Id", "");
        if (!id.equals(""))
            idEdit.setText(id);
        String pass = pref.getString("Pass", "");
        if (pass.equals("") == false)
            passEdit.setText(pass);

        loginCheck = (CheckBox) findViewById(R.id.check);
        loginCheck.setOnClickListener(new CheckBox.OnClickListener() {
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    SharedPreferences pref = getSharedPreferences("Pref", MODE_PRIVATE);
                    SharedPreferences.Editor edit = pref.edit();
                    String id = idEdit.getText().toString();
                    String password = passEdit.getText().toString();
                    edit.putString("Id", id);
                    edit.putString("Pass", password);
                    edit.commit();
                } else {
                    SharedPreferences pref = getSharedPreferences("Pref", MODE_PRIVATE);
                    SharedPreferences.Editor edit = pref.edit();
                    edit.putString("Id", null);
                    edit.putString("Pass", null);
                    edit.commit();
                }

            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQCODE_ACTEDIT:
                if (resultCode == RESULT_OK) {
                    person = (Person) data.getSerializableExtra("PersonIn");
                }
                break;
        }
    }


    public void mOnClick(View v) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean isSuccess = jsonResponse.getBoolean("success");
                    if (isSuccess) {
                        String userID = jsonResponse.getString("userID");
                        String userPassword = jsonResponse.getString("userPassword");
                        Intent intent = new Intent(MainActivity.this, ThreeActivity.class);
                        intent.putExtra("PersonIn", userID);
                        intent.putExtra("userPassword", userPassword);
                        startActivity(intent);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("Login Failed.").setNegativeButton("Retry", null).create().show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        LoginRequest loginRequest = new LoginRequest(idEdit.getText().toString(), passEdit.getText().toString(), responseListener);
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(loginRequest);
    }


    public void lOnClick(View v) {
        Intent intent2 = new Intent(this, TwoActivity.class);
        startActivityForResult(intent2, REQCODE_ACTEDIT);
    }
}
