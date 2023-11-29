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
        Picasso.get().load(manga.getThumbnail()).into(imgThumb);

        TextView tvHomeTitle = (TextView) convertView.findViewById(R.id.tvHomeTitle);
        String strTitle = manga.getTitle();
        String truncateStrTitle = strTitle.length() > 20 ? strTitle.substring(0, 20) + "..." : strTitle;
        tvHomeTitle.setText(truncateStrTitle);

        TextView tvHomeDes = (TextView) convertView.findViewById(R.id.tvHomeChapter);
        String chap = manga.getLatest_chapter();
        tvHomeDes.setText(chap);

        return convertView;
    }

    public CustomHomeAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Manga> arrayList) {
        super(context, resource, arrayList);
        this.context = context;
        this.resource = resource;
        this.arrayList = arrayList;
    }
}