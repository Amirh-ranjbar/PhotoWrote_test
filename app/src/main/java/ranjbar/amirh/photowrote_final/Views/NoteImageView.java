package ranjbar.amirh.photowrote_final.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

import static android.content.ContentValues.TAG;

/**
 * Created by amirh on 13/06/17.
 */

public class NoteImageView extends ImageView {

    private Paint defaultPaint;
    private  int paintColor = Color.YELLOW;

    private float pointX;
    private float pointY;

    private static final float radius = 150;


    public NoteImageView(Context context) {
        super(context);
    }

    public void setupPaint(){

        defaultPaint = new Paint();

        defaultPaint.setColor(paintColor);
        defaultPaint.setAntiAlias(true);
        defaultPaint.setStrokeWidth(5);
        defaultPaint.setStyle(Paint.Style.STROKE);
        defaultPaint.setStrokeJoin(Paint.Join.ROUND);
        defaultPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    public NoteImageView(Context context , AttributeSet attributeSet){
        super(context,attributeSet);

    }

    public void SetViewPoints(float x , float y){
        pointX=x;
        pointY=y;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d(TAG,"value of point :::: " + pointX);

        if(pointX != 0)
            canvas.drawCircle(pointX,pointY,radius,defaultPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
