package com.safaak.amaledu;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

import javax.annotation.Nullable;

public class odevlerinGorunumu extends AppCompatActivity {

    ListView listViewOdevler;
    ArrayList<String> ogretmennameFromFB;
    ArrayList<String> odevtextFromFB;
    ArrayList<String> nowwFromFB;
    odevClass adapterr;

    String currentEmail, userGraduation, userBranch;

    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_odevlerin_gorunumu);

        mAdView = findViewById(R.id.adView1222);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        listViewOdevler = findViewById(R.id.listViewOdevler);

        currentEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        userGraduation = getIntent().getExtras().getString("donem");
        userBranch = getIntent().getExtras().getString("sube");
        ogretmennameFromFB = new ArrayList<>();
        odevtextFromFB = new ArrayList<>();
        nowwFromFB = new ArrayList<>();

        adapterr = new odevClass(ogretmennameFromFB, odevtextFromFB, nowwFromFB, this);
        listViewOdevler.setAdapter(adapterr);

        getDataOdevFromFirebaseFirestore();

        listViewOdevler.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                AlertDialog.Builder alert = new AlertDialog.Builder(odevlerinGorunumu.this);
                alert.setTitle("Date: " + nowwFromFB.get(i));

                return false;
            }
        });



    }

    public void getDataOdevFromFirebaseFirestore() {

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = firebaseFirestore.collection("odevler").document(userGraduation).collection(userBranch.toLowerCase());

        collectionReference
                .orderBy("date", Query.Direction.DESCENDING)
                .addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                        for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {

                            Map<String, Object> dataOdev = snapshot.getData();
                            String ogretmen = (String) dataOdev.get("ogretmen");
                            String odevtext = (String) dataOdev.get("odevtext");
                            String noww = (String) dataOdev.get("now");

                            ogretmennameFromFB.add(ogretmen);
                            odevtextFromFB.add(odevtext);
                            nowwFromFB.add(noww);

                            adapterr.notifyDataSetChanged();

                        }
                    }
                });
    }
}
