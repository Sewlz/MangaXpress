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

public class CustomSearchAdapter extends ArrayAdapter {
    ArrayList<Detail> arrayList = new ArrayList<>();
    Context context;
    Integer resource;
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Detail detail = arrayList.get(position);
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(resource,null);
        }
        ImageView imgSearch = (ImageView) convertView.findViewById(R.id.imgSearch);
        Picasso.get().load(detail.getThumbnail()).resize(100,100).into(imgSearch);
        TextView tvSearchTitle = (TextView) convertView.findViewById(R.id.tvSearchTitle);
        tvSearchTitle.setText(detail.getTitle());
        TextView tvSearchDes = (TextView) convertView.findViewById(R.id.tvSearchDes);
        String des = detail.getSynopsis();
        String truncatedContent = des.length() > 10 ? des.substring(0, 40) + "..." : des;
        tvSearchDes.setText(truncatedContent);
        return convertView;
    }

    public CustomSearchAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Detail> arrayList) {
        super(context, resource, arrayList);
        this.context = context;
        this.resource = resource;
        this.arrayList = arrayList;
    }
}
