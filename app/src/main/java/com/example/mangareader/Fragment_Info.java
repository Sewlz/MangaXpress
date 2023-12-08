package com.example.mangareader;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Info#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Info extends Fragment {

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

    ImageView btnFav;
    FirebaseFirestore db= FirebaseFirestore.getInstance();
    CollectionReference collectionReference = db.collection("UserFav");
    FirebaseAuth mAuth;
    FirebaseUser user;
    String detail_url;
    String thumbnail_url;

    public Fragment_Info() {
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
    public static Fragment_Info newInstance(Detail detail) {
        Fragment_Info fragment = new Fragment_Info();
        Bundle args = new Bundle();
        //mod in order to send data to another fragment in view pager smh -1
        args.putString("synopsis", detail.synopsis);
        args.putString("genre", detail.genre);
        args.putString("thumbnail", detail.thumbnail);
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
        addControls(view);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        ActivityInfo activity = (ActivityInfo) getActivity();

        detail_url = activity.get_detail_url();

        getAllData(detail_url);
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
        btnFav = (ImageView) view.findViewById(R.id.btnFav);
    }

    private void addEvents(){
        btnFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ActivityReading.class);
                intent.putExtra("positionChapter", arrayListChapter.size() - 1);
                intent.putExtra("ArrayListChapter", arrayListChapter);
                intent.putExtra("ArrayListName", arrayList);
                startActivity(intent);
            }
        });

        btnLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ActivityReading.class);
                intent.putExtra("positionChapter", 0);
                intent.putExtra("ArrayListChapter", arrayListChapter);
                intent.putExtra("ArrayListName", arrayList);
                startActivity(intent);
            }
        });

        btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserFav userFav = new UserFav(user.getEmail(),"http"+detail_url.substring(5),thumbnail_url);
                addFav(userFav);
            }
        });
        checkFav();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_info, container, false);
    }

    private void addFav(UserFav userFav){
        collectionReference.whereEqualTo("MANGAURL","http"+detail_url.substring(5))
                .whereEqualTo("EMAIL",user.getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            if(task.getResult().isEmpty()){
                                collectionReference.add(userFav)
                                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(getContext(), "Added Successful", Toast.LENGTH_SHORT).show();
                                                    btnFav.setImageResource(R.drawable.baseline_favorite_24);
                                                }else{
                                                    Toast.makeText(getContext(), "Added UnSuccessful", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }else {
                                QuerySnapshot querySnapshot = task.getResult();
                                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        document.getReference().delete();
                                    }
                                    Toast.makeText(getContext(), "Remove Successful", Toast.LENGTH_SHORT).show();
                                    btnFav.setImageResource(R.drawable.baseline_favorite_border_black_24);
                                }
                            }

                        }
                        else {}
                    }
                });
    }
    private void checkFav() {
        collectionReference.whereEqualTo("MANGAURL", "http"+detail_url.substring(5))
                .whereEqualTo("EMAIL", user.getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.getResult().isEmpty()){
                            btnFav.setImageResource(R.drawable.baseline_favorite_border_black_24);
                        }
                        else {
                            btnFav.setImageResource(R.drawable.baseline_favorite_24);
                        }
                    }
                });
    }

    private void getBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            String synopsis = bundle.getString("synopsis");
            String genre = bundle.getString("genre");
            tvSynopsis.setText(synopsis);
            tvGenres.setText(genre);
            thumbnail_url = bundle.getString("thumbnail");
        }
    }
}