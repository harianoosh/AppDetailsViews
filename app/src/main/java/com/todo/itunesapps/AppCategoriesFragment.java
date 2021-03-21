package com.todo.itunesapps;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/*
Assignment #: HW04
FileName: AppCategoriesFragment
Full Name of Students: Anoosh Hari, Dayakar Ravuri Group 29
 */

public class AppCategoriesFragment extends Fragment {

    private static final String LOGIN_TOKEN = "LOGIN_TOKEN";
    private static final String ACCOUNT_TOKEN = "ACCOUNT_TOKEN";

    private String loginToken;
    private DataServices.Account account;
    private String[] categories = new String[]{};

    TextView welcomeText;

    Button logout;

    RecyclerView recyclerView;
    AppCategoriesRecyclerViewAdapter adapter;
    LinearLayoutManager layoutManager;

    IAppCategoriesHandler listener;

    public AppCategoriesFragment() {

    }

    public static AppCategoriesFragment newInstance(String loginToken, DataServices.Account account) {
        AppCategoriesFragment fragment = new AppCategoriesFragment();
        Bundle args = new Bundle();
        args.putString(LOGIN_TOKEN, loginToken);
        args.putSerializable(ACCOUNT_TOKEN, account);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            loginToken = getArguments().getString(LOGIN_TOKEN);
            account = (DataServices.Account) getArguments().getSerializable(ACCOUNT_TOKEN);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_app_categories, container, false);

        getActivity().setTitle(getString(R.string.app_categories_title));

        welcomeText = view.findViewById(R.id.appCategoriesGreetings);
        logout = view.findViewById(R.id.appCategoriesLogout);

        recyclerView = view.findViewById(R.id.appCategoriesRecyclerview);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        welcomeText.setText(getString(R.string.welcome_label) + " " + account.getName());

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), getString(R.string.success_logout), Toast.LENGTH_SHORT).show();
                listener.onLogoutHandler();
            }
        });

        callBack = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (inProgress) {
                    Toast.makeText(getActivity(), getString(R.string.fetching_data), Toast.LENGTH_SHORT).show();
                } else {
                    getActivity().onBackPressed();
                }
            }
        };
        getActivity().getOnBackPressedDispatcher().addCallback(callBack);
        callBack.setEnabled(false);

        if (categories.length == 0) {
            new AppCategoriesAsyncTask().execute(loginToken);
        }
        adapter = new AppCategoriesRecyclerViewAdapter(listener, categories);
        recyclerView.setAdapter(adapter);

        return view;
    }

    OnBackPressedCallback callBack;
    boolean inProgress = false;

    @Override
    public void onAttach(@NonNull Context context) {
        if (context instanceof IAppCategoriesHandler) {
            listener = (IAppCategoriesHandler) context;
        }
        super.onAttach(context);
    }

    public interface IAppCategoriesHandler {
        void onLogoutHandler();

        void onClickHandler(int position, String[] categories);
    }

    class AppCategoriesAsyncTask extends AsyncTask<String, String, String[]> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            logout.setEnabled(false);
            inProgress = true;
            callBack.setEnabled(inProgress);
        }

        @Override
        protected void onProgressUpdate(String... strings) {
            Toast.makeText(getActivity(), strings[0], Toast.LENGTH_SHORT).show();
            adapter = new AppCategoriesRecyclerViewAdapter(listener, categories);
            recyclerView.setAdapter(adapter);
            if (!strings[0].equalsIgnoreCase(getResources().getString(R.string.fetching_categories))) {
                logout.setEnabled(true);
            }
        }

        @Override
        protected String[] doInBackground(String... strings) {
            try {
                publishProgress(getResources().getString(R.string.fetching_categories));
                ArrayList<String> data = DataServices.getAppCategories(strings[0]);
                categories = data.toArray(new String[data.size()]);
                publishProgress(getResources().getString(R.string.fetching_categories_completed));
            } catch (DataServices.RequestException e) {
                publishProgress(e.getMessage());
            }
            return categories;
        }

        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);
            inProgress = false;
            callBack.setEnabled(inProgress);
            logout.setEnabled(true);
        }
    }
}