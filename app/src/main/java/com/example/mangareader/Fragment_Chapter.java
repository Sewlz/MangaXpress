package com.example.mangareader;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Chapter#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Chapter extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ArrayList<String> arrayList = new ArrayList<>();
    ArrayList<String> arrayListChapter = new ArrayList<>();
    ArrayAdapter<String> adapter;
    ListView lvChapter;
    public Fragment_Chapter() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChapterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Chapter newInstance(String param1, String param2) {
        Fragment_Chapter fragment = new Fragment_Chapter();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chapter, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lvChapter = (ListView) view.findViewById(R.id.lvChapter);
        ActivityInfo activity = (ActivityInfo) getActivity();
        String detail_url = activity.get_detail_url();
        getAllData(detail_url);
        lvChapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), ActivityReading.class);
                intent.putExtra("positionChapter", position);
                intent.putExtra("ArrayListChapter", arrayListChapter);
                intent.putExtra("ArrayListName", arrayList);
                startActivity(intent);
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
        JSONObject dataObj= resObj.getJSONObject("data");
        JSONArray chapArr = dataObj.getJSONArray("chapters");
        for(int i = 0; i<chapArr.length();i++){
            JSONObject jsonObject= chapArr.getJSONObject(i);
            String chapterName =  jsonObject.getString("chapter");
            String url =  jsonObject.getString("detail_url");
            arrayList.add(chapterName);
            arrayListChapter.add(url);
        }
        adapter = new ArrayAdapter<>(getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, arrayList);
        lvChapter.setAdapter(adapter);
    }
}