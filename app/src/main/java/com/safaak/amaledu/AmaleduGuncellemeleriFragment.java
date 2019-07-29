package com.safaak.amaledu;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;


/**
 * A simple {@link Fragment} subclass.
 */
public class AmaleduGuncellemeleriFragment extends Fragment {


    public AmaleduGuncellemeleriFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewOrontes = inflater.inflate(R.layout.fragment_amaledu_guncellemeleri, container, false);

        WebView webViewOrontes = viewOrontes.findViewById(R.id.webViewOrontes);
        webViewOrontes.getSettings().setJavaScriptEnabled(true);
        webViewOrontes.loadUrl("https://www.instagram.com/orontesdev/");

        return viewOrontes;
    }

}
