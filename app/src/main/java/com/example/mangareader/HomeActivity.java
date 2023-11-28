package com.example.mangareader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser user;
    ArrayList<Manga> arrayList = new ArrayList<>();
    CustomHomeAdapter adapter;
    String apiUrl = "https://wibutools.live/api/komiku";
    ListView lvHome;
    ImageButton btnHomePrev, btnHomeNext;
    TextView tvHomePage;
    String next_page,prev_page;
    boolean isLoadingData = false;
    ImageSlider imgHome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        addControl();
        btnHomePrev.setEnabled(false);
        btnHomePrev.setEnabled(false);
        if(user == null){
            Intent intent = new Intent(HomeActivity.this,LoginActivity.class);
            startActivity(intent);
        }else {
            Log.d("TAG", "onCreate: "+user.getEmail());
            addEvent();
            getAllData(apiUrl);
        }
    }
    private void addControl(){
        imgHome = (ImageSlider) findViewById(R.id.imgHome);
        lvHome = (ListView) findViewById(R.id.lvHome);
        btnHomeNext = (ImageButton) findViewById(R.id.btnHomeNext);
        btnHomePrev = (ImageButton) findViewById(R.id.btnHomePrev);
        tvHomePage = (TextView) findViewById(R.id.tvHomePage);
    }
    ArrayList<SlideModel> setImageList = new ArrayList<>();
    private void initImg(){
        setImageList.add(new SlideModel(R.drawable.slide1, ScaleTypes.FIT));
        setImageList.add(new SlideModel(R.drawable.slide2, ScaleTypes.FIT));
        setImageList.add(new SlideModel(R.drawable.slide3, ScaleTypes.FIT));
        setImageList.add(new SlideModel(R.drawable.slide4, ScaleTypes.FIT));
    };

    private void addEvent(){
        initImg();
        imgHome.setImageList(setImageList,ScaleTypes.FIT);
        lvHome.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(HomeActivity.this,InfoActivity.class);
                String detail_url = arrayList.get(position).getDetail_url();
                intent.putExtra("detail_url",detail_url);
                startActivity(intent);
            }
        });
        btnHomeNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLoadingData) { // Kiểm tra xem dữ liệu có đang tải hay không
                    if (next_page.equals("null")) {
                        Toast.makeText(HomeActivity.this, "Can't go any further.", Toast.LENGTH_SHORT).show();
                    } else {
                        isLoadingData = true; // Đánh dấu là đang tải dữ liệu
                        btnHomeNext.setEnabled(false); // Vô hiệu hóa nút để ngăn người dùng nhấn liên tục
                        int page = Integer.parseInt(tvHomePage.getText().toString());
                        page++;
                        tvHomePage.setText(String.valueOf(page));
                        arrayList.clear();
                        next_page = next_page.replace("http", "https");
                        getAllData(next_page);
                    }
                }
            }
        });

        btnHomePrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLoadingData) { // Kiểm tra xem dữ liệu có đang tải hay không
                    if (prev_page.equals("null")) {
                        Toast.makeText(HomeActivity.this, "Can't go any further.", Toast.LENGTH_SHORT).show();
                    } else {
                        isLoadingData = true; // Đánh dấu là đang tải dữ liệu
                        btnHomePrev.setEnabled(false); // Vô hiệu hóa nút để ngăn người dùng nhấn liên tục

                        int page = Integer.parseInt(tvHomePage.getText().toString());
                        page--;
                        tvHomePage.setText(String.valueOf(page));
                        arrayList.clear();
                        prev_page = prev_page.replace("http", "https");
                        getAllData(prev_page);
                    }
                }
            }
        });
    }
    public void getAllData(String url){
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
                Toast.makeText(HomeActivity.this, "Error Data!", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }
    public void JsonDataToArrayList(String response) throws JSONException {
        JSONObject resObj = new JSONObject(response);
        JSONArray dataArr = resObj.getJSONArray("data");
        btnHomePrev.setVisibility(View.VISIBLE);
        btnHomeNext.setVisibility(View.VISIBLE);
        for (int i = 0; i<2;i++){
            Manga m = new Manga();
            next_page = resObj.getString("next_page");
            prev_page = resObj.getString("prev_page");
            if(next_page.contains("null")){
                btnHomeNext.setVisibility(View.INVISIBLE);
            } else if (prev_page.contains("null")) {
                btnHomePrev.setVisibility(View.INVISIBLE);
            }
        }
        for(int i = 0; i<dataArr.length();i++){
            JSONObject jsonObject= dataArr.getJSONObject(i);
            Manga m = new Manga();
            m.title = jsonObject.getString("title");
            m.description = jsonObject.getString("description");
            m.latest_chapter = jsonObject.getString("latest_chapter");
            m.thumbnail = jsonObject.getString("thumbnail");
            m.param = jsonObject.getString("param");
            m.detail_url = jsonObject.getString("detail_url");
            arrayList.add(m);
        }
        adapter = new CustomHomeAdapter(getApplicationContext(),R.layout.custom_home_list_item,arrayList);
        lvHome.setAdapter(adapter);

        isLoadingData = false; // Khi dữ liệu đã được tải xong, đánh dấu là không còn tải dữ liệu
        btnHomeNext.setEnabled(true);
        btnHomePrev.setEnabled(true);
    }
}