package com.example.mangareader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mangareader.CustomAdapterChapterPicture;
import com.example.mangareader.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class ReadingActivity extends AppCompatActivity {
    ListView lvPicture;
    CustomAdapterChapterPicture adapter;
    ArrayList<String> pictureList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading);

        Intent intent = getIntent();
        String url = intent.getStringExtra("chapterUrl");
        getAllPictures(url);
        lvPicture = findViewById(R.id.lvPicture);

    }

    public void getAllPictures(String url) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JsonDataToArrayList(response);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                    }
                }
        );
        queue.add(stringRequest);
    }

    public void JsonDataToArrayList(String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        for (int i = 0; i < jsonArray.length(); i++) {
            String pictureLink = jsonArray.getString(i);
            pictureList.add(pictureLink);
        }
        adapter = new CustomAdapterChapterPicture(getApplicationContext(), R.layout.custom_chapter_picture_list_item, pictureList);
        lvPicture.setAdapter(adapter);
        if (jsonObject.has("title")) {
            TextView tvTitle = findViewById(R.id.tvTitle);
            tvTitle.setText(jsonObject.getString("title"));
        }
    }
}
