package com.safaak.amaledu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raporlarin_gorunumu);

        listViewRaporlar = findViewById(R.id.raporlarlistview);

        dersFromFB = new ArrayList<String>();
        isimFromFB = new ArrayList<String>();
        konuFromFB = new ArrayList<String>();
        sorudakikakontrolFromFB = new ArrayList<String>();
        soruvedakikaFromFB = new ArrayList<String>();
        raportarihiFromFB = new ArrayList<String>();
        useremailFromFB = new ArrayList<String>();

       adapterrapor = new RaporClass(dersFromFB, isimFromFB, konuFromFB, sorudakikakontrolFromFB, soruvedakikaFromFB, raportarihiFromFB, useremailFromFB, this);


    }





}
