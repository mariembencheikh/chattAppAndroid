package com.example.androidproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignUp extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference reference;

    private EditText username,email,password,confirmPassword;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.connfirmPassword);
    }

    public void loginScreen(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void register(View view) {
        String usernameTxt = username.getText().toString().trim() ;
        String emailTxt = email.getText().toString().trim() ;
        String passwordTxt = password.getText().toString().trim();
        String confirmpasswordTxt = confirmPassword.getText().toString().trim();
        progressDialog = new ProgressDialog(this);
        if(usernameTxt.isEmpty()){
            username.setError("Username field is required");
        }else
        if(emailTxt.isEmpty()){
            email.setError("Email field is required");
        }else
        if(passwordTxt.isEmpty() || (passwordTxt.length()<6)){
            password.setError("enter proper password");
        }else
        if(confirmpasswordTxt.isEmpty()){
            confirmPassword.setError("Password field is required");
        }else
        if(!passwordTxt.equals(confirmpasswordTxt)){
            confirmPassword.setError("Password not match both field");
        }
        else {
            progressDialog.setMessage("Please wait while registration");
            progressDialog.setTitle("Registration");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            mAuth.createUserWithEmailAndPassword(emailTxt, passwordTxt).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        assert firebaseUser !=null;
                        String userid = firebaseUser.getUid();
                        reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("id", userid);
                        hashMap.put("username", usernameTxt);
                        hashMap.put("email", emailTxt);
                        hashMap.put("password", passwordTxt);
                        hashMap.put("status", "online");
                        hashMap.put("search", usernameTxt.toLowerCase());

                        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                   // progressDialog.dismiss();
                                    Toast.makeText(SignUp.this, "User added successfully", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(SignUp.this, AcceuilActivity.class));
                                    finish();
                                }
                            }
                        });
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(SignUp.this, "Register failed" + task.getException().getMessage(), Toast.LENGTH_LONG).show();

                    }
                }
            });
//
        }
    }
}