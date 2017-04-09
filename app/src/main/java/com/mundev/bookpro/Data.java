package com.mundev.bookpro;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Data extends Fragment {
    private EditText editTextId;
    private Button buttonGet;
    private TextView textViewResult,textViewResult1;
    public ImageView imageView;
    private ProgressDialog loading;
    String scanContent;
    Context mycontext;
    public TextView title, count,item;
    public ImageView thumbnail, overflow;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mycontext = container.getContext();
        return inflater.inflate(R.layout.activity_data, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            scanContent = bundle.getString("scanContent");
        }
        //scanContent = intent.getStringExtra("scanContent");
        title = (TextView)getActivity().findViewById(R.id.title);
        count = (TextView)getActivity().findViewById(R.id.count);
        thumbnail = (ImageView)getActivity().findViewById(R.id.thumbnail);
        overflow = (ImageView)getActivity().findViewById(R.id.overflow);
        item=(TextView)getActivity().findViewById(R.id.item);
        getData();
    }


    private void getData() {

        loading = ProgressDialog.show(mycontext,"Please wait...","Fetching...",false,false);

        String url = Config.DATA_URL+scanContent;

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();
                showJSON(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       // Toast.makeText(mycontext,error.getMessage().toString(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(mycontext);
        requestQueue.add(stringRequest);
    }
    String pro="";
    String bar="";
    String pr = "";
    String id="";
    String image = "";
    private void showJSON(String response){

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);
            JSONObject bookpro_json = result.getJSONObject(0);
            pro = bookpro_json.getString(Config.KEY_PRODUCT);
            bar = bookpro_json.getString(Config.KEY_BARCODE);
            pr = bookpro_json.getString(Config.KEY_PRICE);
            image=bookpro_json.getString(Config.KEY_IMAGEURL);
            id=bookpro_json.getString("id");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Glide.with(mycontext).load(image)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(thumbnail);
        title.setText(pro);
        count.setText(pr);
        overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(overflow);
            }
        });
        Toast.makeText(mycontext,scanContent,Toast.LENGTH_SHORT).show();
    }
    private void showPopupMenu(View view) {

        PopupMenu popup = new PopupMenu(mycontext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_book, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }


        @Override
        public boolean onMenuItemClick ( final MenuItem menuItem){
            switch (menuItem.getItemId()) {
                case R.id.add2cart:
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://bookpro.pe.hu/cart.php", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(mycontext, response, Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(mycontext, error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {

                            Map<String, String> params = new HashMap<String, String>();
                            params.put("email","nkt3156@gmail.com");
                            params.put("id",id);
                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(mycontext);
                    requestQueue.add(stringRequest);
                    //Toast.makeText(mycontext, dataid, Toast.LENGTH_SHORT).show();
                    Toast.makeText(mycontext,"Added To Cart",Toast.LENGTH_SHORT).show();
                default:
            }
            return false;
        }
    }


}