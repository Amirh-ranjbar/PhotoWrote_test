package ranjbar.amirh.photowrote_final;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import static android.content.ContentValues.TAG;

public class                 EditorActivity extends AppCompatActivity
{


    private Button openPhotoButton;
    private Button takePhotoButton;
    private View imgView1;
    private Button loadEditButton;
    private Button addSaveButton;

    private static final int REQUEST_TAKE_PICTURE = 1;
    private Uri imageUri;
    private static final int REQUEST_CODE = 1;

    // PICK_PHOTO_CODE is a constant integer
    public final static int PICK_PHOTO_CODE = 1046;

    public int type=1;
    public boolean addSaveType=false;
    public boolean loadAddType = false;

    private static final String AUTHORITY=
            BuildConfig.APPLICATION_ID+".provider";

    public static final String NOTE_URI = "note_uri";


    private File output=null;

    private LinearLayout linearLayout;

  //  private Uri noteUri; // Uri of selected contact

    private Uri photoUri;// for loading notes as contactUri
    private FrameLayout frameLayout;

    EditorFragment editorFragment;
    LoaderFragment loaderFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);


        frameLayout = (FrameLayout) findViewById(R.id.drawingViewFrame);


        addSaveButton = (Button)findViewById(R.id.addSaveButton);
        addSaveButton.setEnabled(false);
        addSaveButton.setText("Edit");

        loadEditButton = (Button)findViewById(R.id.loadEditButton) ;
        loadEditButton.setEnabled(false);
        loadEditButton.setText("Load");

        isStoragePermissionGranted();

        openPhotoButton = (Button)findViewById(R.id.openImageButton);
        openPhotoButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                type = 0;
                addSaveButton.setEnabled(true);
                loadEditButton.setEnabled(true);

                //Ask for Saving change's on previous image
                frameLayout.removeAllViews();
                editorFragment=null;
                loaderFragment=null;

                if(addSaveButton.getText().toString().equals("Save")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                    builder.setMessage("Do you want to Save Changes ?")
                            .setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();
                }
                else
                    onPickPhoto(v);
            }
        });

        takePhotoButton = (Button)findViewById(R.id.takeImageButton);
        takePhotoButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                type = 1;
               // takePhoto(v);
            }
        });

        addSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(editorFragment == null)
                {
                    frameLayout.removeAllViews();
                    loaderFragment = null;

                    addSaveButton.setText("Add");

                    Log.d(TAG , " editorFragment Created : " + editorFragment);

                    editorFragment = new EditorFragment();

                    Bundle arguments = new Bundle();
                    arguments.putParcelable(NOTE_URI, photoUri);
                    editorFragment.setArguments(arguments);

                    //"Add" Note Operation
                    FragmentTransaction transaction =
                            getSupportFragmentManager().beginTransaction();
                    transaction.add(R.id.drawingViewFrame, editorFragment);
                    transaction.addToBackStack(null);
                    transaction.commit(); // causes DetailFragment to display

//                    editorFragment.getDrawingView().setEnabled(false);
                }

                else {
                    Log.d(TAG , " editorFragment hastesh : " + addSaveType );

                    if (addSaveType) {
                        addSaveButton.setText("Add");
                        addSaveType = false;

                        editorFragment.saveNote();
                        editorFragment.getDrawingView().setEnabled(false);

                        //setEnablesh false nemishe
                    }
                    else {
                        editorFragment.getDrawingView().setEnabled(true);
                        addSaveButton.setText("Save");
                        addSaveType = true;
                    }
                }
            }
        });

        loadEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (loadAddType) {
                    //open editor with specific photo Uri
                } else {
                    loadEditButton.setText("Edit");

                    if (loaderFragment == null) {

                        frameLayout.removeAllViews();

                        Log.d(TAG, " loaderFragment Created : " + loaderFragment);

                        loaderFragment = new LoaderFragment();

                        Bundle arguments = new Bundle();
                        arguments.putParcelable(NOTE_URI, photoUri);
                        loaderFragment.setArguments(arguments);

                        //"Add" Note Operation
                        FragmentTransaction transaction =
                                getSupportFragmentManager().beginTransaction();
                        transaction.add(R.id.drawingViewFrame, loaderFragment);
                        transaction.addToBackStack(null);
                        transaction.commit(); // causes DetailFragment to display

                    }
                    else {
                        Log.d(TAG, " loaderFragment hastesh : " + loadAddType);

                        editorFragment = null;
                    }
                }
            }
        });

    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            switch (i)
            {
                case DialogInterface.BUTTON_POSITIVE:

                    break;
                case DialogInterface.BUTTON_NEGATIVE:

                    break;
            }
        }
    };

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }

    public void takePhoto(View view) {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photo = new File(Environment.getExternalStorageDirectory(),  "Pic22.jpg");
        Uri outputUri= FileProvider.getUriForFile(this, AUTHORITY, output);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                outputUri);
        imageUri = Uri.fromFile(photo);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PICTURE);
        }
    }

    // Trigger gallery selection for a photo
    public void onPickPhoto(View view) {
        // Create intent for picking a photo from the gallery
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Bring up gallery to select a photo
            startActivityForResult(intent, PICK_PHOTO_CODE);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(type == 0) {
            if (data != null) {
                photoUri = data.getData();
                Log.d(TAG , " PhotoUri from pickPhoto , Uri : " + photoUri);
                Log.d(TAG , " PhotoUri from pickPhoto , LastSeg : " + photoUri.getLastPathSegment());

                // Do something with the photo based on Uri
                Bitmap selectedImage = null;
                try {
                    selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // Load the selected image into a preview
                ImageView ivPreview = (ImageView) findViewById(R.id.imageView);
                ivPreview.setImageBitmap(selectedImage);
            }
        }
        else {
            switch (requestCode) {
                case REQUEST_TAKE_PICTURE:
                    if (resultCode == Activity.RESULT_OK) {
                        Uri selectedImage = imageUri;
                        getContentResolver().notifyChange(selectedImage, null);
                        ImageView imageView = (ImageView) findViewById(R.id.imageView);
                        ContentResolver cr = getContentResolver();
                        Bitmap bitmap;
                        try {
                            bitmap = android.provider.MediaStore.Images.Media
                                    .getBitmap(cr, selectedImage);

                            imageView.setImageBitmap(bitmap);
                            Toast.makeText(this, selectedImage.toString(),
                                    Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT)
                                    .show();
                            Log.e("Camera", e.toString());
                        }
                    }
            }

        }
    }


}