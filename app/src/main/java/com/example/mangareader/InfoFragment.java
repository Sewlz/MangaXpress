package com.example.mangareader;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InfoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView tvSynopsis, tvGenres;
    Button btnFirst, btnLast;
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayList<String> arrayListChapter = new ArrayList<>();

    public InfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
     * @return A new instance of fragment InfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    //mod in order to send data to another fragment in view pager smh -1
    public static InfoFragment newInstance(Detail detail) {
        InfoFragment fragment = new InfoFragment();
        Bundle args = new Bundle();
        //mod in order to send data to another fragment in view pager smh -1
        args.putString("synopsis", detail.synopsis);
        args.putString("genre", detail.genre);
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        InfoActivity activity = (InfoActivity) getActivity();
        String detail_url = activity.get_detail_url();
        getAllData(detail_url);
        addControls(view);
        getBundle();
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
        addEvents();
    }

    private void addControls(View view){
        btnFirst = (Button) view.findViewById(R.id.btnFirstChap);
        btnLast = (Button) view.findViewById(R.id.btnLastChap);
        tvSynopsis = (TextView) view.findViewById(R.id.tvSynopsis);
        tvGenres = (TextView) view.findViewById(R.id.tvGenres);
    }

    private void addEvents(){
        btnFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ReadingActivity.class);
                intent.putExtra("positionChapter", arrayListChapter.size() - 1);
                intent.putExtra("ArrayListChapter", arrayListChapter);
                intent.putExtra("ArrayListName", arrayList);
                startActivity(intent);
            }
        });

        btnLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ReadingActivity.class);
                intent.putExtra("positionChapter", 0);
                intent.putExtra("ArrayListChapter", arrayListChapter);
                intent.putExtra("ArrayListName", arrayList);
                startActivity(intent);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_info, container, false);
    }

    private void getBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            String synopsis = bundle.getString("synopsis");
            String genre = bundle.getString("genre");
            tvSynopsis.setText(synopsis);
            tvGenres.setText(genre);
        }
    }
}