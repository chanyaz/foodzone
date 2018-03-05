package com.maya.wadmin.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by Gokul Kalagara on 1/31/2018.
 */

public class SFBoldTextView extends android.support.v7.widget.AppCompatTextView {
    public SFBoldTextView(Context context) {
        super(context);
        init();
    }

    public SFBoldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SFBoldTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),"fonts/SFBold.ttf");
        setTypeface(tf);
    }

}
