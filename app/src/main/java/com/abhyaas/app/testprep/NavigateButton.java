package com.abhyaas.app.testprep;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import java.util.jar.Attributes;

/**
 * Created by root on 20/9/15.
 */
public class NavigateButton extends Button {
    int increment = 0;
    public NavigateButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void set(int i, String name, OnClickListener clickListener) {
        increment = i;
        this.setText(name);
        this.setOnClickListener(clickListener);
    }
}
