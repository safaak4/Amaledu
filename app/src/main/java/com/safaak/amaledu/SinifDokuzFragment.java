package com.safaak.amaledu;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.ads.AdRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class SinifDokuzFragment extends Fragment {

    ListView listView;
    PostClass adapter;
    ArrayList<String> useremailFromFB;
    ArrayList<String> userNameFromFB;
    ArrayList<String> userimageFromFB;
    ArrayList<String> usercommentFromFB;
    ArrayList<String> profilepicFromFB;
    ArrayList<String> nowFromFB;
    private FirebaseAuth mAuth;
    private Context sinifdokuzcontext;


  /*  @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        sinifdokuzcontext = context;

    } */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_dokuzuncu_sinif, container, false);

        //listView = view.findViewById(R.id.listView);

        return view;
    }

    /* @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);






        useremailFromFB = new ArrayList<String>();
        usercommentFromFB = new ArrayList<String>();
        userimageFromFB = new ArrayList<String>();
        profilepicFromFB = new ArrayList<String>();
        userNameFromFB = new ArrayList<String>();
        nowFromFB = new ArrayList<String>();
        //profilepicFromFB;

        // firebaseDatabase = FirebaseDatabase.getInstance();
        //myRef = firebaseDatabase.getReference();

        adapter = new PostClass(useremailFromFB, userNameFromFB, usercommentFromFB, userimageFromFB, profilepicFromFB, nowFromFB,
                getActivity());
        listView.setAdapter(adapter);


        //firebase firestore
        getDataFromFirebaseFirestore();
    } */

    /*public void getDataFromFirebase(){

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
    }*/

    public void getDataFromFirebaseFirestore(){


        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = firebaseFirestore.collection("ninthgradeposts");


        collectionReference
                .orderBy("date", Query.Direction.DESCENDING)
                .addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {

                        for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){

                            Map<String, Object> data = snapshot.getData();

                            String comment = (String) data.get("usercomment");
                            String username = (String) data.get("usersname");
                            String useremail = (String) data.get("username");
                            String downloadurl = (String) data.get("downloadurl");
                            String profilepicurl = (String) data.get("profilepic");
                            String now = (String) data.get("now");

                            userNameFromFB.add(username);
                            useremailFromFB.add(useremail);
                            userimageFromFB.add(downloadurl);
                            usercommentFromFB.add(comment);
                            profilepicFromFB.add(profilepicurl);
                            nowFromFB.add(now);

                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    public SinifDokuzFragment() {
        // Required empty public constructor


    }

}
