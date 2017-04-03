package android.sample.loginassignment.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.sample.loginassignment.Constants;
import android.sample.loginassignment.R;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

/**
 * Created by viplao on 03/04/17.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText username;
    private EditText password;
    private Button loginButton;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        loginButton = (Button) findViewById(R.id.log_in_button);
        progressBar = (ProgressBar) findViewById(R.id.logging_in_progress_bar);

        username.addTextChangedListener(new UsernameTextWatcher());
        password.addTextChangedListener(new PasswordTextWatcher());
        loginButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.log_in_button){
            loginButton.setEnabled(false);
            LoginAsyncTask loginAsyncTask = new LoginAsyncTask();
            loginAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, username.getText().toString(), password.getText().toString());
        }
    }

    private class UsernameTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(!TextUtils.isEmpty(s)){
                if(s.toString().contains("@") || s.toString().contains(" ")) {
                    username.setError(getResources().getString(R.string.error_invalid_username));
                    loginButton.setEnabled(false);
                }
                else if(password.getText().length() >= 6){
                    loginButton.setEnabled(true);
                    username.setError(null);
                }
            }
            else {
                loginButton.setEnabled(false);
                username.setError(null);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }

    private class PasswordTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(!TextUtils.isEmpty(s) && !TextUtils.isEmpty(username.getText())){
                if(s.length() >= 6  && username.getError() == null) {
                    loginButton.setEnabled(true);
                }
                else if(s.length() < 6){
                    loginButton.setEnabled(false);
                }
            }
            else {
                loginButton.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }

    private class LoginAsyncTask extends AsyncTask<String, Void, Boolean>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            LoginActivity.this.loginButton.setEnabled(false);
            LoginActivity.this.progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String username = params[0];
            String password = params[1];
            boolean validCredentials = false;
            if (!TextUtils.isEmpty(username) && password.length() >= 6) {
                validCredentials = true;
            }
            return validCredentials;
        }

        @Override
        protected void onPostExecute(Boolean validCredentials) {
            super.onPostExecute(validCredentials);
            if(validCredentials){
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                intent.putExtra(Constants.KEY_USER_NAME, LoginActivity.this.username.getText().toString());
                LoginActivity.this.startActivity(intent);
//                LoginActivity.this.loginButton.setEnabled(true);
//                LoginActivity.this.progressBar.setVisibility(View.GONE);
                LoginActivity.this.finish();
            }
        }
    }

}
