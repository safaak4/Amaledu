package com.safaak.amaledu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raporlarin_gorunumu);

        mAdView = findViewById(R.id.adViewabc);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        donem = getIntent().getExtras().getString("extrasinif");
        sube = getIntent().getExtras().getString("extrasube");

        FloatingActionButton fabodev = findViewById(R.id.fabodev);
        fabodev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                if (FirebaseAuth.getInstance().getCurrentUser().getEmail().equals("cumasafak@gmail.com")){
                    final Dialog odevgonderdialogg = new Dialog(raporlarinGorunumu.this);
                    odevgonderdialogg.setContentView(R.layout.odevgonderdialog);
                    odevgonderdialogg.setTitle("Ödev Gönder: " + donem + sube);



                    final EditText odevgonderEditText = odevgonderdialogg.findViewById(R.id.odevgonderEditText);
                    Button odevgonderbutton = odevgonderdialogg.findViewById(R.id.odevgonderButton);

                    odevgonderbutton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (!odevgonderEditText.getText().toString().matches("")){
                                        //isim alınır
                                String usersname = getIntent().getExtras().getString("extraname");

                                //ödev upload edilir
                                Date noww = new Date();

                                HashMap<String, Object> odevmap = new HashMap<>();
                                odevmap.put("odevtext", odevgonderEditText.getText().toString());
                                odevmap.put("ogretmen", usersname);
                                odevmap.put("sinif", donem+sube);
                                odevmap.put("date", FieldValue.serverTimestamp());
                                odevmap.put("now", noww.toString());

                                UUID uuid = UUID.randomUUID();
                                FirebaseFirestore.getInstance().collection("odevler").document(donem)
                                        .collection(sube).document(uuid.toString()).set(odevmap);

                                Toast.makeText(raporlarinGorunumu.this, "Başarılı!" + donem + sube, Toast.LENGTH_SHORT).show();
                                odevgonderdialogg.dismiss();


                            }else{
                                Toast.makeText(raporlarinGorunumu.this, "Boş bırakmayınız!", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                    odevgonderdialogg.show();
                }else{
                    Toast.makeText(raporlarinGorunumu.this, "Buna izniniz yok!", Toast.LENGTH_SHORT).show();
                }
                

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
        CollectionReference collectionReferencee = firebaseFirestore.collection("raporlar").document(donem).collection(sube);

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

                                if (FirebaseAuth.getInstance().getCurrentUser().getEmail().equals("cumasafak@gmail.com")){
                                    //ogretmenler icin

                                    dersFromFB.add(ders);
                                    isimFromFB.add(isim);
                                    konuFromFB.add(konu);
                                    sorudakikakontrolFromFB.add(sorudakikakontrol);
                                    soruvedakikaFromFB.add(soruvedakika);
                                    raportarihiFromFB.add(tarih);
                                    useremailFromFB.add(email);

                                    adapterrapor.notifyDataSetChanged();
                                }else {
                                    if (FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(email)){
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

                            }

                        }
                    });

    }


}
