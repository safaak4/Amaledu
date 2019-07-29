package com.safaak.amaledu;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PostClass extends ArrayAdapter<String> {


    private final ArrayList<String> userEmail;
    private final ArrayList<String> userName;
    private final ArrayList<String> userComment;
    private final ArrayList<String> userImage;
    private final ArrayList<String> profilePic;
    private final ArrayList<String> noww;
    private final Activity context;




    public PostClass(ArrayList<String> userEmail, ArrayList<String> userName, ArrayList<String> userComment, ArrayList<String> userImage,
                     ArrayList<String> profilePic, ArrayList<String> noww, Activity context) {
        super(context, R.layout.custom_view, userEmail);
        this.userEmail = userEmail;
        this.userName = userName;
        this.userComment = userComment;
        this.userImage = userImage;
        this.profilePic = profilePic;
        this.noww = noww;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        LayoutInflater layoutInflater = context.getLayoutInflater();
        View customView = layoutInflater.inflate(R.layout.custom_view,null, true);

        TextView userEmailText = customView.findViewById(R.id.userEmailTextViewCustomView);
        TextView postCommentText = customView.findViewById(R.id.commentTextCustomView);
        ImageView postImageView = customView.findViewById(R.id.imageViewCustomView);
        ImageView imageViewUserImageCustomView = customView.findViewById(R.id.imageViewUserImageCustomView);

        userEmailText.setText(userName.get(position));
        postCommentText.setText(userComment.get(position));
        Picasso.get().load(userImage.get(position)).into(postImageView);
        Picasso.get().load(profilePic.get(position)).resize(150,150).into(imageViewUserImageCustomView);

        return customView;
    }


}
