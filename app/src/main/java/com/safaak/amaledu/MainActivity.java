package com.safaak.amaledu;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.text.Layout;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SnapshotMetadata;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.Menu;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Map;
import java.util.zip.Inflater;

import javax.annotation.Nullable;

import io.opencensus.metrics.export.Summary;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    bugfeedback bugfeedback;
    userFeedbackForm userFeedbackForm;
    DuyurularFragment duyurularFragment;
    TakvimFragment takvimFragment;
    DenemeSonuclariFragment denemeSonuclariFragment;
    AmaleduGuncellemeleriFragment amaleduGuncellemeleriFragment;
    SinifDokuzFragment sinifDokuzFragment;
    TextView emailtextviewonnavigation, usernameonnavigation;
    ImageView profilepicinnavigation;
    private StorageReference mStorageRef;

    ArrayList<String> userimageHomeFromFB;
    ArrayList<String> usercommentHomeFromFB;
    ListView listViewHome;
    HomePostClass adapter0;

    public String currentUserGraduation, currentUserBranch, currentUserProfilePic, currentUserNameandSurname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        listViewHome = findViewById(R.id.listViewHome);

        usercommentHomeFromFB = new ArrayList<String>();
        userimageHomeFromFB = new ArrayList<String>();

        adapter0 = new HomePostClass(usercommentHomeFromFB, userimageHomeFromFB, this);
        listViewHome.setAdapter(adapter0);

        getDataFromFirebaseFirestore0();

        bugfeedback = new bugfeedback();
        userFeedbackForm = new userFeedbackForm();
        duyurularFragment = new DuyurularFragment();
        takvimFragment = new TakvimFragment();
        denemeSonuclariFragment = new DenemeSonuclariFragment();
        amaleduGuncellemeleriFragment = new AmaleduGuncellemeleriFragment();
        sinifDokuzFragment = new SinifDokuzFragment();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);

        emailtextviewonnavigation = headerView.findViewById(R.id.textView123);
        profilepicinnavigation = headerView.findViewById(R.id.imageView123);
        usernameonnavigation = headerView.findViewById(R.id.textViewUserName);

        navigationislemleri();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), upload_activity.class);
                intent.putExtra("currentname", currentUserNameandSurname).putExtra("currentprofilepic", currentUserProfilePic);
                startActivity(intent);

                /* Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show(); */
            }
        });

    }



    public void getDataFromFirebaseFirestore0(){


        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = firebaseFirestore.collection("homeposts");

        collectionReference
                .orderBy("date", Query.Direction.DESCENDING)
                .addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                        for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){

                            Map<String, Object> data = snapshot.getData();

                            String posttext = (String) data.get("posttext");
                            String downloadurl = (String) data.get("downloadurl");

                            userimageHomeFromFB.add(downloadurl);
                            usercommentHomeFromFB.add(posttext);

                            adapter0.notifyDataSetChanged();
                        }
                    }
                });
    }

    private void navigationislemleri(){

        String currentEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        FirebaseFirestore firebaseFirestore2 = FirebaseFirestore.getInstance();
        DocumentReference documentReference = firebaseFirestore2.collection("usersdata").document("allusers")
                .collection(currentEmail).document(currentEmail);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()) {
                        //System.out.println("DocumentSnapshot data: " + document.getData().get("usernameandsurname"));
                        currentUserGraduation = (String) document.getData().get("graduation");
                        currentUserBranch = (String) document.getData().get("branch");
                        currentUserNameandSurname = (String) document.getData().get("usernameandsurname");
                        currentUserProfilePic = (String) document.getData().get("profilepic");

                        Picasso.get().load(currentUserProfilePic).resize(150,150).into(profilepicinnavigation);
                        usernameonnavigation.setText(currentUserNameandSurname);
                    }else {
                        //System.out.println("No such document");
                    }
                }else {
                    //System.out.println("get failed with" + task.getException());
                }


            }
        });

        emailtextviewonnavigation.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
    }


    private void setFragment(Fragment fragment){

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainFrame,fragment);
        fragmentTransaction.commit();

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            final String currentEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

            Intent intent = new Intent(getApplicationContext(), profilePage.class);
            intent.putExtra("profilepic", currentUserProfilePic).putExtra("username", currentUserNameandSurname)
                    .putExtra("email", currentEmail).putExtra("graduation", currentUserGraduation);
            startActivity(intent);

        }else if(id == R.id.userfeedback){
            setFragment(userFeedbackForm);
        }else if(id == R.id.bugfeedback){
            setFragment(bugfeedback);
        }else if(id == R.id.signout){
            FirebaseAuth.getInstance().signOut();
            super.onBackPressed();
        }else if (id == R.id.yoneticisayfasi){
            if (mAuth.getCurrentUser().getEmail().equals("cumasafak@gmail.com") || mAuth.getCurrentUser().getEmail().equals("cumasafak@windowslive.com")){
                Intent intent = new Intent(getApplicationContext(), siniflarinlistesi.class);
                intent.putExtra("usernameee", currentUserNameandSurname);
                startActivity(intent);
            }else{
                Toast.makeText(this, "Buna izniniz yok", Toast.LENGTH_SHORT).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_duyurular) {
            setFragment(duyurularFragment);
        } else if (id == R.id.nav_takvim) {
             //   setFragment(takvimFragment);
        } else if (id == R.id.nav_deneme_sonuclari) {
               //     setFragment(denemeSonuclariFragment);
        } else if (id == R.id.nav_amaledu_guncellemeleri) {
                        setFragment(amaleduGuncellemeleriFragment);
        } else if (id == R.id.nav_odevlerim) {
                Intent intent = new Intent(getApplicationContext(), odevlerinGorunumu.class);
                intent.putExtra("donem", currentUserGraduation).putExtra("sube", currentUserBranch);
                startActivity(intent);
        } else if (id == R.id.nav_dokuzuncu_sinif) {
            //setFragment(sinifDokuzFragment);
            Intent intent = new Intent(getApplicationContext(), DokuzuncuSinif.class);
            startActivity(intent);
        } else if (id == R.id.nav_onuncu_sinif) {
            Intent intent = new Intent(getApplicationContext(), OnuncuSinif.class);
            startActivity(intent);
        } else if (id == R.id.nav_onbirinci_sinif) {
            Intent intent = new Intent(getApplicationContext(), OnBirinciSinif.class);
            startActivity(intent);
        } else if (id == R.id.nav_onikinci_sinif) {
            Intent intent = new Intent(getApplicationContext(), OnikinciSinif.class);
            startActivity(intent);
        } else if (id == R.id.nav_raporgir){
            Intent intent = new Intent(getApplicationContext(), Rapor_giris_ekrani.class);
            startActivity(intent);
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
