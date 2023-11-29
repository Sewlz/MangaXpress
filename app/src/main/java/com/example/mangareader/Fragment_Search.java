package com.example.mangareader;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Search#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Search extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment_Search() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_search.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Search newInstance(String param1, String param2) {
        Fragment_Search fragment = new Fragment_Search();
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

    private EditText edtSearch;
    private String searchParam;
    private String apiUrl;
    ListView lvSearch;
    ArrayList<Detail> arrayList = new ArrayList<>();
    CustomSearchAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        addControls(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addEvents();
    }

    private void addControls(View view){
        edtSearch = (EditText) view.findViewById(R.id.edtSearch);
        lvSearch = (ListView) view.findViewById(R.id.lvSearch);
    }
    private void addEvents(){
        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                searchParam = edtSearch.getText().toString().toLowerCase();
                apiUrl = "https://wibutools.live/api/komiku/" +searchParam.replaceAll("\\s+","-");
                Toast.makeText(getContext(), "test: "+apiUrl, Toast.LENGTH_SHORT).show();
                getAllData(apiUrl);
                return true;
            }
        });
        lvSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(),InfoActivity.class);
                intent.putExtra("searchUrl",apiUrl);
                startActivity(intent);
            }
        });
    }
    private void getAllData(String url){
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
    private void JsonDataToArrayList(String response) throws JSONException {
        arrayList.clear();
        JSONObject resObj = new JSONObject(response);
        JSONObject dataObj= resObj.getJSONObject("data");
        Detail detail = new Detail();
        detail.title =dataObj.getString("title");
        detail.thumbnail = dataObj.getString("thumbnail");
        detail.synopsis = dataObj.getString("synopsis");
        arrayList.add(detail);
        adapter = new CustomSearchAdapter(getContext(), R.layout.custom_search_list_item, arrayList);
        lvSearch.setAdapter(adapter);
    }
}