package com.sourcepoint.gdpr_cmplibrary;

import com.sourcepoint.gdpr_cmplibrary.exception.GenericSDKException;
import com.sourcepoint.gdpr_cmplibrary.exception.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CustomJsonParser {

    static Object getObject(String key, JSONObject j, Logger logger) throws ConsentLibException {
        try {
            return j.get(key);
        } catch (JSONException e) {
            logger.error(new GenericSDKException(e, key + " missing from JSONObject"));
            throw new ConsentLibException(e, key + " missing from JSONObject");
        }
    }

    static boolean getBoolean(String key, JSONObject j, Logger logger) throws ConsentLibException {
        try {
            return !j.isNull(key) ? j.getBoolean(key) : null;
        } catch (JSONException e) {
            logger.error(new GenericSDKException(e, key + " missing from JSONObject"));
            throw new ConsentLibException(e, key + " missing from JSONObject");
        }
    }

    static int getInt(String key, JSONObject j, Logger logger) throws ConsentLibException {
        try {
            return !j.isNull(key) ? j.getInt(key) : null;
        } catch (JSONException e) {
            logger.error(new GenericSDKException(e, key + " missing from JSONObject"));
            throw new ConsentLibException(e, key + " missing from JSONObject");
        }
    }

    static String getString(String key, JSONObject j, Logger logger) throws ConsentLibException {
        try {
            return !j.isNull(key) ? j.getString(key) : null;
        } catch (JSONException e) {
            logger.error(new GenericSDKException(e, key + " missing from JSONObject"));
            throw new ConsentLibException(e, key + " missing from JSONObject");
        }
    }

    static JSONObject getJson(String key, JSONObject j, Logger logger) throws ConsentLibException {
        try {
            return j.getJSONObject(key);
        } catch (JSONException e) {
            logger.error(new GenericSDKException(e, key + " missing from JSONObject"));
            throw new ConsentLibException(e, key + " missing from JSONObject");
        }
    }

    static JSONObject getJson(String strJson, Logger logger) throws ConsentLibException {
        try {
            return new JSONObject(strJson);
        } catch (JSONException e) {
            logger.error(new GenericSDKException(e, "Not possible to convert String to Json"));
            throw new ConsentLibException(e, "Not possible to convert String to Json");
        }
    }

    static JSONArray getJArray(String key, JSONObject j, Logger logger) throws ConsentLibException {
        try {
            return j.getJSONArray(key);
        } catch (JSONException e) {
            logger.error(new GenericSDKException(e, key + " missing from JSONObject"));
            throw new ConsentLibException(e, key + " missing from JSONObject");
        }
    }

    static JSONObject getJson(int i, JSONArray jArray, Logger logger) throws ConsentLibException {
        try {
            return jArray.getJSONObject(i);
        } catch (JSONException e) {
            logger.error(new GenericSDKException(e, "Error trying to get action obj from JSONObject"));
            throw new ConsentLibException(e, "Error trying to get action obj from JSONObject");
        }
    }

    static JSONObject getJson(Map m, Logger logger) throws ConsentLibException {
        try {
            JSONObject json = new JSONObject();
            for(Object key : m.keySet()) json.put((String) key, m.get(key));
            return json;
        } catch (JSONException e) {
            logger.error(new GenericSDKException(e, "Error parsing Map to JSONObject"));
            throw new ConsentLibException(e, "Error parsing Map to JSONObject");
        }
    }

    static String getString(int i, JSONArray jArray, Logger logger) throws ConsentLibException {
        try {
            return jArray.getString(i);
        } catch (JSONException e) {
            logger.error(new GenericSDKException(e, "Error trying to get action obj from JSONObject"));
            throw new ConsentLibException(e, "Error trying to get action obj from JSONObject");
        }
    }

    static <T extends HashMap> T getHashMap(JSONObject jCustomFields, Logger logger) throws ConsentLibException {
        HashMap hMap = new HashMap<>();
        JSONArray names = jCustomFields.names();
        if (names != null){
            for(int i = 0; i < names.length(); i++) {
                String name = getString(i, names, logger);
                hMap.put(name, getObject(name, jCustomFields, logger));
            }
        }
        return (T) hMap;
    }
}
