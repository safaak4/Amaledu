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

public class PostComments extends ArrayAdapter<String> {

    private final ArrayList<String> commentsEmail;
    private final ArrayList<String> comments;
    private final ArrayList<String> profilepicscomments;
    private final ArrayList<String> usernamecomments;
    private final ArrayList<String> nowcomments;
    private final Activity context;


    public PostComments(ArrayList<String> commentsEmail, ArrayList<String> comments, ArrayList<String> profilepicscomments, ArrayList<String> usernamecomments,
                        ArrayList<String> nowcomments, Activity context) {
        super(context, R.layout.customviewforcomments, commentsEmail);
        this.commentsEmail = commentsEmail;
        this.comments = comments;
        this.profilepicscomments = profilepicscomments;
        this.usernamecomments = usernamecomments;
        this.nowcomments = nowcomments;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = context.getLayoutInflater();
        View customViewForComments = layoutInflater.inflate(R.layout.customviewforcomments,null, true);

        TextView userEmailForComments = customViewForComments.findViewById(R.id.userEmailTextViewForComments);
        TextView commenTextCustomViewForComments = customViewForComments.findViewById(R.id.commentTextForComments);
        ImageView imageViewProfilePicComments = customViewForComments.findViewById(R.id.imageViewUserImageComments);

        userEmailForComments.setText(usernamecomments.get(position));
        commenTextCustomViewForComments.setText(comments.get(position));
        Picasso.get().load(profilepicscomments.get(position)).resize(150,150).into(imageViewProfilePicComments);


        return customViewForComments;
    }
}
