package com.example.androidproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText email,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
    }
    public void signup(View view) {
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);

    }

    public void login(View view) {

        String usernameTxt = email.getText().toString().trim() ;
        String passwordTxt = password.getText().toString().trim();
        if(usernameTxt.isEmpty()){
            email.setError("Email field is required");
        }
        if(passwordTxt.isEmpty()){
            password.setError("Password field is required");
        }
        else{
            mAuth.signInWithEmailAndPassword(usernameTxt,passwordTxt).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(LoginActivity.this, AcceuilActivity.class);
                        startActivity(intent);

                    } else {
                        Toast.makeText(LoginActivity.this, "Login failed" + task.getException().getMessage(), Toast.LENGTH_LONG).show();

                    }
                }
            });
//
        }
    }
}