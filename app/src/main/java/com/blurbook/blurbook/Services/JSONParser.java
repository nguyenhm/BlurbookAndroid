package com.blurbook.blurbook.Services;

import android.util.Log;

import com.blurbook.blurbook.Models.User;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Hoang Nguyen on 2/12/2015.
 */
public class JSONParser {

    public JSONParser()
    {
        super();
    }

    public boolean parseUserAuth(JSONObject object) {
        boolean userAtuh = false;
        try {
            userAtuh = object.getBoolean("Value");
        } catch (JSONException e) {
            Log.d("JSONParser => parseUserAuth", e.getMessage());
        }

        return userAtuh;
    }

    public User parseUserDetails(JSONObject object)
    {
        User newUser = new User();

        try {
            JSONObject jsonObj=object.getJSONArray("Value").getJSONObject(0);

            newUser.setFirstName(jsonObj.getString("firstName"));
            newUser.setLastName(jsonObj.getString("lastName"));

        } catch (JSONException e) {
            Log.d("JSONParser => parseUserDetails", e.getMessage());
        }

        return newUser;

    }

}