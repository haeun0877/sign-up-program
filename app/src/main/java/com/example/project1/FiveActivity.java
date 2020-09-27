package com.example.project1;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;

//개인정보 수정페이지
public class FiveActivity extends Fragment {
    private static final int RESULT_OK = 1000;
    EditText pass, name;
    TextView id;
    Bundle bundle;
    Button setting;
    String findId;
    ImageView picture;
    Uri selectImageURI;
    Spinner spinner;
    String major;
    private static int PICK_IMAGE_REQUEST=1;
    public FiveActivity(){

    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.five_activity, container, false);
        bundle = getArguments();
        pass = (EditText) rootView.findViewById(R.id.passedit);
        id = (TextView) rootView.findViewById(R.id.settingId);
        name = (EditText) rootView.findViewById(R.id.settingName);
        setting = (Button) rootView.findViewById(R.id.setting);
        picture = rootView.findViewById(R.id.picture);
        selectImageURI = Uri.EMPTY;
        spinner = rootView.findViewById(R.id.spinner);

        findId = bundle.getString("id");
        id.setText(findId);

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean isSuccess = jsonResponse.getBoolean("success");
                    if (isSuccess) {
                        String userID = jsonResponse.getString("userID");
                        String picture = jsonResponse.getString("userPicture");
                        if(userID.equals(findId)){
                            selectImageURI= Uri.parse(picture);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        SettingLoginRequest settingLoginRequest = new SettingLoginRequest(findId, responseListener);
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(settingLoginRequest);

        picture.setImageURI(selectImageURI);

        setting.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean isSuccess = jsonResponse.getBoolean("success");
                            if (isSuccess) {
                                Toast.makeText(getActivity(), "수정되었습니다.", Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                ModifyPersonRequest modifyPersonRequest = new ModifyPersonRequest(pass.getText().toString(), name.getText().toString(), major, findId ,responseListener);
                RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                requestQueue.add(modifyPersonRequest);
            }
        });

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                major = (String) spinner.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });

        return rootView;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && null != data) {
                selectImageURI = data.getData();

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectImageURI);
                int nh = (int) (bitmap.getHeight()*(1024.0 / bitmap.getWidth()));
                Bitmap scaled =Bitmap.createScaledBitmap(bitmap, 1024, nh, true);

                picture.setImageBitmap(scaled);

            } else{
                Toast.makeText(getActivity(), "취소 되었습니다.", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(getActivity(), "사용할 수 있습니다.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


}

