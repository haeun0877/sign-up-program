package com.example.project1;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


public class TwoActivity extends AppCompatActivity {
    EditText idedit;
    EditText passedit;
    EditText nameedit;
    Person person;
    ImageView picture;
    Uri selectImageURI;
    Spinner spinner;
    String major;
    AlertDialog dialog;
    boolean validate = false;
    private static int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.two_activity);

        idedit = findViewById(R.id.idedit);
        passedit = findViewById(R.id.passedit);
        nameedit = findViewById(R.id.nameedit);
        picture = findViewById(R.id.picture);
        selectImageURI = Uri.EMPTY;
        spinner = (Spinner) findViewById(R.id.spinner);

        person = (Person) getIntent().getSerializableExtra("PersonIn");

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                major = (String) spinner.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && null != data) {
                selectImageURI = data.getData();

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectImageURI);
                int nh = (int) (bitmap.getHeight() * (1024.0 / bitmap.getWidth()));
                Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 1024, nh, true);

                picture.setImageBitmap(scaled);

            } else {
                Toast.makeText(TwoActivity.this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void mOnClick(View v) {
        if (idedit.getText().toString().equals("")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(TwoActivity.this);
            dialog = builder.setMessage("ID is empty.").setNegativeButton("Retry", null).create();
            dialog.show();
            return;
        }
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean isSuccess = jsonResponse.getBoolean("success");
                    if (isSuccess) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(TwoActivity.this);
                        dialog = builder.setMessage("Good ID").setPositiveButton("OK", null).create();
                        dialog.show();
                        idedit.setEnabled(false);
                        validate = true;
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(TwoActivity.this);
                        dialog = builder.setMessage("ID is already used").setNegativeButton("Retry", null).create();
                        dialog.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        ValidateRequest validateRequest = new ValidateRequest(idedit.getText().toString(), responseListener);
        RequestQueue requestQueue = Volley.newRequestQueue(TwoActivity.this);
        requestQueue.add(validateRequest);

    }

    public void goOnClick(View v) {
        if ((idedit.getText().toString().equals("") == true) || (passedit.getText().toString().equals("") == true) || (nameedit.getText().toString().equals("") == true)) {
        } else {
            if(validate) {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean isSuccess = jsonResponse.getBoolean("success");
                            if (isSuccess) {
                                Intent intent = new Intent(TwoActivity.this, MainActivity.class);
                                setResult(RESULT_OK, intent);
                                finish();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(TwoActivity.this);
                                builder.setMessage("Register Failed.").setNegativeButton("Retry", null).create().show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                RegisterRequest registerRequest = new RegisterRequest(idedit.getText().toString(), passedit.getText().toString(), nameedit.getText().toString(), selectImageURI.toString(), major, responseListener);
                RequestQueue requestQueue = Volley.newRequestQueue(TwoActivity.this);
                requestQueue.add(registerRequest);
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(TwoActivity.this);
                builder.setMessage("ID CHECK PLEASE").setNegativeButton("OK", null).create().show();
            }
        }
    }
}

