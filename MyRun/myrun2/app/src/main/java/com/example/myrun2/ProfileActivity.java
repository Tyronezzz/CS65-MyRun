package com.example.myrun2;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import androidx.exifinterface.media.ExifInterface;

import static java.lang.System.currentTimeMillis;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "Profile.lifecyc";

    public static final String IS_TAKEN_CAMERA_KEY = "is_taken_camera";
    public static final String IS_FIRST = "is_FIRST";
    public static final String URI_INSTANCE_STATE_KEY = "saved_uri";

    protected AutoCompleteTextView mNameView;
    protected RadioGroup mGender;
    protected AutoCompleteTextView mEmailView;
    protected EditText mPasswordView;
    protected AutoCompleteTextView mPhone;
    protected AutoCompleteTextView mMajor;
    protected AutoCompleteTextView mClass;

    protected ImageView mImageView;
    protected Uri mImageCaptureUri;
    protected boolean isTakenFromCamera;
    protected Bitmap rotatedBitmap;

    public static final int REQUEST_CODE_TAKE_FROM_CAMERA = 2;
    public static final int REQUEST_CODE_GALLERY = 3;
    public static final int INITIAL_REQUEST= 0;
    public static final int CHECK_CAMERA_REQUEST = 1;
    //    public static final int CHECK_STORAGE_REQUEST = 2;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    protected boolean isFirst = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        myToolbar.setTitle("Profile");
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);   // set up the tool bar


        mImageView = findViewById(R.id.imageView);
        mNameView = findViewById(R.id.register_name);
        mEmailView = findViewById(R.id.register_email);
        mPasswordView = findViewById(R.id.register_password);
        mPhone = findViewById(R.id.register_phone);
        mMajor = findViewById(R.id.register_major);
        mClass = findViewById(R.id.register_class);
        mGender = findViewById(R.id.register_gender);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());


        if (savedInstanceState != null) {
            mImageCaptureUri = savedInstanceState.getParcelable(URI_INSTANCE_STATE_KEY);      // get the picture
            isFirst = savedInstanceState.getBoolean(IS_FIRST);
            isTakenFromCamera = savedInstanceState.getBoolean(IS_TAKEN_CAMERA_KEY);
        }

        if(isFirst)            // ask for permissions for the first time
        {
            checkPermissions();
            isFirst = false;
        }

        loadSnap();

        // load snap????


        sharedPreferences = getSharedPreferences("profile", Context.MODE_PRIVATE);
        String old_name= sharedPreferences.getString("key_name", "");
        String old_email= sharedPreferences.getString("key_email", "");
        String old_password = sharedPreferences.getString("key_password", "");
        String old_phone = sharedPreferences.getString("key_phone", "");
        String old_major = sharedPreferences.getString("key_major", "");
        int dclass = sharedPreferences.getInt("key_class", 0);
        int gender = sharedPreferences.getInt("key_gender", 0);


        mNameView.setText(old_name);
        mEmailView.setText(old_email);
        mPasswordView.setText(old_password);
        mPhone.setText(old_phone);
        mMajor.setText(old_major);

        if(dclass==-1)
            mClass.setText("");

        else
            mClass.setText(String.valueOf(dclass));

        mGender.check(mGender.getChildAt(gender).getId());
        mEmailView.setFocusable(false);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.register_menu, menu);     // Inflate the menu
        MenuItem item = menu.findItem(R.id.action_register);
        item.setTitle("SAVE");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {    // operations on the toolbar
        switch (item.getItemId()) {

            case R.id.action_register:
                Log.d(TAG, "save...");
                checkValid();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void checkValid()
    {
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

        int Dclass = -1;
        String tmp = mClass.getText().toString();
        if(!tmp.isEmpty())
            Dclass= Integer.valueOf(tmp);

        ArrayList<String> putstr = new ArrayList<String>();
        putstr.add(name);
        putstr.add(email);
        putstr.add(password);
        putstr.add(phone);
        putstr.add(major);

        ArrayList<Integer> putint = new ArrayList<Integer>();
        putint.add(Dclass);
        putint.add(gender);

        attemptSave(cancel, focusView, putstr, putint);
    }


    // attempt to register
    protected void attemptSave(boolean cancel, View focusView, ArrayList<String> putstr, ArrayList<Integer> putint) {

        if (cancel) {
            focusView.requestFocus();
        }
        else {


            String pwd_tmp = sharedPreferences.getString("key_password", "");

            sharedPreferences = getSharedPreferences("profile", Context.MODE_PRIVATE);       //store the profile in the sharedpreference
            editor = sharedPreferences.edit();
//            editor.clear();
            editor.putString("key_name", putstr.get(0));
            editor.putString("key_email", putstr.get(1));
            editor.putString("key_password", putstr.get(2));
            editor.putString("key_phone", putstr.get(3));
            editor.putString("key_major", putstr.get(4));
            editor.putInt("key_class", putint.get(0));
            editor.putInt("key_gender", putint.get(1));
            editor.apply();
            saveSnap();
            Toast.makeText(this,"Successfully Saved", Toast.LENGTH_LONG).show();

            if(!putstr.get(2).equals(pwd_tmp))
            {
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }

            else
                finish();

        }
    }

    public boolean isEmailValid(String email) {
        return email.contains("@");
    }

    public boolean isPasswordValid(String password) {
        return password.length() >= 6;
    }


    // after click the change button, call this function
    public void onChangePhotoClicked(View v) {
        int id = MyRunsDialogFragment.DIALOG_ID_PHOTO_PICKER_PROFILE;
        MyRunsDialogFragment fragment = MyRunsDialogFragment.newInstance(id);
        fragment.show(getSupportFragmentManager(), getString(R.string.dialog_fragment_tag_photo_picker));
    }

    // select the item in the dialog menu
    public void onPhotoPickerItemSelected(int Item){
        switch (Item)
        {
            case MyRunsDialogFragment.ID_PHOTO_PICKER_FROM_CAMERA:

                if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED )      // check and request permissions again
                {
                    startCamera();
                }
                else
                {
                    checkCameraPermission();
                }
                break;

            case MyRunsDialogFragment.ID_GALLERY_PICKER_FROM_CAMERA:
                Log.d(TAG, "gallery");
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, REQUEST_CODE_GALLERY);//one can be replaced with any action code



                break;

            default:
                break;
        }
    }

    public void checkCameraPermission() {        // check permissions for camera
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CHECK_CAMERA_REQUEST);
        }
    }

