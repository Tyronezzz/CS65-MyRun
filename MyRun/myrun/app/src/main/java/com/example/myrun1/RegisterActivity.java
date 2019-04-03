package com.example.myrun1;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import androidx.exifinterface.media.ExifInterface;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity.lfcycl";
    private static final String IS_TAKEN_CAMERA_KEY = "is_taken_camera";

    private AutoCompleteTextView mNameView;
    private RadioGroup mGender;
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private AutoCompleteTextView mPhone;
    private AutoCompleteTextView mMajor;
    private AutoCompleteTextView mClass;
    //    private LoginActivity.UserLoginTask mAuthTask = null;
//    private Button change_button;
//    private Button mregister_button;
    public static final int REQUEST_CODE_TAKE_FROM_CAMERA = 0;
    private static final String URI_INSTANCE_STATE_KEY = "saved_uri";
    private static final int INITIAL_REQUEST= 0;
    private static final int CHECK_CAMERA_REQUEST = 1;

    private Uri mImageCaptureUri;
    private ImageView mImageView;
    private boolean isTakenFromCamera;
    private Bitmap rotatedBitmap;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private Bitmap mbitmap = null;
    //profile_Shared preferenceManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        checkPermissions();

        mImageView = findViewById(R.id.imageView);
        mNameView = findViewById(R.id.register_name);
        mEmailView = findViewById(R.id.register_email);
        mPasswordView = findViewById(R.id.register_password);
        mPhone = findViewById(R.id.register_phone);
        mMajor = findViewById(R.id.register_major);
        mClass = findViewById(R.id.register_class);
        mGender = findViewById(R.id.register_gender);

        if (savedInstanceState != null) {
            mImageCaptureUri = savedInstanceState.getParcelable(URI_INSTANCE_STATE_KEY);

            try {
                Bitmap bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mImageCaptureUri);
                mbitmap = bm;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        loadSnap();



//        mregister_button.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
//                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
//                    attemptRegister();
//                    return true;
//                }
//                return false;
//            }
//        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.register_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

            case R.id.action_register:
                attemptRegister();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void attemptRegister() {
        // Reset errors.
        mNameView.setError(null);
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the register attempt.
        String name = mNameView.getText().toString();
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String phone = mPhone.getText().toString();
        String major = mMajor.getText().toString();


        int radioButtonID = mGender.getCheckedRadioButtonId();
        View radioButton = mGender.findViewById(radioButtonID);
        int gender = mGender.indexOfChild(radioButton);

        boolean cancel = false;
        View focusView = null;

        // check gender
        if(gender == -1)
        {
            Toast.makeText(this,"Gender is required!", Toast.LENGTH_LONG).show();
            focusView = mGender;
            cancel = true;
        }

        // Check for a valid name
        if (TextUtils.isEmpty(name)) {
            mNameView.setError(getString(R.string.error_field_required));
            focusView = mNameView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }
        else if(!isPasswordValid(password))
        {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        int Dclass = 20;
        String tmp = mClass.getText().toString();
        if(!tmp.isEmpty())
            Dclass= Integer.valueOf(tmp);

        if (cancel) {
            focusView.requestFocus();
        }
        else {
            sharedPreferences = getSharedPreferences("profile", Context.MODE_PRIVATE);
            // photo???
            editor = sharedPreferences.edit();
            editor.clear();
            editor.putString("key_name", name);
            editor.putString("key_email", email);
            editor.putString("key_password", password);
            editor.putString("key_phone", phone);
            editor.putString("key_major", major);
            editor.putInt("key_class", Dclass);
            editor.putInt("key_gender", gender);
            editor.apply();

            saveSnap();
            Toast.makeText(this,"Successfully Registered", Toast.LENGTH_LONG).show();
            finish();
//            NavUtils.navigateUpFromSameTask(this);

        }
    }


    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 6;
    }


    // after click the change button, call this function
    public void onChangePhotoClicked(View v) {
        int id = MyRunsDialogFragment.DIALOG_ID_PHOTO_PICKER;
        MyRunsDialogFragment fragment = MyRunsDialogFragment.newInstance(id);
        fragment.show(getSupportFragmentManager(), getString(R.string.dialog_fragment_tag_photo_picker));
    }

    // select the item in the dialog menu
    public void onPhotoPickerItemSelected(int Item){

//        Intent intent;
        switch (Item)
        {
            case MyRunsDialogFragment.ID_PHOTO_PICKER_FROM_CAMERA:
                // check and request permissions again

                if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED )
                {
                    startCamera();
                }
                else
                {
                    checkCameraPermission();
                }
//                {
//                    intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    ContentValues values = new ContentValues(1);
//                    values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
//                    mImageCaptureUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
//                    intent.putExtra("return-data", true);
//
//                    try {
//                        // Start a camera capturing activity
//                        // REQUEST_CODE_TAKE_FROM_CAMERA is an integer tag you
//                        // defined to identify the activity in onActivityResult()
//                        // when it returns
//                        startActivityForResult(intent, REQUEST_CODE_TAKE_FROM_CAMERA);
//
//                    } catch (ActivityNotFoundException e) {
//                        Log.d(TAG, String.valueOf(e));
//                        e.printStackTrace();
//                    }
//                    isTakenFromCamera = true;
//                }
                break;

            case MyRunsDialogFragment.ID_GALLERY_PICKER_FROM_CAMERA:
                Log.d(TAG, "gallery");

                checkStoragePermissions();
                if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                {
                    //do sth;

                }

                break;

            default:
                break;
        }
    }

    private void checkCameraPermission() {
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CHECK_CAMERA_REQUEST);
        }

        Log.d(TAG, "FK>");
    }

    private void checkStoragePermissions() {       // check permissions for storage and camera
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
    }

    private void checkPermissions() {       // check permissions for storage and camera
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, INITIAL_REQUEST);
        }
    }

    private void startCamera()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE).addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        Log.d(TAG, "grant it");

