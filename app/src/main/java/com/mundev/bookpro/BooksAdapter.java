package com.mundev.bookpro;

/**
 * Created by Nitin Tonger on 25-03-2017.
 */
import android.app.Dialog;
import android.content.Context;
import android.service.voice.VoiceInteractionSession;
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
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.MyViewHolder> {

    private Context mContext;
    private List<Books> albumList;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count,item;
        public ImageView thumbnail, overflow;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            count = (TextView) view.findViewById(R.id.count);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            overflow = (ImageView) view.findViewById(R.id.overflow);
            item=(TextView)view.findViewById(R.id.item);
        }
    }
String user_email;

    public BooksAdapter(Context mContext, List<Books> albumList,String user_email) {
        this.mContext = mContext;
        this.albumList = albumList;
        this.user_email=user_email;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_card, parent, false);

        return new MyViewHolder(itemView);
    }
    Books album;
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        album = albumList.get(position);

        holder.title.setText(album.getproduct());
        holder.count.setText("Price:"+""+album.getPrice());
        Glide.with(mContext).load(album.getImgurl()).into(holder.thumbnail);
        holder.item.setText(album.getId());
        holder.item.setVisibility(View.INVISIBLE);
        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataid = holder.item.getText().toString();
                showPopupMenu(holder.overflow);
            }
        });
    }

    private void showPopupMenu(View view) {

        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_book, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }
    String dataid;
    /**
     * Click listener for popup menu items
     */

Dialog dialog;
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
                              //  Toast.makeText(mContext, response, Toast.LENGTH_SHORT).show();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                             //   Toast.makeText(mContext, error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {

                                Map<String, String> params = new HashMap<String, String>();
                                params.put("email",user_email);
                                params.put("id",dataid);
                                return params;
                            }
                        };
                        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
                        requestQueue.add(stringRequest);
                       Toast.makeText(mContext,user_email, Toast.LENGTH_SHORT).show();
                        Toast.makeText(mContext,"Added To Cart",Toast.LENGTH_SHORT).show();
                    default:
                }
                return false;
            }
        }


    @Override
    public int getItemCount() {
        return albumList.size();
    }
}