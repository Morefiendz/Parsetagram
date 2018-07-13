package com.plusultra.parsetagram;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.plusultra.parsetagram.model.Post;

import java.util.List;

public class InstaAdapter extends RecyclerView.Adapter<InstaAdapter.ViewHolder>{
    Context context;
    private List mPosts;

    public InstaAdapter(List<Post> posts) {
        mPosts = posts;
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
        Post post = (Post) mPosts.get(position);

        //populate the views according to this data
        holder.tvUser.setText(post.getUser().getUsername());
        holder.tvCaption.setText(post.getDescription());

        Glide.with(context)
                .load(post.getImage().getUrl())
                .into(holder.ivPicture);
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    //create ViewHolder

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivPicture;
        public TextView tvUser, tvCaption;

        public ViewHolder(View itemView) {
            super(itemView);

            //findByViewId lookups
            ivPicture = itemView.findViewById(R.id.ivPic);
            tvUser = itemView.findViewById(R.id.etUser);
            tvCaption = itemView.findViewById(R.id.tvCaption);
        }
    }
}
