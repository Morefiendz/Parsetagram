package com.plusultra.parsetagram;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.plusultra.parsetagram.model.Post;

import org.parceler.Parcels;

import java.util.List;

public class InstaAdapter extends RecyclerView.Adapter<InstaAdapter.ViewHolder>{
    Context context;
    List<Post> posts;

    public InstaAdapter(List<Post> posts) {
        this.posts = posts;
    }

    // Clean all elements of the recycler
    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Post> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View postView = inflater.inflate(R.layout.item_post, parent, false);
        ViewHolder viewHolder = new ViewHolder(postView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //get data according to position
        Post post = (Post) posts.get(position);

        //populate the views according to this data
        holder.tvUser.setText(post.getUser().getUsername());
        holder.tvCaption.setText(post.getDescription());
        holder.tvStamp.setText(post.getCreatedAt().toString());

        Glide.with(context)
                .load(post.getImage().getUrl())
                .into(holder.ivPicture);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    //create ViewHolder

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView ivPicture;
        public TextView tvUser, tvCaption, tvStamp;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            //findByViewId lookups
            ivPicture = itemView.findViewById(R.id.ivPic);
            tvUser = itemView.findViewById(R.id.etUser);
            tvCaption = itemView.findViewById(R.id.tvCaption);
            tvStamp = itemView.findViewById(R.id.tvStamp);
        }

        @Override
        public void onClick(View view) {
            //gets item position
            int position = getAdapterPosition();
            //make sure the position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                //get the movie at the position, this won't work if the class is static
                Post post = posts.get(position);
                //create intent for the new activity
                Intent intent = new Intent(context, DetailActivity.class);
                // serialize the movie for the new activity
                intent.putExtra("post", Parcels.wrap(post));
                //show the activity
                context.startActivity(intent);
            }
        }
    }
}