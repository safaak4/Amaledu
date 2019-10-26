package com.safaak.amaledu;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class odevClass extends ArrayAdapter {

    private final ArrayList<String> ogretmenname;
    private final ArrayList<String> odevtext;
    private final ArrayList<String> noww;
    private final Activity contexta;


    public odevClass(ArrayList<String> ogretmenname, ArrayList<String> odevtext, ArrayList<String> noww, Activity contexta) {
        super(contexta, R.layout.customviewforodev, ogretmenname);
        this.ogretmenname = ogretmenname;
        this.odevtext = odevtext;
        this.noww = noww;
        this.contexta = contexta;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflaterOdev = contexta.getLayoutInflater();
        View customViewOdev = layoutInflaterOdev.inflate(R.layout.customviewforodev,null, true);

        TextView textViewOgretmenname = customViewOdev.findViewById(R.id.textViewOgretmenname);
        TextView textViewOdevText = customViewOdev.findViewById(R.id.textViewOdevText);

        textViewOgretmenname.setText(ogretmenname.get(position));
        textViewOdevText.setText(odevtext.get(position));

        return customViewOdev;
    }
}
