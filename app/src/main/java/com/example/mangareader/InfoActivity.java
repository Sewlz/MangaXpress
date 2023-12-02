package com.example.mangareader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

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
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class InfoActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager2 viewPager;
    ViewPagerAdapter viewPagerAdapter;
    TextView tvInfoTitle;
    ImageView imgInfoThumb;
    private String detail_url;
    private String search_url;
    Detail detail = new Detail();
    String genre = "";
    ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Intent intent = getIntent();
        detail_url = intent.getStringExtra("detail_url");
        search_url =  intent.getStringExtra("searchUrl");
        if(detail_url == null){
            detail_url = search_url;
        }else if(detail_url!=null){
            detail_url = detail_url.replace("http","https");
        }else{
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
        addControl();
        getAllData(detail_url);

    }

    private void addControl(){
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager2) findViewById(R.id.viewPager);
        imgInfoThumb = (ImageView) findViewById(R.id.imgInfoThumb);
        tvInfoTitle = (TextView) findViewById(R.id.tvInfoTitle);
        btnBack = (ImageButton) findViewById(R.id.imgBtnBackInfo);
    };
    private void addEvent(Detail detail){
        //mod in order to send data to another fragment in view pager smh -1
        viewPagerAdapter = new ViewPagerAdapter(this,detail);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public String get_detail_url() {
        return detail_url;
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
                Toast.makeText(InfoActivity.this, "Error Data!", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

    private void JsonDataToArrayList(String response) throws JSONException {
        JSONObject resObj = new JSONObject(response);
        JSONObject dataObj= resObj.getJSONObject("data");
        detail.title =dataObj.getString("title");
        detail.thumbnail = dataObj.getString("thumbnail");
        detail.synopsis = dataObj.getString("synopsis");
        JSONArray genreArr = dataObj.getJSONArray("genre");
        for(int i=0;i<genreArr.length();i++){
            genre = genre + genreArr.getString(i) + " - ";
        }
        detail.genre = genre;
        inflateView(detail);
        addEvent(detail);
    }

    private void inflateView(Detail detail){
        tvInfoTitle.setText(detail.getTitle().replace("Komik",""));
        Picasso.get().load(detail.getThumbnail()).into(imgInfoThumb);
    }
}