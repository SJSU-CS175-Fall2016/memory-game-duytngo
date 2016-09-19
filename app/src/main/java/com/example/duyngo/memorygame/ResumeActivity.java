package com.example.duyngo.memorygame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ResumeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume);
    }

    protected void clickResume(View view){
        Intent resume = new Intent(this, GameActivity.class);
        resume.putExtra("resume", true);
        startActivity(resume);
        finish();

    }

    protected void clickRestart(View view){
        Intent restart = new Intent(this, GameActivity.class);
        startActivity(restart);
        finish();
    }
}
