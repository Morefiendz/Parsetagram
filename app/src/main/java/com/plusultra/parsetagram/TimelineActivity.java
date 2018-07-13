package com.plusultra.parsetagram;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.plusultra.parsetagram.model.Post;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class TimelineActivity extends AppCompatActivity {
    PostActivity home = new PostActivity();
    Button create, profile;
    InstaAdapter instaAdapter;
    ArrayList<Post> posts;
    RecyclerView rvPosts;
    private final int REQUEST_CODE = 20;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ParseUser user = ParseUser.getCurrentUser();
        if (user == null) {
            final Intent intent = new Intent(TimelineActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        setContentView(R.layout.activity_timeline);

        create = findViewById(R.id.btnCreate);
        profile = findViewById(R.id.btnProfile);
        //find RecyclerView
        rvPosts = findViewById(R.id.rvInsta);
        //init the arraylist (data source)
        posts = new ArrayList<>();
        //construct adapter from data source
        instaAdapter = new InstaAdapter(posts);
        //RecyclerView setup
        rvPosts.setLayoutManager(new LinearLayoutManager(this));
        //set the adapter
        rvPosts.setAdapter(instaAdapter);
        loadTopPosts();

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeAPost();
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(TimelineActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        // Lookup the swipe container view
        swipeContainer = findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                loadTopPosts();
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                swipeContainer.setRefreshing(false);
                fetchTimelineAsync(0);
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }

    public void fetchTimelineAsync(int page) {
        // Remember to CLEAR OUT old items before appending in the new ones
        instaAdapter.clear();
        // ...the data has come back, add new items to your adapter...
        instaAdapter.addAll(posts);
        // Now we call setRefreshing(false) to signal refresh has finished
        swipeContainer.setRefreshing(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE){
            Post post = Parcels.unwrap(data.getParcelableExtra("post"));

            posts.add(0, post);
            instaAdapter.notifyItemInserted(0);
            rvPosts.scrollToPosition(0);
        }
    }

    public void loadTopPosts() {
        final Post.Query postQuery = new Post.Query();
        postQuery
                .getTop()
                .withUser();

        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null){
                    for (int i = 0; i < objects.size(); i++){
                        posts.add(0, objects.get(i));
                        instaAdapter.notifyItemInserted(0);
                        // Log.d("PostActivity", "Post id: [" + i + "] = " + objects.get(i).getDescription() + "\nusername = " + objects.get(i).getUser().getUsername());
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    private void makeAPost(){
        final Intent intent;
        intent = new Intent(TimelineActivity.this, PostActivity.class);
        startActivity(intent);
    }
}
