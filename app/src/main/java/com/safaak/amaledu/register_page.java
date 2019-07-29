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
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;

public class register_page extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    EditText email1, password2, passwordagain, name, graduationYear;
    ImageView profilepicupload;
    Uri selectedImage2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        mAuth = FirebaseAuth.getInstance();
        email1 = findViewById(R.id.editText5);
        password2 = findViewById(R.id.editText7);
        passwordagain = findViewById(R.id.editText8);
        name = findViewById(R.id.editText3);
        graduationYear = findViewById(R.id.editText6);
        profilepicupload = findViewById(R.id.imageView2);
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    public void selectimage22(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 2);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2);
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            selectedImage2 = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage2);
                profilepicupload.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (requestCode == 2 && resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public void signUp(View view) {

        if (email1.getText().toString().matches("") || password2.getText().toString().matches("") || passwordagain.getText().toString().matches("")
        || name.getText().toString().matches("") || !password2.getText().toString().equals(passwordagain.getText().toString()) || graduationYear.getText().toString().matches("")) {
            Toast.makeText(this, "Error in register: 0", Toast.LENGTH_SHORT).show();
        } else {

            int graduationYearInteger = Integer.parseInt(graduationYear.getText().toString());
            if ( graduationYearInteger >= 2020 && graduationYearInteger <= 2025) {

                mAuth.createUserWithEmailAndPassword(email1.getText().toString(), password2.getText().toString())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {

                                    FirebaseAuth auth = FirebaseAuth.getInstance();
                                    FirebaseUser user = auth.getCurrentUser();

                                    user.sendEmailVerification()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(register_page.this, "Doğrulama maili gönderildi", Toast.LENGTH_SHORT).show();
                                                        createProfileOnFirestore();
                                                    }
                                                }
                                            });


                                } else {
                                    Toast.makeText(register_page.this, "Error in register: 1", Toast.LENGTH_SHORT).show();
                                }

                            }
                        }).addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(register_page.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


            } else {
                Toast.makeText(this, "Lütfen bilgilerinizi kontrol ediniz", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public void createProfileOnFirestore() {

        final String imageName2 = "images/users/"+email1.getText().toString()+".jpg";
        final StorageReference storageReference2 = mStorageRef.child(imageName2);
        if (selectedImage2 != null) {
            storageReference2.putFile(selectedImage2).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    StorageReference newReference2 = FirebaseStorage.getInstance().getReference(imageName2);
                    newReference2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String downloadURL2 = uri.toString();


                            String userNameandSurname = name.getText().toString();
                            String userEmail2 = email1.getText().toString();

                            HashMap<String, Object> userData = new HashMap<>();
                            userData.put("profilepic", downloadURL2);
                            userData.put("usernameandsurname", userNameandSurname);
                            userData.put("useremail", userEmail2);
                            userData.put("graduation", graduationYear.getText().toString());

                            FirebaseFirestore.getInstance().collection("usersdata").document("allusers")
                                    .collection(userEmail2).document(userEmail2).set(userData)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(register_page.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mAuth.getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(register_page.this, "Error", Toast.LENGTH_SHORT).show();
                                    storageReference2.delete();

                                }
                            });

                        }
                    });


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(register_page.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            mAuth.getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(register_page.this, "Plase upload a profile picture", Toast.LENGTH_SHORT).show();
                }
            });
        }


    }

}
