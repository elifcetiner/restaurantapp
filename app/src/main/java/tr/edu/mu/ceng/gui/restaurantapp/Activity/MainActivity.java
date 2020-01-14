package tr.edu.mu.ceng.gui.restaurantapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
package com.yusufcakal.ra.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cengalabs.flatui.views.FlatButton;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;
import static com.google.android.gms.internal.zzt.TAG;

public class MainActivity extends Activity implements View.OnClickListener{

    private Typeface tfBold, tfRegular;
    private FlatButton btnStaff, btnOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tfBold = Typeface.createFromAsset(getAssets(), "fonts/lato/Lato-Bold.ttf");
        tfRegular = Typeface.createFromAsset(getAssets(), "fonts/lato/Lato-Regular.ttf");

        btnStaff = (FlatButton) findViewById(R.id.btnStaff);
        btnOrder = (FlatButton) findViewById(R.id.btnOrder);
        btnStaff.setOnClickListener(this);
        btnOrder.setOnClickListener(this);
        btnStaff.setTypeface(tfRegular);
        btnOrder.setTypeface(tfRegular);
/*
        HashMap<String, String> params = new HashMap();
        params.put("username", "admin");
        params.put("password", "admin");
        JSONObject jsonObject = new JSONObject(params);
        VolleyClient.getInstance(this).addToRequestQueue( new JsonObjectRequest("http://192.168.1.38:8080/login",
                jsonObject,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject o) {
                        Log.d(getPackageName(), o.toString());
                    }
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(getPackageName(), "Error: " +  error.toString());
            }
        } ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                return params;
            }
        });*/

    }

    @Override
    public void onClick(View v) {
        if (v.equals(btnStaff)){
            startActivity(new Intent(this, StaffLoginActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }else if (v.equals(btnOrder)){
            //Intent ıntent = new Intent(this, CamActivity.class);
            //startActivity(ıntent);
            startActivity(new Intent(getApplicationContext(), UserActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            SharedPreferences sharedpreferences = getSharedPreferences("userBasket", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putBoolean("user", true);
            editor.commit();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //TODO:App is finish
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
