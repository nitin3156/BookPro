package com.mundev.bookpro;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class Usercart extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayList<String> arrayList1 = new ArrayList<>();
    ArrayList<String> arrayList2 = new ArrayList<>();
    ArrayList<String> arrayList3 = new ArrayList<>();
    ArrayList<String> arrayList4=new ArrayList<>();
    public RecyclerView recyclerView;
    public Usercart()
    {

    }
    public CartAdapter adapter;
    private List<Books> albumList;
    private ProgressDialog loading;
    String user_mail;
    Context mycontext;
    TextView textView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mycontext = container.getContext();
        return inflater.inflate(R.layout.activity_usercart, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
       // Toast.makeText(getContext(),user_mail,Toast.LENGTH_SHORT).show();
      /*  FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/recyclerView = (RecyclerView)getActivity().findViewById(R.id.recycler_view1);
        albumList = new ArrayList<>();
        albumList.clear();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            user_mail= bundle.getString("st2");
        }
        adapter = new CartAdapter(mycontext, albumList,user_mail);
        swipeRefreshLayout=(SwipeRefreshLayout)getActivity().findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(mycontext, 1);
        textView=(TextView)getActivity().findViewById(R.id.text_price);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        getData();
       textView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if(arrayList1.size()>0) {
                   Intent intent = new Intent(mycontext, ConfirmOrder.class);
                   String pp = String.valueOf(total_price);
                   intent.putExtra("price", pp);
                   intent.putExtra("id", idstring);
                   startActivity(intent);
               }
               else {
                   Toast.makeText(mycontext,"Cart is Empty!!! Please add some product",Toast.LENGTH_SHORT).show();
               }
           }
       });

    }
    @Override
    public void onRefresh() {
       // int a=adapter.getItemCount();
       // adapter.notifyItemRangeRemoved(0,a);
        albumList.clear();
        total_price=0;
        recyclerView.getRecycledViewPool().clear();
       // Toast.makeText(getApplication(),String.valueOf(a),Toast.LENGTH_SHORT).show();
        recyclerView.removeAllViewsInLayout();
        arrayList.clear();arrayList1.clear();arrayList2.clear();arrayList3.clear();
        Toast.makeText(mycontext,String.valueOf(arrayList.size()),Toast.LENGTH_SHORT).show();

        getData();
    }
    public void getData() {

       // loading = ProgressDialog.show(Usercart.this,"Please wait...","Fetching...",false,false);

        String url = "http://bookpro.pe.hu/cartfetch.php?email="+user_mail;
        Toast.makeText(mycontext,"getData",Toast.LENGTH_LONG).show();
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                loading.dismiss();
                // Toast.makeText(getContext(),response,Toast.LENGTH_LONG).show();
                showJSON(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(mycontext,error.getMessage().toString(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(mycontext);
        requestQueue.add(stringRequest);
    }
    private void showJSON(String response) {
        String pro = "";
        String bar = "";
        String pr = "";
        String image = "";
        String rat = "";
        String id="";
        //Toast.makeText(getContext(),response,Toast.LENGTH_SHORT).show();
        try {
            JSONArray contacts = new JSONArray(response);
            for (int j=0;j<contacts.length();j++)
            {
                JSONObject c = contacts.getJSONObject(j);
                pro = c.getString(Config.KEY_PRODUCT);
                arrayList.add(pro);
                bar = c.getString(Config.KEY_BARCODE);
                arrayList1.add(bar);
                image=c.getString(Config.KEY_IMAGEURL);
                arrayList2.add(image);
                pr=c.getString(Config.KEY_PRICE);
                arrayList3.add(pr);
                id=c.getString("id");
                arrayList4.add(id);
                // Toast.makeText(getContext(), pro, Toast.LENGTH_SHORT).show();
            }
            prepare();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */


    /**
     * Adding few albums for testing
     */
    int total_price=0;
    String price1,idstring="";
    private void prepare() {
        for (int i = 0; i < arrayList.size(); i++) {

            String product = arrayList.get(i);
            String bar = arrayList1.get(i);
            String imgsrc = arrayList2.get(i);
            String price = arrayList3.get(i);
            price1=price;
            price1=price1.replace(",","");
            total_price=total_price+Integer.parseInt(price1);
            String id=arrayList4.get(i);
            idstring=idstring+id+" ";
            Books a = new Books(product, bar,imgsrc,price,id);
            albumList.add(a);

        }
        if(arrayList1.size()==0)
        {
            textView.setText("Cart is Empty!!! Please add some product");
        }
        else {
            textView.setText("Total price is" + " " + String.valueOf(total_price));
        }
        //total_price=0;
        adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

}