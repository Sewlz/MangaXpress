package com.example.mangareader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
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

public class SearchActivity extends AppCompatActivity {
    private EditText edtSearch;
    private String searchParam;
    private String apiUrl;
    ListView lvSearch;
    ArrayList<Detail> arrayList = new ArrayList<>();
    CustomSearchAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        addControl();
        addEvent();
        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                searchParam = edtSearch.getText().toString().toLowerCase();
                apiUrl = "https://wibutools.live/api/komiku/" +searchParam.replaceAll("\\s+","-");
                Toast.makeText(SearchActivity.this, "test: "+apiUrl, Toast.LENGTH_SHORT).show();
                getAllData(apiUrl);
                return true;
            }
        });
    }
    private void addControl(){
        edtSearch = (EditText) findViewById(R.id.edtSearch);
        lvSearch = (ListView) findViewById(R.id.lvSearch);
    }
    private void addEvent(){
        lvSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SearchActivity.this,InfoActivity.class);
                intent.putExtra("searchUrl",apiUrl);
                startActivity(intent);
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
                Toast.makeText(SearchActivity.this, "Error Data!", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }
    private void JsonDataToArrayList(String response) throws JSONException {
        arrayList.clear();
        JSONObject resObj = new JSONObject(response);
        JSONObject dataObj= resObj.getJSONObject("data");
        Detail detail = new Detail();
        detail.title =dataObj.getString("title");
        detail.thumbnail = dataObj.getString("thumbnail");
        detail.synopsis = dataObj.getString("synopsis");
        arrayList.add(detail);
        adapter = new CustomSearchAdapter(getApplicationContext(), R.layout.custom_search_list_item,arrayList);
        lvSearch.setAdapter(adapter);
    }
}