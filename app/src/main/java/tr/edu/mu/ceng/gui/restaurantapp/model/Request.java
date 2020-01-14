package tr.edu.mu.ceng.gui.restaurantapp.model;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import tr.edu.mu.ceng.gui.restaurantapp.interfaces.ChangeDeskStatus;
import tr.edu.mu.ceng.gui.restaurantapp.interfaces.DeleteCallback;
import tr.edu.mu.ceng.gui.restaurantapp.interfaces.DeskList;
import tr.edu.mu.ceng.gui.restaurantapp.interfaces.VolleyCallback;
import tr.edu.mu.ceng.gui.restaurantapp.interfaces.VolleyTemp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Request {

    private RequestQueue requestQueue;
    private Context context;
    private String url;
    private int requestMethod;

    public Request(Context context, String url, int requestMethod){
        Log.d(context.getPackageName(), "URL: "+ url);
        this.url = url;
        this.context = context;
        this.requestMethod = requestMethod;
        requestQueue = VolleyClient.getInstance(context).getRequestQueue();
    }

    public void requestVolley(final VolleyCallback callback){

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(requestMethod, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("MSG", error.toString());
            }
        });

        requestQueue.add(jsonObjectRequest);

    }

    public void requestDeleteBasket(final DeleteCallback callback, final int basketId){

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(requestMethod, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callback.deleteResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("MSG", error.toString());
            }
        }){
            @Override
            public byte[] getBody() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("basketId", String.valueOf(basketId));
                return new JSONObject(params).toString().getBytes();
            }
        };

        requestQueue.add(jsonObjectRequest);

    }

    public void requestVolleyAuth(final VolleyCallback callback, final String username, final String password){
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", username);
        params.put("password", password);

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(requestMethod, url,
                new JSONObject(params),
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    callback.onSuccessAuth(response);
                } catch (JSONException e) {
                    Log.e("MSG", e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, String.valueOf(error), Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                return params;
            }

            /*@Override
            public byte[] getBody() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", password);
                return new JSONObject(params).toString().getBytes();
            }*/
        };

        requestQueue.add(jsonObjectRequest);

    }

    public void requestVolleyDeskList(final DeskList deskList){

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(requestMethod, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                deskList.getDesk(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, String.valueOf(error), Toast.LENGTH_SHORT).show();
                Log.e("MSG", error.toString());
            }
        });

            requestQueue.add(jsonObjectRequest);

    }

    public void requestVolleyDeskList(final ChangeDeskStatus changeDeskStatus, final int orderId, final int status){

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(requestMethod, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                changeDeskStatus.changeStatus(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, String.valueOf(error), Toast.LENGTH_SHORT).show();
                Log.e("MSG", error.toString());
            }
        }){
            @Override
            public byte[] getBody() {
                HashMap<String, Integer> params = new HashMap<String, Integer>();
                params.put("orderId", orderId);
                params.put("status", status);
                return new JSONObject(params).toString().getBytes();
            }
        };

        requestQueue.add(jsonObjectRequest);

    }

    public void requestVolleyTempDesk(final VolleyTemp callback, final int deskId){

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(requestMethod, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callback.getTempId(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, String.valueOf(error), Toast.LENGTH_SHORT).show();
                Log.e("MSG", error.toString());
            }
        }){
            @Override
            public byte[] getBody() {
                HashMap<String, Integer> params = new HashMap<String, Integer>();
                params.put("deskid", deskId);
                return new JSONObject(params).toString().getBytes();
            }
        };

        requestQueue.add(jsonObjectRequest);

    }

}

