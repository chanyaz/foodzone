package com.maya.wcustomer.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by Gokul Kalagara on 2/6/2018.
 */

public class RBMediumTextView extends android.support.v7.widget.AppCompatTextView {
    public RBMediumTextView(Context context) {
        super(context);
        init();
    }

    public RBMediumTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RBMediumTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),"fonts/RBMedium.ttf");
        setTypeface(tf);
    }

}