package com.example.mangareader;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class CustomAdapterChapterPicture extends ArrayAdapter<String> {
    private Context context;
    private int resource;
    private ArrayList<String> pictureList;

    public CustomAdapterChapterPicture(@NonNull Context context, int resource, ArrayList<String> pictureList) {
        super(context, resource, pictureList);
        this.context = context;
        this.resource = resource;
        this.pictureList = pictureList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String pictureLink = pictureList.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, null);
        }
        ImageView imgPicture = convertView.findViewById(R.id.imgPicture);
        Picasso.get().load(pictureLink).into(imgPicture);
        return convertView;
    }
}