package tr.edu.mu.ceng.gui.restaurantapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import tr.edu.mu.ceng.gui.restaurantapp.R;
import tr.edu.mu.ceng.gui.restaurantapp.adapter.GridAdapterDesk;
import tr.edu.mu.ceng.gui.restaurantapp.adapter.OrderHistoryAdapter;
import tr.edu.mu.ceng.gui.restaurantapp.constants.AppConstants;
import tr.edu.mu.ceng.gui.restaurantapp.interfaces.VolleyCallback;
import tr.edu.mu.ceng.gui.restaurantapp.model.Desk;
import tr.edu.mu.ceng.gui.restaurantapp.model.OrderHistory;
import tr.edu.mu.ceng.gui.restaurantapp.model.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class DeskOrdersActivity extends AppCompatActivity implements
        VolleyCallback,
        AdapterView.OnItemClickListener{

    private TextView tvActionBar;
    private ListView lvOrderHistory;
    private String orderId;
    private String url = AppConstants.HOST+ "/deskOrders/";
    private OrderHistoryAdapter orderHistoryAdapter;
    private List<OrderHistory> orderHistoryList;
    private OrderHistory orderHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeStaffLogin);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desk_orders);

        orderHistoryList = new ArrayList<>();
        orderHistoryAdapter = new OrderHistoryAdapter(this, orderHistoryList);

        getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar);
        tvActionBar = (TextView)getSupportActionBar().getCustomView().findViewById(R.id.tvActionBar);
        tvActionBar.setText(getResources().getText(R.string.orderHistory));

        lvOrderHistory = (ListView) findViewById(R.id.lvOrderHistory);
        lvOrderHistory.setOnItemClickListener(this);

        orderId = getIntent().getStringExtra("orderId");
        url+=orderId;

        Log.d("MSG", "URL: "+url);
        Request request = new Request(this, url, com.android.volley.Request.Method.GET);
        request.requestVolley(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent ıntent = new Intent(this, DeskBasketActivity.class);
        ıntent.putExtra("orderId",orderHistoryList.get(position).getOrderId());
        ıntent.putExtra("status",orderHistoryList.get(position).getStatus());
        startActivity(ıntent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    public void onSuccess(JSONObject result) {
        Log.d("MSG", result.toString());
        try {
            JSONArray deskList = result.getJSONArray("deskOrders");

            for (int i=0; i<deskList.length(); i++){
                JSONObject desk = (JSONObject) deskList.get(i);

                int orderId = desk.getInt("orderId"); // changed from 'orderID'
                int status = desk.getInt("status");
                String date = desk.getString("date"); // tempId gelicek
                Double totalPrice = desk.getDouble("total"); // tempId gelicek

                orderHistory = new OrderHistory(orderId, status, date, totalPrice);
                orderHistoryList.add(orderHistory);

            }

            lvOrderHistory.setAdapter(orderHistoryAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccessAuth(JSONObject result) throws JSONException {

    }

}
