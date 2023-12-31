package com.example.workoutapp;

import androidx.annotation.ColorRes;
import androidx.appcompat.app.AppCompatActivity;
import com.opencsv.CSVWriter;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class startUp extends AppCompatActivity {
    private Button getStartedButton, backButton, nextButton, button1, button2;
    private Vibrator vibrator;
    private ImageView[] surveyQ = new ImageView[4];

    private RelativeLayout getStartedLayout, surveyLayout;
    private int surveyQNum = 0;
    private boolean survey = false;

    private boolean height = false;

    private boolean metric = false;

    private DBHandler dbHandler;

    private TextView startUpName, questionText,messageText, textView1, textView2;
    private EditText editText1, editText2, editText3, editText4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startup_xml);
        dbHandler = new DBHandler(startUp.this);
        Database.setDBHandler(dbHandler);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        startUpName = findViewById(R.id.startUpName);
        String name = "fit-track";
        startUpName.setText("");
        // update values in assets/sample.csv
        try {
            FileWriter writer = new FileWriter(new File("assets/sample.csv"));
            CSVWriter csvWriter = new CSVWriter(writer);
            csvWriter.writeNext(new String[]{"Date", "Weight", "Chest", "Back", "Abs", "Shoulders", "Triceps", "Biceps", "Legs", "Cardio"});
            Calendar cal = Calendar.getInstance();
            int x = -1;
            for(int i =0; i<120; i++){
                cal.add(Calendar.DATE, -x);
                Date date = cal.getTime();
                String dateStr = new SimpleDateFormat("MM/dd/yyyy").format(date);
                int randWeight = (int) (Math.random()*100 + 100);
                int randChest = (int) (Math.random()*20) * 60;
                int randBack = (int) (Math.random()*20) * 60;
                int randAbs = (int) (Math.random()*20) * 60;
                int randShoulders = (int) (Math.random()*20) * 60;
                int randTriceps = (int) (Math.random()*20) * 60;
                int randBiceps = (int) (Math.random()*20) * 60;
                int randLegs = (int) (Math.random()*20) * 60;
                int randCardio = (int) (Math.random()*20) * 60;
                csvWriter.writeNext(new String[]{dateStr, randWeight + "", randChest + "", randBack + "", randAbs + "", randShoulders + "", randTriceps + "", randBiceps + "", randLegs + "", randCardio + ""});
                x++;
            }
            csvWriter.close();
            writer.close();
        } catch (Exception e) {
            Log.d("CSV", "Error writing CSV file");
            e.printStackTrace();
        }

        CSVReader.loadCSVFile(
                startUp.this,
                "sample.csv",
                dbHandler.getWritableDatabase());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i< name.length(); i++){
                    final int finalI = i;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startUpName.setText(startUpName.getText() + "" + name.charAt(finalI));
                        }
                    }, 50*i);
                }
            }
        }, 200);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!dbHandler.allFilled()){
                    switchToGetStarted();
                } else {
                    changeToMain();
                }
            }
        }, 1000);
    }

    private void setStatusBarColor(@ColorRes int colorRes) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(colorRes));
        }
    }

    private void switchToGetStarted() {
        survey = false;
        setContentView(R.layout.get_started);
        setStatusBarColor(R.color.black);
        getStartedLayout = findViewById(R.id.getStartedLayout);
        getStartedButton = findViewById(R.id.btnGetStarted);


        getStartedButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TranslateAnimation animate = new TranslateAnimation(0, -getStartedLayout.getWidth(), 0, 0);
                animate.setDuration(200);
                animate.setFillAfter(true);
                getStartedLayout.startAnimation(animate);
                codeForSurvey();
            }
        });
    }

    private void codeForSurvey(){
        setContentView(R.layout.survey);
        setStatusBarColor(R.color.beige);
        System.out.println("First Name: " + dbHandler.getFirstName() + "\nLast Name: " + dbHandler.getLastName() + "\nHeight: " + dbHandler.getHeight() + "\nHeight Metric: " + dbHandler.getHeightMetric() + "\nWeight: " + dbHandler.getWeight() + "\nWeight Metric: " + dbHandler.getWeightMetric() + "\nGoal Weight: " + dbHandler.getGoalWeight() + "\nGoal Weight Metric: " + dbHandler.getGoalWeightMetric());
        survey = true;
        surveyQ[0] = findViewById(R.id.progress1);
        surveyQ[1] = findViewById(R.id.progress2);
        surveyQ[2] = findViewById(R.id.progress3);
        surveyQ[3] = findViewById(R.id.progress4);
        backButton = findViewById(R.id.backButton);

        surveyLayout = findViewById(R.id.surveyLayout);
        questionText = findViewById(R.id.questionText);
        messageText = findViewById(R.id.messageText);
        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);
        editText3 = findViewById(R.id.editText3);
        editText4 = findViewById(R.id.editText4);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        textView1 = findViewById(R.id.textview1);
        textView2 = findViewById(R.id.textview2);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!metric){
                    return;
                }
                vibrator.vibrate((long) 2.5);
                button1.setBackground(getResources().getDrawable(R.drawable.button2));
                button2.setBackground(getResources().getDrawable(R.drawable.button3));
                if(height){
                    textView1.setVisibility(View.INVISIBLE);
                    textView2.setVisibility(View.INVISIBLE);
                    if(metric && !editText3.getText().toString().equals("")){
                        int cm = Integer.parseInt(editText3.getText().toString());
                        int feet = (int) (cm/30.48);
                        int inches = (int) ((cm - feet*30.48)/2.54 + 0.5);
                        editText3.setText(feet + "");
                        editText4.setText(inches + "");
                        System.out.println(cm + "cm");
                        System.out.println(feet + "ft  " + inches);
                    }
                    metric = false;
                    textView1.setText("ft");
                    textView2.setText("in");
                    editText3.setX(editText1.getX() +  editText1.getWidth()/30);
                    editText4.setX(editText1.getX() + editText1.getWidth() - editText4.getWidth() - editText1.getWidth()/30);
                    button1.setX(editText3.getX() + editText3.getWidth() - button1.getWidth());
                    button2.setX(editText4.getX());
                    editText3.setVisibility(View.VISIBLE);
                    editText4.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            textView1.setX(editText3.getX() + editText3.getWidth() - textView1.getWidth());
                            textView2.setX(editText4.getX() + editText4.getWidth() - textView2.getWidth());
                            textView1.setVisibility(View.VISIBLE);
                            textView2.setVisibility(View.VISIBLE);
                        }
                    }, 1);
                } else {
                    textView1.setVisibility(View.INVISIBLE);
                    if(metric && !editText3.getText().toString().equals("")){
                        int kg = Integer.parseInt(editText3.getText().toString());
                        int lbs = (int) (kg*2.205 + 0.5);
                        editText3.setText(lbs + "");
                    }
                    metric = false;
                    textView1.setText("lbs");
                    editText3.setX(editText1.getX() + editText1.getWidth()/2 - editText4.getWidth()/2);
                    button1.setX(editText3.getX());
                    button2.setX(editText3.getX() + editText3.getWidth() - button2.getWidth());
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            textView1.setX(editText3.getX() + editText3.getWidth() - textView1.getWidth() + 15);
                            textView1.setVisibility(View.VISIBLE);
                        }
                    }, 1);
                }
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(metric){
                    return;
                }
                vibrator.vibrate((long) 2.5);
                button1.setBackground(getResources().getDrawable(R.drawable.button3));
                button2.setBackground(getResources().getDrawable(R.drawable.button2));
                if(height){
                    textView1.setVisibility(View.INVISIBLE);
                    textView2.setVisibility(View.INVISIBLE);
                    if(!metric && !editText3.getText().toString().equals("") && !editText4.getText().toString().equals("")){
                        int feet = Integer.parseInt(editText3.getText().toString());
                        int inches = Integer.parseInt(editText4.getText().toString());
                        int cm = (int) (feet*30.48 + inches*2.54 );
                        editText3.setText(cm + "");
                        editText4.setText("");
                    }
                    metric = true;
                    textView1.setText(""); textView2.setText("");
                    editText4.setVisibility(View.INVISIBLE);
                    editText3.setVisibility(View.VISIBLE);
                    editText3.setX(editText1.getX() + editText1.getWidth()/2 - editText4.getWidth()/2);
                    button1.setX(editText3.getX());
                    button2.setX(editText3.getX() + editText3.getWidth() - button2.getWidth());
                    textView1.setText("cm");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            textView1.setX(editText3.getX() + editText3.getWidth() - textView1.getWidth() + 15);
                            textView1.setVisibility(View.VISIBLE);
                        }
                    }, 1);
                } else {
                    textView1.setVisibility(View.INVISIBLE);
                    if(!metric && !editText3.getText().toString().equals("")){
                        int lbs = Integer.parseInt(editText3.getText().toString());
                        int kg = (int) (lbs/2.205 + 0.5);
                        editText3.setText(kg + "");
                    }
                    metric = true;
                    textView1.setText("kg");
                    editText3.setX(editText1.getX() + editText1.getWidth()/2 - editText4.getWidth()/2);
                    button1.setX(editText3.getX());
                    button2.setX(editText3.getX() + editText3.getWidth() - button2.getWidth());
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            textView1.setX(editText3.getX() + editText3.getWidth() - textView1.getWidth() + 15);
                            textView1.setVisibility(View.VISIBLE);
                        }
                    }, 1);
                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(surveyQNum == 0){
                    TranslateAnimation animate = new TranslateAnimation(0, surveyLayout.getWidth(), 0, 0);
                    animate.setDuration(200);
                    animate.setFillAfter(true);
                    surveyLayout.startAnimation(animate);
                    switchToGetStarted();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            TranslateAnimation animate = new TranslateAnimation(-getStartedLayout.getWidth(), 0, 0, 0);
                            animate.setDuration(200);
                            animate.setFillAfter(true);
                            getStartedLayout.startAnimation(animate);
                        }
                    }, 1);
                } else {
                    surveyQNum--;
                    updateProgress();
                }
            }
        });
        nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(surveyQNum == 0) {
                    if(editText1.getText().toString().equals("") || editText2.getText().toString().equals("")){
                        Toast.makeText(getApplicationContext(), "Please enter your name", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(dbHandler.checkEmpty()) {
                        dbHandler.addDataToStart(editText1.getText().toString(), editText2.getText().toString(), "", "", "", "", "", "");
                    } else {
                        dbHandler.updateData(editText1.getText().toString(), editText2.getText().toString(), dbHandler.getHeight(), dbHandler.getHeightMetric(), dbHandler.getWeight(), dbHandler.getWeightMetric(), dbHandler.getGoalWeight(), dbHandler.getGoalWeightMetric());
                    }
                } else if(surveyQNum == 1){
                    if((editText3.getText().toString().equals("") || editText4.getText().toString().equals("") && !metric) || (editText3.getText().toString().equals("") && metric)){
                        Toast.makeText(getApplicationContext(), "Please enter your height", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(metric) {
                        dbHandler.updateData(dbHandler.getFirstName(), dbHandler.getLastName(), editText3.getText().toString(), metric + "", dbHandler.getWeight(), dbHandler.getWeightMetric(), dbHandler.getGoalWeight(), dbHandler.getGoalWeightMetric());
                    } else {
                        int inches = Integer.parseInt(editText4.getText().toString()) + Integer.parseInt(editText3.getText().toString())*12;
                        dbHandler.updateData(dbHandler.getFirstName(), dbHandler.getLastName(), inches + "", metric + "", dbHandler.getWeight(), dbHandler.getWeightMetric(), dbHandler.getGoalWeight(), dbHandler.getGoalWeightMetric());
                    }
                } else if(surveyQNum == 2){
                    if(editText3.getText().toString().equals("")){
                        Toast.makeText(getApplicationContext(), "Please enter your weight", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    System.out.println("Weight is CURRENTLY " + dbHandler.getWeight() + " and is changing to " + editText3.getText().toString());
                    dbHandler.updateData(dbHandler.getFirstName(), dbHandler.getLastName(), dbHandler.getHeight(), dbHandler.getHeightMetric(), editText3.getText().toString(), metric + "", dbHandler.getGoalWeight(), dbHandler.getGoalWeightMetric());
                } else if(surveyQNum == 3){
                    if(editText3.getText().toString().equals("")){
                        Toast.makeText(getApplicationContext(), "Please enter your goal weight", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    System.out.println("Goal Weight is changing");
                    dbHandler.updateData(dbHandler.getFirstName(), dbHandler.getLastName(), dbHandler.getHeight(), dbHandler.getHeightMetric(), dbHandler.getWeight(), dbHandler.getWeightMetric(), editText3.getText().toString(), metric + "");
                    changeToMain();
                }
                surveyQNum++;
                updateProgress();
            }
        });
        updateProgress();
        // after 1s
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                TranslateAnimation animate = new TranslateAnimation(surveyLayout.getWidth(), 0, 0, 0);
                animate.setDuration(200);
                animate.setFillAfter(true);
                surveyLayout.startAnimation(animate);
            }
        }, 1);
    }

    private void updateProgress(){
        questionText.setText(""); messageText.setText(""); editText1.setText(""); editText2.setText(""); editText3.setText(""); editText4.setText(""); textView1.setText(""); textView2.setText("");
        editText1.setVisibility(View.INVISIBLE); editText2.setVisibility(View.INVISIBLE); editText3.setVisibility(View.INVISIBLE); editText4.setVisibility(View.INVISIBLE); textView1.setVisibility(View.INVISIBLE); textView2.setVisibility(View.INVISIBLE); button1.setVisibility(View.INVISIBLE); button2.setVisibility(View.INVISIBLE);
        for(int i = 0; i < 4; i++){
            if(i <= surveyQNum){
                surveyQ[i].setImageResource(R.drawable.greenrect);
            }
            else{
                surveyQ[i].setImageResource(R.drawable.beigerect);
            }
        }
        metric = false;
        if(surveyQNum == 0){
            questionText.setText("What's your name?");
            messageText.setText("We'll use this to personalize your experience.");
            editText1.setVisibility(View.VISIBLE);
            editText2.setVisibility(View.VISIBLE);
            editText1.setHint("First Name");
            editText2.setHint("Last Name");
            if(!dbHandler.checkEmpty()){
                editText1.setText(dbHandler.getFirstName());
                editText2.setText(dbHandler.getLastName());
            }
        } else if (surveyQNum == 1){
            height = true;
            button1.setBackground(getResources().getDrawable(R.drawable.button2));
            button2.setBackground(getResources().getDrawable(R.drawable.button3));
            button1.setVisibility(View.VISIBLE);
            button2.setVisibility(View.VISIBLE);
            button1.setText("Ft/In");
            button2.setText("Cm");
            questionText.setText("What's your height?");
            messageText.setText("");

            editText3.setX(editText1.getX() +  editText1.getWidth()/30);
            editText4.setX(editText1.getX() + editText1.getWidth() - editText4.getWidth() - editText1.getWidth()/30);
            button1.setX(editText3.getX() + editText3.getWidth() - button1.getWidth());
            button2.setX(editText4.getX());
            editText3.setVisibility(View.VISIBLE);
            editText4.setVisibility(View.VISIBLE);
            textView1.setVisibility(View.VISIBLE);
            textView2.setVisibility(View.VISIBLE);
            textView1.setText("ft");
            textView2.setText("in");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    textView1.setX(editText3.getX() + editText3.getWidth() - textView1.getWidth());
                    textView2.setX(editText4.getX() + editText4.getWidth() - textView2.getWidth());
                }
            }, 1);
            if(!dbHandler.checkEmpty() && !dbHandler.getHeight().equals("")){
                if(dbHandler.getHeightMetric().equals("true")){
                    metric = true;
                    button1.setBackground(getResources().getDrawable(R.drawable.button3));
                    button2.setBackground(getResources().getDrawable(R.drawable.button2));
                    textView1.setVisibility(View.INVISIBLE);
                    textView2.setVisibility(View.INVISIBLE);
                    editText4.setVisibility(View.INVISIBLE);
                    editText3.setVisibility(View.VISIBLE);
                    editText3.setX(editText1.getX() + editText1.getWidth()/2 - editText4.getWidth()/2);
                    button1.setX(editText3.getX());
                    button2.setX(editText3.getX() + editText3.getWidth() - button2.getWidth());
                    textView1.setText("cm");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            textView1.setX(editText3.getX() + editText3.getWidth() - textView1.getWidth() + 15);
                            textView1.setVisibility(View.VISIBLE);
                        }
                    }, 1);
                    editText3.setText(dbHandler.getHeight());
                } else {
                    metric = false;
                    editText3.setText(dbHandler.getHeight());
                    int inches = Integer.parseInt(dbHandler.getHeight());
                    int feet = (int) (inches/12);
                    inches = inches - feet*12;
                    editText3.setText(feet + "");
                    editText4.setText(inches + "");
                }
            }
        } else if (surveyQNum == 2){
            button1.setVisibility(View.VISIBLE); button2.setVisibility(View.VISIBLE);
            button1.setBackground(getResources().getDrawable(R.drawable.button2));
            button2.setBackground(getResources().getDrawable(R.drawable.button3));
            height = false;
            questionText.setText("What's your weight?");
            messageText.setText("Great, almost done!");
            editText3.setVisibility(View.VISIBLE);
            editText3.setX(editText1.getX() + editText1.getWidth()/2 - editText4.getWidth()/2);
            button1.setX(editText3.getX());
            button2.setX(editText3.getX() + editText3.getWidth() - button2.getWidth());
            button1.setText("Lbs");
            button2.setText("Kg");
            textView1.setText("lbs");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    textView1.setX(editText3.getX() + editText3.getWidth() - textView1.getWidth() + 15);
                    textView1.setVisibility(View.VISIBLE);
                }
            }, 1);
            if(!dbHandler.checkEmpty() && !dbHandler.getWeight().equals("")){
                if(dbHandler.getWeightMetric().equals("true")){
                    metric = true;
                    button1.setBackground(getResources().getDrawable(R.drawable.button3));
                    button2.setBackground(getResources().getDrawable(R.drawable.button2));
                    textView1.setVisibility(View.INVISIBLE);
                    editText3.setVisibility(View.VISIBLE);
                    editText3.setX(editText1.getX() + editText1.getWidth()/2 - editText4.getWidth()/2);
                    button1.setX(editText3.getX());
                    button2.setX(editText3.getX() + editText3.getWidth() - button2.getWidth());
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            textView1.setX(editText3.getX() + editText3.getWidth() - textView1.getWidth() + 15);
                            textView1.setVisibility(View.VISIBLE);
                        }
                    }, 1);
                    editText3.setText(dbHandler.getWeight());
                } else {
                    metric = false;
                    editText3.setText(dbHandler.getWeight());
                }
            }
        } else if (surveyQNum == 3){
            button1.setVisibility(View.VISIBLE); button2.setVisibility(View.VISIBLE);
            button1.setBackground(getResources().getDrawable(R.drawable.button2));
            button2.setBackground(getResources().getDrawable(R.drawable.button3));
            height = false;
            questionText.setText("What's your goal weight?");
            messageText.setText("");
            editText3.setVisibility(View.VISIBLE);
            editText3.setX(editText1.getX() + editText1.getWidth()/2 - editText4.getWidth()/2);
            button1.setX(editText3.getX());
            button2.setX(editText3.getX() + editText3.getWidth() - button2.getWidth());
            button1.setText("Lbs");
            button2.setText("Kg");
            textView1.setText("lbs");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    textView1.setX(editText3.getX() + editText3.getWidth() - textView1.getWidth() + 15);
                    textView1.setVisibility(View.VISIBLE);
                }
            }, 1);
            if(!dbHandler.checkEmpty() && !dbHandler.getGoalWeight().equals("")){
                if(dbHandler.getGoalWeightMetric().equals("true")){
                    metric = true;
                    button1.setBackground(getResources().getDrawable(R.drawable.button3));
                    button2.setBackground(getResources().getDrawable(R.drawable.button2));
                    textView1.setVisibility(View.INVISIBLE);
                    editText3.setVisibility(View.VISIBLE);
                    editText3.setX(editText1.getX() + editText1.getWidth()/2 - editText4.getWidth()/2);
                    button1.setX(editText3.getX());
                    button2.setX(editText3.getX() + editText3.getWidth() - button2.getWidth());
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            textView1.setX(editText3.getX() + editText3.getWidth() - textView1.getWidth() + 15);
                            textView1.setVisibility(View.VISIBLE);
                        }
                    }, 1);
                    editText3.setText(dbHandler.getGoalWeight());
                } else {
                    metric = false;
                    editText3.setText(dbHandler.getGoalWeight());
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(survey){
            if(surveyQNum == 0){
                TranslateAnimation animate = new TranslateAnimation(0, surveyLayout.getWidth(), 0, 0);
                animate.setDuration(200);
                animate.setFillAfter(true);
                surveyLayout.startAnimation(animate);
                switchToGetStarted();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        TranslateAnimation animate = new TranslateAnimation(-getStartedLayout.getWidth(), 0, 0, 0);
                        animate.setDuration(200);
                        animate.setFillAfter(true);
                        getStartedLayout.startAnimation(animate);
                    }
                }, 1);
            } else {
                surveyQNum--;
                updateProgress();
            }
        } else {
            super.onBackPressed();
        }
    }

    public void changeToMain(){
        if(dbHandler.checkIfReminderExists()){
           dbHandler.addReminderData("false", "6:00");
        }
        Intent intent = new Intent(this, main.class);
        startActivity(intent);
    }


}