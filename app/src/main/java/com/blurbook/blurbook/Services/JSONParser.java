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

    public boolean parseUserExisted(JSONObject object){
        boolean userExisted = false;
        try{
            userExisted = object.getBoolean("Value");
        }catch (JSONException e){
            Log.d("JSONParser => parseUserExisted", e.getMessage());
        }

        return userExisted;
    }

    public User parseUserDetails(JSONObject object)
    {
        User user=new User();

        try {
            JSONObject jsonObj=object.getJSONArray("Value").getJSONObject(0);

            user.setFirstName(jsonObj.getString("FName"));
            user.setLastName(jsonObj.getString("LName"));

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Log.d("JSONParser => parseUserDetails", e.getMessage());
        }

        return user;

    }



}