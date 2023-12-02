package com.example.mangareader;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class ReadingActivity extends AppCompatActivity {
    ListView lvPicture;
    ImageButton btnNextChap, btnPrevChap, btnBack;
    Button btnShow;
    TextView tvNumChap;
    CustomAdapterChapterPicture adapter;
    ArrayList<String> pictureList = new ArrayList<>(), arrayListChapter = new ArrayList<>(), arrayListName = new ArrayList<>();
    String next_url, prev_url, url;
    int positionChapter;
    boolean isLoadingData = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading);

        addControls();

        Intent intent = getIntent();
        positionChapter = intent.getIntExtra("positionChapter", 1);
        arrayListChapter = intent.getStringArrayListExtra("ArrayListChapter");
        arrayListName = intent.getStringArrayListExtra("ArrayListName");
        updateUrl(positionChapter);
        getAllPictures(url);

        addEvents();
    }

    private void updateUrl(int pos){
        String nameChap = arrayListName.get(pos);
        btnShow.setText(nameChap);
        tvNumChap.setText(nameChap);
        btnPrevChap.setEnabled(true);
        btnPrevChap.setAlpha(1f);
        btnNextChap.setEnabled(true);
        btnNextChap.setAlpha(1f);
        url = "https" + arrayListChapter.get(pos).substring(4);
        if(url.equals("https" + arrayListChapter.get(0).substring(4))){
            btnNextChap.setEnabled(false);
            btnNextChap.setAlpha(0.5f);
        }else {
            next_url = "https" + arrayListChapter.get(pos - 1).substring(4);
        }
        if(url.equals("https" + arrayListChapter.get(arrayListChapter.size() - 1).substring(4))){
            btnPrevChap.setEnabled(false);
            btnPrevChap.setAlpha(0.5f);
        }else {
            prev_url = "https" + arrayListChapter.get(pos + 1).substring(4);
        }
    }

    private void addControls(){
        btnNextChap = (ImageButton) findViewById(R.id.imgBtnNextChap);
        btnPrevChap = (ImageButton) findViewById(R.id.imgBtnPrevChap);
        btnBack = (ImageButton) findViewById(R.id.imgBtnBackReading);
        btnShow = (Button) findViewById(R.id.btnShowListChap);
        lvPicture = (ListView) findViewById(R.id.lvPicture);
        tvNumChap = (TextView) findViewById(R.id.tvNumChap);
    }

    private void addEvents(){
        btnNextChap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isLoadingData){
                    isLoadingData = true;
                    pictureList.clear();
                    positionChapter -= 1;
                    updateUrl(positionChapter);
                    getAllPictures(url);
                }
            }
        });

        btnPrevChap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isLoadingData){
                    isLoadingData = true;
                    pictureList.clear();
                    positionChapter += 1;
                    updateUrl(positionChapter);
                    getAllPictures(url);
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ReadingActivity.this);
                builder.setTitle("Danh sách chap: ");

                String[] arrayChapter = arrayListName.toArray(new String[0]);

                builder.setItems(arrayChapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pictureList.clear();
                        updateUrl(which);
                        getAllPictures(url);
                    }
                });

                builder.setNegativeButton(Html.fromHtml("<font color='#E14949'>Đóng</font>"), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
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

        isLoadingData = false;
    }
}
