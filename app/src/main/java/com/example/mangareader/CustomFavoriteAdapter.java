package com.example.mangareader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.auth.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomFavoriteAdapter extends ArrayAdapter {
    ArrayList<UserFav> arrayList = new ArrayList<>();
    Context context;
    Integer resource;
    public CustomFavoriteAdapter(@NonNull Context context, int resource, @NonNull ArrayList<UserFav> arrayList) {
        super(context, resource, arrayList);
        this.context = context;
        this.resource = resource;
        this.arrayList = arrayList;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        UserFav userFav = arrayList.get(position);
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(resource,null);
        }
        ImageView imgFav = (ImageView) convertView.findViewById(R.id.imgFav);
        Picasso.get().load(userFav.getThumbnail()).into(imgFav);
        TextView tvFavTitle = (TextView) convertView.findViewById(R.id.tvFavTitle);
        tvFavTitle.setText(userFav.mangaUrl.substring(33 ).replace("-"," ").toUpperCase());
        return convertView;
    }

}
