package com.example.duyngo.memorygame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class WinActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);
    }

    protected void clickHome(View view){
        SharedPreferences prefs = getSharedPreferences("GameActivity", MODE_PRIVATE);
        prefs.edit().clear().commit();
        Intent home = new Intent(this, HomeActivity.class);
        startActivity(home);
    }
}
