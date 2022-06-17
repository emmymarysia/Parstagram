package com.example.parstagram;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.target.Target;
import com.parse.ParseFile;

import org.parceler.Parcels;

import java.util.Date;
import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    private Context context;
    private List<Post> posts;
    MainActivity mainActivity;


    public PostsAdapter(Context context, List<Post> posts, MainActivity mainActivity) {
        this.context = context;
        this.posts = posts;
        this.mainActivity = mainActivity;
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

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvUsername;
        private ImageView ivPostMedia;
        private TextView tvUsernameCaption;
        private ImageView ivProfileImage;
        private TextView tvTimestampFeed;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            ivPostMedia = itemView.findViewById(R.id.ivPostMedia);
            tvUsernameCaption = itemView.findViewById(R.id.tvUsernameCaption);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvTimestampFeed = itemView.findViewById(R.id.tvTimestampFeed);
            itemView.setOnClickListener(this);

        }

        public void bind(Post post) {
            tvUsername.setText(post.getUser().getUsername());
            String usernameCaption = "<b>" + post.getUser().getUsername() + "</b>  " + post.getDescription();
            tvUsernameCaption.setText(Html.fromHtml(usernameCaption));
            String timestamp = calculateTimeAgo(post.getCreatedAt());
            tvTimestampFeed.setText(timestamp);
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
                Glide.with(context)
                        .load(profileImage.getUrl())
                        .circleCrop()
                        .into(ivProfileImage);
            }

            ivProfileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProfileFragment fragment = new ProfileFragment(post.getUser());
                    mainActivity.fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                }
            });
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Post post = posts.get(position);
                Intent intent = new Intent(context, PostDetailActivity.class);
                intent.putExtra("post", Parcels.wrap(post));
                context.startActivity(intent);
            }
        }

        public String calculateTimeAgo(Date createdAt) {

            int SECOND_MILLIS = 1000;
            int MINUTE_MILLIS = 60 * SECOND_MILLIS;
            int HOUR_MILLIS = 60 * MINUTE_MILLIS;
            int DAY_MILLIS = 24 * HOUR_MILLIS;

            try {
                createdAt.getTime();
                long time = createdAt.getTime();
                long now = System.currentTimeMillis();

                final long diff = now - time;
                if (diff < MINUTE_MILLIS) {
                    return "just now";
                } else if (diff < 2 * MINUTE_MILLIS) {
                    return "a minute ago";
                } else if (diff < 50 * MINUTE_MILLIS) {
                    return diff / MINUTE_MILLIS + "m";
                } else if (diff < 90 * MINUTE_MILLIS) {
                    return "an hour ago";
                } else if (diff < 24 * HOUR_MILLIS) {
                    return diff / HOUR_MILLIS + "h";
                } else if (diff < 48 * HOUR_MILLIS) {
                    return "yesterday";
                } else {
                    return diff / DAY_MILLIS + "d";
                }
            } catch (Exception e) {
                Log.i("Error:", "getRelativeTimeAgo failed", e);
                e.printStackTrace();
            }

            return "";
        }
    }

    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }
}
