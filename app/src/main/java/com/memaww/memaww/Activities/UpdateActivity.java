package com.memaww.memaww.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.memaww.memaww.R;

public class UpdateActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mUpdateCoverImage;
    private TextView mUpdateTitle, mUpdateDescription;
    private Button mUpdateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);


        mUpdateCoverImage = findViewById(R.id.update_activity_imageView1);
        mUpdateTitle = findViewById(R.id.update_activity_textview1);
        mUpdateDescription = findViewById(R.id.update_activity_textview2);
        mUpdateButton = findViewById(R.id.update_activity_button1);

        mUpdateCoverImage.setAlpha(0f);
        mUpdateCoverImage.setVisibility(View.VISIBLE);

        mUpdateTitle.setAlpha(0f);
        mUpdateTitle.setVisibility(View.VISIBLE);

        mUpdateDescription.setAlpha(0f);
        mUpdateDescription.setVisibility(View.VISIBLE);

        mUpdateButton.setAlpha(0f);
        mUpdateButton.setVisibility(View.VISIBLE);

        // TURNING ON ALPHA TO SLOWLY FADE IN VIEWS
        int mediumAnimationTime = getResources().getInteger(android.R.integer.config_mediumAnimTime);

        mUpdateCoverImage.animate()
                .alpha(1f)
                .setDuration(mediumAnimationTime)
                .setListener(null);
        mUpdateTitle.animate()
                .alpha(1f)
                .setDuration(mediumAnimationTime)
                .setListener(null);
        mUpdateDescription.animate()
                .alpha(1f)
                .setDuration(mediumAnimationTime)
                .setListener(null);
        mUpdateButton.animate()
                .alpha(1f)
                .setDuration(mediumAnimationTime)
                .setListener(null);

        mUpdateButton.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.update_activity_button1){

            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=org.christecclesia.pjdigitalpool")));
        }
    }

}
