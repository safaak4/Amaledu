package com.safaak.amaledu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;

public class profilePage extends AppCompatActivity {

    ImageView imageView;
    TextView textViewuserName, textViewemail, textViewGraduation;
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        mAdView = findViewById(R.id.adViewprofilepage);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        imageView = findViewById(R.id.imageView);
        textViewuserName = findViewById(R.id.textView12);
        textViewemail = findViewById(R.id.textView14);
        textViewGraduation = findViewById(R.id.textView16);

        String downloadurl = getIntent().getExtras().getString("profilepic");
        String username = getIntent().getExtras().getString("username");
        String useremail = getIntent().getExtras().getString("email");
        String usergraduation = getIntent().getExtras().getString("graduation");

        Picasso.get().load(downloadurl).into(imageView);
        textViewuserName.setText(username);
        textViewemail.setText(useremail);
        textViewGraduation.setText(usergraduation);


    }







}
