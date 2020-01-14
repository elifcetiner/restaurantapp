package tr.edu.mu.ceng.gui.restaurantapp.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import tr.edu.mu.ceng.gui.restaurantapp.R;
import tr.edu.mu.ceng.gui.restaurantapp.adapter.GridAdapterCategory;
import tr.edu.mu.ceng.gui.restaurantapp.constants.AppConstants;
import tr.edu.mu.ceng.gui.restaurantapp.interfaces.CategoryCallback;
import tr.edu.mu.ceng.gui.restaurantapp.interfaces.VolleyCallback;
import tr.edu.mu.ceng.gui.restaurantapp.model.Category;
import tr.edu.mu.ceng.gui.restaurantapp.model.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
public class CategoryFragment extends Fragment implements AdapterView.OnItemClickListener, VolleyCallback{

    private View view;
    private GridView gridView;
    private GridAdapterCategory gridAdapterCategory;
    private List<Category> categoryList;
    private CategoryCallback categoryCallback;
    private String urlCategory = AppConstants.HOST+ "/category";
    private ProgressDialog progressDialog;
    private Category category;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_category, container, false);

        categoryList = new ArrayList<>();

        Request request = new Request(getActivity(), urlCategory, com.android.volley.Request.Method.GET);
        request.requestVolley(this);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.loading_menu));
        progressDialog.show();

        gridView = (GridView) view.findViewById(R.id.gvCat);
        gridView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        categoryCallback.call(new ProductFragment(), position+1);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        categoryCallback = (CategoryCallback) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        categoryCallback = null;
    }

    @Override
    public void onSuccess(JSONObject result) {
        Log.d("MSG", result.toString());

        JSONArray jsonArray = null;
        try {
            jsonArray = result.getJSONArray("categories");
            for (int i=0; i<jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String catId = jsonObject.getString("categoryId");
                String name = jsonObject.getString("name");
                String image = jsonObject.getString("image");

                category = new Category(catId, name, image);
                categoryList.add(category);

            }
        } catch (JSONException e) {
            Log.e("MSG", e.getMessage());
        }

        gridAdapterCategory = new GridAdapterCategory(getActivity(), categoryList);
        gridView.setAdapter(gridAdapterCategory);

        progressDialog.hide();
    }

    @Override
    public void onSuccessAuth(JSONObject result) {

    }

}
