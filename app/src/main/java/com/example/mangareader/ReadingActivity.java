package com.example.mangareader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReadingActivity extends AppCompatActivity {
    ArrayList<String> pannelList= new ArrayList<>();
    ImageButton btnNext, btnPrev;
    ImageView imgPannel;
    TextView tvPage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading);
        Intent intent = getIntent();
        String url = intent.getStringExtra("chapterUrl");
        getAllData(url);
        addControl();
        addEvent();
    }
    private void addControl(){
        btnNext = (ImageButton) findViewById(R.id.btnNext);
        btnPrev = (ImageButton) findViewById(R.id.btnPrev);
        imgPannel = (ImageView) findViewById(R.id.imgPannel);
        tvPage = (TextView) findViewById(R.id.tvPage);
    }
    int i = 0;
    private void addEvent(){
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i++;
                if(i+1 > pannelList.size()){
                    i--;
                    Log.d("Page", ""+i);
                    Toast.makeText(ReadingActivity.this, "Can't go any further.", Toast.LENGTH_SHORT).show();
                }else {
                    Integer curPage = Integer.parseInt(tvPage.getText().toString());
                    curPage++;
                    Log.d("Page", ""+i);
                    tvPage.setText(String.valueOf(curPage));
                    Picasso.get().load(pannelList.get(i)).into(imgPannel);
                }
            }
        });
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i--;
                if(i < 0){
                    i++;
                    Log.d("Page", ""+i);
                    Toast.makeText(ReadingActivity.this, "Can't go less any more.", Toast.LENGTH_SHORT).show();
                }else {
                    Integer curPage = Integer.parseInt(tvPage.getText().toString());
                    curPage--;
                    tvPage.setText(String.valueOf(curPage));
                    Picasso.get().load(pannelList.get(i)).into(imgPannel);
                }
            }
        });
    }
    private void getAllData(String url){
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JsonDataToArrayList(response);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ReadingActivity.this, "Error Data!", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }
    private void JsonDataToArrayList(String response) throws JSONException {
        JSONObject resObj = new JSONObject(response);
        JSONArray dataArr= resObj.getJSONArray("data");
        for(int i=0;i<dataArr.length();i++){
            String url = dataArr.get(i).toString();
            pannelList.add(url);
        }
        Picasso.get().load(pannelList.get(0)).into(imgPannel);
    }
}