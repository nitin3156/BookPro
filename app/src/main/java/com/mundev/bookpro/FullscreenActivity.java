package com.mundev.bookpro;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.drive.query.internal.LogicalFilter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class FullscreenActivity extends AppCompatActivity implements SurfaceHolder.Callback{
       private MediaPlayer mp = null;
    Button button;
    SurfaceView mSurfaceView=null;
    ProgressDialog progressDialog;
    EditText email,pass;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);

          setContentView(R.layout.activity_fullscreen);

          mp = new MediaPlayer();
          mSurfaceView = (SurfaceView) findViewById(R.id.surface);
          mSurfaceView.getHolder().addCallback(this);
          email=(EditText)findViewById(R.id.editText);
          pass=(EditText)findViewById(R.id.editText2);
          progressDialog=new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();
          button=(Button)findViewById(R.id.button);
          button.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  button_submit();
              }
          });
      }
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        String filename = "android.resource://" + this.getPackageName() + "/raw/videoplayback";

        mp = new MediaPlayer();
        try { mp.setDataSource(this, Uri.parse(filename)); } catch (Exception e) {}
        try { mp.prepare(); } catch (Exception e) {}
        //Get the dimensions of the video
        int videoWidth = mp.getVideoWidth();
        int videoHeight = mp.getVideoHeight();

        //Get the width of the screen
        int screenWidth = getWindowManager().getDefaultDisplay().getWidth();

        //Get the SurfaceView layout parameters
        android.view.ViewGroup.LayoutParams lp = mSurfaceView.getLayoutParams();

        //Set the width of the SurfaceView to the width of the screen
        lp.width = screenWidth;

        //Set the height of the SurfaceView to match the aspect ratio of the video
        //be sure to cast these as floats otherwise the calculation will likely be 0
        lp.height = (int) (((float)videoHeight / (float)videoWidth) * (float)screenWidth);

        //Commit the layout parameters
        mSurfaceView.setLayoutParams(lp);

        //Start video
        mp.setDisplay(surfaceHolder);
        mp.start();

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }
    public void button_submit() {
        String a=email.getText().toString();
        String b=pass.getText().toString();
        progressDialog.setMessage("Registering User...Please Wait.");
        progressDialog.show();
        auth.createUserWithEmailAndPassword(a,b).addOnCompleteListener(FullscreenActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    //display some message here
                    Toast.makeText(getApplicationContext(),"Successfully registered",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                }else{
                    //display some message here
                    Toast.makeText(getApplicationContext(),"Registration Error Try Again!",Toast.LENGTH_LONG).show();
                }
                progressDialog.dismiss();
            }
        });


    }
}
