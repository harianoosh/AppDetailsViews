package com.todo.itunesapps;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

/*
Assignment #: HW04
FileName: AppItemAdapter
Full Name of Students: Anoosh Hari, Dayakar Ravuri Group 29
 */

public class AppItemAdapter extends ArrayAdapter<com.todo.itunesapps.DataServices.App> {

    public AppItemAdapter(@NonNull Context context, int resource, @NonNull List<com.todo.itunesapps.DataServices.App> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.app_detail_list,parent,false);
        }

        com.todo.itunesapps.DataServices.App app = getItem(position);

        TextView appName = convertView.findViewById(R.id.itemAppName);
        TextView artistName = convertView.findViewById(R.id.itemArtistName);
        TextView releaseDate = convertView.findViewById(R.id.itemReleaseDate);

        appName.setText(app.name);
        artistName.setText(app.artistName);
        releaseDate.setText(app.releaseDate);

        return convertView;
    }
}
