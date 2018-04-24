package com.maya.wadmin.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.widget.TextViewCompat;
import android.util.AttributeSet;
import android.widget.TextView;


/**
 * Created by Gokul Kalagara on 1/25/2018.
 */

public class RegularTextView extends android.support.v7.widget.AppCompatTextView {
    public RegularTextView(Context context) {
        super(context);
        init();
    }

    public RegularTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RegularTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),"fonts/regular.ttf");
        setTypeface(tf);
    }

}
