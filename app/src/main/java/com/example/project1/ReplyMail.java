package com.example.project1;

import android.app.AlertDialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ReplyMail extends Fragment {
    EditText receiveId, replyTitle, content;
    Bundle bundle;
    Mail mail;
    Button send, cancel;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.replymail_activity, container, false);
        receiveId = (EditText) rootView.findViewById(R.id.receiveId);
        replyTitle = (EditText) rootView.findViewById(R.id.replyTitle);
        content = (EditText) rootView.findViewById(R.id.content);
        bundle = getArguments();
        send = (Button) rootView.findViewById(R.id.send);
        cancel = (Button) rootView.findViewById(R.id.cancel);

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

                MailAddRequest mailRequest = new MailAddRequest(receiveId.getText().toString(), bundle.getString("id"), replyTitle.getText().toString(), content.getText().toString(), formatData,responseListener);
                RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                requestQueue.add(mailRequest);

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReadingMail readM = new ReadingMail();
                readM.setArguments(bundle);
                ((ThreeActivity)getActivity()).replaceFragment(readM);
                ((ThreeActivity)getActivity()).toolbar.setTitle("메시지 읽기");
            }
        });

        return rootView;
    }
}
