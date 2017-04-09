package com.mundev.bookpro;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import xyz.danoz.recyclerviewfastscroller.vertical.VerticalRecyclerViewFastScroller;

/**
 * Created by Nitin Tonger on 26-03-2017.
 */

public class BookGenreFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    public BookGenreFragment() {

    }
    private SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayList<String> arrayList1 = new ArrayList<>();
    ArrayList<String> arrayList2 = new ArrayList<>();
    ArrayList<String> arrayList3 = new ArrayList<>();
    ArrayList<String> arrayList4=new ArrayList<>();
    public RecyclerView recyclerView;
    public BooksAdapter adapter;
    private List<Books> albumList;
    private ProgressDialog loading;
    public Context mycontext;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mycontext = container.getContext();
        return inflater.inflate(R.layout.activity_product_view, container, false);


    }
    String user_email;
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView = (RecyclerView)getActivity().findViewById(R.id.recycler_view);
        //recyclerView = (ShimmerRecyclerView) getActivity().findViewById(R.id.recycler_view);
        albumList = new ArrayList<>();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            user_email= bundle.getString("st2");
        }
        Toast.makeText(mycontext,user_email,Toast.LENGTH_SHORT).show();
        adapter = new BooksAdapter(mycontext, albumList,user_email);
        swipeRefreshLayout=(SwipeRefreshLayout)getActivity().findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(mycontext, 1);
        VerticalRecyclerViewFastScroller fastScroller = (VerticalRecyclerViewFastScroller)getActivity().findViewById(R.id.fast_scroller);
        fastScroller.setRecyclerView(recyclerView);
        swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#004d40"));
        recyclerView.setOnScrollListener(fastScroller.getOnScrollListener());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        //getData();
        onRefresh();

    }
    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        int a=adapter.getItemCount();
        adapter.notifyItemRangeRemoved(0,a);
        albumList.clear();
        recyclerView.getRecycledViewPool().clear();
        //Toast.makeText(mycontext,String.valueOf(a),Toast.LENGTH_SHORT).show();
        recyclerView.removeAllViewsInLayout();
        arrayList.clear();arrayList1.clear();arrayList2.clear();arrayList3.clear();
        //Toast.makeText(mycontext,String.valueOf(arrayList.size()),Toast.LENGTH_SHORT).show();

        getData();
    }
    private void getData() {

        //loading = ProgressDialog.show(mycontext,"Please wait...","Fetching...",false,false);

        String url = "http://bookpro.pe.hu/fetch.php";
       // Toast.makeText(mycontext,"getData",Toast.LENGTH_LONG).show();
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               // loading.dismiss();
                // Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
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
       // Toast.makeText(mycontext,response,Toast.LENGTH_SHORT).show();
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
                // Toast.makeText(getApplicationContext(), pro, Toast.LENGTH_SHORT).show();
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
    private void prepare() {
           for (int i = 0; i < arrayList.size(); i++) {

            String product = arrayList.get(i);
            String bar = arrayList1.get(i);
            String imgsrc = arrayList2.get(i);
            String price = arrayList3.get(i);
            String id=arrayList4.get(i);
            Books a = new Books(product, bar,imgsrc,price,id);
            albumList.add(a);

        }

        adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }
public void mera()
{
    onRefresh();
}

}