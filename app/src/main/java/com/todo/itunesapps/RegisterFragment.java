package com.todo.itunesapps;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

/*
Assignment #: HW04
FileName: RegisterFragment
Full Name of Students: Anoosh Hari, Dayakar Ravuri Group 29
 */

public class RegisterFragment extends Fragment {

    TextView cancel;
    Button submitButton;

    TextView name;
    TextView email;
    TextView password;

    String token;
    DataServices.Account account;

    private IRegisterFragmentHandler listener;

    public RegisterFragment() {

    }

    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        getActivity().setTitle(getString(R.string.register_title));

        submitButton = view.findViewById(R.id.registerSubmit);
        cancel = view.findViewById(R.id.registerCancel);

        name = view.findViewById(R.id.registerName);
        email = view.findViewById(R.id.registerEmail);
        password = view.findViewById(R.id.registerPassword);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String registerName = name.getText().toString();
                String registerEmail = email.getText().toString();
                String registerPassword = password.getText().toString();
                try {
                    if (registerName.isEmpty()) {
                        throw new Exception(getString(R.string.invalid_name));
                    }
                    if (registerEmail.isEmpty()) {
                        throw new Exception(getString(R.string.invalid_email));
                    }

                    if (registerPassword.isEmpty()) {
                        throw new Exception(getString(R.string.invalid_password));
                    }

                    new RegisterAsyncTask().execute(new String[]{registerName, registerEmail, registerPassword});
                } catch (Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), getString(R.string.cancelled_register), Toast.LENGTH_SHORT).show();
                listener.onRegistrationCancelHandler();
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
        return view;
    }

    OnBackPressedCallback callBack;
    boolean inProgress = false;

    private void updateVisibility(boolean isVisibile) {
        if (isVisibile) {
            submitButton.setEnabled(true);
            cancel.setVisibility(View.VISIBLE);
        } else {
            submitButton.setEnabled(false);
            cancel.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        if (context instanceof IRegisterFragmentHandler) {
            listener = (IRegisterFragmentHandler) context;
        }
        super.onAttach(context);
    }

    public interface IRegisterFragmentHandler {
        void onRegistrationSubmit(String token, DataServices.Account account);

        void onRegistrationCancelHandler();
    }

    class RegisterAsyncTask extends AsyncTask<String[], String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            inProgress = true;
            callBack.setEnabled(inProgress);
            updateVisibility(false);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Toast.makeText(getActivity(), values[0], Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String[]... strings) {
            try {
                publishProgress(getResources().getString(R.string.registering));
                token = DataServices.register(strings[0][0], strings[0][1], strings[0][2]);
                publishProgress(getResources().getString(R.string.success_register));
                if (token != null) {
                    publishProgress(getResources().getString(R.string.fetching_account_details));
                    account = DataServices.getAccount(token);
                }
            } catch (DataServices.RequestException e) {
                publishProgress(e.getMessage());
            }
            return token;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            inProgress = false;
            callBack.setEnabled(inProgress);
            updateVisibility(true);
            listener.onRegistrationSubmit(token, account);
        }
    }
}