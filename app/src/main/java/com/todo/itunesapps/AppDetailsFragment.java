package com.todo.itunesapps;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/*
Assignment #: HW04
FileName: AppDetailsFragment
Full Name of Students: Anoosh Hari, Dayakar Ravuri Group 29
 */

public class AppDetailsFragment extends Fragment {

    private static final String APP_DETAILS = "APP_DETAILS";

    private com.todo.itunesapps.DataServices.App app;

    TextView appName;
    TextView artistName;
    TextView releaseDate;

    RecyclerView recyclerView;
    AppDetailsRecyclerViewAdapter adapter;
    LinearLayoutManager linearLayoutManager;

    String[] genres;

    public AppDetailsFragment() {

    }

    public static com.todo.itunesapps.AppDetailsFragment newInstance(com.todo.itunesapps.DataServices.App app) {
        com.todo.itunesapps.AppDetailsFragment fragment = new com.todo.itunesapps.AppDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(APP_DETAILS, app);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            app = (com.todo.itunesapps.DataServices.App) getArguments().getSerializable(APP_DETAILS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_app_details, container, false);

        getActivity().setTitle(getString(R.string.app_details_title));

        appName = view.findViewById(R.id.appDetailsAppName);
        artistName = view.findViewById(R.id.appDetailsArtistName);
        releaseDate = view.findViewById(R.id.appDetailsReleaseDate);


        recyclerView = view.findViewById(R.id.appDetailsRecyclerView);

        genres = app.genres.toArray(new String[app.genres.size()]);

        appName.setText(app.name);
        artistName.setText(app.artistName);
        releaseDate.setText(app.releaseDate);

        linearLayoutManager = new LinearLayoutManager(getContext());
        adapter = new AppDetailsRecyclerViewAdapter(genres);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        return view;
    }
}