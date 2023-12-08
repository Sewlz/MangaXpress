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

public class ActivityRegister extends AppCompatActivity {
    private EditText edtComfirm, edtEmailReg,edtPassReg;
    private Button buttonRegister;
    private TextView textViewLogin;
    private int[] imageArray = {R.drawable.login_background,R.drawable.login_background_1,R.drawable.login_background_2};
    private int currentIndex = 0;
    private ConstraintLayout constraintlayoutReg;
    private Handler handler = new Handler();
    private static final int INTERVAL = 3000;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        constraintlayoutReg = (ConstraintLayout) findViewById(R.id.constraintlayoutReg);
        startImageChangeLoop();
        addControl();
        addEvent();
    }
    private void addControl(){
        edtEmailReg = (EditText) findViewById(R.id.edtEmailReg);
        edtComfirm = (EditText) findViewById(R.id.edtComfirmPass);
        edtPassReg = (EditText) findViewById(R.id.edtPassReg);
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        textViewLogin = (TextView) findViewById(R.id.textViewLogin);
    }
    private void addEvent(){
        mAuth = FirebaseAuth.getInstance();
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, pass, comfirmPass;
                email = String.valueOf(edtEmailReg.getText().toString());
                pass = String.valueOf(edtPassReg.getText().toString());
                comfirmPass = String.valueOf(edtComfirm.getText().toString());
                if (pass.length() > 6 && containsNumber(pass)) {
                    if(pass.equals(comfirmPass)){
                        mAuth.createUserWithEmailAndPassword(email, pass)
                                .addOnCompleteListener(ActivityRegister.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            Intent intent = new Intent(ActivityRegister.this, ActivityLogin.class);
                                            startActivity(intent);
                                            Toast.makeText(ActivityRegister.this, "Account Created", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(ActivityRegister.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }else {
                        Toast.makeText(ActivityRegister.this, "Incorect comfirm password!!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ActivityRegister.this, "Password should be at least 8 characters long and contain at least 1 digit", Toast.LENGTH_SHORT).show();
                }

            }
        });

        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityRegister.this, ActivityLogin.class);
                startActivity(intent);
            }
        });
    }

    private boolean containsNumber(String password) {
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) {
                return true;
            }
        }
        return false;
    }
    
    private void startImageChangeLoop() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Change the image
                constraintlayoutReg.setBackgroundResource(imageArray[currentIndex]);
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