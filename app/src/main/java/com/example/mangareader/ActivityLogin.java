package com.example.mangareader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class ActivityLogin extends AppCompatActivity {
    private int[] imageArray = {R.drawable.login_background,R.drawable.login_background_1,R.drawable.login_background_2};
    private int currentIndex = 0;
    private ConstraintLayout constraintlayout;
    private Handler handler = new Handler();
    private static final int INTERVAL = 3000;
    private FirebaseAuth mAuth;
    private EditText edtEmail,edtPass;
    private Button buttonLogin;
    private TextView textViewRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        constraintlayout = (ConstraintLayout) findViewById(R.id.constraintlayout);
        startImageChangeLoop();
        addControl();
        addEvent();
    }
    private void addControl(){
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPass = (EditText) findViewById(R.id.edtPass);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        textViewRegister = (TextView) findViewById(R.id.textViewRegister);
    }
    private void addEvent(){
        mAuth = FirebaseAuth.getInstance();
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString();
                String pass = edtPass.getText().toString();
                mAuth.signInWithEmailAndPassword(email,pass)
                        .addOnCompleteListener(ActivityLogin.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Intent intent = new Intent(ActivityLogin.this, ActivityHome.class);
                                    startActivity(intent);
                                    Toast.makeText(ActivityLogin.this, "Sign in successfully", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(ActivityLogin.this, "Sign in failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityLogin.this, ActivityRegister.class);
                startActivity(intent);
            }
        });
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