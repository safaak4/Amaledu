package com.safaak.amaledu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class PostPrivatePage extends AppCompatActivity {

    TextView userEmailPostPrivate, commentTextPostPrivate;
    public ImageView imageViewPostPrivate;

    ListView listviewforcomments;
    PostComments adapterforcomments;
    ArrayList<String> userEmailForCommentsFromFirebase;
    ArrayList<String> commentsFromFirebase;
    ArrayList<String> profilepicsFromFirebase;
    ArrayList<String> usernameForCommentsFromFB;
    ArrayList<String> nowComments;
    String postImage, profilepicimage;
    ImageView imageViewProfilePicCommentsPage;

    private FirebaseAuth mAuth;
    EditText userscomment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_private_page);
        listviewforcomments = findViewById(R.id.listViewForComments);

        mAuth = FirebaseAuth.getInstance();
        userscomment = findViewById(R.id.editText4);

        userEmailPostPrivate = findViewById(R.id.userEmailTextViewCustomView);
        commentTextPostPrivate = findViewById(R.id.commentTextCustomView);
        imageViewPostPrivate = findViewById(R.id.imageViewCustomView);
        imageViewProfilePicCommentsPage = findViewById(R.id.imageViewUserImagePostPrivate);

        userEmailPostPrivate.setText(getIntent().getExtras().getString("4"));
        commentTextPostPrivate.setText(getIntent().getExtras().getString("1"));

        postImage = getIntent().getExtras().getString("2");
        Picasso.get().load(postImage).into(imageViewPostPrivate);

        profilepicimage = getIntent().getExtras().getString("3");
        Picasso.get().load(profilepicimage).resize(150,150).into(imageViewProfilePicCommentsPage);


        userEmailForCommentsFromFirebase = new ArrayList<String>();
        commentsFromFirebase = new ArrayList<String>();
        profilepicsFromFirebase = new ArrayList<String>();
        usernameForCommentsFromFB = new ArrayList<String>();
        nowComments = new ArrayList<String>();

        adapterforcomments = new PostComments(userEmailForCommentsFromFirebase, commentsFromFirebase, profilepicsFromFirebase, usernameForCommentsFromFB, nowComments, this);
        listviewforcomments.setAdapter(adapterforcomments);

        getCommentsFromFirestore();

        listviewforcomments.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {

                AlertDialog.Builder alert = new AlertDialog.Builder(PostPrivatePage.this);
                alert.setTitle("Date: " + nowComments.get(position));


                final String currentemail = mAuth.getCurrentUser().getEmail();
                String adminEmail = "cumasafak@gmail.com";
                final String downloadurlposition = postImage;
                int downloadurllenght = downloadurlposition.length();
                int downloadurllenght0 = downloadurllenght - 36;
                final String postCommentIDD = downloadurlposition.substring(downloadurllenght0, downloadurllenght);

                if (userEmailForCommentsFromFirebase.get(position).equals(currentemail) || currentemail.equals(adminEmail)){
                    alert.setNeutralButton("Kaldır", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            final FirebaseFirestore firebaseFirestore1 = FirebaseFirestore.getInstance();
                            DocumentReference db = firebaseFirestore1.collection("comments").document(postCommentIDD)
                                    .collection("commentss").document(nowComments.get(position));
                            db.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    DocumentReference db2 = firebaseFirestore1.collection("comments").document(postCommentIDD);
                                    db2.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(PostPrivatePage.this, "Succesfully deleted", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(PostPrivatePage.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(PostPrivatePage.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });


                }

                alert.setNegativeButton("şikayet et", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        HashMap<String, Object> reportmap = new HashMap<>();
                        reportmap.put("postid", postCommentIDD);
                        reportmap.put("postFromEmail", userEmailForCommentsFromFirebase.get(position));
                        reportmap.put("postFrom", usernameForCommentsFromFB.get(position));
                        reportmap.put("postText", commentsFromFirebase.get(position));
                        reportmap.put("postImage", postImage);
                        reportmap.put("reportFrom", currentemail);
                        reportmap.put("date", FieldValue.serverTimestamp());


                        FirebaseFirestore.getInstance().collection("reportComment").document(postCommentIDD)
                                .collection(currentemail).document(userEmailForCommentsFromFirebase.get(position) + " " + commentsFromFirebase.get(position)).set(reportmap)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(PostPrivatePage.this, "Succesfully reported", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(PostPrivatePage.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                alert.setPositiveButton("Profili gör", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        FirebaseFirestore firebaseFirestore3 = FirebaseFirestore.getInstance();
                        DocumentReference documentReference = firebaseFirestore3.collection("usersdata").document("allusers")
                                .collection(userEmailForCommentsFromFirebase.get(position)).document(userEmailForCommentsFromFirebase.get(position));

                        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                if(task.isSuccessful()){
                                    DocumentSnapshot documentSnapshot = task.getResult();
                                    if (documentSnapshot.exists()){
                                        String usergraduation = documentSnapshot.getData().get("graduation").toString();

                                        Intent intent = new Intent(getApplicationContext(), profilePage.class);
                                        intent.putExtra("profilepic", profilepicsFromFirebase.get(position)).putExtra("username", usernameForCommentsFromFB.get(position))
                                                .putExtra("email", userEmailForCommentsFromFirebase.get(position)).putExtra("graduation", usergraduation);
                                        startActivity(intent);
                                    }
                                }
                            }
                        });

                    }
                });

                alert.show();
                return true;
            }
        });

    }

    public void uploadComment(View view){

        if (userscomment.getText().toString().matches("")){
            Toast.makeText(this, "Lütfen bir giriş yapın!", Toast.LENGTH_SHORT).show();
        }else {

            String userEmailForComment = mAuth.getCurrentUser().getEmail();
            String userscomments = userscomment.getText().toString();

            Date now = new Date();
            final String nowstring = now.toString();

            HashMap<String, Object> postComments = new HashMap<>();
            postComments.put("useremail", userEmailForComment);
            postComments.put("comment", userscomments);
            postComments.put("date", FieldValue.serverTimestamp());
            postComments.put("now", nowstring);

            int postImagelenght = postImage.length();
            int postImagelenght0 = postImagelenght - 36;
            //System.out.println("FBV: " + postImage.substring(postImagelenght0, postImagelenght));
            final String postCommentID = postImage.substring(postImagelenght0, postImagelenght);
            //System.out.println("FBV:  " + FieldValue.serverTimestamp().toString());
            //System.out.println("FBS: " + now.toString());

            FirebaseFirestore firebaseFirestore4 = FirebaseFirestore.getInstance();
            DocumentReference documentReference = firebaseFirestore4.collection("usersdata").document("allusers")
                    .collection(userEmailForComment).document(userEmailForComment);
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if(task.isSuccessful()){
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (documentSnapshot.exists()){
                            String username = documentSnapshot.getData().get("usernameandsurname").toString();
                            String profilepicc = documentSnapshot.getData().get("profilepic").toString();

                            HashMap<String, Object> postName = new HashMap<>();
                            postName.put("username", username);
                            postName.put("profilepic", profilepicc);

                            FirebaseFirestore.getInstance().collection("comments").document(postCommentID)
                                    .collection("commentss").document(nowstring).update(postName);

                        }
                    }

                }
            });

            FirebaseFirestore.getInstance().collection("comments").document(postCommentID)
                    .collection("commentss").document(nowstring).set(postComments)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            userscomment.setText("");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PostPrivatePage.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void getCommentsFromFirestore() {

        int postImagelenght = postImage.length();
        int postImagelenght0 = postImagelenght - 36;
        String postCommentID = postImage.substring(postImagelenght0, postImagelenght);

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        CollectionReference collectionReference2 = firebaseFirestore.collection("comments").document(postCommentID).collection("commentss");
        collectionReference2
                .orderBy("date", Query.Direction.ASCENDING)
                .addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                        for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){

                            Map<String, Object> data = snapshot.getData();

                            String comment = (String) data.get("comment");
                            String useremail = (String) data.get("useremail");
                            String profilepic = (String) data.get("profilepic");
                            String username = (String) data.get("username");
                            String now = (String) data.get("now");

                            userEmailForCommentsFromFirebase.add(useremail);
                            commentsFromFirebase.add(comment);
                            profilepicsFromFirebase.add(profilepic);
                            usernameForCommentsFromFB.add(username);
                            nowComments.add(now);

                            adapterforcomments.notifyDataSetChanged();

                        }
                    }
                });
    }

}
