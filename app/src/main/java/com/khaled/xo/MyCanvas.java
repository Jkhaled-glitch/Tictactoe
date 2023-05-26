package com.khaled.xo;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class MyCanvas extends View {
    Rect rect;

    Paint paint;

    float xPos,yPos;

    float x1= 0;
    float x2 = 0;
    float y1 = 0;
    float y2 = 0;
    int point = 1;


    public MyCanvas(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        rect = new Rect(0,100,200,300);
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(40);
        paint.setStyle(Paint.Style.STROKE);

        //Draw Rectangle

        //  canvas.drawRect(rect,paint);

        //Draw Circle

        // canvas.drawCircle(xPos,yPos,100,paint);

        //Draw Line
        /*
        if(point == 2){
            canvas.drawLine(x1,y1,x2,y2,paint);

        }

         */

        //Draw x

        canvas.drawLine(40,40,getWidth()-40,getWidth() - 40,paint);
        canvas.drawLine(40,getWidth()-40,getWidth()-40,40,paint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //xPos = event.getX();
        // yPos = event.getY();

        //Line

        if(point == 1){
            x1 = event.getX();
            y1 = event.getY();
            point = 2;
        }else {

            x2 = event.getX();
            y2 = event.getY();
            point = 1;

        }
        String coord = "x1: "+x1+"y1: "+y1+"x2: "+x2+"y2: "+y2;

        Toast.makeText(getContext(), coord, Toast.LENGTH_LONG).show();
        invalidate();
        return super.onTouchEvent(event);
    }
}

