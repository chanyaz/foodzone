package com.maya.wadmin.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;

import com.maya.wadmin.R;
import com.maya.wadmin.utilities.Utility;

/**
 * Created by Gokul Kalagara on 2/21/2018.
 */

public class Seekbar extends AppCompatSeekBar
{

    public Seekbar(Context context) {
        super(context);
    }

    public Seekbar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Seekbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas c)
    {
        super.onDraw(c);
        int thumb_x = (int) (( (double)this.getProgress()/this.getMax() ) * (double)this.getWidth());
        float middle = (float) (this.getHeight());
        Paint paint = new Paint();
        paint.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        c.drawCircle(thumb_x+70,middle - 20,30,paint);
        paint.setColor(Color.WHITE);
        paint.setTextSize(Utility.dpSize(getContext(),14));
        c.drawText(""+this.getProgress(), thumb_x + 70, middle - 20, paint);
    }
}
