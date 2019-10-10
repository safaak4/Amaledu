package com.safaak.amaledu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class raporlarinGorunumu extends AppCompatActivity {

    ListView listViewRaporlar;
    RaporClass adapterrapor;
    ArrayList<String> dersFromFB;
    ArrayList<String> isimFromFB;
    ArrayList<String> konuFromFB;
    ArrayList<String> sorudakikakontrolFromFB;
    ArrayList<String> soruvedakikaFromFB;
    ArrayList<String> raportarihiFromFB;
    ArrayList<String> useremailFromFB;
    String donem, sube;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raporlarin_gorunumu);

        donem = getIntent().getExtras().getString("extrasinif");
        sube = getIntent().getExtras().getString("extrasube");

        FloatingActionButton fabodev = findViewById(R.id.fabodev);
        fabodev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog odevgonderdialogg = new Dialog(raporlarinGorunumu.this);
                odevgonderdialogg.setContentView(R.layout.odevgonderdialog);
                odevgonderdialogg.setTitle("Ödev Gönder: " + donem + sube);

                final EditText odevgonderEditText = odevgonderdialogg.findViewById(R.id.odevgonderEditText);
                Button odevgonderbutton = odevgonderdialogg.findViewById(R.id.odevgonderButton);

                odevgonderbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (!odevgonderEditText.getText().toString().matches("")){

                            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                            DocumentReference documentReference = firebaseFirestore.collection(donem).document(sube);

                            HashMap<String, Object> odevData = new HashMap<>();
                            odevData.put("ogretmenemail", FirebaseAuth.getInstance().getCurrentUser().getEmail());
                            odevData.put("odevtext", odevgonderEditText.getText().toString());
                            odevData.put("sınıf", donem+sube);

                            documentReference.set(odevData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(raporlarinGorunumu.this, "Başarılı!" + donem + sube, Toast.LENGTH_SHORT).show();
                                    odevgonderdialogg.dismiss();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(raporlarinGorunumu.this, "Gönderilemedi!", Toast.LENGTH_SHORT).show();
                                    odevgonderdialogg.dismiss();
                                }
                            });
                        }else{
                            Toast.makeText(raporlarinGorunumu.this, "Boş bırakmayınız!", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                odevgonderdialogg.show();

            }
        });

        listViewRaporlar = findViewById(R.id.raporlarlistview);

        dersFromFB = new ArrayList<String>();
        isimFromFB = new ArrayList<String>();
        konuFromFB = new ArrayList<String>();
        sorudakikakontrolFromFB = new ArrayList<String>();
        soruvedakikaFromFB = new ArrayList<String>();
        raportarihiFromFB = new ArrayList<String>();
        useremailFromFB = new ArrayList<String>();

        adapterrapor = new RaporClass(dersFromFB, isimFromFB, konuFromFB, sorudakikakontrolFromFB, soruvedakikaFromFB, raportarihiFromFB, useremailFromFB, this);
        listViewRaporlar.setAdapter(adapterrapor);

        getRaporFromFirestore();

    }

    public void getRaporFromFirestore(){

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        CollectionReference collectionReferencee = firebaseFirestore.collection("Raporlar").document(donem).collection(sube);

        collectionReferencee
                    .orderBy("date", Query.Direction.DESCENDING)
                    .addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                            for (DocumentSnapshot snapshots : queryDocumentSnapshots.getDocuments()){

                                Map<String, Object> data = snapshots.getData();

                                String ders = (String) data.get("ders");
                                String isim = (String) data.get("isim");
                                String konu = (String) data.get("konu");
                                String sorudakikakontrol = (String) data.get("sorudakikakontrol");
                                String soruvedakika = (String) data.get("soruvedakika");
                                String tarih = (String) data.get("tarih");
                                String email = (String) data.get("email");

                                System.out.println("FBV: " + ders + " " + isim + " " + konu);

                                dersFromFB.add(ders);
                                isimFromFB.add(isim);
                                konuFromFB.add(konu);
                                sorudakikakontrolFromFB.add(sorudakikakontrol);
                                soruvedakikaFromFB.add(soruvedakika);
                                raportarihiFromFB.add(tarih);
                                useremailFromFB.add(email);

                                adapterrapor.notifyDataSetChanged();

                            }

                        }
                    });

    }


}
