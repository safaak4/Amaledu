package com.safaak.amaledu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class upload_activity extends AppCompatActivity {

    EditText postCommentText;
    ImageView postImage;
    int sinif;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    Uri selectedImage;

    private AdView mAdView;

    String[]Siniflar = {"Dokuzuncu Sınıf", "Onuncu Sınıf", "Onbirinci Sınıf", "Onikinci Sınıf"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_activity);

        mAdView = findViewById(R.id.adViewupload);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        postCommentText = findViewById(R.id.postCommentText);
        postImage = findViewById(R.id.postImage);

        //firebaseDatabase = FirebaseDatabase.getInstance();
        //myRef = firebaseDatabase.getReference();

        mAuth = FirebaseAuth.getInstance();

        mStorageRef = FirebaseStorage.getInstance().getReference();



        //spinner islemleri
        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(upload_activity.this, android.R.layout.simple_spinner_item,Siniflar);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        sinif = 9;
                        break;
                    case 1:
                        sinif = 10;
                        break;
                    case 2:
                        sinif = 11;
                        break;
                    case 3:
                        sinif = 12;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public void selectimage(View view){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent,2);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == 1){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,2);
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

       if(requestCode == 2 && resultCode == RESULT_OK && data != null){
           selectedImage = data.getData();

           try {
               Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
               postImage.setImageBitmap(bitmap);
           } catch (IOException e) {
               e.printStackTrace();
           }


       }  else if(requestCode == 2 && resultCode == RESULT_CANCELED){
           Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
       }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void upload(View view) {

        if (!postCommentText.getText().toString().matches("") && postImage != null){
            UUID uuid = UUID.randomUUID();

            Date now = new Date();
            final String nowstring = now.toString();
            if (sinif == 9) {
                final String imageName = "images/ninethgrade/" + uuid + ".jpg";

                StorageReference storageReference = mStorageRef.child(imageName);
                storageReference.putFile(selectedImage).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        //download url

                        StorageReference newReference = FirebaseStorage.getInstance().getReference(imageName);
                        newReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                final String downloadURL = uri.toString();
                                // System.out.println("Download URL =" + downloadURL);
                                FirebaseUser user = mAuth.getCurrentUser();
                                String userEmail = user.getEmail();

                                String userComment = postCommentText.getText().toString();


                                //UUID uuidtwo = UUID.randomUUID();
                                //String uuidString = uuidtwo.toString();

                                FirebaseFirestore firebaseFirestore3 = FirebaseFirestore.getInstance();
                                DocumentReference documentReference = firebaseFirestore3.collection("usersdata").document("allusers")
                                        .collection(userEmail).document(userEmail);
                                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                        if(task.isSuccessful()){
                                            DocumentSnapshot documentSnapshot = task.getResult();
                                            if (documentSnapshot.exists()){
                                                String usersname = documentSnapshot.getData().get("usernameandsurname").toString();
                                                String profilepicc = documentSnapshot.getData().get("profilepic").toString();

                                                int downloadurllenght = downloadURL.length();
                                                int downloadurllenght0 = downloadurllenght - 36;
                                                String postCommentIDD = downloadURL.substring(downloadurllenght0, downloadurllenght);

                                                HashMap<String, Object> postName = new HashMap<>();
                                                postName.put("usersname", usersname);
                                                postName.put("profilepic", profilepicc);

                                                FirebaseFirestore.getInstance().collection("ninthgradeposts").document(postCommentIDD).update(postName);


                                            }
                                        }


                                    }
                                });

                                HashMap<String, Object> postData = new HashMap<>();
                                postData.put("username", userEmail);
                                postData.put("usercomment", userComment);
                                postData.put("downloadurl", downloadURL);
                                postData.put("date", FieldValue.serverTimestamp());
                                postData.put("now", nowstring);

                                int downloadurllenght = downloadURL.length();
                                int downloadurllenght0 = downloadurllenght - 36;
                                String postCommentIDD = downloadURL.substring(downloadurllenght0, downloadurllenght);

                                FirebaseFirestore.getInstance().collection("ninthgradeposts").document(postCommentIDD)
                                        .set(postData)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                upload_activity.super.onBackPressed();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(upload_activity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                                //myRef.child("ninthgradeposts").child(uuidString).child("username").setValue(userEmail);
                                //myRef.child("ninthgradeposts").child(uuidString).child("usercomment").setValue(userComment);
                                //myRef.child("ninthgradeposts").child(uuidString).child("downloadurl").setValue(downloadURL);

                                //Toast.makeText(upload_activity.this, "Post created", Toast.LENGTH_SHORT).show();

                                // Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                //startActivity(intent);
                            }
                        });

                        //username, comment


                    }
                }).addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(upload_activity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else if (sinif == 10) {

                final String imageName = "images/tenthgrade/" + uuid + ".jpg";

                StorageReference storageReference = mStorageRef.child(imageName);
                storageReference.putFile(selectedImage).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        StorageReference newReference = FirebaseStorage.getInstance().getReference(imageName);
                        newReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                final String downloadURL = uri.toString();
                                FirebaseUser user = mAuth.getCurrentUser();
                                String userEmail = user.getEmail();

                                String userComment = postCommentText.getText().toString();

                                FirebaseFirestore firebaseFirestore3 = FirebaseFirestore.getInstance();
                                DocumentReference documentReference = firebaseFirestore3.collection("usersdata").document("allusers")
                                        .collection(userEmail).document(userEmail);
                                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                        if(task.isSuccessful()){
                                            DocumentSnapshot documentSnapshot = task.getResult();
                                            if (documentSnapshot.exists()){
                                                String usersname = documentSnapshot.getData().get("usernameandsurname").toString();
                                                String profilepicc = documentSnapshot.getData().get("profilepic").toString();

                                                int downloadurllenght = downloadURL.length();
                                                int downloadurllenght0 = downloadurllenght - 36;
                                                String postCommentIDD = downloadURL.substring(downloadurllenght0, downloadurllenght);

                                                HashMap<String, Object> postName = new HashMap<>();
                                                postName.put("usersname", usersname);
                                                postName.put("profilepic", profilepicc);

                                                FirebaseFirestore.getInstance().collection("tenthgradeposts").document(postCommentIDD)
                                                        .update(postName);


                                            }
                                        }


                                    }
                                });


                                HashMap<String, Object> postData = new HashMap<>();
                                postData.put("username", userEmail);
                                postData.put("usercomment", userComment);
                                postData.put("downloadurl", downloadURL);
                                postData.put("date", FieldValue.serverTimestamp());
                                postData.put("now", nowstring);

                                int downloadurllenght = downloadURL.length();
                                int downloadurllenght0 = downloadurllenght - 36;
                                String postCommentIDD = downloadURL.substring(downloadurllenght0, downloadurllenght);

                                FirebaseFirestore.getInstance().collection("tenthgradeposts").document(postCommentIDD).set(postData)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                upload_activity.super.onBackPressed();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(upload_activity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });


                            }
                        });


                    }
                }).addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(upload_activity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


            } else if (sinif == 11) {
                final String imageName = "images/eleventhgrade/" + uuid + ".jpg";

                StorageReference storageReference = mStorageRef.child(imageName);
                storageReference.putFile(selectedImage).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        StorageReference newReference = FirebaseStorage.getInstance().getReference(imageName);
                        newReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                final String downloadURL = uri.toString();
                                FirebaseUser user = mAuth.getCurrentUser();
                                String userEmail = user.getEmail();

                                String userComment = postCommentText.getText().toString();

                                FirebaseFirestore firebaseFirestore3 = FirebaseFirestore.getInstance();
                                DocumentReference documentReference = firebaseFirestore3.collection("usersdata").document("allusers")
                                        .collection(userEmail).document(userEmail);
                                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                        if(task.isSuccessful()){
                                            DocumentSnapshot documentSnapshot = task.getResult();
                                            if (documentSnapshot.exists()){
                                                String usersname = documentSnapshot.getData().get("usernameandsurname").toString();
                                                String profilepicc = documentSnapshot.getData().get("profilepic").toString();

                                                int downloadurllenght = downloadURL.length();
                                                int downloadurllenght0 = downloadurllenght - 36;
                                                String postCommentIDD = downloadURL.substring(downloadurllenght0, downloadurllenght);

                                                HashMap<String, Object> postName = new HashMap<>();
                                                postName.put("usersname", usersname);
                                                postName.put("profilepic", profilepicc);

                                                FirebaseFirestore.getInstance().collection("eleventhgradeposts").document(postCommentIDD)
                                                        .update(postName);


                                            }
                                        }


                                    }
                                });

                                HashMap<String, Object> postData = new HashMap<>();
                                postData.put("username", userEmail);
                                postData.put("usercomment", userComment);
                                postData.put("downloadurl", downloadURL);
                                postData.put("date", FieldValue.serverTimestamp());
                                postData.put("now", nowstring);

                                int downloadurllenght = downloadURL.length();
                                int downloadurllenght0 = downloadurllenght - 36;
                                String postCommentIDD = downloadURL.substring(downloadurllenght0, downloadurllenght);

                                FirebaseFirestore.getInstance().collection("eleventhgradeposts").document(postCommentIDD).set(postData)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                upload_activity.super.onBackPressed();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(upload_activity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });


                            }
                        });


                    }
                }).addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(upload_activity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            } else if (sinif == 12) {
                final String imageName = "images/twelfthgrade/" + uuid + ".jpg";

                StorageReference storageReference = mStorageRef.child(imageName);
                storageReference.putFile(selectedImage).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        StorageReference newReference = FirebaseStorage.getInstance().getReference(imageName);
                        newReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                final String downloadURL = uri.toString();
                                FirebaseUser user = mAuth.getCurrentUser();
                                String userEmail = user.getEmail();

                                String userComment = postCommentText.getText().toString();

                                FirebaseFirestore firebaseFirestore3 = FirebaseFirestore.getInstance();
                                DocumentReference documentReference = firebaseFirestore3.collection("usersdata").document("allusers")
                                        .collection(userEmail).document(userEmail);
                                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                        if(task.isSuccessful()){
                                            DocumentSnapshot documentSnapshot = task.getResult();
                                            if (documentSnapshot.exists()){
                                                String usersname = documentSnapshot.getData().get("usernameandsurname").toString();
                                                String profilepicc = documentSnapshot.getData().get("profilepic").toString();

                                                int downloadurllenght = downloadURL.length();
                                                int downloadurllenght0 = downloadurllenght - 36;
                                                String postCommentIDD = downloadURL.substring(downloadurllenght0, downloadurllenght);

                                                HashMap<String, Object> postName = new HashMap<>();
                                                postName.put("usersname", usersname);
                                                postName.put("profilepic", profilepicc);

                                                FirebaseFirestore.getInstance().collection("twelfthgradeposts").document(postCommentIDD)
                                                        .update(postName);


                                            }
                                        }


                                    }
                                });

                                HashMap<String, Object> postData = new HashMap<>();
                                postData.put("username", userEmail);
                                postData.put("usercomment", userComment);
                                postData.put("downloadurl", downloadURL);
                                postData.put("date", FieldValue.serverTimestamp());
                                postData.put("now", nowstring);

                                int downloadurllenght = downloadURL.length();
                                int downloadurllenght0 = downloadurllenght - 36;
                                String postCommentIDD = downloadURL.substring(downloadurllenght0, downloadurllenght);

                                FirebaseFirestore.getInstance().collection("twelfthgradeposts").document(postCommentIDD).set(postData)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                upload_activity.super.onBackPressed();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(upload_activity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });


                            }
                        });


                    }
                }).addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(upload_activity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                Toast.makeText(this, "Error in upload_activity: 1", Toast.LENGTH_SHORT).show();
            }


        } else {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }

    }


    //String usersName = document.getData().get("usernameandsurname").toString();
}
