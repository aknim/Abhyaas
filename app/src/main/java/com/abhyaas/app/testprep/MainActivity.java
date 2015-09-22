package com.abhyaas.app.testprep;

import android.provider.MediaStore;
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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


public class MainActivity extends ActionBarActivity {

    Question q;
    QuestionPaper qp;
    int currQ;

    private View.OnClickListener navigateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("abc","def");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialise();
    }

    protected void initialise(){
        setupQuestionPaper();
        //q = new Question();
        Log.d("b4","b4 qp.nxt");
        q = qp.getNext(1);
        setupQuestion();
        setupListeners();
        setupView();
    }

    private void setupQuestionPaper() {
        String file = "data.txt";
        try {
            Log.d("b","try of setupQP");
            BufferedReader br = new BufferedReader(new InputStreamReader(getResources().openRawResource(R.raw.data)));
            qp = new QuestionPaper(br);
        }
        catch(Exception e){
            Log.d("a", e.getMessage());
            ((TextView)findViewById(R.id.debug)).setText((CharSequence) e.toString());
        }
    }

    private void setupListeners() {
        navigateListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioGroup rg = (RadioGroup)findViewById(R.id.rgroup);
                q.setMarkedAnswer(rg.indexOfChild(rg.findViewById(rg.getCheckedRadioButtonId()))+1);
                q = qp.getNext(((NavigateButton)v).increment);
                ((TextView)findViewById(R.id.debug)).setText("lsnr"+q.markedAnswer);
                setupQuestion();
            }
        };

        ((Button)findViewById(R.id.finish)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayResult();
            }
        });
    }

    private void setupView() {
        ((TextView)findViewById(R.id.head)).setText("Do you got what it takes!!");
        ((NavigateButton) findViewById(R.id.next)).set(1, "next", navigateListener);
        ((NavigateButton) findViewById(R.id.previous)).set(-1, "previous", navigateListener);
    }

    private void displayResult() {
        ((TextView)findViewById(R.id.debug)).setText(qp.result());
    }


    private void setupQuestion() {
        ((TextView)findViewById     (R.id.quesText)).setText(q.question);
        ((RadioButton)findViewById  (R.id.op1)).setText(q.op1);
        ((RadioButton)findViewById  (R.id.op2)).setText(q.op2);
        ((RadioButton)findViewById  (R.id.op3)).setText(q.op3);
        ((RadioButton)findViewById  (R.id.op4)).setText(q.op4);

        ((RadioGroup)findViewById(R.id.rgroup)).clearCheck();

        RadioGroup rg = (RadioGroup) findViewById(R.id.rgroup);
        if(q.markedAnswer!=-2 && q.markedAnswer!=0) rg.check(((RadioButton) rg.getChildAt(q.markedAnswer-1)).getId());
        //Set to 0, when it clears the check. When it clears the check, it stores the value of 0
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}