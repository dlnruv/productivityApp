package com.example.workoutapp;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHandler extends SQLiteOpenHelper{

    private static final String DB_Name = "userData";

    private static final int DB_VERSION = 2;

    private static final String TABLE_NAME = "startData";

    private static final String STOPWATCH_TABLE_NAME = "stopwatchData";

    private static final String FIRST_NAME_COL = "firstName";

    private static final String LAST_NAME_COL = "lastName";

    private static final String HEIGHT_COL = "heightCol";

    private static final String HEIGHT_METRIC_COL = "heightMetricCol";

    private static final String WEIGHT_COL = "weightCol";

    private static final String WEIGHT_METRIC_COL = "weightMetricCol";

    private static final String GOAL_WEIGHT_COL = "goalWeightCol";

    private static final String GOAL_WEIGHT_METRIC_COL = "goalWeightMetricCol";

    private static final String STOPWATCH_SECONDS_COL = "stopwatchSecondsCol";
    private static final String STOPWATCH_MUSCLE_GROUP_COL = "stopwatchMuscleGroupCol";
    private static final String STOPWATCH_CLOSING_TIME_COL = "stopwatchClosingTimeCol";







    public DBHandler(Context context) {
        super(context, DB_Name, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String startData = "CREATE TABLE " + TABLE_NAME + " ("
                + FIRST_NAME_COL + " TEXT,"
                + LAST_NAME_COL + " TEXT,"
                + HEIGHT_COL + " TEXT,"
                + HEIGHT_METRIC_COL + " TEXT,"
                + WEIGHT_COL + " TEXT,"
                + WEIGHT_METRIC_COL + " TEXT,"
                + GOAL_WEIGHT_COL + " TEXT,"
                + GOAL_WEIGHT_METRIC_COL + " TEXT)";
        db.execSQL(startData);

        String stopwatchQuery = "CREATE TABLE " + STOPWATCH_TABLE_NAME + " ("
                + STOPWATCH_SECONDS_COL + " TEXT,"
                + STOPWATCH_MUSCLE_GROUP_COL + " TEXT,"
                + STOPWATCH_CLOSING_TIME_COL + " TEXT)";
        db.execSQL(stopwatchQuery);
    }

    public void addDataToStart(String firstName, String lastName, String height, String heightMetric, String weight, String weightMetric, String goalWeight, String goalWeightMetric) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(FIRST_NAME_COL, firstName);
        values.put(LAST_NAME_COL, lastName);
        values.put(HEIGHT_COL, height);
        values.put(HEIGHT_METRIC_COL, heightMetric);
        values.put(WEIGHT_COL, weight);
        values.put(WEIGHT_METRIC_COL, weightMetric);
        values.put(GOAL_WEIGHT_COL, goalWeight);
        values.put(GOAL_WEIGHT_METRIC_COL, goalWeightMetric);

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public void updateData(String firstName, String lastName, String height, String heightMetric, String weight, String weightMetric, String goalWeight, String goalWeightMetric) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(FIRST_NAME_COL, firstName);
        values.put(LAST_NAME_COL, lastName);
        values.put(HEIGHT_COL, height);
        values.put(HEIGHT_METRIC_COL, heightMetric);
        values.put(WEIGHT_COL, weight);
        values.put(WEIGHT_METRIC_COL, weightMetric);
        values.put(GOAL_WEIGHT_COL, goalWeight);
        values.put(GOAL_WEIGHT_METRIC_COL, goalWeightMetric);

        db.update(TABLE_NAME, values, null, null);
        db.close();
    }

    public void addStopwatchData(String seconds, String muscleGroup, String closingTime){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(STOPWATCH_SECONDS_COL, seconds);
        values.put(STOPWATCH_MUSCLE_GROUP_COL, muscleGroup);
        values.put(STOPWATCH_CLOSING_TIME_COL, closingTime);

        db.insert(STOPWATCH_TABLE_NAME, null, values);
        db.close();
    }

    public void updateStopwatchData(String seconds, String muscleGroup, String closingTime){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(STOPWATCH_SECONDS_COL, seconds);
        values.put(STOPWATCH_MUSCLE_GROUP_COL, muscleGroup);
        values.put(STOPWATCH_CLOSING_TIME_COL, closingTime);

        db.update(STOPWATCH_TABLE_NAME, values, null, null);
        db.close();
    }

    public int getStopWatchSeconds(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + STOPWATCH_SECONDS_COL + " FROM " + STOPWATCH_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        int seconds = 0;
        if (cursor.moveToFirst()) {
            seconds = Integer.parseInt(cursor.getString(0));
        }
        cursor.close();
        return seconds;
    }

    public String getStopWatchMuscleGroup(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + STOPWATCH_MUSCLE_GROUP_COL + " FROM " + STOPWATCH_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        String muscleGroup = "";
        if (cursor.moveToFirst()) {
            muscleGroup = cursor.getString(0);
        }
        cursor.close();
        return muscleGroup;
    }

    public long getStopWatchClosingTime(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + STOPWATCH_CLOSING_TIME_COL + " FROM " + STOPWATCH_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        long closingTime = 0;
        if (cursor.moveToFirst()) {
            closingTime = Long.parseLong(cursor.getString(0));
        }
        cursor.close();
        return closingTime;
    }

    public boolean getIfStopWatchExists(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + STOPWATCH_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }



    public String getFirstName() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + FIRST_NAME_COL + " FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        String firstName = "";
        if (cursor.moveToFirst()) {
            firstName = cursor.getString(0);
        }
        cursor.close();
        return firstName;
    }

    public String getLastName() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + LAST_NAME_COL + " FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        String lastName = "";
        if (cursor.moveToFirst()) {
            lastName = cursor.getString(0);
        }
        cursor.close();
        return lastName;
    }

    public String getHeight() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + HEIGHT_COL + " FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        String height = "";
        if (cursor.moveToFirst()) {
            height = cursor.getString(0);
        }
        cursor.close();
        return height;
    }

    public String getHeightMetric() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + HEIGHT_METRIC_COL + " FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        String heightMetric = "";
        if (cursor.moveToFirst()) {
            heightMetric = cursor.getString(0);
        }
        cursor.close();
        return heightMetric;
    }

    public String getWeight() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + WEIGHT_COL + " FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        String weight = "";
        if (cursor.moveToFirst()) {
            weight = cursor.getString(0);
        }
        cursor.close();
        return weight;
    }

    public String getWeightMetric() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + WEIGHT_METRIC_COL + " FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        String weightMetric = "";
        if (cursor.moveToFirst()) {
            weightMetric = cursor.getString(0);
        }
        cursor.close();
        return weightMetric;
    }

    public String getGoalWeight() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + GOAL_WEIGHT_COL + " FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        String goalWeight = "";
        if (cursor.moveToFirst()) {
            goalWeight = cursor.getString(0);
        }
        cursor.close();
        return goalWeight;
    }

    public String getGoalWeightMetric() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + GOAL_WEIGHT_METRIC_COL + " FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        String goalWeightMetric = "";
        if (cursor.moveToFirst()) {
            goalWeightMetric = cursor.getString(0);
        }
        cursor.close();
        return goalWeightMetric;
    }



    public boolean checkEmpty() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    public boolean allFilled(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            if(!cursor.getString(0).equals("") && !cursor.getString(1).equals("") && !cursor.getString(2).equals("") && !cursor.getString(3).equals("") && !cursor.getString(4).equals("") && !cursor.getString(5).equals("") && !cursor.getString(6).equals("") && !cursor.getString(7).equals("")){
                cursor.close();
                return true;
            }
        }
        cursor.close();
        return false;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}