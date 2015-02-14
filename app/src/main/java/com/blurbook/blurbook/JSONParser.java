package com.blurbook.blurbook;

import android.util.Log;

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

    public boolean parseUserAuth(JSONObject object)
    {     boolean userAtuh=false;
        try {
            userAtuh= object.getBoolean("Value");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Log.d("JSONParser => parseUserAuth", e.getMessage());
        }

        return userAtuh;
    }

}