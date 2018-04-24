package com.maya.vgarages.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by Gokul Kalagara on 2/7/2018.
 */

public class SFSemiBoldTextView extends android.support.v7.widget.AppCompatTextView {
    public SFSemiBoldTextView(Context context) {
        super(context);
        init();
    }

    public SFSemiBoldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SFSemiBoldTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),"fonts/SFSemibold.ttf");
        setTypeface(tf);
    }

}
