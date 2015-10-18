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
    private int [] marked;
    private int [] time;
    int numOfQs = -1;
    int currQ = -1;
    int score = 0;


    int getNext(int inc){
        currQ += inc;
        return currQ<numOfQs? currQ:-1;
    }

    void mark(int ans){
        marked[currQ] = ans;
    }

    void markTime(int markTime){
        time[currQ] = markTime;
        updateScore();
    }
    void updateScore(){
        score+=time[currQ]*2;
    }


    int getScore(){
        return score;
    }

    boolean isCurrCorrect(){
        return ql[currQ].isCorrect(marked[currQ]);
    }

    String result(){
        String ans = "";
        for(int i=0;i<numOfQs;i++){
            ans+="q: "+i+" marked: "+marked[i]+" correct: "+ql[i].answer+"\n";
            score += time[i]*2;
        }
        return score+"\n"+ans;
    }

    public QuestionPaper(BufferedReader br) throws IOException {
        String line = br.readLine();
        numOfQs = Integer.parseInt(line);
        Log.d("numOfQs", ""+numOfQs);
        ql = new Question[numOfQs];
        marked = new int[numOfQs];
        time = new int[numOfQs];
        for(int i =0;i<numOfQs;i++){
            line = br.readLine();
            ql[i] = new Question(line);
            marked[i] = -1;
            time[i] = 0;
        }
    }

    public QuestionPaper(String qp_text){
        String [] lines = qp_text.split("\n");
        String line = lines[0];
        numOfQs = Integer.parseInt(line);
        Log.d("numOfQs", ""+numOfQs);
        ql = new Question[numOfQs];
        marked = new int[numOfQs];
        time = new int[numOfQs];
        for(int i =0;i<numOfQs;i++){
            line = lines[i+1];//0th line gave number of qs
            ql[i] = new Question(line);
            marked[i] = -1;
            time[i] = 0;
        }
    }
}
