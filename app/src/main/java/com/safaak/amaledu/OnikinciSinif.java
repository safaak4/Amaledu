package com.safaak.amaledu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class OnikinciSinif extends AppCompatActivity {

    ListView listViewOniki;
    PostClass adapter;
    ArrayList<String> useremailFromFB;
    ArrayList<String> userNameFromFB;
    ArrayList<String> userimageFromFB;
    ArrayList<String> usercommentFromFB;
    ArrayList<String> profilepicFromFB;
    ArrayList<String> nowFromFB;
    private FirebaseAuth mAuth;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onikinci_sinif);

        mAdView = findViewById(R.id.adView12);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        listViewOniki = findViewById(R.id.listViewOniki);

        useremailFromFB = new ArrayList<String>();
        userNameFromFB = new ArrayList<String>();
        usercommentFromFB = new ArrayList<String>();
        userimageFromFB = new ArrayList<String>();
        profilepicFromFB = new ArrayList<String>();
        nowFromFB = new ArrayList<String>();

        // firebaseDatabase = FirebaseDatabase.getInstance();
        //myRef = firebaseDatabase.getReference();

        adapter = new PostClass(useremailFromFB, userNameFromFB, usercommentFromFB, userimageFromFB, profilepicFromFB, nowFromFB, this);
        listViewOniki.setAdapter(adapter);


        //firebase firestore
        getDataFromFirebaseFirestore();

        //realtime database
        //getDataFromFirebase();

        listViewOniki.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                // valla aklıma gelmiyor
                // deneme yanılma be garides gafalar
                // umarım bunları github'da biri okumaz


                Intent intent = new Intent(getApplicationContext(), PostPrivatePage.class);
                //intent.putExtra(useremailFromFB.get(position), 0);
                //intent.putExtra(usercommentFromFB.get(position), 1);
                //intent.putExtra(userimageFromFB.get(position), 2);
                intent.putExtra("0" , useremailFromFB.get(position)).putExtra("1", usercommentFromFB.get(position)).putExtra("2", userimageFromFB.get(position))
                        .putExtra("3", profilepicFromFB.get(position)).putExtra("4", userNameFromFB.get(position));
                startActivity(intent);

            }
        });

        mAuth = FirebaseAuth.getInstance();

        listViewOniki.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {

                AlertDialog.Builder alert = new AlertDialog.Builder(OnikinciSinif.this);
                alert.setTitle("Date: " + nowFromFB.get(position));

                final String currentemail = mAuth.getCurrentUser().getEmail();
                String adminEmail = "cumasafak@gmail.com";
                final String downloadurlposition = userimageFromFB.get(position);

                int downloadurllenght = downloadurlposition.length();
                int downloadurllenght0 = downloadurllenght - 36;
                final String postCommentIDD = downloadurlposition.substring(downloadurllenght0, downloadurllenght);

                if (useremailFromFB.get(position).equals(currentemail) || currentemail.equals(adminEmail)){

                    alert.setNeutralButton("Kaldır", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {



                            final FirebaseFirestore firebaseFirestore1 = FirebaseFirestore.getInstance();
                            DocumentReference db = firebaseFirestore1.collection("twelfthgradeposts").document(postCommentIDD);
                            db.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    DocumentReference db2 = firebaseFirestore1.collection("comments").document(postCommentIDD);
                                    db2.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(OnikinciSinif.this, "Succesfully deleted", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(OnikinciSinif.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(OnikinciSinif.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });

                }

                alert.setNegativeButton("şikayet et", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        HashMap<String, Object> reportmap = new HashMap<>();
                        reportmap.put("postid", postCommentIDD);
                        reportmap.put("postFromEmail", useremailFromFB.get(position));
                        reportmap.put("postFrom", userNameFromFB.get(position));
                        reportmap.put("postText", usercommentFromFB.get(position));
                        reportmap.put("postImage", userimageFromFB.get(position));
                        reportmap.put("reportFrom", currentemail);
                        reportmap.put("date", FieldValue.serverTimestamp());


                        FirebaseFirestore.getInstance().collection("reportPost").document(postCommentIDD).set(reportmap)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(OnikinciSinif.this, "Succesfully reported", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(OnikinciSinif.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                alert.setPositiveButton("Profili gör", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        FirebaseFirestore firebaseFirestore3 = FirebaseFirestore.getInstance();
                        DocumentReference documentReference = firebaseFirestore3.collection("usersdata").document("allusers")
                                .collection(useremailFromFB.get(position)).document(useremailFromFB.get(position));

                        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                if(task.isSuccessful()){
                                    DocumentSnapshot documentSnapshot = task.getResult();
                                    if (documentSnapshot.exists()){
                                        String usergraduation = documentSnapshot.getData().get("graduation").toString();

                                        Intent intent = new Intent(getApplicationContext(), profilePage.class);
                                        intent.putExtra("profilepic", profilepicFromFB.get(position)).putExtra("username", userNameFromFB.get(position))
                                                .putExtra("email", useremailFromFB.get(position)).putExtra("graduation", usergraduation);
                                        startActivity(intent);
                                    }
                                }
                            }
                        });

                    }
                });


                alert.show();

                return true;

            }
        });

    }


    public void getDataFromFirebaseFirestore(){

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = firebaseFirestore.collection("twelfthgradeposts");

        collectionReference
                .orderBy("date", Query.Direction.DESCENDING)
                .addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                        for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){

                            Map<String, Object> data = snapshot.getData();

                            String comment = (String) data.get("usercomment");
                            String username = (String) data.get("usersname");
                            String useremail = (String) data.get("username");
                            String downloadurl = (String) data.get("downloadurl");
                            String profilepicurl = (String) data.get("profilepic");
                            String now = (String) data.get("now");

                            userNameFromFB.add(username);
                            useremailFromFB.add(useremail);
                            userimageFromFB.add(downloadurl);
                            usercommentFromFB.add(comment);
                            profilepicFromFB.add(profilepicurl);
                            nowFromFB.add(now);
                            adapter.notifyDataSetChanged();

                        }
                    }
                });
    }

}
