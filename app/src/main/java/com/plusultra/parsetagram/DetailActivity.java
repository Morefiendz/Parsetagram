package com.plusultra.parsetagram;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.ParseUser;
import com.plusultra.parsetagram.model.Post;

import org.parceler.Parcels;

public class DetailActivity extends AppCompatActivity {
    private ImageView picture;
    private TextView name, caption, time;
    Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ParseUser user = ParseUser.getCurrentUser();
        post = Parcels.unwrap(getIntent().getParcelableExtra("post"));

        picture = findViewById(R.id.ivPhoto);
        name = findViewById(R.id.tvName);
        caption = findViewById(R.id.tvCap);
        time = findViewById(R.id.tvTime);

        Glide.with(this)
                .load(post.getImage().getUrl())
                .into(picture);

        name.setText(String.valueOf(user.getUsername()));
        caption.setText(String.valueOf(post.getDescription()));
        time.setText(String.valueOf(post.getCreatedAt()));
    }
}
