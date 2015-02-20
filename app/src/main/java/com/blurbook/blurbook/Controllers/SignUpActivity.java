package com.blurbook.blurbook.Controllers;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.blurbook.blurbook.Models.User;
import com.blurbook.blurbook.R;
import com.blurbook.blurbook.Services.JSONParser;
import com.blurbook.blurbook.Services.RestAPI;

import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class SignUpActivity extends ActionBarActivity {

    Toolbar toolbar;
    Context context;

    EditText etFirstName, etLastName, etEmail, etPassword;
    Button btnCreateUser;

    String firstName, lastName, email, password, encryptedPassword;
    User newUser;

    private static String cryptoPass = "Blurbook";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize  the layout components
        context=this;
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

                try {
                    DESKeySpec keySpec = new DESKeySpec(cryptoPass.getBytes("UTF8"));
                    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
                    SecretKey key = keyFactory.generateSecret(keySpec);

                    byte[] clearText = password.getBytes("UTF8");
                    // Cipher is not thread safe
                    Cipher cipher = Cipher.getInstance("DES");
                    cipher.init(Cipher.ENCRYPT_MODE, key);

                    encryptedPassword = Base64.encodeToString(cipher.doFinal(clearText), Base64.DEFAULT);

                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (InvalidKeySpecException e) {
                    e.printStackTrace();
                }

                newUser = new User(firstName, lastName, email, encryptedPassword);

                if(!firstName.equals("") && !lastName.equals("") && !email.equals("") && !password.equals(""))
                {
                    // Execute the AsyncCreateUser class
                    NetAsync(v);
                }
                else if(firstName.equals(""))
                {
                    Toast.makeText(context, "First Name field is empty", Toast.LENGTH_SHORT).show();
                }
                else if(lastName.equals(""))
                {
                    Toast.makeText(context,"Last Name field is empty", Toast.LENGTH_SHORT).show();
                }
                else if(email.equals(""))
                {
                    Toast.makeText(context,"Email field is empty", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(context,"Password field is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Async Task to check whether internet connection is working.
     **/
    private class NetCheck extends AsyncTask<String, Void, Boolean>
    {
        private ProgressDialog nDialog;
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            nDialog = new ProgressDialog(SignUpActivity.this);
            nDialog.setTitle("Checking Network");
            nDialog.setMessage("Loading..");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... args) {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try {
                    URL url = new URL("http://www.google.com");
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    urlc.setConnectTimeout(3000);
                    urlc.connect();
                    if (urlc.getResponseCode() == 200) {
                        return true;
                    }
                } catch (MalformedURLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return false;
        }
        @Override
        protected void onPostExecute(Boolean th){
            if(th == true){
                nDialog.dismiss();
                new AsyncIsUserExisted().execute(email);
            }
            else{
                nDialog.dismiss();
                //loginErrorMsg.setText("Error in Network Connection");
                Toast.makeText(context,"Error in Network Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class AsyncIsUserExisted extends AsyncTask<String, JSONObject, Boolean>{

        @Override
        protected Boolean doInBackground(String... params) {
            RestAPI api = new RestAPI();
            boolean userExisted = false;
            try{
                // Call the IsUserExisted Method in API
                JSONObject jsonObj = api.IsUserExisted(params[0]);

                //Parse the JSON Object to boolean
                JSONParser parser = new JSONParser();
                userExisted = parser.parseUserExisted(jsonObj);
                email=params[0];
            }
            catch (Exception e) {
                // TODO Auto-generated catch block
                Log.d("AsyncIsUserExisted", e.getMessage());
            }
            return userExisted;
        }
        @Override
        protected void onPostExecute(Boolean result) {
            // TODO Auto-generated method stub
            //Check user validity
            if (result) {
                Toast.makeText(context, "Email is already used! ", Toast.LENGTH_SHORT).show();
            }
            else
            {
                new AsyncCreateUser().execute(newUser);
            }
        }
    }

    protected class AsyncCreateUser extends
            AsyncTask<User, Void, Void> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pDialog = new ProgressDialog(SignUpActivity.this);
            pDialog.setTitle("Contacting Servers");
            pDialog.setMessage("Registering ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

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

    public void NetAsync(View v){
        new NetCheck().execute();
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