//                grantUriPermission("com.example.myrun1."), mImageCaptureUri,Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        ContentValues values = new ContentValues(1);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
        mImageCaptureUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
        intent.putExtra("return-data", true);

        try {
            // Start a camera capturing activity
            // REQUEST_CODE_TAKE_FROM_CAMERA is an integer tag you
            // defined to identify the activity in onActivityResult()
            // when it returns
            startActivityForResult(intent, REQUEST_CODE_TAKE_FROM_CAMERA);

        } catch (ActivityNotFoundException e) {
            Log.d(TAG, String.valueOf(e));
            e.printStackTrace();
        }
        isTakenFromCamera = true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        Log.d(TAG, "code "+String.valueOf(requestCode));
        if(requestCode == INITIAL_REQUEST)
        {
            Log.d(TAG, "First time request");
        }

        else if(requestCode == CHECK_CAMERA_REQUEST)
        {
            Log.d(TAG, "second time request");
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)   // camera granted
            {
                startCamera();
            }
        }

        if(permissions.length>1)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "ALL permit");
            }
            else if (grantResults[0] == PackageManager.PERMISSION_DENIED || grantResults[1] == PackageManager.PERMISSION_DENIED)
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)||shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE))
                    {
                        Log.d(TAG, "partail permit");
                    }
                    else{
                        //Never ask again and handle your app without permission.
                        Log.d(TAG, "DK");
                    }
                }
            }
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the image capture uri before the activity goes into background

        Log.d(TAG, "OnSaveInstance called!");
        outState.putParcelable(URI_INSTANCE_STATE_KEY, mImageCaptureUri);
    }


    // Handle data after activity returns.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
            return;

        switch (requestCode) {
            case REQUEST_CODE_TAKE_FROM_CAMERA:
                // Send image taken from camera for cropping
                beginCrop(mImageCaptureUri);
                break;

            case Crop.REQUEST_CROP: //We changed the RequestCode to the one being used by the library.
                // Update image view after image crop
                handleCrop(resultCode, data);

                // Delete temporary image taken by camera after crop.
                if (isTakenFromCamera) {
                    File f = new File(mImageCaptureUri.getPath());
                    if (f.exists())
                        f.delete();

                    //saveSnap();
                    Log.d(TAG, "chose OK");

                }
                break;

        }
    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            Uri uri = Crop.getOutput(result);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                mImageView.setImageBitmap(imageOreintationValidator(bitmap, uri.getPath()));
                mbitmap = bitmap;

            }catch (Exception e){
                Log.d("Error", "error");
            }

        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private void saveSnap() {

        // Save profile image into internal storage.
        mImageView.buildDrawingCache();
        Bitmap bmap = mImageView.getDrawingCache();
        try {
            FileOutputStream fos = openFileOutput(getString(R.string.profile_photo_file_name), MODE_PRIVATE);
            bmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            String path = this.getFilesDir().getAbsolutePath();
            Log.d(TAG, "path of profile_photo.png is" + path);
            fos.flush();
            fos.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }


    private void loadSnap() {
        // Load profile photo from internal storage

        if(mbitmap == null)
            mImageView.setImageResource(R.drawable.ic_launcher);

        else
        {
            try {
                FileInputStream fis = openFileInput(getString(R.string.profile_photo_file_name));
                Bitmap bmap = BitmapFactory.decodeStream(fis);
                bmap = mbitmap;
                mImageView.setImageBitmap(bmap);
                fis.close();
                String path = this.getFilesDir().getAbsolutePath();
                Log.d(TAG, "Load.  path of profile_photo.png is" + path);

            } catch (IOException e) {
                Log.d(TAG, "loadSnap error!"+ e);
                mImageView.setImageResource(R.drawable.ic_launcher);
            }
        }




    }

    // code to handle image orientation issue -- sometimes the orientation is not right on the imageview
    // https://github.com/jdamcd/android-crop/issues/258
    private Bitmap imageOreintationValidator(Bitmap bitmap, String path) {
        ExifInterface ei;
        try {
            ei = new ExifInterface(path);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);
            rotatedBitmap = null;
            switch(orientation) {

                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotatedBitmap = rotateImage(bitmap, 90);

                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotatedBitmap = rotateImage(bitmap, 180);

                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotatedBitmap = rotateImage(bitmap, 270);

                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    rotatedBitmap = bitmap;

                    break;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return rotatedBitmap;
    }

    private Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

}
