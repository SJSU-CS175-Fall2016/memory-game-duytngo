package com.example.duyngo.memorygame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GameActivity extends AppCompatActivity {
    private List<Integer> images = Arrays.asList(
            R.drawable.cat, R.drawable.cow, R.drawable.dog, R.drawable.elephant, R.drawable.monkey
            , R.drawable.mouse, R.drawable.octopus, R.drawable.rabbit, R.drawable.snail,
            R.drawable.tiger,  R.drawable.cat, R.drawable.cow, R.drawable.dog, R.drawable.elephant, R.drawable.monkey
            , R.drawable.mouse, R.drawable.octopus, R.drawable.rabbit, R.drawable.snail,
            R.drawable.tiger);

    private ArrayList<Integer> invisible = new ArrayList<Integer>();
    private ArrayList<Integer> faceUp = new ArrayList<Integer>();


    private int turn = 0;
    private int currentImage = 0;
    private int currentImage2 = 0;
    private ImageButton button1;
    private ImageButton button2;
    private int button1id;
    private int button2id;
    private int points = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

       Bundle extra = getIntent().getExtras();

        //Check for Resume
        if(extra != null) {
            if(extra.getBoolean("resume") == true){
                getPrefs();
            }
        }
        else{
            populateCards();
        }

        TextView pts = (TextView) findViewById(R.id.pointsText);
        pts.setText(getResources().getString(R.string.points) + points);

    }




    public void getPrefs(){

        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        List<Integer> tempImgs = new ArrayList<>();
        ArrayList<Integer> tempInvis = new ArrayList<Integer>();
        ArrayList<Integer> tempFaceUp = new ArrayList<Integer>();
        for(int i = 0; i < 20; i++){
           // Log.i("getImages", "image" + i);
            tempImgs.add(prefs.getInt("image" + i, R.drawable.joker));
        }
        if(tempImgs.get(0) != R.drawable.joker){
            images = tempImgs;
        }
        int c = 0;
        while(prefs.getInt("invis" + c, 0) != 0){
            //Log.i("invisible", prefs.getInt("invis" + c, 0)+ "c: "  + c);
            tempInvis.add(prefs.getInt("invis" + c, 0));
            c++;
        }

        c = 0;
        while(prefs.getInt("faceUp" + c , 0) != 0){
            tempFaceUp.add(prefs.getInt("faceUp" + c , 0));
            c++;
        }



        if(tempInvis.size() > 0){
            invisible = tempInvis;

            ImageButton tempImgButton;
            for(int invis : invisible){
                tempImgButton = (ImageButton) findViewById(invis);
                //Log.i("invisible", "ImageButton" + invis + tempImgButton + " ::"+getResources().getResourceEntryName(invis) + ":" +getResources().getResourceEntryName(invis));
                tempImgButton.setVisibility(View.INVISIBLE);
            }
        }
        if(tempFaceUp.size() > 0){
            faceUp = tempFaceUp;

            ImageButton tempFaceButton;
            for(int face : faceUp){
                tempFaceButton = (ImageButton) findViewById(face);
                int animal=Integer.parseInt(getResources().getResourceEntryName(face).replaceAll("[\\D]", ""));
                tempFaceButton.setImageResource(images.get(animal - 1));
            }
        }

        turn = prefs.getInt("turn", 0);
        currentImage = prefs.getInt("currImg", 0);
        currentImage2 = prefs.getInt("currImg2", 0);
        button1id = prefs.getInt("b1id", 0);
        button2id = prefs.getInt("b2id", 0);
        points = prefs.getInt("points", 0);
        button1 = (ImageButton) findViewById(prefs.getInt("b1", R.id.ib1));
        button2 = (ImageButton) findViewById(prefs.getInt("b2", R.id.ib2));

        //Log.i("getPrefs", turn + ", " + currentImage + ", " + currentImage2+", " +
        //button1id+", " +button2id+", " +points+", " +button1+", " +button2);
    }
    @Override
    public void onStop(){
        super.onStop();
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.clear().commit();
        prefsEditor.putBoolean("exists", true);
        int counter = 0;
        for(int i : images){
            prefsEditor.putInt("image" + counter, i);
            //Log.i("prefImages", "image" + counter);
            counter++;
        }
        counter = 0;
        for(int inv : invisible){
            prefsEditor.putInt("invis" + counter, inv);
            counter++;
        }
        counter = 0;
        for(int face : faceUp){
            prefsEditor.putInt("faceUp" + counter, face);
            counter++;
        }
        //Log.i("prefImages", "Done image pref");
        prefsEditor.putInt("turn", turn);
        prefsEditor.putInt("currImg", currentImage);
        prefsEditor.putInt("currImg2", currentImage2);
        prefsEditor.putInt("b1id", button1id);
        prefsEditor.putInt("b2id", button2id);
        prefsEditor.putInt("points", points);
        //Log.i("prefMain", "Commit");
        if(button1 != null) {
            prefsEditor.putInt("b1", button1.getId());
            //Log.i("prefMain", "button1 saved");
        }
        if(button2 != null) {
            prefsEditor.putInt("b2", button2.getId());
            //Log.i("prefMain", "button2 saved");
        }
       // Log.i("button1log", button1.getId() + "" );
        prefsEditor.commit();
    }

    protected void populateCards(){
        Collections.shuffle(images);
    }

    protected void turnOver(View view){
        ImageButton image = (ImageButton) view;
        int imgId = 0;
        int buttonId = 0;
        if(turn == 2) {
            checkMatch();
            //Log.i("checkMatch", "check match called");
        }
        switch(image.getId()){
            case R.id.ib1:
                image.setImageResource(images.get(0));
                imgId = images.get(0);
                buttonId = R.id.ib1;
                break;
            case R.id.ib2:
                image.setImageResource(images.get(1));
                imgId = images.get(1);
                buttonId = R.id.ib2;
                break;
            case R.id.ib3:
                image.setImageResource(images.get(2));
                imgId = images.get(2);
                buttonId = R.id.ib3;
                break;
            case R.id.ib4:
                image.setImageResource(images.get(3));
                imgId = images.get(3);
                buttonId = R.id.ib4;
                break;
            case R.id.ib5:
                image.setImageResource(images.get(4));
                imgId = images.get(4);
                buttonId = R.id.ib5;
                break;
            case R.id.ib6:
                image.setImageResource(images.get(5));
                imgId = images.get(5);
                buttonId = R.id.ib6;
                break;
            case R.id.ib7:
                image.setImageResource(images.get(6));
                imgId = images.get(6);
                buttonId = R.id.ib7;
                break;
            case R.id.ib8:
                image.setImageResource(images.get(7));
                imgId = images.get(7);
                buttonId = R.id.ib8;
                break;
            case R.id.ib9:
                image.setImageResource(images.get(8));
                imgId = images.get(8);
                buttonId = R.id.ib9;
                break;
            case R.id.ib10:
                image.setImageResource(images.get(9));
                imgId = images.get(9);
                buttonId = R.id.ib10;
                break;
            case R.id.ib11:
                image.setImageResource(images.get(10));
                imgId = images.get(10);
                buttonId = R.id.ib11;
                break;
            case R.id.ib12:
                image.setImageResource(images.get(11));
                imgId = images.get(11);
                buttonId = R.id.ib12;
                break;
            case R.id.ib13:
                image.setImageResource(images.get(12));
                imgId = images.get(12);
                buttonId = R.id.ib13;
                break;
            case R.id.ib14:
                image.setImageResource(images.get(13));
                imgId = images.get(13);
                buttonId = R.id.ib14;
                break;
            case R.id.ib15:
                image.setImageResource(images.get(14));
                imgId = images.get(14);
                buttonId = R.id.ib15;
                break;
            case R.id.ib16:
                image.setImageResource(images.get(15));
                imgId = images.get(15);
                buttonId = R.id.ib16;
                break;
            case R.id.ib17:
                image.setImageResource(images.get(16));
                imgId = images.get(16);
                buttonId = R.id.ib17;
                break;
            case R.id.ib18:
                image.setImageResource(images.get(17));
                imgId = images.get(17);
                buttonId = R.id.ib18;
                break;
            case R.id.ib19:
                image.setImageResource(images.get(18));
                imgId = images.get(18);
                buttonId = R.id.ib19;
                break;
            case R.id.ib20:
                image.setImageResource(images.get(19));
                imgId = images.get(19);
                buttonId = R.id.ib20;
                break;


        }
        //Log.i("turnOverr", imgId + "," + image + "," + buttonId + "," + turn);
        trackTurn(imgId , image , buttonId);
        if(turn == 2 && points == 9){
            checkMatch();
        }


    }
    protected void trackTurn(int imgId, ImageButton v, int buttonId){
        //Log.i("trackTurn", "track turn called");
        turn++;
        if(turn == 1){
            currentImage = imgId;
            button1 = v;
            button1id = buttonId;
            faceUp.add(button1.getId());
        }
        else if(turn == 2) {
            currentImage2 = imgId;
            button2 = v;
            button2id = buttonId;
            //Log.i("trackTurn", "turn == 2 condition");
            faceUp.add(button2.getId());
        }

    }

    protected void checkMatch(){
        turn = 0;
        if(currentImage == currentImage2 && button1id != button2id){

            button1.setVisibility(View.INVISIBLE);
            button2.setVisibility(View.INVISIBLE);
            invisible.add(button1.getId());
            invisible.add(button2.getId());
           //Log.i("invisible", button1.getId() + ":" + button2.getId() + " - " + button1id +": " + button2id + "- " + (ImageButton) findViewById(button1id) + ":" + button1);
            points++;
            TextView pts = (TextView) findViewById(R.id.pointsText);
            pts.setText(getResources().getString(R.string.points) + points);
            faceUp.clear();

            if(points == 10){
                Intent win = new Intent(this, WinActivity.class);
                startActivity(win);
               // overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }

        }
        else if(button1 != null && button2 != null){
            button1.setImageResource(R.drawable.joker);
            button2.setImageResource(R.drawable.joker);
            faceUp.clear();
        }
    }


}
