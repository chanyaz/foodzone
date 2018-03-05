package com.maya.wadmin.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by Gokul Kalagara on 2/6/2018.
 */

public class RBBoldTextView extends android.support.v7.widget.AppCompatTextView {
    public RBBoldTextView(Context context) {
        super(context);
        init();
    }

    public RBBoldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RBBoldTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),"fonts/RBBold.ttf");
        setTypeface(tf);
    }

}