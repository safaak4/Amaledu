package com.safaak.amaledu;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class SinifDokuzFragment extends Fragment {

    /*ListView listView;
    PostClass adapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    ArrayList<String> useremailFromFB;
    ArrayList<String> userimageFromFB;
    ArrayList<String> usercommentFromFB;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        useremailFromFB = new ArrayList<String>();
        usercommentFromFB = new ArrayList<String>();
        userimageFromFB = new ArrayList<String>();

        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();

        adapter = new PostClass(useremailFromFB, usercommentFromFB, userimageFromFB, getActivity());
        listView.setAdapter(adapter);

        getDataFromFirebase();
    }

    public void getDataFromFirebase(){

        DatabaseReference newReference = firebaseDatabase.getReference("ninthgradeposts");
        newReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //System.out.println("FBV children: " + dataSnapshot.getChildren());
                //System.out.println("FBV key: " + dataSnapshot.getKey());
                //System.out.println("FBV value: " + dataSnapshot.getValue());
                //System.out.println("FBV priority: " + dataSnapshot.getPriority());

                for (DataSnapshot ds : dataSnapshot.getChildren()){

                    //System.out.println("FBV ds value :" + ds.getValue());


                    HashMap<String, String> hashMap = (HashMap<String, String>) ds.getValue();

                    //System.out.println("FBV User email: " + hashMap.get("username"));
                    useremailFromFB.add(hashMap.get("username"));
                    usercommentFromFB.add(hashMap.get("usercomment"));
                    userimageFromFB.add(hashMap.get("downloadurl"));
                    adapter.notifyDataSetChanged();



                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });
    } */

    public SinifDokuzFragment() {
        // Required empty public constructor


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sinif_dokuz, container, false);

       // listView = view.findViewById(R.id.listView);

        return view;
    }

}
