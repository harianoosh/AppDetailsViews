package com.todo.itunesapps;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

/*
    Assignment #: HW04
    FileName: AppDetailsRecyclerViewAdapter
    Full Name of Students: Anoosh Hari, Dayakar Ravuri Group 29
*/

public class AppDetailsRecyclerViewAdapter
        extends RecyclerView.Adapter<AppDetailsRecyclerViewAdapter.AppDetailsViewHolder> {

    private String[] genres;
    private Resources resources;

    public AppDetailsRecyclerViewAdapter(String[] genres) {
        this.genres = genres;
    }

    @NonNull
    @Override
    public AppDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.app_categories_list_item, parent, false);
        resources = parent.getResources();
        AppDetailsViewHolder appViewHolder = new AppDetailsViewHolder(view);
        return appViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AppDetailsViewHolder holder, int position) {
        holder.textView.setText(genres[position]);
    }

    @Override
    public int getItemCount() {
        return this.genres != null ? this.genres.length : 0;
    }

    public class AppDetailsViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private ConstraintLayout box;

        public AppDetailsViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.itemCategoryName);
            box = itemView.findViewById(R.id.itemCategoriesBoxContainer);
            box.setBackground(resources.getDrawable(R.drawable.border));
        }
    }
}
