//comment
package com.blurbook.blurbook.Controllers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
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

public class LoginActivity extends ActionBarActivity {

    Toolbar toolbar;

    EditText etEmail, etPassword;
    Button btnLogin;
    Context context;

    String email, password, encryptedPassword;

    private static String cryptoPass = "Blurbook";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button signUpButton = (Button) findViewById(R.id.sign_up_button);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });

        // Initialize  the layout components
        context = this;
        etEmail = (EditText) findViewById(R.id.et_email);
        etPassword = (EditText) findViewById(R.id.et_password);
        btnLogin = (Button) findViewById(R.id.btn_Login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Auto-generated method stub
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

                if (!email.equals("") && !password.equals("")) {
                    // Execute the AsyncLogin class
                    NetAsync(v);
                } else if (email.equals("")) {
                    Toast.makeText(context, "Email field is empty", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Password field is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Async Task to check whether internet connection is working.
     */
    private class NetCheck extends AsyncTask<String, Void, Boolean> {
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            nDialog = new ProgressDialog(LoginActivity.this);
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
        protected void onPostExecute(Boolean th) {
            if (th) {
                nDialog.dismiss();
                new AsyncLogin().execute(email, encryptedPassword);
            } else {
                nDialog.dismiss();
                //loginErrorMsg.setText("Error in Network Connection");
                Toast.makeText(context, "Error in Network Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected class AsyncLogin extends AsyncTask<String, JSONObject, Boolean> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setTitle("Contacting Servers");
            pDialog.setMessage("Logging in ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            RestAPI api = new RestAPI();
            boolean userAuth = false;
            try {
                // Call the User Authentication Method in API
                JSONObject jsonObj = api.UserAuthentication(params[0],
                        params[1]);

                //Parse the JSON Object to boolean
                JSONParser parser = new JSONParser();
                userAuth = parser.parseUserAuth(jsonObj);
                email = params[0];
            } catch (Exception e) {
                // TODO Auto-generated catch block
                Log.d("AsyncLogin", e.getMessage());
            }
            return userAuth;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // TODO Auto-generated method stub
            //Check user validity
            if (result) {
                new AsyncUserDetails().execute(email);
            } else {
                Toast.makeText(context, "Not valid email/password " + email + "   " + password, Toast.LENGTH_SHORT).show();
                pDialog.dismiss();
            }
        }
    }

    protected class AsyncUserDetails extends AsyncTask<String, Void, User> {
        private ProgressDialog uDialog;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            uDialog = new ProgressDialog(LoginActivity.this);
            uDialog.setTitle("Contacting Servers");
            uDialog.setMessage("Updating data...");
            uDialog.setIndeterminate(false);
            uDialog.setCancelable(true);
            uDialog.show();
        }
        @Override
        protected User doInBackground(String... params) {
            // TODO Auto-generated method stub
            User user = null;
            RestAPI api = new RestAPI();
            try {
                JSONObject jsonObj = api.GetUserByEmail(params[0]);
                JSONParser parser = new JSONParser();
                user = parser.parseUserDetails(jsonObj);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                Log.d("AsyncUserDetails", e.getMessage());
            }
            return user;
        }

        @Override
        protected void onPostExecute(User user) {
            // TODO Auto-generated method stub
            SharedPreferences sharedPreferences = getSharedPreferences("LoginSession", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("email", etEmail.getText().toString());
            editor.putString("firstName", user.getFirstName());
            editor.putString("lastName", user.getLastName());
            editor.putString("avatarLink", user.getAvatarLink());
            editor.commit();

            Intent i = new Intent(LoginActivity.this,
                    MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);

            Toast.makeText(context, "Welcome " + user.getFirstName() + " " + user.getLastName(), Toast.LENGTH_SHORT).show();

        }
    }

    public void NetAsync(View v) {
        new NetCheck().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
        } else if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);
    }
}
