package com.plusultra.parsetagram;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.ParseUser;
import com.plusultra.parsetagram.model.Post;

public class DetailActivity extends AppCompatActivity {
    private ImageView picture;
    private TextView name, caption, time;
    Post post = new Post();
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ParseUser user = ParseUser.getCurrentUser();

        picture = findViewById(R.id.ivPhoto);
        name = findViewById(R.id.tvName);
        caption = findViewById(R.id.tvCap);
        time = findViewById(R.id.tvTime);

        Glide.with(context)
                .load(post.getImage().getUrl())
                .into(picture);

        name.setText(String.valueOf(user.getUsername()));
        caption.setText(String.valueOf(post.getDescription()));
        time.setText(String.valueOf(post.getCreatedAt()));
    }
}
