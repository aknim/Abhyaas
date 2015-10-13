package com.abhyaas.app.testprep;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Environment;
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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

import static android.content.Context.MODE_PRIVATE;


public class MainActivity extends ActionBarActivity {

    Question q;
    QuestionPaper qp;
    int currQ;

    int totalSize;

    private View.OnClickListener navigateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("tracing", "started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialise();
    }

    protected void initialise() {
        setupQuestionPaper();
        Log.d("tracing", "b4 qp.nxt");
        q = qp.getNext(1);
        setupQuestion();
        setupListeners();
        setupView();
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

    private void setupQuestionPaper(){
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
                RadioGroup rg = (RadioGroup) findViewById(R.id.rgroup);
                q.setMarkedAnswer(rg.indexOfChild(rg.findViewById(rg.getCheckedRadioButtonId())) + 1);
                Log.d("bugMOT", qp.result());
                q = qp.getNext(((NavigateButton) v).increment);
                ((TextView) findViewById(R.id.debug)).setText("lsnr" + q.markedAnswer);
                setupQuestion();
            }
        };

        ((Button) findViewById(R.id.finish)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioGroup rg = (RadioGroup) findViewById(R.id.rgroup);
                q.setMarkedAnswer(rg.indexOfChild(rg.findViewById(rg.getCheckedRadioButtonId())) + 1);
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
        ((NavigateButton) findViewById(R.id.next)).set(1, "next", navigateListener);
        ((NavigateButton) findViewById(R.id.previous)).set(-1, "previous", navigateListener);
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


    private void setupQuestion() {
        ((TextView) findViewById(R.id.quesText)).setText(q.question);
        RadioButton rb1 = ((RadioButton) findViewById(R.id.op1));
        RadioButton rb2 = ((RadioButton) findViewById(R.id.op2));
        RadioButton rb3 = ((RadioButton) findViewById(R.id.op3));
        RadioButton rb4 = ((RadioButton) findViewById(R.id.op4));

        rb1.setText(q.op1);
        rb2.setText(q.op2);
        rb3.setText(q.op3);
        rb4.setText(q.op4);
        RadioGroup rg = (RadioGroup) findViewById(R.id.rgroup);

        int checkId = rg.getCheckedRadioButtonId();
        rg.clearCheck();
        if (q.markedAnswer != -2 && q.markedAnswer != 0) {
            rg.check(((RadioButton) rg.getChildAt(q.markedAnswer - 1)).getId());
            ((RadioButton) rg.getChildAt(q.markedAnswer - 1)).setChecked(true);
        }
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
