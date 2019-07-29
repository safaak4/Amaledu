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
public class bugfeedback extends Fragment {


    public bugfeedback() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view2 = inflater.inflate(R.layout.fragment_bugfeedback, container, false);

        WebView webViewBug = view2.findViewById(R.id.webViewBug);
        webViewBug.getSettings().setJavaScriptEnabled(true);
        webViewBug.loadUrl("https://forms.gle/UFhzakYNsxsDxq7R7");


        return view2;
    }

}
