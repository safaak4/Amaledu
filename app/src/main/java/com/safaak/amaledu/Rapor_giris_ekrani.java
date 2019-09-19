package com.safaak.amaledu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.DataOutputStream;
import java.util.HashMap;

public class Rapor_giris_ekrani extends AppCompatActivity {


    EditText calisilanKonuEditText, soruSayisiEditText, calisilanDakikakEditText;
    String[]dersler = {"Matematik", "Türk Dili ve Edebiyatı", "Coğrafya", "Tarih", "Fizik", "Biyoloji", "Kimya", "Felsefe", "İngilizce", "Almanca"};
    String secilenders;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rapor_giris_ekrani);

        calisilanKonuEditText = findViewById(R.id.editText11);
        soruSayisiEditText = findViewById(R.id.editText12);
        calisilanDakikakEditText = findViewById(R.id.editText13);
        mAuth = FirebaseAuth.getInstance();


        Spinner spinner2 = findViewById(R.id.spinner2);
        ArrayAdapter adapterforlessons = new ArrayAdapter(Rapor_giris_ekrani.this, android.R.layout.simple_spinner_item, dersler);
        adapterforlessons.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner2.setAdapter(adapterforlessons);


        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch(i) {
                    case 0:
                        secilenders = "Matematik";
                        break;
                    case 1:
                        secilenders = "Edebiyat";
                        break;
                    case 2:
                        secilenders = "Coğrafya";
                        break;
                    case 3:
                        secilenders = "Tarih";
                        break;
                    case 4:
                        secilenders = "Fizik";
                        break;
                    case 5:
                        secilenders = "Biyoloji";
                        break;
                    //"Kimya", "Felsefe", "İngilizce", "Almanca"
                    case 6:
                        secilenders = "Kimya";
                        break;
                    case 7:
                        secilenders = "Felsefe";
                        break;
                    case 8:
                        secilenders = "İngilizce";
                        break;
                    case 9:
                        secilenders = "Almanca";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    } // oncreate

    public void raporupload(View view){

        if (calisilanKonuEditText.getText().toString().matches("") || secilenders.matches("")){
            Toast.makeText(this, "Hatalı giriş!", Toast.LENGTH_SHORT).show();
        }else{

            if (soruSayisiEditText.getText().toString().matches("") && calisilanDakikakEditText.getText().toString().matches("")){
                Toast.makeText(this, "Lütfen bir değer girin", Toast.LENGTH_SHORT).show();
            }else if (!soruSayisiEditText.getText().toString().matches("") && calisilanDakikakEditText.getText().toString().matches("")){
                // soru sayısı upload edilr
                final DocumentReference documentReference = FirebaseFirestore.getInstance().collection("usersdata").document("allusers")
                        .collection(mAuth.getCurrentUser().getEmail()).document(mAuth.getCurrentUser().getEmail());

                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();

                            if (documentSnapshot.exists()){
                                String usersname = documentSnapshot.getData().get("usernameandsurname").toString();
                                String usersgraduation = documentSnapshot.getData().get("graduation").toString();
                                String usersbranch = documentSnapshot.getData().get("branch").toString();

                                HashMap<String, Object> girilenraporr = new HashMap<>();
                                girilenraporr.put("isim", usersname);
                                girilenraporr.put("sınıfı", usersgraduation + usersbranch);


                                //                 DERS ÇALIŞMANIN TARİHİ GİRİLECEK.           //
                                // DİALOG İÇERİSİNE CALENDERVİEW YERLEŞTİR  //

                               // FirebaseFirestore.getInstance().collection("raporlar").document( /*sınıfı */ ).collection(/* sınıfı */ )
                                 //       .document(/*tarih*/).set();
                            }

                        }


                    }
                });








            }else if (soruSayisiEditText.getText().toString().matches("") && !calisilanDakikakEditText.getText().toString().matches("")){
                // calisilan dakika upload edilir

            }else {
                // iki edittext de dolu olacağı için toast mesajı verilir
                Toast.makeText(this, "Lütfen sadece bir değer giriniz", Toast.LENGTH_SHORT).show();
            }

        }








    }



}
