package com.mundev.bookpro;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.mxn.soul.flowingdrawer_core.ElasticDrawer;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;


public class Home extends AppCompatActivity
        implements View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener {
    ImageView imageView;
    TextView name, email;
    FlowingDrawer mDrawer;
    DrawerLayout mDrawerLayout;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 007;
    String st1, st2;
    Animation animation;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        animation= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.anim_zoomin);

        // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        Intent intent = getIntent();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        String personPhotoUrl = intent.getStringExtra("personPhotoUrl");
        st1 = intent.getStringExtra("name");
        st2 = intent.getStringExtra("emailp");
       // FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
       /* fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#004d40")));*/
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        imageView = (ImageView) header.findViewById(R.id.imageView);
        Glide.with(getApplicationContext()).load(personPhotoUrl)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
        name = (TextView) header.findViewById(R.id.nav_name);
        email = (TextView) header.findViewById(R.id.nav_email);
        name.setText(st1);
        email.setText(st2);
        mDrawer = (FlowingDrawer) findViewById(R.id.drawerlayout);
        mDrawer.setTouchMode(ElasticDrawer.TOUCH_MODE_BEZEL);
        mDrawer.setOnDrawerStateChangeListener(new ElasticDrawer.OnDrawerStateChangeListener() {
            @Override
            public void onDrawerStateChange(int oldState, int newState) {
                if (newState == ElasticDrawer.STATE_CLOSED) {
                    Log.i("MainActivity", "Drawer STATE_CLOSED");
                }
            }

            @Override
            public void onDrawerSlide(float openRatio, int offsetPixels) {
                Log.i("MainActivity", "openRatio=" + openRatio + " ,offsetPixels=" + offsetPixels);
            }
        });

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        Bundle bundle = new Bundle();
        bundle.putString("st2",st2);
        Toast.makeText(getApplicationContext(),st2,Toast.LENGTH_SHORT).show();
        fragmentManager = getFragmentManager();
        fragment = new BookGenreFragment();
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction()
                .replace(R.id.frame, fragment)
                .commit();
        setupToolbar();
        setupMenu();
    }

    @Override
    public void onBackPressed() {
        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //if (drawer.isDrawerOpen(GravityCompat.START)) {
        //    drawer.closeDrawer(GravityCompat.START);
        //} else {
        //  super.onBackPressed();
        //}
        if (mDrawer.isMenuVisible()) {
            mDrawer.closeMenu();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {

            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        }
                    });

            return true;
        } else if (id == R.id.cartmenu) {
            mDrawer.closeMenu();
            Bundle bundle = new Bundle();
            bundle.putString("st2", st2);
            Toast.makeText(getApplicationContext(), st2, Toast.LENGTH_SHORT).show();
            Usercart fragment = new Usercart();
            fragment.setArguments(bundle);
            fragmentManager.beginTransaction().replace(R.id.frame, fragment).commit();
        }

        return super.onOptionsItemSelected(item);
    }

    Fragment fragment;
    FragmentManager fragmentManager;
    com.google.zxing.integration.android.IntentIntegrator scanIntegrator;
    public void setupMenu() {
        // Handle navigation view item clicks here.
        fragmentManager = getFragmentManager();
        scanIntegrator = new com.google.zxing.integration.android.IntentIntegrator(this);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == R.id.barcode) {
                    scanIntegrator.initiateScan();
                    mDrawer.closeMenu();

                } else if (id == R.id.books) {
                    mDrawer.closeMenu();
                    Bundle bundle = new Bundle();
                    bundle.putString("st2", st2);
                    Toast.makeText(getApplicationContext(), st2, Toast.LENGTH_SHORT).show();
                    BookGenreFragment fragment = new BookGenreFragment();
                    fragment.setArguments(bundle);
                    fragmentManager.beginTransaction().replace(R.id.frame, fragment).commit();

                }

                else if(id==R.id.cart)
                {
                    Toast.makeText(getApplicationContext(),"afasf"+st2,Toast.LENGTH_SHORT).show();
                    mDrawer.closeMenu();
                    Bundle bundle = new Bundle();
                    bundle.putString("st2", st2);
                    Toast.makeText(getApplicationContext(), st2, Toast.LENGTH_SHORT).show();
                    Usercart fragment = new Usercart();
                    fragment.setArguments(bundle);
                    fragmentManager.beginTransaction().replace(R.id.frame, fragment).commit();
                }
                //fragmentManager.commit();
                return false;
            }
        }) ;






        // DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        // drawer.closeDrawer(GravityCompat.START);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            String scanContent = scanningResult.getContents();
            //String scanFormat = scanningResult.getFormatName();
            Toast.makeText(getApplicationContext(), scanContent, Toast.LENGTH_SHORT).show();
            Bundle bundle = new Bundle();
            bundle.putString("scanContent", scanContent);
            bundle.putString("st2",st2);
            Data fragment = new Data();
            fragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.frame, fragment)
                    .commit();
        } else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    protected void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // toolbar.setNavigationIcon(R.drawable.ic_menu_white);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawer.toggleMenu();
            }
        });
    }

}
