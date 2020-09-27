package com.example.project1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ThreeFragment extends Fragment {
    public static Context mContext;
    PersonAdapter adapter;
    ViewGroup rootView;
    Bundle bundle;
    ArrayList<Mail> arrayMail;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    String sendOrReceive;


    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.three_activity, container, false);
        mContext = container.getContext();
        bundle = getArguments();

        sendOrReceive = bundle.getString("or");
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        arrayMail = new ArrayList<Mail>();
        adapter = new PersonAdapter(arrayMail);

        layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        new BackgroundTask().execute();

        adapter.setOnItemClickListener(new PersonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, final int position) {
                final ReadingMail readM = new ReadingMail();
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean isSuccess = jsonResponse.getBoolean("success");
                            String receiveId, sendId, title, content;
                            String id = bundle.getString("id");
                            if (isSuccess) {
                                String sendTime = jsonResponse.getString("sendTime");
                                if(sendTime.equals(arrayMail.get(position).getTime())){
                                    receiveId = jsonResponse.getString("receiveID");
                                    sendId = jsonResponse.getString("sendID");
                                    title = jsonResponse.getString("title");
                                    content = jsonResponse.getString("content");

                                    bundle.putString("receiveId", receiveId);
                                    bundle.putString("sendId", sendId);
                                    bundle.putString("title", title);
                                    bundle.putString("time", sendTime);
                                    bundle.putString("content", content);
                                    bundle.putString("id",id);
                                    bundle.putString("or",sendOrReceive);

                                    readM.setArguments(bundle);
                                    ((ThreeActivity) getActivity()).replaceFragment(readM);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                ReadMailRequest readMailRequest = new ReadMailRequest(arrayMail.get(position).getTime(), responseListener);
                RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                requestQueue.add(readMailRequest);
            }
        });

        recyclerView.setAdapter(adapter);

        return rootView;
    }


    class BackgroundTask extends AsyncTask<Void, Void, String> {
        String target;

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                while ((temp = bufferedReader.readLine()) != null) {
                    stringBuilder.append(temp + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPreExecute() {
            target = "http://haeunhosting.dothome.co.kr/PersonManagement/MailSelect.php";
        }

        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                String noticeTitle, noticeSendId, noticeReceiveId, noticeSendTime, noticeContent;
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject row = jsonArray.getJSONObject(i);
                    noticeTitle = row.getString("title");
                    noticeSendId = row.getString("sendID");
                    noticeReceiveId = row.getString("receivedID");
                    noticeSendTime = row.getString("sendtime");
                    noticeContent = row.getString("content");
                    Mail mail = new Mail(noticeReceiveId, noticeSendId, noticeTitle, noticeContent, noticeSendTime);
                    if(sendOrReceive==null){
                        if(noticeReceiveId.equals(bundle.getString("id"))){
                            arrayMail.add(mail);
                        }
                    }else{
                        if(sendOrReceive.equals("receive")){
                            if(noticeReceiveId.equals(bundle.getString("id"))){
                                arrayMail.add(mail);
                            }
                        }else if(sendOrReceive.equals("send")) {
                            if (noticeSendId.equals(bundle.getString("id"))) {
                                arrayMail.add(mail);
                            }
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
