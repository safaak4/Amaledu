package com.safaak.amaledu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.onesignal.OneSignal;


public class login_activity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText email,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activity);

        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.editText);
        password = findViewById(R.id.editText2);

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null && user.isEmailVerified()){
            Intent intent0 = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent0);
        }
    }

    public void login(View view){

        if (email.getText().toString().matches("") || password.getText().toString().matches("")){
            Toast.makeText(this, "Check your email and password", Toast.LENGTH_SHORT).show();
        }else{


                mAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    final FirebaseUser user = mAuth.getCurrentUser();
                                    if(user.isEmailVerified()){
                                        Intent intent0 = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent0);
                                    }else{
                                        Toast.makeText(login_activity.this, "Lütfen email adresinize gönderilen linki doğrulayın", Toast.LENGTH_SHORT).show();
                                        mAuth.getCurrentUser().sendEmailVerification()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(login_activity.this, "Doğrulama maili gönderildi", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    }


                                }
                            }
                        }).addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(login_activity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


        }
    }

    public void gotoregisterpage(View view){
        Intent intent1 = new Intent(getApplicationContext(), register_page.class);
        startActivity(intent1);
    }

}
