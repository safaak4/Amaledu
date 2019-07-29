package com.safaak.amaledu;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;


/**
 * A simple {@link Fragment} subclass.
 */
public class userFeedbackForm extends Fragment {


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);







    }

    public userFeedbackForm() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_feedback_form, container, false);
        WebView webView = view.findViewById(R.id.webViewUserFeedback);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://forms.gle/rVtWrA1E2T1BowTt6");

        return view;
    }

}
