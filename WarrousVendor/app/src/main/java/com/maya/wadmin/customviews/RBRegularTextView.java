package com.maya.wadmin.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by Gokul Kalagara on 2/6/2018.
 */

public class RBRegularTextView extends android.support.v7.widget.AppCompatTextView {
    public RBRegularTextView(Context context) {
        super(context);
        init();
    }

    public RBRegularTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RBRegularTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),"fonts/RBRegular.ttf");
        setTypeface(tf);
    }

}
