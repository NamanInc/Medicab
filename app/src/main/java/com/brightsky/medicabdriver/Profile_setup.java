package com.brightsky.medicabdriver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

public class Profile_setup extends AppCompatActivity {

    ImageView profileimage;
    EditText emailtxt,firstnametxt,lastnametxt,phoneNotxt;
    String email,phone,firstname,lastname,profile;
    CardView nextbtn;
    public static final int CODE = 1;
    Uri imageuri;
    private FirebaseStorage storage;
    StorageReference storageReference;
    String downloadurl;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    String uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);

        Intent intent = getIntent();

        String phoneNo = intent.getStringExtra("phoneNo");

        profileimage = findViewById(R.id.profileImage);
        emailtxt = findViewById(R.id.emailid);
        firstnametxt = findViewById(R.id.firstname);
        lastnametxt = findViewById(R.id.lastname);
        phoneNotxt = findViewById(R.id.phoneNo);
        nextbtn = findViewById(R.id.nextbtn);
        firebaseAuth = FirebaseAuth.getInstance();
        uid = firebaseAuth.getCurrentUser().getUid();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("DriversProfile");

        phoneNotxt.setText(phoneNo);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference("UploadImages/");



        profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickiamge();






            }




        });



        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                email = emailtxt.getText().toString().trim();
                firstname = firstnametxt.getText().toString().trim();
                lastname = lastnametxt.getText().toString().trim();
                phone = phoneNotxt.getText().toString().trim();








                checkfields();

                if (imageuri!=null){
                    uploadpicture();

                }
                else {
                    Toast.makeText(Profile_setup.this, "Upload Profile", Toast.LENGTH_SHORT).show();
                }





            }
        });





    }

    private void uploadpicture() {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading....");
        progressDialog.show();

        final  String randomkey = UUID.randomUUID().toString();
        StorageReference riversRef = storageReference.child("images/"+randomkey);
        riversRef.putFile(imageuri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                downloadurl = String.valueOf(uri);

//                                HashMap<String, String>  hashMap = new HashMap<>();
//
//                                hashMap.put("Firstname" , firstname);
//                                hashMap.put("Lastname",lastname);
//                                hashMap.put("email",email);
//                                hashMap.put("phoneNo",phone);
//                                hashMap.put("Block","0");
//                                hashMap.put("profielurl",downloadurl);
//                                hashMap.put("uid",uid);
                               // hashMap.put("Ambulancetype","");

                                databaseReference.child(uid).child("profileurl").setValue(downloadurl);
                                databaseReference.child(uid).child("Block").setValue(0);
                                databaseReference.child(uid).child("phoneNo").setValue(phone);
                                databaseReference.child(uid).child("email").setValue(email);
                                databaseReference.child(uid).child("Firstname").setValue(firstname);
                                databaseReference.child(uid).child("Lastname").setValue(lastname);
                                databaseReference.child(uid).child("uid").setValue(uid);
                                databaseReference.child(uid).child("Ambulancetype").setValue(true);


                                Toast.makeText(Profile_setup.this, "Setup sucessful", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();

                                Intent intent = new Intent(Profile_setup.this,DriverHomePage.class);
                                startActivity(intent);
                                finish();


//                                databaseReference.child(uid).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//
//                                        Toast.makeText(Profile_setup.this, "Setup sucessful", Toast.LENGTH_SHORT).show();
//                                        progressDialog.dismiss();
//
//                                        Intent intent = new Intent(Profile_setup.this,DriverHomePage.class);
//                                        startActivity(intent);
//                                        finish();
//
//                                    }
//                                }).addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//
//                                        Toast.makeText(Profile_setup.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                                        progressDialog.dismiss();
//
//                                    }
//                                });
//
//
//
//
//
//
//
//
//
//
//
//
//                                //  Toast.makeText(Profile_setup.this, downloadurl, Toast.LENGTH_SHORT).show();
//
//
//
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Profile_setup.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(Profile_setup.this, exception.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });


    }

    private void pickiamge() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(intent,CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CODE && resultCode == RESULT_OK && data!=null){

            imageuri = data.getData();

            profileimage.setImageURI(imageuri);




        }
    }

    private void checkfields() {

        if (TextUtils.isEmpty(email)){
            Toast.makeText(this, "Enter your Email First", Toast.LENGTH_SHORT).show();
        }
        else {
            if (TextUtils.isEmpty(firstname)){
                Toast.makeText(this, "Enter your First Name", Toast.LENGTH_SHORT).show();
            }
            else if (TextUtils.isEmpty(lastname)){
                Toast.makeText(this, "Enter your Last Name", Toast.LENGTH_SHORT).show();
            }
            else if (TextUtils.isEmpty(phone)){
                Toast.makeText(this, "Enter your phone Number", Toast.LENGTH_SHORT).show();
            }

            else {

                uploadpicture();
            }
        }







    }
}