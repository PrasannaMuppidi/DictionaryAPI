package com.example.prasanna.dictionary;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class home extends AppCompatActivity {

    TextView displayText;
    String response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        displayText = (TextView) findViewById(R.id.display);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_logout) {
            Intent i = new Intent(home.this, MainActivity.class);
            startActivity(i);
            finish();
        }else if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void dictionary(View v) {
        if (v.getId() == R.id.dictionary) {
            String Word = ((EditText) findViewById(R.id.word)).getText().toString();
            final String urlText = "https://dictionary.yandex.net/api/v1/dicservice.json/lookup?" +
                    "key=dict.1.1.20160202T025303Z.884393d1bc654f0c.e6ec9d84d2be290482388dda13e526a807092c05&lang=en-en&text=" + Word;

            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        URL url = new URL(urlText);
                        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                        urlConnection.setRequestMethod("GET");
                        urlConnection.setDoInput(true);
                        urlConnection.setDoOutput(true);
                        StringBuilder stringBuilder = new StringBuilder();
                        InputStream is;
                        is = urlConnection.getInputStream();
                        BufferedReader br = new BufferedReader(new InputStreamReader(is));
                        String line;
                        while ((line = br.readLine()) != null) {
                            stringBuilder.append(line);
                        }
                        response = stringBuilder.toString();
                        JSONObject j = new JSONObject(response);
                        JSONArray k = j.getJSONArray("def");
                        JSONArray l = k.getJSONObject(0).getJSONArray("tr").getJSONObject(0).getJSONArray("syn");
                        response = "1."+l.getJSONObject(0).getString("text").toString()+ " \n2." + l.getJSONObject(1).getString("text").toString();
                        System.out.println(l);
                        is.close();
                        urlConnection.disconnect();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    displayText.setText(response);
                }
            },1500);

        }
    }
}


