package ranjbar.amirh.photowrote_final;

import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import ranjbar.amirh.photowrote_final.Views.DrawingView;
import ranjbar.amirh.photowrote_final.data.DataBaseDescription;

import static android.content.ContentValues.TAG;

/**
 * Created by amirh on 07/06/17.
 */

public class EditorFragment extends Fragment
implements LoaderManager.LoaderCallbacks<Cursor>{


    private boolean addingNewNote =true;

    private Uri photoUri;// for loading notes as contactUri

    DrawingView drawingView;

    private FrameLayout frameLayout;

    private View customView;

    private static final int NOTE_LOADER = 0; // identifies the Loader

    EditorFragmentListener listener;

    public interface EditorFragmentListener{
        void onSavingNote();
    }


    @Override
    public View onCreateView(LayoutInflater inflater
            , ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(
                R.layout.fragment_drawingview, container , false);

        drawingView = (DrawingView) view.findViewById(R.id.drawingViewFragment);
        drawingView.setLoadEditType(false); //Entering in Edit mode


        Bundle arguments = getArguments();
        if (arguments != null)
            photoUri = arguments.getParcelable(EditorActivity.NOTE_URI);


       // getLoaderManager().initLoader(NOTE_LOADER, null, this);

        return view;
    }

    // saves Note information to the database
    public void saveNote() {
        drawingView.setPhotoUri(photoUri);
        drawingView.SavingNote();
    }


    public DrawingView getDrawingView() {
        return drawingView;
    }



    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args){

        CursorLoader cursorLoader;

        Log.d(TAG , " onCreateLoaderrrrrrrrrrrr omad , id : " + id);
        Log.d(TAG , " onCreateLoaderrrrrrrrrrrr omad , noteUri : " + DataBaseDescription.Note.CONTENT_URI);


        switch (id){
            case NOTE_LOADER:
                cursorLoader = new CursorLoader(getActivity() , DataBaseDescription.Note.CONTENT_URI,null,null,null,null );
                break;
            default:
                cursorLoader = null;
                break;
        }

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        Log.d(TAG , " onLoadFinished  shod  , data : "+data);
        Log.d(TAG , " onLoadFinished  shod  , loader : "+loader);


        // if the contact exists in the database, display its data
        if (data != null && data.moveToFirst()) {

            Toast.makeText(getContext(), "Previous Notes has Successfully Loaded", Toast.LENGTH_LONG).show();

            do {
                int nameIndex = data.getColumnIndex(DataBaseDescription.Note.COLUMN_NAME);
                Log.d(TAG, " onLoadFinished  shod  , name index : " + nameIndex);
                String name = data.getString(nameIndex);
                Log.d(TAG, " onLoadFinished  shod  , name  : " + name);
                Log.d(TAG, " onLoadFinished  shod  , PhotoUri , last  : " + photoUri.getLastPathSegment());


                if (name != null && !name.isEmpty()) {
                    if(  name.equals(photoUri.getLastPathSegment())){
                        // get the column index for each data item
                        int pointX1Index = data.getColumnIndex(DataBaseDescription.Note.COLUMN_POINTX1);
                        int pointY1Index = data.getColumnIndex(DataBaseDescription.Note.COLUMN_POINTY1);
                        int pointX2Index = data.getColumnIndex(DataBaseDescription.Note.COLUMN_POINTX2);
                        int pointY2Index = data.getColumnIndex(DataBaseDescription.Note.COLUMN_POINTY2);

                        Log.d(TAG, " onLoadFinished  shod  , point index : " + pointX1Index);
                        Log.d(TAG, " onLoadFinished  shod  , point index : " + pointY1Index);
                        Log.d(TAG, " onLoadFinished  shod  , point index : " + pointX2Index);
                        Log.d(TAG, " onLoadFinished  shod  , point index : " + pointY2Index);

                        float pointX1 = Float.valueOf(data.getString(pointX1Index));
                        float pointY1 = Float.valueOf(data.getString(pointY1Index));
                        float pointX2 = Float.valueOf(data.getString(pointX2Index));
                        float pointY2 = Float.valueOf(data.getString(pointY2Index));

                        Log.d(TAG, " onLoadFinished  shod  , point  : " + pointX1);
                        Log.d(TAG, " onLoadFinished  shod  , point  : " + pointY1);
                        Log.d(TAG, " onLoadFinished  shod  , point  : " + pointX2);
                        Log.d(TAG, " onLoadFinished  shod  , point  : " + pointY2);


                        float p[] = {pointX1, pointY1, pointX2, pointY2};

                        drawingView.setArrayOfPoints(p);
                        Canvas canvas =new Canvas();
                        Paint paint = drawingView.getDrawPaint();
                        paint.setColor(Color.RED);
                        canvas.drawLine(pointX1,pointY1,pointX2,pointY2,paint);
                        drawingView.draw(canvas);

                    }
                }

            }while (data.moveToNext());

        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


}

