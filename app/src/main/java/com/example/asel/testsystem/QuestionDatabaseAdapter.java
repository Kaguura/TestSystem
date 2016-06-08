package com.example.asel.testsystem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Asel on 08.06.2016.
 */
public class QuestionDatabaseAdapter {
    QuestionHelper helper;

    public QuestionDatabaseAdapter(Context context){
        helper = new QuestionHelper(context);
    }

    public long insertData(String question, ArrayList<String> variants, int answerNum){//dobavlenie
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(QuestionHelper.QUESTION, question);



        contentValues.put(QuestionHelper.VARIANT+'A', variants.get(0));
        contentValues.put(QuestionHelper.VARIANT+'B', variants.get(1));
        contentValues.put(QuestionHelper.VARIANT+'C', variants.get(2));
        contentValues.put(QuestionHelper.ANSWER, variants.get(answerNum));



        long question_id = db.insert(QuestionHelper.QUESTION_TABLE, null, contentValues);

        /*for (int i = 0; i < variants.size(); i++) {
            contentValues.put(QuestionHelper.VARIANT, variants.get(i));
            contentValues.put(QuestionHelper.QUESTION_REF, question_id);
            if (i == answerNum) contentValues.put(QuestionHelper.IS_ANSWER, "TRUE");
            else contentValues.put(QuestionHelper.IS_ANSWER, "FALSE");
            db.insert(QuestionHelper.VARIANT_TABLE, null, contentValues);
        }*/

        return question_id;
    }

    public ArrayList<String> getAllQuestions(){
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {QuestionHelper.QUESTION};
        Cursor cursor = db.query(QuestionHelper.QUESTION_TABLE, columns, null, null, null, null, null);
        ArrayList<String> list = new ArrayList<String>();
        while (cursor.moveToNext()){
            String q = cursor.getString(0);
            list.add(q);
        }
        return list;
    }
    public String getQuestion(int question_id){
        SQLiteDatabase db = helper.getReadableDatabase();
        String[] columns = {QuestionHelper.QUESTION_ID, QuestionHelper.QUESTION};
        Cursor cursor = db.query(QuestionHelper.QUESTION_TABLE, columns, null, null, null, null, null);
        String question = "";
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String q = cursor.getString(1);
            if (id == question_id) {
                question = q;
                break;
            }
        }
        return question;
    }

    public ArrayList<String> getVariants(int question_id){
        SQLiteDatabase db = helper.getReadableDatabase();
        String[] columns = {QuestionHelper.QUESTION_ID, QuestionHelper.VARIANT+'A', QuestionHelper.VARIANT+'B', QuestionHelper.VARIANT+'C'};
        Cursor cursor = db.query(QuestionHelper.QUESTION_TABLE, columns, null, null, null, null, null);
        ArrayList<String> variants = new ArrayList<>();
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String a = cursor.getString(1);
            String b = cursor.getString(2);
            String c = cursor.getString(3);
            Log.i(id+"", question_id+"");
            if (id == question_id) {
                variants.add(a);
                variants.add(b);
                variants.add(c);
            }
        }
        return variants;



        /*SQLiteDatabase db = helper.getReadableDatabase();
        String[] columns = {QuestionHelper.VARIANT};
        Cursor cursor = db.query(QuestionHelper.VARIANT_TABLE, columns, null, null, null, null, null);
        Log.i("getvar", "!!!");
        ArrayList<String> variants = new ArrayList<>();
        while (cursor.moveToNext()){
            Log.i("while", "!!!");
            int ref_id = cursor.getInt(0);
            String v = cursor.getString(0);
            if (ref_id == question_id)
                variants.add(v);
        }
        return variants;*/
    }

    public boolean isRightAnswer(int question_id, String answer){
        SQLiteDatabase db = helper.getReadableDatabase();
        String[] columns = {QuestionHelper.QUESTION_ID, QuestionHelper.ANSWER};
        Cursor cursor = db.query(QuestionHelper.QUESTION_TABLE, columns, null, null, null, null, null);
        boolean isRight = false;
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String ans = cursor.getString(1);
            if (id == question_id) {
                isRight = true;
            }
        }
        return isRight;




        /*SQLiteDatabase db = helper.getReadableDatabase();
        String[] columns = {QuestionHelper.QUESTION_REF, QuestionHelper.VARIANT, QuestionHelper.IS_ANSWER};
        Cursor cursor = db.query(QuestionHelper.VARIANT_TABLE, columns, null, null, null, null, null);
        boolean isRight = false;
        while (cursor.moveToNext()){
            int ref_id = cursor.getInt(0);
            String v = cursor.getString(1);
            String isAns = cursor.getString(2);
            if (ref_id == question_id && v.equals(answer) && isAns.equals("TRUE")) {
                isRight = true;
                break;
            }
        }
        return isRight;*/
    }


    class QuestionHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "question_database";
        private static final String QUESTION_TABLE = "question_table";
        private static final String VARIANT_TABLE = "variant_table";
        private static final int DATABASE_VERSION = 1;
        private static final String QUESTION_ID = "question_id";
        private static final String VARIANT_ID = "variant_id";
        private static final String QUESTION_REF = "question_ref";
        private static final String QUESTION = "question";
        private static final String VARIANT = "variant";
        private static final String IS_ANSWER = "is_answer";
        private static final String ANSWER = "answer";

        private static final String CREATE_TABLE_QUESTION = "create table "+QUESTION_TABLE+"( " +QUESTION_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
                +QUESTION+" VARCHAR(255), " +VARIANT+"A VARCHAR(255), "+VARIANT+"B VARCHAR(255), "+VARIANT+"C VARCHAR(255), "+ANSWER+" VARCHAR(255))";
        /*private static final String CREATE_TABLE_VARIANT = "create table "+VARIANT_TABLE+"( " +VARIANT_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
                +VARIANT+" VARCHAR(255), "+QUESTION_REF+" INTEGER, FOREIGN_KEY ("+QUESTION_REF+") REFERENCES "+QUESTION_TABLE+"("+QUESTION_ID+") "
                +IS_ANSWER+" BOOLEAN)";
        */
        private static final String DROP_TABLE_QUESTION  = "DROP TABLE IF EXISTS " + QUESTION_TABLE;
        //private static final String DROP_TABLE_VARIANT  = "DROP TABLE IF EXISTS " + VARIANT_TABLE;

        private Context context;


        public QuestionHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
            Toast.makeText(context, "constructer called", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(CREATE_TABLE_QUESTION);
                //db.execSQL(CREATE_TABLE_VARIANT);
            }catch(SQLException e){
                Toast.makeText(context, ""+e, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                db.execSQL(DROP_TABLE_QUESTION);
                //db.execSQL(DROP_TABLE_VARIANT);
                onCreate(db);
            }catch (SQLException e) {
                Toast.makeText(context, ""+e, Toast.LENGTH_LONG).show();
            }
        }
    }
}
