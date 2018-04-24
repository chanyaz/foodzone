package com.maya.vgarages.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by Gokul Kalagara on 4/6/2018.
 */

public class WSSemiBoldTextView extends android.support.v7.widget.AppCompatTextView {
    public WSSemiBoldTextView(Context context) {
        super(context);
        init();
    }

    public WSSemiBoldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WSSemiBoldTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),"fonts/WorkSansSemiBold.ttf");
        setTypeface(tf);
    }

}
