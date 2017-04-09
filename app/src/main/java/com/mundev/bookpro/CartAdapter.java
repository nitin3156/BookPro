package com.mundev.bookpro;

/**
 * Created by Nitin Tonger on 27-03-2017.
 */
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.service.voice.VoiceInteractionSession;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.mundev.bookpro.Usercart;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> implements GoogleApiClient.OnConnectionFailedListener{
    private Context mContext;
    private List<Books> albumList;
    public CartAdapter adapter;
    private GoogleApiClient mGoogleApiClient;
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count,item;
        public ImageView thumbnail, overflow;
        public TextView textView;
        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            count = (TextView) view.findViewById(R.id.count);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            overflow = (ImageView) view.findViewById(R.id.overflow);
            item=(TextView)view.findViewById(R.id.item);

        }
    }
String mail;

    public CartAdapter(Context mContext, List<Books> albumList,String mail) {
        this.mContext = mContext;
        this.albumList = albumList;
        this.adapter = this;
        this.mail=mail;
    }
/*AlertDialog.Builder alertDialog = new AlertDialog.Builder(AlertDialogActivity.this);

    // Setting Dialog Title
    alertDialog.setTitle("Confirm Delete...");

    // Setting Dialog Message
    alertDialog.setMessage("Are you sure you want delete this?");

    // Setting Icon to Dialog
    alertDialog.setIcon(R.drawable.delete);

    // Setting Positive "Yes" Button
    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog,int which) {

            // Write your code here to invoke YES event
            Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
        }
    });

    // Setting Negative "NO" Button
    alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            // Write your code here to invoke NO event
            Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
            dialog.cancel();
        }
    });

    // Showing Alert Message
    alertDialog.show();
    */
    @Override
    public CartAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclercart, parent, false);

        return new CartAdapter.MyViewHolder(itemView);
    }
    Books album;
    String dataid;
    String url="http://bookpro.pe.hu/removecart.php?id=";
    @Override
    public void onBindViewHolder(final CartAdapter.MyViewHolder holder, final int position) {
        album = albumList.get(position);
        holder.title.setText(album.getproduct());
        holder.count.setText("Price:"+""+album.getPrice());
        //holder.textView.setText("ITEM NO:"+""+String.valueOf(position+1));

        Glide.with(mContext).load(album.getImgurl()).into(holder.thumbnail);
        holder.item.setText(album.getId());
        //holder.item.setVisibility(View.INVISIBLE);

       holder.overflow.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
              dataid=holder.item.getText().toString();
               url=url+dataid;
               Toast.makeText(mContext,url,Toast.LENGTH_SHORT).show();
               StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
                   @Override
                   public void onResponse(String response) {

                   }
               },
                       new Response.ErrorListener() {
                           @Override
                           public void onErrorResponse(VolleyError error) {
                           }
                       });
               RequestQueue requestQueue = Volley.newRequestQueue(mContext);
               requestQueue.add(stringRequest);
               url=url.replace(dataid,"");
               //Toast.makeText(mContext, dataid, Toast.LENGTH_SHORT).show();
              // Toast.makeText(mContext,"Item Removed",Toast.LENGTH_SHORT).show();

               //((Usercart)mContext).finish();
               try {
                   Thread.sleep(1000);

                   Home act = (Home) mContext;
                   FragmentManager fragmentManager = act.getFragmentManager();
                   Usercart fragment = new Usercart();
                   Bundle bundle = new Bundle();
                   bundle.putString("st2", mail);
                   fragment.setArguments(bundle);
                   FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                   fragmentTransaction.replace(R.id.frame, fragment);
                   fragmentTransaction.detach(fragment);
                   fragmentTransaction.attach(fragment);
                   fragmentTransaction.commit();
               }
               catch (Exception e)
               {
                   Toast.makeText(mContext,e.toString(),Toast.LENGTH_SHORT).show();
               }
               }
       });
    }



    @Override
    public int getItemCount() {
        return albumList.size();
    }
}
