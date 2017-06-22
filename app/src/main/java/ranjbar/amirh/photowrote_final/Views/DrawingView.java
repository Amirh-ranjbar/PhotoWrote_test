package ranjbar.amirh.photowrote_final.Views;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ranjbar.amirh.photowrote_final.R;
import ranjbar.amirh.photowrote_final.data.DataBaseDescription.Note;

import static android.content.ContentValues.TAG;


public class DrawingView extends View{

    private boolean addingNewNote =true;

    private Uri photoUri;// for loading notes as contactUri


    // setup initial color
    private  int paintColor = Color.BLACK;
    // defines paint and canvas
    private Paint drawPaint;
    private Paint drawPaint2;
    // stores next circle
    private ArrayList<Path> paths = new ArrayList<>();
    private final Map<Integer, Path> pathMap = new HashMap<>();

    private Map<Path, Integer> colorsMap = new HashMap<Path, Integer>();

    private boolean loadEditType= false;//Enter in Edit Mode by defualt

    float pointX1 ;
    float pointY1 ;
    float pointX2 ;
    float pointY2 ;
    private float arrayOfPoints[] ;

    private int lastPointLineId;

    Bitmap noteImageBitmap;

    public DrawingView(Context context , AttributeSet attrs) {
        super(context, attrs);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setupPaint();

    }

    public DrawingView(Context context)
    {
        super(context);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setupPaint();

    }

    public void setupPaint() {

        noteImageBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.noteimage);

        // Setup paint with color and stroke styles
        drawPaint = new Paint();

        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(5);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    public void setPhotoUri(Uri uri){
        photoUri = uri;
    }

    public float[] getArrayOfPoints(){
        Log.d(TAG, " points   : " + pointX1 +" + " + pointY1);

        arrayOfPoints = new float[5];
        arrayOfPoints[0] = pointX1;
        arrayOfPoints[1] = pointY1;
        arrayOfPoints[2] = pointX2;
        arrayOfPoints[3] = pointY2;

        return arrayOfPoints;
    }

    public void setArrayOfPoints(float p[])
    {
        pointX1 = p[0];
        pointY1 = p[1];
        pointX2 = p[2];
        pointY2 = p[3];

        Path path = new Path();
        path.moveTo(pointX1,pointY1);
        path.lineTo(pointX2,pointY2);
        paths.add(path);

    }

    public void setLoadEditType(boolean type){
        loadEditType = type;
    }
    public Paint getDrawPaint(){
        return drawPaint;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        float height = noteImageBitmap.getHeight();
        float width = noteImageBitmap.getWidth();

        PathMeasure pm ;

        float endPoint[]={0f,0f};


        if(pointX1 == pointX2 && pointY1 == pointY2)
        {
            Toast.makeText(getContext(), "Please Draw a Line", Toast.LENGTH_LONG).show();
        }
        else {
            canvas.drawLine(pointX1, pointY1, pointX2, pointY2, drawPaint);
            canvas.drawBitmap(noteImageBitmap,pointX2 - width,pointY2- height ,drawPaint);

        }
        for(Path p : paths ) {
            pm = new PathMeasure(p,false);
            pm.getPosTan(pm.getLength() ,endPoint,null);

            canvas.drawPath(p, drawPaint);
            canvas.drawBitmap(noteImageBitmap, endPoint[0] - width , endPoint[1] - height ,drawPaint);

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getActionMasked(); // event type
        int actionIndex = event.getActionIndex(); // pointer (i.e., finger)

        float pX = event.getX(actionIndex);
        float pY = event.getY(actionIndex);

        PathMeasure pm ;

        float endPoint[]={0f,0f};

        if(loadEditType){
            // determine whether touch started, ended or is moving
            if (action == MotionEvent.ACTION_DOWN ||
                    action == MotionEvent.ACTION_POINTER_DOWN) {

            }
            else if (action == MotionEvent.ACTION_UP ||
                    action == MotionEvent.ACTION_POINTER_UP) {

                Log.d(TAG, "paein omad , Action_down :    " + pointX1 + " , " + pointY1);

                for(Path p : paths) {
                    pm = new PathMeasure(p,false);
                    pm.getPosTan(pm.getLength() ,endPoint,null);
                    Log.d(TAG , " end point of path - noteImageBitmap.getHeight :" +( endPoint[0] - noteImageBitmap.getWidth())  );
                    Log.d(TAG , " end point of path  X: " +endPoint[0]  );
                    Log.d(TAG , " midel point of path - noteImageBitmap.getHeight : " + (endPoint[1] - noteImageBitmap.getHeight() ) );
                    Log.d(TAG , " end point of path Y : " +endPoint[1]  );

                    if(pX > (endPoint[0] - noteImageBitmap.getWidth())
                            && pX < endPoint[0]
                            && pY > (endPoint[1] - noteImageBitmap.getHeight())
                            && pY < (endPoint[1]))
                    {
                        //call the detailFragment
                        Log.d(TAG , " omad tosh : kooooooooooooooooooon topol "  );
                        //need DrawingView interface that implement in EditorActivity
                    }
                }
            }
            else {
                //touchMoved(event);
                return false;
            }
            invalidate(); // redraw
        }
        else {
            //Edit Mode

            // Checks for the event that occurs
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    pointX1 = pX;
                    pointY1 = pY;
                    Log.d(TAG, "paein omad , Action_down :    " + pointX1 + " , " + pointY1);
                    return true;
//		case MotionEvent.ACTION_MOVE:
//			break;
                case MotionEvent.ACTION_UP:
                    pointX2 = pX;
                    pointY2 = pY;
                    Log.d(TAG, "bala omad , Action_up:    " + pointX2 + " , " + pointY2);

                    lastPointLineId = event.getPointerId(actionIndex);
                    break;
                default:
                    return false;
            }
            // Force a view to draw again
            postInvalidate();
        }
        return true;
    }

