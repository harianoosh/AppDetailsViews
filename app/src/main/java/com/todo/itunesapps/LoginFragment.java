package com.todo.itunesapps;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

/*
    Assignment #: HW04
    FileName: LoginFragment
    Full Name of Students: Anoosh Hari, Dayakar Ravuri Group 29
*/

public class LoginFragment extends Fragment {

    TextView createNewAccount;

    EditText email;
    EditText password;

    Button login;

    String loginToken;
    DataServices.Account account;

    private ILoginHandler listener;

    OnBackPressedCallback callBack;
    boolean gettingData = false;

    public LoginFragment() {

    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        getActivity().setTitle(getString(R.string.login_title));

        createNewAccount = view.findViewById(R.id.loginCreateNewAccount);
        email = view.findViewById(R.id.loginEmail);
        password = view.findViewById(R.id.loginPassword);
        login = view.findViewById(R.id.loginButtonLogin);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginEmail = email.getText().toString();
                String loginPassword = password.getText().toString();
                try {
                    if (loginEmail.isEmpty()) {
                        throw new Exception(getString(R.string.invalid_email));
                    }

                    if (loginPassword.isEmpty()) {
                        throw new Exception(getString(R.string.invalid_password));
                    }
                    new LoginAsyncTask().execute(new String[]{loginEmail, loginPassword});
                } catch (Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        createNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCreateNewAccountHandler();
            }
        });

        callBack = new OnBackPressedCallback(false) {
            @Override
            public void handleOnBackPressed() {
                if (!gettingData){
                    getActivity().onBackPressed();
                } else {
                    Toast.makeText(getActivity(), getString(R.string.fetching_data), Toast.LENGTH_SHORT).show();
                }
            }
        };
        getActivity().getOnBackPressedDispatcher().addCallback(callBack);
        callBack.setEnabled(false);
        return view;
    }

    private void updateVisibility(boolean isVisibile) {
        if (isVisibile){
            login.setEnabled(true);
            createNewAccount.setVisibility(View.VISIBLE);
        } else {
            login.setEnabled(false);
            createNewAccount.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        if (context instanceof ILoginHandler) {
            listener = (ILoginHandler) context;
        }
        super.onAttach(context);
    }

    public interface ILoginHandler {
        void onLoginHandler(String loginToken, DataServices.Account account);
        void onCreateNewAccountHandler();
    }

    class LoginAsyncTask extends AsyncTask<String[], String, DataServices.Account> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            updateVisibility(false);
            gettingData = true;
            callBack.setEnabled(true);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            Toast.makeText(getActivity(), values[0], Toast.LENGTH_SHORT).show();
        }

        @Override
        protected DataServices.Account doInBackground(String[]... strings) {
            DataServices.Account acnt = null;
            try {
                loginToken = DataServices.login(strings[0][0], strings[0][1]);
                acnt = DataServices.getAccount(loginToken);
                if (acnt != null){
                    publishProgress(getResources().getString(R.string.success_login));
                }
            } catch (DataServices.RequestException e) {
                publishProgress(e.getMessage());
            }
            return acnt;
        }

        @Override
        protected void onPostExecute(DataServices.Account acnt) {
            super.onPostExecute(acnt);
            updateVisibility(true);
            account = acnt;
            gettingData = false;
            if(account != null){
                callBack.setEnabled(false);
                listener.onLoginHandler(loginToken, account);
            }

        }
    }
}