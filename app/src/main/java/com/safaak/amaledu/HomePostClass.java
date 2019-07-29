package com.safaak.amaledu;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomePostClass extends ArrayAdapter<String> {


    private final ArrayList<String> posttext;
    private final ArrayList<String> postimage;
    private final Activity contexthome;


    public HomePostClass(ArrayList<String> posttext, ArrayList<String> postimage, Activity contexthome) {
        super(contexthome, R.layout.homecustomview, posttext);
        this.posttext = posttext;
        this.postimage = postimage;
        this.contexthome = contexthome;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = contexthome.getLayoutInflater();
        View customView0 = layoutInflater.inflate(R.layout.homecustomview,null, true);

        TextView hometext = customView0.findViewById(R.id.commentTextHomeCustomView);
        ImageView homeimage = customView0.findViewById(R.id.imageViewHomeCustomView);

        hometext.setText(posttext.get(position));
        Picasso.get().load(postimage.get(position)).into(homeimage);

        return customView0;
    }
}
