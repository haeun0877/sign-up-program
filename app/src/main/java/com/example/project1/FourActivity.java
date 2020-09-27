package com.example.project1;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FourActivity extends Fragment {
    Button send;
    EditText receiveId, text, title;
    Bundle bundle;
    Mail mail;
    ImageView selectReceiver;

    //메시지 전송 페이지
    public FourActivity(){
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState)  {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.four_activity, container, false);
        send = (Button) rootView.findViewById(R.id.send);
        receiveId = rootView.findViewById(R.id.receiveName);
        text = rootView.findViewById(R.id.contents);
        title = rootView.findViewById(R.id.title);
        bundle = getArguments();
        selectReceiver = rootView.findViewById(R.id.selectReceiver);

        if(bundle.getString("searchID")!=""){
            receiveId.setText(bundle.getString("searchID"));
        }

        send.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                String formatData = sdfNow.format(date);

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean isSuccess = jsonResponse.getBoolean("success");
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            if (isSuccess) {
                                builder.setMessage("전송되었습니다.").setNegativeButton("OK", null).create().show();
                            } else {
                                builder.setMessage("Register Failed.").setNegativeButton("Retry", null).create().show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };


                MailAddRequest mailRequest = new MailAddRequest(receiveId.getText().toString(), bundle.getString("id"), title.getText().toString(), text.getText().toString(), formatData,responseListener);
                    RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                    requestQueue.add(mailRequest);

                }


            });

        selectReceiver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectReceiver selectReceiver = new SelectReceiver();
                ((ThreeActivity)getActivity()).replaceFragment(selectReceiver);
                ((ThreeActivity)getActivity()).toolbar.setTitle("수신자 선택");
            }
        });

        return rootView;
    }
}
