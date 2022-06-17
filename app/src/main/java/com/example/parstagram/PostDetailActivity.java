package com.example.parstagram;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;

import org.parceler.Parcels;
import org.w3c.dom.Text;

import java.util.Date;

public class PostDetailActivity extends AppCompatActivity {

    private ImageView ivDetailProfileImage;
    private TextView tvDetailUsername;
    private ImageView ivDetailPostMedia;
    private TextView tvDetailUsernameCaption;
    private TextView tvTimestamp;
    Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        post = Parcels.unwrap(getIntent().getParcelableExtra("post"));

        setContentView(R.layout.activity_post_detail);

        ivDetailProfileImage = findViewById(R.id.ivDetailProfileImage);
        tvDetailUsername = findViewById(R.id.tvDetailUsername);
        ivDetailPostMedia = findViewById(R.id.ivDetailPostMedia);
        tvDetailUsernameCaption = findViewById(R.id.tvDetailUsernameCaption);
        tvTimestamp = findViewById(R.id.tvTimestamp);

        tvDetailUsername.setText(post.getUser().getUsername());
        String usernameCaption = "<b>" + post.getUser().getUsername() + "</b>  " + post.getDescription();
        tvDetailUsernameCaption.setText(Html.fromHtml(usernameCaption));
        ParseFile profileImage = post.getUser().getParseFile("profileImage");
        ParseFile image = post.getImage();

        Date createdAt = post.getCreatedAt();
        String timestamp = calculateTimeAgo(createdAt);
        tvTimestamp.setText(timestamp);

        if (image != null) {
            Glide.with(this)
                    .load(image.getUrl())
                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .centerInside()
                    .into(ivDetailPostMedia);
        }
        if (profileImage != null) {
            Glide.with(this)
                    .load(profileImage.getUrl())
                    .circleCrop()
                    .into(ivDetailProfileImage);
        }
    }

    public static String calculateTimeAgo(Date createdAt) {

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