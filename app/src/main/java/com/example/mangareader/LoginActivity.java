package com.example.mangareader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.os.Handler;

public class LoginActivity extends AppCompatActivity {
    private int[] imageArray = {R.drawable.login_background,R.drawable.login_background_1,R.drawable.login_background_2};
    private int currentIndex = 0;
    private ConstraintLayout constraintlayout;
    private Handler handler = new Handler();
    private static final int INTERVAL = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        constraintlayout = (ConstraintLayout) findViewById(R.id.constraintlayout);
        startImageChangeLoop();
    }
    private void startImageChangeLoop() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Change the image
                constraintlayout.setBackgroundResource(imageArray[currentIndex]);
                currentIndex = (currentIndex + 1) % imageArray.length;
                handler.postDelayed(this, INTERVAL);
            }
        }, INTERVAL);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}