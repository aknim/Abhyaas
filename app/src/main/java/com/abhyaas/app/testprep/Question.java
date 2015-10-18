package com.abhyaas.app.testprep;

import android.util.Log;

/**
 * Created by root on 19/9/15.
 */
public class Question {
    String question, op1, op2, op3, op4;
    int answer = -1;//0-3

    public Question(String q, String o1, String o2, String o3, String o4, int ans){
        question = q;
        op1 = o1;
        op2 = o2;
        op3 = o3;
        op4 = o4;
        answer = ans;
    }

    public boolean isCorrect(int option){
        return option == answer;
    }
    public Question(String line){
        String [] lineSplit = line.split(";");
        question = lineSplit[0];
        op1 = lineSplit[1];
        op2 = lineSplit[2];
        op3 = lineSplit[3];
        op4 = lineSplit[4];
        answer = Integer.parseInt(lineSplit[5]);
    }
}
