package com.brightsky.medicabdriver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.brightsky.medicabdriver.R;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class otp_loagin extends AppCompatActivity {
    private CardView nextotpmobile;
    private TextView googlelogin;

    private FirebaseAuth mAuth;
    private final String COUNTRY_CODE = "+91";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_loagin);

        EditText phoneNumberEditText = findViewById(R.id.phonenumber);
        nextotpmobile = findViewById(R.id.next_mobile);
        googlelogin = findViewById(R.id.google);

        mAuth = FirebaseAuth.getInstance();


        nextotpmobile.setOnClickListener(v -> {
            String phoneNumber = phoneNumberEditText.getText().toString();

            if (phoneNumber.isEmpty())
                Toast.makeText(otp_loagin.this, "Enter your phone number", Toast.LENGTH_SHORT).show();
            else {


                PhoneAuthOptions options =
                        PhoneAuthOptions.newBuilder(mAuth)
                                .setPhoneNumber(COUNTRY_CODE + phoneNumber)
                                .setTimeout(60L, TimeUnit.SECONDS)
                                .setActivity(otp_loagin.this)
                                .setCallbacks(onVerificationStateChangedCallbacks)
                                .build();

                PhoneAuthProvider.verifyPhoneNumber(options);
            }

            Intent intent = new Intent(otp_loagin.this,otpinput.class);
            startActivity(intent);
            finish();
        });
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks onVerificationStateChangedCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
//            startActivity(new Intent(PhoneOtpAuth.this, DriverSignup.class));
//            finish();
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            Log.e("PhoneOtpAuth", "OTP Sent");
            startActivity(new Intent(otp_loagin.this, otpinput.class).putExtra("verificationId", s));
            finish();

            //Activity Jump two times have to fix it.
        }
    };
}