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
public class DuyurularFragment extends Fragment {


    public DuyurularFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View viewduyurular = inflater.inflate(R.layout.fragment_duyurular, container, false);
        WebView webViewDuyurular = viewduyurular.findViewById(R.id.webViewDuyurular);
        webViewDuyurular.getSettings().setJavaScriptEnabled(true);
        webViewDuyurular.loadUrl("http://amal.meb.k12.tr/");


        return viewduyurular;
    }

}
