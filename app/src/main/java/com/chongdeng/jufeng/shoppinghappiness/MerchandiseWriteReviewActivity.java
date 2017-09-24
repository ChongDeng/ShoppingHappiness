package com.chongdeng.jufeng.shoppinghappiness;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class MerchandiseWriteReviewActivity extends AppCompatActivity {

    TextView MerchandiseNameView;
    String MerchadiseName = null;

    RatingBar rating_bar = null;
    Button post_review;
    EditText review_content_view;

    ImageView close_button;

    int rating_level = 0;
    boolean rated = false;
    String review_content = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchandise_write_review);

        MerchadiseName = getIntent().getStringExtra("merchandise name");
        MerchandiseNameView = (TextView) findViewById(R.id.MerchandiseName);
        MerchandiseNameView.setText(MerchadiseName);

        rating_bar = (RatingBar) findViewById(R.id.rating_bar);
        rating_bar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                rating_level = (int) rating;
                rated = true;
            }
        });


        post_review = (Button) findViewById(R.id.post_review);
        post_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(review_content_view.getText().toString().isEmpty()) {
                    Toast.makeText(getApplication(), "please type the review content!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!rated){
                    Toast.makeText(getApplication(), "please select the star level!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                //upload function
                Toast.makeText(getApplication(), "write review successfully!",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        review_content_view = (EditText) findViewById(R.id.review_content);

        close_button = (ImageView)findViewById(R.id.close_button);
        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}
