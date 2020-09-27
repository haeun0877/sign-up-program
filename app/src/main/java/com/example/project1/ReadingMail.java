package com.example.project1;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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


public class ReadingMail extends Fragment {
    TextView receiveId, sendId, time, title, content;
    Bundle bundle;
    Button delete, back, reply;
    String stringTime, id, sendOrReceive;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.readingmail_activity, container, false);
        bundle = getArguments();
        receiveId = (TextView)rootView.findViewById(R.id.receiveId);
        sendId = (TextView)rootView.findViewById(R.id.sendId);
        time = (TextView)rootView.findViewById(R.id.time);
        title = (TextView)rootView.findViewById(R.id.title);
        content = (TextView)rootView.findViewById(R.id.content);
        delete = (Button)rootView.findViewById(R.id.delete);
        back = (Button)rootView.findViewById(R.id.back);
        reply = (Button)rootView.findViewById(R.id.reply);

        receiveId.setText(bundle.getString("receiveId"));
        sendId.setText(bundle.getString("sendId"));
        time.setText(bundle.getString("time"));
        title.setText(bundle.getString("title"));
        content.setText(bundle.getString("content"));

        id = bundle.getString("id");
        stringTime = bundle.getString("time");
        sendOrReceive = bundle.getString("or");


        if(sendOrReceive==null){
            reply.setVisibility(View.VISIBLE);
        }else{
            if(sendOrReceive.equals("send")){
                reply.setVisibility(View.INVISIBLE);
            }else{
                reply.setVisibility(View.VISIBLE);
            }
        }


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThreeFragment three = new ThreeFragment();
                bundle.putString("or", sendOrReceive);
                three.setArguments(bundle);
                ((ThreeActivity)getActivity()).replaceFragment(three);
                if(sendOrReceive==null){
                    ((ThreeActivity)getActivity()).toolbar.setTitle("받은 메시지 함");
                }else{
                    if(sendOrReceive.equals("send")){
                        ((ThreeActivity)getActivity()).toolbar.setTitle("보낸 메시지 함");
                    }else{
                        ((ThreeActivity)getActivity()).toolbar.setTitle("받은 메시지 함");
                    }
                }
            }
        });


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean isSuccess = jsonResponse.getBoolean("success");
                            if (isSuccess) {
                                String sendTime = jsonResponse.getString("sendTime");
                                if(sendTime.equals(stringTime)){
                                    bundle.putString("id", id);
                                    bundle.putString("or",sendOrReceive);

                                    if(sendOrReceive==null){
                                        ((ThreeActivity)getActivity()).toolbar.setTitle("받은 메시지 함");
                                    }else{
                                        if(sendOrReceive.equals("send")){
                                            ((ThreeActivity)getActivity()).toolbar.setTitle("보낸 메시지 함");
                                        }else{
                                            ((ThreeActivity)getActivity()).toolbar.setTitle("받은 메시지 함");
                                        }
                                    }
                                    ThreeFragment three = new ThreeFragment();
                                    three.setArguments(bundle);
                                    ((ThreeActivity)getActivity()).replaceFragment(three);
                                    Toast.makeText(getActivity(), "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };
                DeleteMailRequest deleteMailRequest = new DeleteMailRequest(stringTime, responseListener);
                RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                requestQueue.add(deleteMailRequest);

            }
        });

        reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReplyMail replyMail = new ReplyMail();
                replyMail.setArguments(bundle);
                ((ThreeActivity)getActivity()).replaceFragment(replyMail);
                ((ThreeActivity)getActivity()).toolbar.setTitle("메시지 작성");
            }
        });

        return rootView;
    }



}
