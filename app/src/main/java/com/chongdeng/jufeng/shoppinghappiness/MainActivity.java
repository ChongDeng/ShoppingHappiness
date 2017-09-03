package com.chongdeng.jufeng.shoppinghappiness;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import helper.SQLiteHandler;
import helper.SessionManager;

/**
 * Created by Chong Deng on 17/08/30.
 */

public class MainActivity extends AppCompatActivity {

    TextView signin;
    TextView signup;

    private SessionManager session;
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Session manager
        session = new SessionManager(getApplicationContext());
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to home activity
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

        signin = (TextView)findViewById(R.id.signin);
        signup = (TextView)findViewById(R.id.signup);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(MainActivity.this, SigninActivity.class);
                startActivity(it);
            }
        });


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(it);

            }
        });
    }
}
