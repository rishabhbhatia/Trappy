package com.satiate.trapi.customs;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Rishabh Bhatia on 24/11/16.
 */

public class SlantView extends View {

    private Context mContext;
    Paint paint ;
    Path path;

    public SlantView(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
        mContext = ctx;
        setWillNotDraw(false);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int w = getWidth(), h = getHeight();
        paint.setStrokeWidth(2);
        paint.setColor(Color.parseColor("#424242"));
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setAntiAlias(true);

        path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(w,0);
        path.lineTo(w,h);
        path.lineTo(0,h);
        path.close();
        canvas.drawPath(path, paint);
    }
}