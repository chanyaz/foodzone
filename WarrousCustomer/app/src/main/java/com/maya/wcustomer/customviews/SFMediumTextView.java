package com.maya.wcustomer.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by Gokul Kalagara on 1/31/2018.
 */

public class SFMediumTextView extends android.support.v7.widget.AppCompatTextView {
    public SFMediumTextView(Context context) {
        super(context);
        init();
    }

    public SFMediumTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SFMediumTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),"fonts/SFMedium.ttf");
        setTypeface(tf);
    }

}
