package com.maya.vgarages.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by Gokul Kalagara on 4/6/2018.
 */

public class WSRegularTextView extends android.support.v7.widget.AppCompatTextView {
    public WSRegularTextView(Context context) {
        super(context);
        init();
    }

    public WSRegularTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WSRegularTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),"fonts/WorkSansRegular.ttf");
        setTypeface(tf);
    }

}
