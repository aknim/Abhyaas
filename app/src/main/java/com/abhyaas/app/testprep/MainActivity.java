package com.abhyaas.app.testprep;

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
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;


public class MainActivity extends ActionBarActivity {

    RadioGroup rg;
    RadioButton op1, op2, op3, op4;
    TextView ques, debug;
    NavigateButton next, previous;
    Button finish;

    int currQ;
    QuestionPaper qp;

    enum states{newQuestionState, OptionSelectedState, CorrectOptionState, IncorrectOptionState
    }

    states state;
    private View.OnClickListener navigateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("tracing", "started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialise();
    }

    protected void initialise() {
        //make global views
        rg = (RadioGroup)findViewById(R.id.rgroup);
        op1 = (RadioButton)findViewById(R.id.op1);
        op2 = (RadioButton)findViewById(R.id.op2);
        op3 = (RadioButton)findViewById(R.id.op3);
        op4 = (RadioButton)findViewById(R.id.op4);
        ques = (TextView)findViewById(R.id.quesText);
        debug = (TextView)findViewById(R.id.debug);
        next = (NavigateButton)findViewById(R.id.next);
        previous = (NavigateButton)findViewById(R.id.previous);
        finish = (Button)findViewById(R.id.finish);

        downloadAndSetupQP();
        setupQuestion(1);
        setupListeners();
        setupView();
    }

    private void enterNewQuestionState(){
        rg.clearCheck();
        op1.setBackgroundColor(getResources().getColor(R.color.notSelected));
        op2.setBackgroundColor(getResources().getColor(R.color.notSelected));
        op3.setBackgroundColor(getResources().getColor(R.color.notSelected));
        op4.setBackgroundColor(getResources().getColor(R.color.notSelected));

        previous.setEnabled(false);
        next.setEnabled(false);

        state = states.newQuestionState;
    }

    private void enterOptionClickedState(){
        state = states.OptionSelectedState;
    }

    private void enterCorrectOptionClickedState(int ans){
        rg.getChildAt(ans).setBackgroundColor(getResources().getColor(R.color.correct));

        previous.setEnabled(true);
        next.setEnabled(true);

        state = states.CorrectOptionState;
    }

    private void enterIncorrectOptionClickedState(int ans, int wrong){
        rg.getChildAt(ans).setBackgroundColor(getResources().getColor(R.color.correct));
        rg.getChildAt(wrong).setBackgroundColor(getResources().getColor(R.color.incorrect));

        previous.setEnabled(true);
        next.setEnabled(true);

        state = states.IncorrectOptionState;
    }
    /*
    Not in use
     */
    private void FileToQuestion() {
        String file = "data.txt";
        try {
            Log.d("tracing", "try of setupQP1");
            BufferedReader br = new BufferedReader(new InputStreamReader(getResources().openRawResource(R.raw.data)));
            qp = new QuestionPaper(br);
        } catch (Exception e) {
            Log.d("setupQPException1", e.getMessage());
            ((TextView) findViewById(R.id.debug)).setText((CharSequence) e.toString());
        }
    }

    private void downloadAndSetupQP(){
        try {
            Log.d("tracing", "try of setupQP");
            String qp_text = new Post().execute("req","").get();
            qp = new QuestionPaper(qp_text);
            //qp = new QuestionPaper(new DownloadTask().execute("").get());
        } catch (Exception e) {
            Log.d("setupQPException", e.getMessage());
            ((TextView) findViewById(R.id.debug)).setText((CharSequence) e.toString());
        }
    }

    private void setupListeners() {
        navigateListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    setupQuestion(((NavigateButton) v).increment);
            }
        };


        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(state == states.newQuestionState) {//checking as the fun is called even on resetting
                    int marked = rg.indexOfChild((findViewById(rg.getCheckedRadioButtonId())));
                    int correct = qp.ql[currQ].answer;

                    qp.marked[currQ] = marked;
                    if (qp.isCurrCorrect()) {
                        enterCorrectOptionClickedState(correct);
                    } else {
                        enterIncorrectOptionClickedState(correct, marked);
                    }
                }
            }
        });

        ((Button) findViewById(R.id.finish)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayResult();
            }
        });

        ((Button) findViewById(R.id.send)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = (String) ((TextView)findViewById(R.id.myMsg)).getText();
                //new QuestionPaper(new DownloadTask().execute("").get());
                try {
                    text = new Post().execute(text).get();
                } catch (InterruptedException e) {
                    Log.d("post",e.toString());
                } catch (ExecutionException e) {
                    Log.d("post",e.toString());
                }
                ((TextView) findViewById(R.id.serverResponse)).setText(text);
            }
            });
    }

    private void setupView() {
        ((TextView) findViewById(R.id.head)).setText("Do you got what it takes!!");
        (next).set(1, "next", navigateListener);
        (previous).set(-1, "previous", navigateListener);
    }

    private void displayResult() {
        String result = qp.result();
        int score = Integer.parseInt(result.split("\n")[0]);
        String fromServer = "";
        try {
            fromServer = new Post().execute("data","1;"+score).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        ((TextView) findViewById(R.id.debug)).setText(fromServer + "\n"+result);
    }


    private void setupQuestion(int increment) {
        currQ = qp.getNext(increment);
        
        Question q = qp.ql[currQ];
        //setValues
        ques.setText(q.question);
        op1.setText(q.op1);
        op2.setText(q.op2);
        op3.setText(q.op3);
        op4.setText(q.op4);
        //state related changes. The above defines the state
        enterNewQuestionState();
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
