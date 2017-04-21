package com.mundev.bookpro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class ConfirmOrder extends AppCompatActivity implements View.OnClickListener {
    String st1,st2;
    EditText name ,price , address, phone;
    Button button;
    public static final String REGISTER_URL ="http://bookpro.pe.hu/ship.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        Intent intent=getIntent();
        st1 = intent.getStringExtra("price");
        st2 = intent.getStringExtra("id");
        name=(EditText)findViewById(R.id.edit_name);
        price=(EditText)findViewById(R.id.price);
        address=(EditText)findViewById(R.id.add);
        phone=(EditText)findViewById(R.id.phone);
        price.setText(st1);
        button=(Button)findViewById(R.id.proceed);
        button.setOnClickListener(this);
    }
    public void senddata()
    {
        final String name1=name.getText().toString();
        final String add=address.getText().toString();
        final String ph=String.valueOf(phone.getText().toString());
        StringRequest stringRequest=new StringRequest(Request.Method.POST, REGISTER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String, String>();
                params.put("name",name1);
                params.put("address",add);
                params.put("phone",ph);
                params.put("product",st2);
                params.put("price",st1);
                params.put("status","Processing");
                return  params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    @Override
    public void onClick(View view) {
        if(view==button)
        {
            senddata();
        }
    }
}
