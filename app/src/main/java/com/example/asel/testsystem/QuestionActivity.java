package com.example.asel.testsystem;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;


public class QuestionActivity extends ActionBarActivity implements View.OnClickListener {
    TextView textView;
    RadioGroup radioGroup;
    Button buttonNext;
    int correctAns = 0, currentQuestion = 0;
    ArrayList<String> questions;
    QuestionDatabaseAdapter db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        initComponents();
        //Toast.makeText(this, , Toast.LENGTH_LONG).show();
        showQuestion();
    }

    public void initComponents() {
        db = new QuestionDatabaseAdapter(this);
        textView = (TextView) findViewById(R.id.textView);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        questions = db.getAllQuestions();
        buttonNext = (Button) findViewById(R.id.button);
        buttonNext.setOnClickListener(this);
    }

    public void showQuestion() {
        String question = questions.get(currentQuestion);
        textView.setText("Question number " + (currentQuestion+1) + " out of " + questions.size() + "\n" + question);
        ArrayList<String> variants = db.getVariants(currentQuestion+1);
        Log.i("nvkjf", variants.toString());
        for (String v : variants) {
            Log.i("var", v);
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(v);
            radioGroup.addView(radioButton);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_question, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button) {
            int checked = radioGroup.getCheckedRadioButtonId();
            String ans = (String) ((RadioButton) findViewById(checked)).getText();
            if (ans.equals(db.getQuestion(currentQuestion+1))) correctAns++;
            Toast.makeText(this, "clicked "+checked, Toast.LENGTH_LONG).show();
            if (currentQuestion+1 != questions.size()) {
                radioGroup.removeAllViews();
                currentQuestion++;
                showQuestion();
            }
            else {
                float res = (correctAns*100)/questions.size();
                Intent i = new Intent(this, ResultActivity.class);
                i.putExtra("result in percent", res);
                startActivity(i);
            }
        }
    }
}
