package com.example.mangareader;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class    Fragment_Home extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment_Home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Home.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Home newInstance(String param1, String param2) {
        Fragment_Home fragment = new Fragment_Home();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        addControls(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if(user == null){
            Intent intent = new Intent(getContext(), ActivityLogin.class);
            startActivity(intent);
        }else {
            Log.d("TAG", "onCreate: "+user.getEmail());
            addEvent();
            getAllData(apiUrl);
        }
    }

    private void addControls(View view){
        imgHome = (ImageSlider) view.findViewById(R.id.imgHome);
        lvHome = (ListView) view.findViewById(R.id.lvHome);
        btnHomeNext = (ImageButton) view.findViewById(R.id.btnHomeNext);
        btnHomePrev = (ImageButton) view.findViewById(R.id.btnHomePrev);
        tvHomePage = (TextView) view.findViewById(R.id.tvHomePage);
    }
    ArrayList<SlideModel> setImageList = new ArrayList<>();
    private void initImg(){
        setImageList.add(new SlideModel(R.drawable.slide1, ScaleTypes.FIT));
        setImageList.add(new SlideModel(R.drawable.slide2, ScaleTypes.FIT));
        setImageList.add(new SlideModel(R.drawable.slide3, ScaleTypes.FIT));
        setImageList.add(new SlideModel(R.drawable.slide4, ScaleTypes.FIT));
        setImageList.add(new SlideModel(R.drawable.slide5, ScaleTypes.FIT));
        setImageList.add(new SlideModel(R.drawable.slide6, ScaleTypes.FIT));
    };

    private void addEvent(){
        initImg();
        imgHome.setImageList(setImageList,ScaleTypes.FIT);
        lvHome.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), ActivityInfo.class);
                String detail_url = arrayList.get(position).getDetail_url();
                String title = arrayList.get(position).getTitle();
                intent.putExtra("detail_url",detail_url);
                startActivity(intent);
            }
        });
        btnHomeNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLoadingData) { // Kiểm tra xem dữ liệu có đang tải hay không
                    if (next_page.equals("null")) {
                        Toast.makeText(getContext(), "Can't go any further.", Toast.LENGTH_SHORT).show();
                    } else {
                        isLoadingData = true; // Đánh dấu là đang tải dữ liệu
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
                if (!isLoadingData) {
                    if (prev_page.equals("null")) {
                        Toast.makeText(getContext(), "Can't go any further.", Toast.LENGTH_SHORT).show();
                    } else {
                        isLoadingData = true;

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
        RequestQueue queue = Volley.newRequestQueue(getContext());
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
                Toast.makeText(getContext(), "Error Data!", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }
    public void JsonDataToArrayList(String response) throws JSONException {
        JSONObject resObj = new JSONObject(response);
        JSONArray dataArr = resObj.getJSONArray("data");
        btnHomeNext.setEnabled(true);
        btnHomeNext.setAlpha(1f);
        btnHomePrev.setEnabled(true);
        btnHomePrev.setAlpha(1f);
        for (int i = 0; i<2;i++){
            Manga m = new Manga();
            next_page = resObj.getString("next_page");
            prev_page = resObj.getString("prev_page");
            if(next_page.contains("null")){
                btnHomeNext.setEnabled(false);
                btnHomeNext.setAlpha(0.5f);
            } else if (prev_page.contains("null")) {
                btnHomePrev.setEnabled(false);
                btnHomePrev.setAlpha(0.5f);
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
        if (getContext() != null && arrayList != null) {
            adapter = new CustomHomeAdapter(getContext(), R.layout.custom_home_list_item, arrayList);
        }
        lvHome.setAdapter(adapter);

        isLoadingData = false;
    }
}