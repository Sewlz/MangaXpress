package com.example.mangareader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ActivityFavorite extends AppCompatActivity {
    CustomFavoriteAdapter adapter;
    ArrayList<UserFav> arrayList = new ArrayList<>();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth;
    FirebaseUser user;
    ListView lvFav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        getUserFav();
        lvFav = (ListView) findViewById(R.id.lvFavorite);

        lvFav.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserFav userFav = arrayList.get(position);
                Intent intent = new Intent(ActivityFavorite.this, ActivityInfo.class);
                intent.putExtra("detail_url",userFav.getMangaUrl());
                startActivity(intent);
            }
        });
    }
    private void getUserFav(){
        CollectionReference collectionReference = firestore.collection("UserFav");
        collectionReference.whereEqualTo("EMAIL",user.getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(DocumentSnapshot dc: task.getResult()){
                                arrayList.add(dc.toObject(UserFav.class));
                            }
                            adapter = new CustomFavoriteAdapter(getApplicationContext(),R.layout.custom_favorite_list_item,arrayList);
                            lvFav.setAdapter(adapter);
                        }
                        else {}
                    }
                });
    }
}