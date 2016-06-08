package com.example.asel.testsystem;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;


public class AddQuestionActivity extends ActionBarActivity implements View.OnClickListener {
    RadioGroup radioGroup;
    Button addVariant, submitQuestion;
    EditText editQuestion, editVariant;
    ArrayList<String> variants;
    QuestionDatabaseAdapter db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);
        initComponents();
    }

    public void initComponents() {
        db = new QuestionDatabaseAdapter(this);
        this.deleteDatabase("question_database");
        radioGroup = (RadioGroup)findViewById(R.id.radioVariants);
        addVariant = (Button)findViewById(R.id.addVariant);
        addVariant.setOnClickListener(this);
        submitQuestion = (Button)findViewById(R.id.submitQuestion);
        submitQuestion.setOnClickListener(this);
        editVariant = (EditText)findViewById(R.id.editVariant);
        editQuestion = (EditText)findViewById(R.id.editText);
        variants = new ArrayList<>();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_question, menu);
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
        if (v.getId() == R.id.addVariant) {
            RadioButton radioButton = new RadioButton(this);
            String varText = editVariant.getText().toString();
            radioButton.setText(varText);
            variants.add(varText);
            editVariant.setText("");
            radioGroup.addView(radioButton);
        }
        if (v.getId() == R.id.submitQuestion) {
            /*for (String i: variants) {
                Log.i("var", i);
            }*/
            //Log.i("check", radioGroup.getCheckedRadioButtonId()+"");
            long id = db.insertData(editQuestion.getText().toString(), variants, radioGroup.getCheckedRadioButtonId()-1);
            Toast.makeText(this, ""+id, Toast.LENGTH_LONG).show();
        }

    }
}
