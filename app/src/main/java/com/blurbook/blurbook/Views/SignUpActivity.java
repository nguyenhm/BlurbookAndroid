package com.blurbook.blurbook.Views;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.blurbook.blurbook.Models.User;
import com.blurbook.blurbook.R;
import com.blurbook.blurbook.Services.RestAPI;

public class SignUpActivity extends ActionBarActivity {

    Toolbar toolbar;

    EditText etFirstName, etLastName, etEmail, etPassword;
    Button btnCreateUser;

    String firstName, lastName, email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etFirstName = (EditText) findViewById(R.id.et_fisrtname);
        etLastName = (EditText) findViewById(R.id.et_lastname);
        etEmail = (EditText) findViewById(R.id.et_cu_email);
        etPassword = (EditText) findViewById(R.id.et_cu_password);
        btnCreateUser = (Button) findViewById(R.id.btn_createuser);

        btnCreateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firstName = etFirstName.getText().toString();
                lastName = etLastName.getText().toString();
                email = etEmail.getText().toString();
                password = etPassword.getText().toString();

                User newUser = new User(firstName, lastName, email, password);

                new AsyncCreateUser().execute(newUser);
            }
        });
    }

    protected class AsyncCreateUser extends
            AsyncTask<User, Void, Void> {

        @Override
        protected Void doInBackground(User... params) {

            RestAPI api = new RestAPI();
            try {

                api.CreateNewAccount(params[0].getFirstName(),
                        params[0].getLastName(), params[0].getEmail(),
                        params[0].getPassword());

            } catch (Exception e) {
                Log.d("AsyncCreateUser", e.getMessage());

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(i);
        }

    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if(id==android.R.id.home)
        {
            NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);
    }
}
