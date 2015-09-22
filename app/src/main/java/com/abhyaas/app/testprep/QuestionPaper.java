package com.abhyaas.app.testprep;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by root on 20/9/15.
 */
public class QuestionPaper {
    Question [] ql;
    int numOfQs = -1;
    int currQ = -1;
    Question getNext(int inc){
        currQ = (currQ+inc+numOfQs)%numOfQs;
        return ql[currQ];
    }

    void mark(int ans){
        ql[currQ].markedAnswer = ans;
    }

    String result(){
        String ans = "";
        for(int i=0;i<numOfQs;i++){
            ans+="q: "+i+" marked: "+ql[i].markedAnswer+" correct: "+ql[i].answer+"\n";
        }
        return ans;
    }

    public QuestionPaper(BufferedReader br) throws IOException {
        String line = br.readLine();
        numOfQs = Integer.parseInt(line);
        Log.d("numOfQs", ""+numOfQs);
        ql = new Question[numOfQs];
        for(int i =0;i<numOfQs;i++){
            line = br.readLine();
            ql[i] = new Question(line);
        }
    }
}
