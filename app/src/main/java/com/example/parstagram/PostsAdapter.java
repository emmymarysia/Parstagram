package com.example.parstagram;

import android.content.Context;
import android.media.Image;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.target.Target;
import com.parse.ParseFile;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    private Context context;
    private List<Post> posts;


    public PostsAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public PostsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostsAdapter.ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvUsername;
        private ImageView ivPostMedia;
        private TextView tvUsernameCaption;
        private ImageView ivProfileImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            ivPostMedia = itemView.findViewById(R.id.ivPostMedia);
            tvUsernameCaption = itemView.findViewById(R.id.tvUsernameCaption);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);

        }

        public void bind(Post post) {
            tvUsername.setText(post.getUser().getUsername());
            String usernameCaption = "<b>" + post.getUser().getUsername() + "</b>  " + post.getDescription();
            tvUsernameCaption.setText(Html.fromHtml(usernameCaption));
            ParseFile profileImage = post.getUser().getParseFile("profileImage");
            ParseFile image = post.getImage();


            if (image != null) {
                Glide.with(context)
                        .load(image.getUrl())
                        .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .centerInside()
                        .into(ivPostMedia);
            }
            if (profileImage != null) {
                //Glide.with(context).load(profileImage.getUrl()).centerInside().transform(new RoundedCorners(90)).into(ivProfileImage);
                Glide.with(context)
                        .load(profileImage.getUrl())
                        .circleCrop()
                        .into(ivProfileImage);
            }
        }
    }

    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Post> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }
}
