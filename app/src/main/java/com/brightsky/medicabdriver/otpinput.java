package com.brightsky.medicabdriver;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.brightsky.medicabdriver.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class otpinput extends AppCompatActivity {
    private String phoneno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpinput);

        Intent intent = getIntent();

        String verificationId = intent.getStringExtra("verificationId");
        String phoneNo  = intent.getStringExtra("phoneNo");

        EditText code1 = findViewById(R.id.code_1);
        EditText code2 = findViewById(R.id.code_2);
        EditText code3 = findViewById(R.id.code_3);
        EditText code4 = findViewById(R.id.code_4);
        EditText code5 = findViewById(R.id.code_5);
        EditText code6 = findViewById(R.id.code_6);

        findViewById(R.id.verify_otp).setOnClickListener(v -> {
            String code1String = code1.getText().toString();
            String code2String = code2.getText().toString();
            String code3String = code3.getText().toString();
            String code4String = code4.getText().toString();
            String code5String = code5.getText().toString();
            String code6String = code6.getText().toString();

            String code = code1String + code2String + code3String + code4String + code5String + code6String;

            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            signInUser(credential);
        });
    }

    private void signInUser(PhoneAuthCredential credential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {


                        Intent intent = new Intent(otpinput.this,Profile_setup.class);

                        startActivity(intent);

                        finish();
                    } else {
                        Log.d("PhoneOtpAuth", "onComplete:" + task.getException());
                    }
                });
    }
}