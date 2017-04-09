package com.mundev.bookpro;

import android.animation.Animator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import static java.lang.Thread.sleep;

public class Test extends AppCompatActivity implements Animator.AnimatorListener {
    TextView textView;
    Animation animation,animation1;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        textView=(TextView)findViewById(R.id.mytext);
        animation= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.anim_slideup);
        animation1=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.anim_zoomin);
        button=(Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setVisibility(View.INVISIBLE);
                textView.startAnimation(animation1);
                try {
                    sleep(1000);
                }
                catch (Exception e)
                {

                }

               // textView.startAnimation(animation);

            }
        });

    }

    @Override
    public void onAnimationStart(Animator animator) {

    }

    @Override
    public void onAnimationEnd(Animator animator) {

    }

    @Override
    public void onAnimationCancel(Animator animator) {

    }

    @Override
    public void onAnimationRepeat(Animator animator) {

    }
}
