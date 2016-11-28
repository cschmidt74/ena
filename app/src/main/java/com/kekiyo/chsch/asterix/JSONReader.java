package com.kekiyo.chsch.asterix;


import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by CSean on 17.11.2016.
 */

public class JSONReader {

    static Context c;

    public JSONReader(Context context){
        c = context;
    }

    public static String JSON2String(InputStream is) throws IOException {
        String jsonData = "";
        BufferedReader br = null;

        String line;

        br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        while ((line = br.readLine()) != null) {
            jsonData += line + "\n";
        }

        br.close();

        return jsonData;
    }

    public static Map<String, List<POI>> ParsePOI() throws IOException, JSONException {
        InputStream is = c.getResources().openRawResource(R.raw.poi);
        String jsonData = JSON2String(is);
        JSONArray jar = new JSONArray(jsonData);

        List<POI> poilist_fuel = new ArrayList<POI>();
        List<POI> poilist_amenity = new ArrayList<POI>();
        List<POI> poilist_pub = new ArrayList<POI>();
        Map<String, List<POI>> poimap = new HashMap<String, List<POI>>();

        for(int i = 0; i < jar.length(); i++){
            JSONObject job = jar.getJSONObject(i);
            String type = job.getString("type");

            POI curpoi = null;


            switch (type) {
                case "fuel":
                    curpoi = new POI();
                    //set all variables
                    curpoi.type = "fuel";
                    curpoi.name = job.getString("name");
                    curpoi.address = job.getString("address");
                    curpoi.latitude = job.getDouble("lat");
                    curpoi.longitude = job.getDouble("lon");
                    curpoi.icon = null;
                    curpoi.capacity = 0;

                    //add to poilist_fuel
                    poilist_fuel.add(curpoi);
                    break;
                case "amenity":
                    curpoi = new POI();
                    //set all variables
                    curpoi.type = "amenity";
                    curpoi.name = job.getString("name");
                    curpoi.address = job.getString("address");
                    curpoi.latitude = job.getDouble("lat");
                    curpoi.longitude = job.getDouble("lon");
                    curpoi.icon = job.getString("icon");
                    curpoi.capacity = 0;

                    //add to poilist_amenity
                    poilist_amenity.add(curpoi);
                    break;
                case "pub":
                    curpoi = new POI();
                    //set all variables
                    curpoi.type = "amenity";
                    curpoi.name = job.getString("name");
                    curpoi.address = job.getString("address");
                    curpoi.latitude = job.getDouble("lat");
                    curpoi.longitude = job.getDouble("lon");
                    curpoi.icon = null;
                    curpoi.capacity = job.getInt("capacity");

                    //add to poilist_pub
                    poilist_pub.add(curpoi);
                    break;
                default:
                    // something
                    break;
            }
        }

        //add all polists to map using category. ie. poimap <- "fuel"|poilist_fuel
        poimap.put("fuel", poilist_fuel);
        poimap.put("amenity", poilist_amenity);
        poimap.put("pub", poilist_pub);

        return poimap;
    }

}
