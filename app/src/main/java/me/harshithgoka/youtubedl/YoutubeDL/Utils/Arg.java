package me.harshithgoka.youtubedl.YoutubeDL.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by harshithg on 17/1/18.
 */



public class Arg extends JSONObject {
    public static final String VAL = "val";
    public Arg() {
        super();
    }

    public Arg(String s) {
        super();
        try {
            this.put(VAL, s);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
    public Arg(int i) {
        super();
        try {
            this.put(VAL, i);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Arg(JSONArray a) {
        super();
        try {
            this.put(VAL, a);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
}