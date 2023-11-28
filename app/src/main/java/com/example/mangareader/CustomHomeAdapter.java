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

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomHomeAdapter extends ArrayAdapter {
    ArrayList<Manga> arrayList = new ArrayList<>();
    Context context;
    Integer resource;
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Manga manga = arrayList.get(position);
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(resource,null);
        }
        ImageView imgThumb = (ImageView) convertView.findViewById(R.id.imgThumb);
        Picasso.get().load(manga.getThumbnail()).resize(100,100).into(imgThumb);
        TextView tvHomeTitle = (TextView) convertView.findViewById(R.id.tvHomeTitle);
        tvHomeTitle.setText(manga.getTitle());
        TextView tvHomeDes = (TextView) convertView.findViewById(R.id.tvHomeDes);
        String des = manga.getLatest_chapter();
//        String truncatedContent = des.length() > 10 ? des.substring(0, 40) + "..." : des;
        tvHomeDes.setText(des);
        return convertView;
    }

    public CustomHomeAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Manga> arrayList) {
        super(context, resource, arrayList);
        this.context = context;
        this.resource = resource;
        this.arrayList = arrayList;
    }
}