//    private void checkStoragePermissions() {       // check permissions for storage
//        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CHECK_STORAGE_REQUEST);
//        }
//    }

    public void checkPermissions() {       // check permissions for storage and camera
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, INITIAL_REQUEST);
        }
    }

    public void startCamera()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        ContentValues values = new ContentValues(1);
//        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
//        mImageCaptureUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        File file = new File(getExternalCacheDir(), "photo.jpg");
        mImageCaptureUri = Uri.fromFile(file);         // define a tmp uri, save the picture in this uri

        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
        intent.putExtra("return-data", true);
        Log.d(TAG, "Uri is "+mImageCaptureUri);

        try {
            startActivityForResult(intent, REQUEST_CODE_TAKE_FROM_CAMERA);      // Start a camera capturing activity
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
        isTakenFromCamera = true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == INITIAL_REQUEST) {
            Log.d(TAG, "First time request");
        } else if (requestCode == CHECK_CAMERA_REQUEST) {
            Log.d(TAG, "Second time request");
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)   // camera granted
            {
                startCamera();
            }
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the image capture uri before the activity goes into background

        Log.d(TAG, "OnSaveInstance called!");
        outState.putParcelable(URI_INSTANCE_STATE_KEY, mImageCaptureUri);
        outState.putBoolean(IS_FIRST, isFirst);
        outState.putBoolean(IS_TAKEN_CAMERA_KEY, isTakenFromCamera);
    }


    // Handle data after activity returns.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
        {
            Log.d(TAG, "GG");
            return;
        }

        switch (requestCode) {
            case REQUEST_CODE_TAKE_FROM_CAMERA:    // after taking the pic, begin crop
            case REQUEST_CODE_GALLERY:
                beginCrop(mImageCaptureUri);       // Send image taken from camera for cropping
                break;

            case Crop.REQUEST_CROP:     //
                handleCrop(resultCode, data);      // Update image view after image crop
//                if (isTakenFromCamera) {
//                    File f = new File(mImageCaptureUri.getPath());
//                    if (f.exists())
//                        f.delete();
//                    Log.d(TAG, "chose OK");
//                }
                break;
        }
    }

    public void beginCrop(Uri source) {

        try{

//            Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
            String tmp = getExternalCacheDir() +"/"+ String.valueOf(currentTimeMillis()) + "photo.jpg";
            Uri destination = Uri.fromFile(new File(tmp));   //getCacheDir(), "cropped"
            mImageCaptureUri = destination;
            Crop.of(source, destination).asSquare().start(this);
        }catch (Exception e)
        {
            Log.d(TAG, "Error here");
        }
    }

    public void handleCrop(int resultCode, Intent result) {

        if (resultCode == RESULT_OK) {
            Uri uri = Crop.getOutput(result);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                mImageView.setImageBitmap(imageOreintationValidator(bitmap, uri.getPath()));
                mImageCaptureUri = uri;
            }catch (Exception e){
                Log.d("Error", "error");
            }

        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    public void saveSnap() {

        if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)    // check permission to determine whether save in phone
        {
            mImageView.buildDrawingCache();
            Bitmap bmap = mImageView.getDrawingCache();
            try {
                FileOutputStream fos = openFileOutput(getString(R.string.profile_photo_file_name), MODE_PRIVATE);
                bmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
//                String path = this.getFilesDir().getAbsolutePath();
//                Log.d(TAG, "path of profile_photo.png is" + path);
                fos.flush();
                fos.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }


    public void loadSnap() {
        // Load profile photo from internal storage
        if(!isTakenFromCamera)
            mImageView.setImageResource(R.drawable.ic_launcher);

        else
        {
            try {
                Bitmap bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mImageCaptureUri);
                mImageView.setImageBitmap(bm);
            }
            catch (IOException e) {
                Log.d(TAG, "loadSnap error!" + e);
                mImageView.setImageResource(R.drawable.ic_launcher);
            }
        }
    }

    // code to handle image orientation issue -- sometimes the orientation is not right on the imageview
    // https://github.com/jdamcd/android-crop/issues/258
    public Bitmap imageOreintationValidator(Bitmap bitmap, String path) {
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


    public Bitmap rotateImage(Bitmap source, float angle) {     // rotate the img
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }



}
