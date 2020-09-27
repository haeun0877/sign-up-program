package com.example.project1;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

public class SelectReceiver extends Fragment {
    Button cancel, search;
    Bundle bundle;
    Spinner spinner;
    String major, searchID;
    RecyclerView recyclerView;
    SearchPersonAdapter adapter;
    ArrayList<Person> arrayPerson;
    LinearLayoutManager layoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.selectreceiver, container, false);

        cancel = rootView.findViewById(R.id.cancel);
        bundle = new Bundle();
        spinner = rootView.findViewById(R.id.spinner);
        search =rootView.findViewById(R.id.search);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        arrayPerson = new ArrayList<Person>();
        adapter = new SearchPersonAdapter(arrayPerson);

        layoutManager = new LinearLayoutManager(container.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FourActivity writeMessage = new FourActivity();
                writeMessage.setArguments(bundle);
                ((ThreeActivity)getActivity()).replaceFragment(writeMessage);
                ((ThreeActivity)getActivity()).toolbar.setTitle("메시지 작성");
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


        adapter.setOnItemClickListener(new SearchPersonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                searchID=arrayPerson.get(position).getId();
                FourActivity writeMessage = new FourActivity();
                bundle.putString("searchID", searchID);
                writeMessage.setArguments(bundle);
                ((ThreeActivity)getActivity()).replaceFragment(writeMessage);
                ((ThreeActivity)getActivity()).toolbar.setTitle("메시지 작성");
            }
        });


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrayPerson.clear();
                new BackgroundTask().execute();

            }
        });

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
            target = "http://haeunhosting.dothome.co.kr/PersonManagement/SelectMajorPerson.php";
        }

        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                String userID,userPassword, userName, userPicture, userMajor;
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject row = jsonArray.getJSONObject(i);
                    userID = row.getString("userID");
                    userPassword = row.getString("userPassword");
                    userName = row.getString("userName");
                    userPicture = row.getString("userPicture");
                    userMajor = row.getString("userMajor");
                    Person person = new Person(userName, userID, userPassword, Uri.parse(userPicture), major);
                    if(major.equals(userMajor)){
                        arrayPerson.add(person);
                    }
                }
                adapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
