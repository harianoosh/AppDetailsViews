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
FileName: AppCategoriesRecyclerViewAdapter
Full Name of Students: Anoosh Hari, Dayakar Ravuri Group 29
 */

public class AppCategoriesRecyclerViewAdapter
        extends RecyclerView.Adapter<AppCategoriesRecyclerViewAdapter.AppCategoriesViewHolder> {

    private AppCategoriesFragment.IAppCategoriesHandler appCategoriesListener;

    private String[] categories;

    private Resources resources;

    public AppCategoriesRecyclerViewAdapter(AppCategoriesFragment.IAppCategoriesHandler appCategoriesListener, String[] categories) {
        this.categories = categories;
        this.appCategoriesListener = appCategoriesListener;
    }

    @NonNull
    @Override
    public AppCategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_categories_list_item, parent, false);
        this.resources = parent.getContext().getResources();
        AppCategoriesViewHolder appViewHolder = new AppCategoriesViewHolder(view);
        return appViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AppCategoriesViewHolder holder, int position) {
        holder.position = position;
        holder.appCategoriesListener = appCategoriesListener;
        holder.categories = categories;
        holder.textView.setText(categories[position]);
    }

    @Override
    public int getItemCount() {
        return (this.categories != null ? this.categories.length : 0);
    }

    public class AppCategoriesViewHolder extends RecyclerView.ViewHolder {
        private int position;
        private AppCategoriesFragment.IAppCategoriesHandler appCategoriesListener;
        private String[] categories;
        private TextView textView;
        private ConstraintLayout box;

        public AppCategoriesViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.itemCategoryName);
            box = itemView.findViewById(R.id.itemCategoriesBoxContainer);
            box.setBackground(resources.getDrawable(R.drawable.border));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    appCategoriesListener.onClickHandler(position, categories);
                }
            });
        }
    }
}
