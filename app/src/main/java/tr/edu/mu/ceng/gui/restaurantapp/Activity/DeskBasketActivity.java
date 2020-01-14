package tr.edu.mu.ceng.gui.restaurantapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cengalabs.flatui.views.FlatButton;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnItemClickListener;
import tr.edu.mu.ceng.gui.restaurantapp.R;
import tr.edu.mu.ceng.gui.restaurantapp.adapter.DeskOrderAdapter;
import tr.edu.mu.ceng.gui.restaurantapp.constants.AppConstants;
import tr.edu.mu.ceng.gui.restaurantapp.interfaces.ChangeDeskStatus;
import tr.edu.mu.ceng.gui.restaurantapp.interfaces.DeleteCallback;
import tr.edu.mu.ceng.gui.restaurantapp.interfaces.DeleteCallback;
import tr.edu.mu.ceng.gui.restaurantapp.interfaces.DeskList;
import tr.edu.mu.ceng.gui.restaurantapp.model.Product;
import tr.edu.mu.ceng.gui.restaurantapp.model.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DeskBasketActivity extends AppCompatActivity implements
        DeleteCallback,
        AdapterView.OnItemLongClickListener,
        AdapterView.OnItemClickListener,
        DeskList,
        View.OnClickListener,
        ChangeDeskStatus
{

    private TextView tvActionBar;
    private int orderId, status;
    private ListView lvBasket;
    private DeskOrderAdapter deskOrderAdapter;
    private List<Product> productList;
    private String url = AppConstants.HOST+ "/baskets/";
    private String urlChangeStatus = AppConstants.HOST+ "/changestatus";
    private String urlDeleteBasket = AppConstants.HOST+ "/deletebasket";
    private String name, image;
    private int piece, basketID;
    private double price;
    private Product product;
    private FlatButton btnOrderVerify;
    private SweetAlertDialog sweetAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeStaffLogin);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desk_basket);

        lvBasket= (ListView) findViewById(R.id.lvBasket);
        lvBasket.setOnItemLongClickListener(this);
        lvBasket.setOnItemClickListener(this);
        productList = new ArrayList<>();

        btnOrderVerify = (FlatButton) findViewById(R.id.btnOrderVerify);
        btnOrderVerify.setOnClickListener(this);

        getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar);
        tvActionBar = (TextView)getSupportActionBar().getCustomView().findViewById(R.id.tvActionBar);
        tvActionBar.setText(getResources().getString(R.string.staffDeskBasketHeader));
        tvActionBar.setTextAppearance(this, android.R.style.TextAppearance_Large);
        tvActionBar.setTextColor(Color.WHITE);

        orderId = getIntent().getIntExtra("orderId",-1);
        status = getIntent().getIntExtra("status",-1);
        url+=orderId;

        if (status == 0){
            btnOrderVerify.setBackgroundColor(getResources().getColor(R.color.status0));
            btnOrderVerify.setText("NO ORDER");
            btnOrderVerify.setClickable(false);
        }else if (status == 1){
            btnOrderVerify.setBackgroundColor(getResources().getColor(R.color.status1));
            btnOrderVerify.setText("ORDER CONFIRM");
            btnOrderVerify.setClickable(true);
        }else{
            btnOrderVerify.setBackgroundColor(getResources().getColor(R.color.status2));
            btnOrderVerify.setText("ORDER APPROVED");
            btnOrderVerify.setClickable(false);
        }

        Request request = new Request(this, url, com.android.volley.Request.Method.GET);
        request.requestVolleyDeskList(this);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, StaffDeskActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    public void getDesk(JSONObject result) {
        Log.d("MSG", result.toString());
        try {
            JSONArray desks = result.getJSONArray("baskets");
            for (int i=0; i<desks.length(); i++){
                JSONObject desk = (JSONObject) desks.get(i);
                name = desk.getString("name");
                image = desk.getString("image");
                piece = desk.getInt("piece");
                price = desk.getDouble("total");
                basketID = desk.getInt("basketId"); // changed from 'basketID'

                product = new Product(piece, name, image, price, basketID);
                productList.add(product);

            }

            deskOrderAdapter = new DeskOrderAdapter(this, productList);
            lvBasket.setAdapter(deskOrderAdapter);

        } catch (JSONException e) {
            Log.e("MSG", e.getMessage());
        }

    }

    @Override
    public void onClick(View v) {
        if (v.equals(btnOrderVerify)){
            SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);
            sweetAlertDialog.setTitleText("APPROVED");
            sweetAlertDialog.setContentText("Order Confirmed");
            sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            Request request = new Request(getApplicationContext(), urlChangeStatus, com.android.volley.Request.Method.POST);
                            request.requestVolleyDeskList(DeskBasketActivity.this, Integer.valueOf(orderId), 2);
                        }
                    });
            sweetAlertDialog.show();
        }
    }

    @Override
    public void changeStatus(JSONObject result) {
        try {
            boolean flag = result.getBoolean("result");
            if (flag){
                startActivity(new Intent(this, StaffDeskActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        } catch (JSONException e) {
            Log.e("MSG", e.getMessage());
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

        basketID = productList.get(position).getBasketID();
        sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialog.setTitleText("DELETE PRODUCT?");
        sweetAlertDialog.setContentText("Remove product from order?");
        sweetAlertDialog.setConfirmText("Delete");
        sweetAlertDialog.setCancelText("Close");
        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                try {
                    Log.d("MSG", "Delete BasketID: " + basketID + ", URL: " + urlDeleteBasket);
                    Request request = new Request(getApplicationContext(), urlDeleteBasket, com.android.volley.Request.Method.POST);
                    request.requestDeleteBasket(DeskBasketActivity.this, productList.get(position).getBasketID());
                }catch (Exception e){
                    Log.e("MSG", e.getMessage());
                }

            }
        });
        sweetAlertDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
            }
        });
        sweetAlertDialog.show();

        return true;
    }

    @Override
    public void deleteResponse(JSONObject jsonObject) {
        try {
            boolean flag = jsonObject.getBoolean("result");
            if (flag){
                sweetAlertDialog.dismiss();
                for (Product product : productList) {
                    if (product.getBasketID() == basketID){
                        productList.remove(product);
                        deskOrderAdapter = new DeskOrderAdapter(getApplicationContext(), productList);
                        lvBasket.setAdapter(deskOrderAdapter);
                    }
                }
            }
        } catch (Exception e) {
            Log.e("MSG", e.getMessage());
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        List<Product> products = new ArrayList<>();
        final Product product = productList.get(position);
        products.add(product);
        DeskOrderAdapter deskOrderAdapter = new DeskOrderAdapter(this, products);

        DialogPlus dialog = DialogPlus.newDialog(this)
                .setAdapter(deskOrderAdapter)
                .setFooter(R.layout.deskorder_footer)
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                        Toast.makeText(DeskBasketActivity.this, productList.get(position).getPiece()+"", Toast.LENGTH_SHORT).show();
                    }
                })
                .setExpanded(true)  // This will enable the expand feature, (similar to android L share dialog)
                .create();
        dialog.show();

        Button btnIncrementPiece = (Button) dialog.getFooterView().findViewById(R.id.btnIncrementPiece);
        Button btnDecrementPiece = (Button) dialog.getFooterView().findViewById(R.id.btnDecrementPiece);

        btnIncrementPiece.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                product.incrementerPiece();
                Toast.makeText(DeskBasketActivity.this, product.getPiece()+"", Toast.LENGTH_SHORT).show();
            }
        });

        btnDecrementPiece.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                product.decrementerPiece();
                Toast.makeText(DeskBasketActivity.this, product.getPiece()+"", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