    public void SavingNote() {

        if (pointX1 == pointX2 && pointY1 == pointY2) {
            Toast.makeText(getContext(), "Please Draw a Line", Toast.LENGTH_LONG).show();
        }
        else {

            Log.d(TAG, " points   : " + pointX1 + " + " + pointY1);
            Log.d(TAG, " points   : " + pointX2 + " + " + pointY2);
            Log.d(TAG, " Photo Name , lastSegment : " + photoUri.getLastPathSegment());
            // create ContentValues object containing    contact's key-value pairs
            ContentValues contentValues = new ContentValues();
            contentValues.put(Note.COLUMN_NAME, photoUri.getLastPathSegment());
            contentValues.put(Note.COLUMN_POINTX1, pointX1);
            contentValues.put(Note.COLUMN_POINTY1, pointY1);
            contentValues.put(Note.COLUMN_POINTX2, pointX2);
            contentValues.put(Note.COLUMN_POINTY2, pointY2);
            contentValues.put(Note.COLUMN_TITLE, "");
            contentValues.put(Note.COLUMN_INFO, "");


            if (addingNewNote) {
                // when called SavingNote , add new path to paths array
                Path path = new Path();
                path.moveTo(pointX1, pointY1);
                path.lineTo(pointX2, pointY2);
                paths.add(path);

                // use Activity's ContentResolver to invoke
                // insert on the AddressBookContentProvider
                Uri newContactUri = getContext().getContentResolver().insert(
                        Note.CONTENT_URI, contentValues);

                if (newContactUri != null) {
                    Log.v(TAG, " add shod , NewContactUri : " + newContactUri);
                    Toast.makeText(getContext(), "New Note has Successfully Added", Toast.LENGTH_LONG).show();

                    //Snackbar.make(linearLayout,
                    //       R.string.note_added,Snackbar.LENGTH_LONG).show();
                    //listener.onAddEditCompleted(newContactUri);
                } else {
                    //Snackbar.make(linearLayout,
                    //        R.string.note_not_added, Snackbar.LENGTH_LONG).show();
                    Log.d(TAG, "kirrrrrrrrrrr  2::  " + R.string.note_not_added);
                }
            } else {
                // use Activity's ContentResolver to invoke
                // insert on the AddressBookContentProvider
                int updatedRows = getContext().getContentResolver().update(
                        photoUri, contentValues, null, null);

                if (updatedRows > 0) {
                    // listener.onAddEditCompleted(contactUri);
                    //   Snackbar.make(linearLayout,
                    //         R.string.note_updated, Snackbar.LENGTH_LONG).show();
                    Log.d(TAG, "kirrrrrrrrrrr 3::  " + R.string.note_not_added);
                } else {
                    //  Snackbar.make(linearLayout,
                    //        R.string.note_not_updated, Snackbar.LENGTH_LONG).show();
                    Log.d(TAG, "kirrrrrrrrrrr 4::  " + R.string.note_not_updated);
                }
            }
        }
    }
}
