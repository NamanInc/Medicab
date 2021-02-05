package com.brightsky.medicabdriver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

public class drivinglicense extends AppCompatActivity {

    ImageView licenseImage;
    RadioButton als,bls,moutuary,intercity;
    public static final int CODE = 12325;
    private Uri imageUri;
    String ambulanceNo;
    private EditText ambulanceNotxt;
    CardView nextbtn;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    private HashMap<String,String > hashMap;
    private FirebaseAuth firebaseAuth;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drivinglicense);
        licenseImage = findViewById(R.id.licenceimage);
        als = findViewById(R.id.alsswitch);
        bls = findViewById(R.id.blsswitch);
        moutuary = findViewById(R.id.morturaryswitch);
        intercity = findViewById(R.id.intercityswitch);
        ambulanceNotxt = findViewById(R.id.ambulanceNo);
        nextbtn  = findViewById(R.id.next123);
        firebaseAuth = FirebaseAuth.getInstance();

        uid = firebaseAuth.getCurrentUser().getUid();


        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("DriversProfile");

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference("Driver_license");

        hashMap = new HashMap<>();






        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ambulanceNo = ambulanceNotxt.getText().toString();

                if(TextUtils.isEmpty(ambulanceNo)){
                    Toast.makeText(drivinglicense.this, "Fill your Ambulance No", Toast.LENGTH_SHORT).show();
                }
                else {

                    checkambulancetypefill();


                    reference.child(uid).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            Toast.makeText(drivinglicense.this, "DONE", Toast.LENGTH_SHORT).show();

                        }
                    });


                }
            }
        });




        licenseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pickImage();




            }


        });








    }

    private void checkambulancetypefill() {

        if (als.isChecked()){
            hashMap.put("Ambulancetype","ALS");
        }
        else if (bls.isChecked()){
            hashMap.put("Ambulancetype","BLS");
        }
        else if (moutuary.isChecked()){
            hashMap.put("Ambulancetype","Moutuary");
        }
        else if (intercity.isChecked()){
            hashMap.put("Ambulancetype","INTERCITY");
        }
        else {
            Toast.makeText(this, "Choose the type of ambulance", Toast.LENGTH_SHORT).show();
        }
    }

    private void pickImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(intent,CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CODE && resultCode == RESULT_OK && data!=null){

            imageUri = data.getData();

            licenseImage.setImageURI(imageUri);








        }
    }
}