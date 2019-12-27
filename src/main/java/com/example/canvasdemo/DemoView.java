package com.example.canvasdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

public class DemoView extends View {

    public DemoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private static ArrayList DataList = new ArrayList();
    private static ArrayList CentreList = new ArrayList();
    final Paint paint = new Paint();

    @Override
    public void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        // clears the background
        paint.setColor(getResources().getColor(R.color.colorCanvas));
        canvas.drawPaint(paint);
        // draws the points and centres
        drawPoints(canvas);
        drawCentroids(canvas);
    }

    public void setDataList(ArrayList DataList) {
        this.DataList = DataList;
    }

    public void setCentreList(ArrayList CentreList) {
        this.CentreList = CentreList;
    }

    public void drawPoints(Canvas canvas) {
        for (int i = 0; i < DataList.size(); i++) {
            Point p = (Point) DataList.get(i);
            paint.setColor(p.getColor());
            canvas.drawCircle((int) p.getX(), (int) p.getY(), 3, paint);
        }
    }

    public void drawCentroids(Canvas canvas) {
        for (int i = 0; i < CentreList.size(); i++) {
            Point p = (Point) CentreList.get(i);
            paint.setColor(p.getColor());
            canvas.drawCircle((int) p.getX(), (int) p.getY(), 9, paint);
        }
    }
}