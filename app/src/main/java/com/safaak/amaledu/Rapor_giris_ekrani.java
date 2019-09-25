package com.safaak.amaledu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;

public class Rapor_giris_ekrani extends AppCompatActivity {

    Button tarihsecbutton;
    EditText calisilanKonuEditText, soruSayisiEditText, calisilanDakikakEditText;
    String[]dersler = {"Matematik", "Türk Dili ve Edebiyatı", "Coğrafya", "Tarih", "Fizik", "Biyoloji", "Kimya", "Felsefe", "İngilizce", "Almanca"};
    String secilenders, selectedDate;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rapor_giris_ekrani);

        calisilanKonuEditText = findViewById(R.id.editText11);
        soruSayisiEditText = findViewById(R.id.editText12);
        calisilanDakikakEditText = findViewById(R.id.editText13);
        mAuth = FirebaseAuth.getInstance();
        tarihsecbutton = findViewById(R.id.tarihsecbutton);


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

        if (calisilanKonuEditText.getText().toString().matches("") || secilenders.matches("") || selectedDate.matches("")){
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
                                Date now = new Date();
                                String documentdatee = now.toString().substring(4,7)+ now.toString().substring(8,10)+ "." + now.toString().substring(11,13) + now.toString().substring(14,16) + now.toString().substring(17,19);
                                //String documentdatee = selectedDate + now.toString().substring(11,13) + now.toString().substring(14,16) + now.toString().substring(17,19);
                                //System.out.println("FBV: " + documentdatee);

                                HashMap<String, Object> girilenraporr = new HashMap<>();
                                girilenraporr.put("isim", usersname);
                                girilenraporr.put("sınıf", usersgraduation + usersbranch);
                                girilenraporr.put("tarih", selectedDate);
                                girilenraporr.put("ders", secilenders);
                                girilenraporr.put("konu", calisilanKonuEditText.getText().toString());
                                girilenraporr.put("soruvedakika", soruSayisiEditText.getText().toString());
                                girilenraporr.put("sorudakikakontrol", "1");
                                girilenraporr.put("email", mAuth.getCurrentUser().getEmail());

                                //                 DERS ÇALIŞMANIN TARİHİ GİRİLECEK.           //  yapıldı
                                // DİALOG İÇERİSİNE CALENDERVİEW YERLEŞTİR  //    yapıldı

                                FirebaseFirestore.getInstance().collection("raporlar").document(usersgraduation).collection(usersbranch)
                                        .document(documentdatee).set(girilenraporr)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(Rapor_giris_ekrani.this, "Gönderildi", Toast.LENGTH_SHORT).show();
                                                Rapor_giris_ekrani.super.onBackPressed();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Rapor_giris_ekrani.this, "Hata! Rapor gönderilemedi.", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }

                        }

                    }
                });

            }else if (soruSayisiEditText.getText().toString().matches("") && !calisilanDakikakEditText.getText().toString().matches("")){
                // calisilan dakika upload edilir

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
                                Date now = new Date();
                                String documentdatee = now.toString().substring(4,7)+ now.toString().substring(8,10)+ "." + now.toString().substring(11,13) + now.toString().substring(14,16) + now.toString().substring(17,19);
                               // System.out.println("FBV: " + documentdatee);

                                HashMap<String, Object> girilenraporr = new HashMap<>();
                                girilenraporr.put("isim", usersname);
                                girilenraporr.put("sınıf", usersgraduation + usersbranch);
                                girilenraporr.put("tarih", selectedDate);
                                girilenraporr.put("ders", secilenders);
                                girilenraporr.put("konu", calisilanKonuEditText.getText().toString());
                                girilenraporr.put("soruvedakika", calisilanDakikakEditText.getText().toString());
                                girilenraporr.put("sorudakikakontrol", "0");
                                girilenraporr.put("email", mAuth.getCurrentUser().getEmail());

                                //                 DERS ÇALIŞMANIN TARİHİ GİRİLECEK.           //  yapıldı
                                // DİALOG İÇERİSİNE CALENDERVİEW YERLEŞTİR  //    yapıldı

                                FirebaseFirestore.getInstance().collection("raporlar").document(usersgraduation).collection(usersbranch)
                                        .document(documentdatee).set(girilenraporr)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(Rapor_giris_ekrani.this, "Gönderildi", Toast.LENGTH_SHORT).show();
                                                Rapor_giris_ekrani.super.onBackPressed();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Rapor_giris_ekrani.this, "Hata! Rapor gönderilemedi.", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }

                        }

                    }
                });

            }else {
                // iki edittext de dolu olacağı için toast mesajı verilir
                Toast.makeText(this, "Lütfen sadece bir değer giriniz", Toast.LENGTH_SHORT).show();
            }

        }

    }

    public void tarihsec(View view){
        final Dialog tarihsecdialog = new Dialog(this);
        tarihsecdialog.setContentView(R.layout.datedialog);

        CalendarView calendarView = tarihsecdialog.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int i, int i1, int i2) {

                selectedDate = i2 + "." + i1 + "." + i;
                tarihsecbutton.setText(selectedDate);
                tarihsecdialog.dismiss();
                //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                //String selectedDate = simpleDateFormat.format(new Date(calendarView.));
               // System.out.println("FBVV: " + selectedDate);
            }
        });
        tarihsecdialog.show();
    }

}
