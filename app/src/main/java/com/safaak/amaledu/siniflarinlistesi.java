package com.safaak.amaledu;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class siniflarinlistesi extends AppCompatActivity {

    ListView siniflarlistview;
    public String[] siniflarisimleri = {"2020a", "2020b", "2020c", "2020d", "2020e", "2020f", "2021a", "2021b", "2021c", "2021d", "2021e",
    "2022a", "2022b", "2022c", "2022d", "2023a", "2023b", "2023c", "2023d"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_siniflarinlistesi);



        siniflarlistview = findViewById(R.id.siniflarlistview);

        final ArrayAdapter<String> sinifAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                android.R.id.text1, siniflarisimleri);

        siniflarlistview.setAdapter(sinifAdapter);

        siniflarlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            //19 sınıf var
                Intent intent00 = new Intent(getApplicationContext(), raporlarinGorunumu.class);

                switch (i){
                    case 0:
                        //2020a
                        intent00.putExtra("extrasinif", "2020").putExtra("extrasube", "a");
                        startActivity(intent00);
                        break;
                    case 1:
                        //2020b
                        intent00.putExtra("extrasinif", "2020").putExtra("extrasube", "b");
                        startActivity(intent00);
                        break;
                    case 2:
                        //2020c
                        intent00.putExtra("extrasinif", "2020").putExtra("extrasube", "c");
                        startActivity(intent00);
                        break;
                    case 3:
                        //2020d
                        intent00.putExtra("extrasinif", "2020").putExtra("extrasube", "d");
                        startActivity(intent00);
                        break;
                    case 4:
                        //2020e
                        intent00.putExtra("extrasinif", "2020").putExtra("extrasube", "e");
                        startActivity(intent00);
                        break;
                    case 5:
                        //2020f
                        intent00.putExtra("extrasinif", "2020").putExtra("extrasube", "f");
                        startActivity(intent00);
                        break;
                    case 6:
                        //2021a
                        intent00.putExtra("extrasinif", "2021").putExtra("extrasube", "a");
                        startActivity(intent00);
                        break;
                    case 7:
                        //2021b
                        intent00.putExtra("extrasinif", "2021").putExtra("extrasube", "b");
                        startActivity(intent00);
                        break;
                    case 8:
                        //2021c
                        intent00.putExtra("extrasinif", "2021").putExtra("extrasube", "c");
                        startActivity(intent00);
                        break;
                    case 9:
                        //2021d
                        intent00.putExtra("extrasinif", "2021").putExtra("extrasube", "d");
                        startActivity(intent00);
                        break;
                    case 10:
                        //2021e
                        intent00.putExtra("extrasinif", "2021").putExtra("extrasube", "e");
                        startActivity(intent00);
                        break;
                    case 11:
                        //2022a
                        intent00.putExtra("extrasinif", "2022").putExtra("extrasube", "a");
                        startActivity(intent00);
                        break;
                    case 12:
                        //2022b
                        intent00.putExtra("extrasinif", "2022").putExtra("extrasube", "b");
                        startActivity(intent00);
                        break;
                    case 13:
                        //2022c
                        intent00.putExtra("extrasinif", "2022").putExtra("extrasube", "c");
                        startActivity(intent00);
                        break;
                    case 14:
                        //2022d
                        intent00.putExtra("extrasinif", "2022").putExtra("extrasube", "d");
                        startActivity(intent00);
                        break;
                    case 15:
                        //2023a
                        intent00.putExtra("extrasinif", "2023").putExtra("extrasube", "a");
                        startActivity(intent00);
                        break;
                    case 16:
                        //2023b
                        intent00.putExtra("extrasinif", "2023").putExtra("extrasube", "b");
                        startActivity(intent00);
                        break;
                    case 17:
                        //2023c
                        intent00.putExtra("extrasinif", "2023").putExtra("extrasube", "c");
                        startActivity(intent00);
                        break;
                    case 18:
                        //2023d
                        intent00.putExtra("extrasinif", "2023").putExtra("extrasube", "d");
                        startActivity(intent00);
                        break;
                    default:
                        Toast.makeText(siniflarinlistesi.this, "Bir sorun oluştu", Toast.LENGTH_SHORT).show();
                }



            }
        });

    }
}
