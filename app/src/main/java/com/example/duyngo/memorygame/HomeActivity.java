package com.example.duyngo.memorygame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    protected void clickPlay(View view){
        SharedPreferences prefs = getSharedPreferences("GameActivity", MODE_PRIVATE);
        Log.i("prefMain", "check pref contains exist");
        if(prefs.contains("exists")){
            Log.i("prefMain", "prefs exist");
            Intent resume = new Intent(this, ResumeActivity.class);
            startActivity(resume);
        }else{
            Intent play = new Intent(this, GameActivity.class);
            startActivity(play);
        }

    }

    protected void clickRules(View view){
        Intent rules = new Intent(this, RulesActivity.class);
        startActivity(rules);
    }

}
