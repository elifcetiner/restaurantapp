package tr.edu.mu.ceng.gui.restaurantapp;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import tr.edu.mu.ceng.gui.restaurantapp.R;
import tr.edu.mu.ceng.gui.restaurantapp.fragment.BasketFragment;
import tr.edu.mu.ceng.gui.restaurantapp.fragment.CategoryFragment;
import tr.edu.mu.ceng.gui.restaurantapp.fragment.DetailProductFragment;
import tr.edu.mu.ceng.gui.restaurantapp.interfaces.CategoryCallback;
import tr.edu.mu.ceng.gui.restaurantapp.interfaces.ProductDetailBarChange;
import tr.edu.mu.ceng.gui.restaurantapp.interfaces.ProductDetailCallback;

public class UserActivity extends AppCompatActivity implements
        View.OnClickListener,
        CategoryCallback,
        ProductDetailCallback,
        ProductDetailBarChange
        {


        private TextView tvActionBar;
        private ImageView imCart;
        private FragmentManager fragmentManager;
        //private SharedPref sharedPref;
        private ImageView imBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeStaffLogin);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        fragmentManager = getSupportFragmentManager();
        startFragment(new CategoryFragment());

        getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_actionbaruser);
        tvActionBar = (TextView)getSupportActionBar().getCustomView().findViewById(R.id.tvActionBar);
        imBack = (ImageView) getSupportActionBar().getCustomView().findViewById(R.id.imBack);
        imCart = (ImageView) getSupportActionBar().getCustomView().findViewById(R.id.imCart);
        imBack.setOnClickListener(this);
        imCart.setOnClickListener(this);
        tvActionBar.setText(getResources().getString(R.string.orderCategory));

        //sharedPref = new SharedPref(this, "desk_id");
        //Toast.makeText(this, sharedPref.getVariable("deskID"), Toast.LENGTH_SHORT).show();

    }

    private void startFragment(Fragment fragment){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onClick(View v) {
        if (v.equals(imCart)){
            startFragment(new BasketFragment());
            tvActionBar.setText(getString(R.string.shoppingCart) );
        }else if (v.equals(imBack)){
            startFragment(new CategoryFragment());
            imBack.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    public void call(Fragment fragment, int id) {
        Bundle bundle = new Bundle();
        bundle.putInt("id",id);
        fragment.setArguments(bundle);
        startFragment(fragment);
        tvActionBar.setText(getString(R.string.product));
    }

    @Override
    public void onStartProductDetailFragment(DetailProductFragment productDetailFragment, int id) {
        Bundle bundle = new Bundle();
        bundle.putInt("id",id);
        productDetailFragment.setArguments(bundle);
        startFragment(productDetailFragment);
    }

    @Override
    public void onChangeBar(String title) {
        tvActionBar.setText(title);
        tvActionBar.setAllCaps(true);
        imBack.setVisibility(View.VISIBLE);
    }

}
