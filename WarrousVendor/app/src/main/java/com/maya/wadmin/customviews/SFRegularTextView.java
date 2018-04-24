package com.maya.wadmin.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by Gokul Kalagara on 1/31/2018.
 */

public class SFRegularTextView extends android.support.v7.widget.AppCompatTextView {
    public SFRegularTextView(Context context) {
        super(context);
        init();
    }

    public SFRegularTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SFRegularTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),"fonts/SFRegular.ttf");
        setTypeface(tf);
    }

}
