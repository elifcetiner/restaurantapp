package tr.edu.mu.ceng.gui.restaurantapp.interfaces;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public interface VolleyCallback {

    void onSuccess(JSONObject result);
    void onSuccessAuth(JSONObject result) throws JSONException;

}
