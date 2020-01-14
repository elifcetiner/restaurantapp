package tr.edu.mu.ceng.gui.restaurantapp.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.SyncStateContract;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import tr.edu.mu.ceng.gui.restaurantapp.R;
import tr.edu.mu.ceng.gui.restaurantapp.constants.AppConstants;
import tr.edu.mu.ceng.gui.restaurantapp.interfaces.VolleyCallback;
import tr.edu.mu.ceng.gui.restaurantapp.model.Request;
import tr.edu.mu.ceng.gui.restaurantapp.model.VolleyClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class StaffLoginActivity extends AppCompatActivity implements View.OnClickListener, VolleyCallback{

    private TextView tvActionBar;
    private EditText etUsername, etPassword;
    private Button btnLogin;
    private String url = AppConstants.HOST+"/login";
    private ProgressDialog progressDialog;
    private boolean loginFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeStaffLogin);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_login);

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

        getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar);
        tvActionBar = (TextView)getSupportActionBar().getCustomView().findViewById(R.id.tvActionBar);
        tvActionBar.setText(getResources().getString(R.string.staffLoginHeader));

        SharedPreferences sharedpreferences = getSharedPreferences("staff", Context.MODE_PRIVATE);
        loginFlag = sharedpreferences.getBoolean("login",false);

        if (loginFlag){
            startActivity(new Intent(this, StaffDeskActivity.class));
            //startActivity(new Intent(this, DeskOrdersActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }

    }

    @Override
    public void onClick(View v) {
        TextInputLayout tilUsername = (TextInputLayout) findViewById(R.id.TILUsername);
        TextInputLayout tilPassword = (TextInputLayout) findViewById(R.id.TILPassword);

        final String strUsername = etUsername.getText().toString();
        final String strPassword = etPassword.getText().toString();

        if (v.equals(btnLogin)){
            if (strUsername.equals("")){
                tilUsername.setError("The user name cannot be empty.");
            }else if (strPassword.equals("")){
                    tilPassword.setError("The password name cannot be empty.");
            }else{

                Request request = new Request(this, url, com.android.volley.Request.Method.POST);
                request.requestVolleyAuth(this,  strUsername,  strPassword);
                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Login ...");
                progressDialog.show();

            }
        }
    }


    @Override
    public void onSuccess(JSONObject result) {

    }

    @Override
    public void onSuccessAuth(JSONObject result) throws JSONException {

        progressDialog.hide();

        boolean getFlag = result.getBoolean("result");
        if (getFlag){
            startActivity(new Intent(this, StaffDeskActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            SharedPreferences sharedpreferences = getSharedPreferences("staff", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putBoolean("login", true);
            editor.commit();
        }else{
            Toast.makeText(this, "Could not log in.", Toast.LENGTH_SHORT).show();
        }
    }
}
