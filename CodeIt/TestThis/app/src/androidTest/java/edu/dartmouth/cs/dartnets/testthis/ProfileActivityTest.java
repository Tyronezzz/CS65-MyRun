package edu.dartmouth.cs.dartnets.testthis;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.io.ByteArrayOutputStream;

/**
 *
 * Individual tests are defined as any method beginning with 'test'.
 *
 * ActivityInstrumentationTestCase2 allows these tests to run alongside a running
 * copy of the application under inspection. Calling getActivity() will return a
 * handle to this activity (launching it if needed).
 *
 * Instrumented test, which will execute on an Android device.
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ProfileActivityTest extends ActivityInstrumentationTestCase2<ProfileActivity> {

    private static final String TEST_PHONE_VALUE = "6177777777";
    private static final String TEST_NAME_VALUE = "Test name";
    private static final String TEST_EMAIL_VALUE = "TEST@TEST.com";
    private static final int TEST_GENDER_VALUE = 1;
    private static final String TEST_CLASS_VALUE = "20";
    private static final String TEST_MAJOR_VALUE = "CS";


    public ProfileActivityTest() {
        super(ProfileActivity.class);
    }

    /**
     * Test to make sure the image is persisted after screen rotation.
     *
     * Launches the main activity, sets a test bitmap, rotates the screen.
     * Checks to make sure that the bitmap value matches what we set it to.
     */
    public void testImagePersistedAfterRotate() throws InterruptedException {
        // Define a test bitmap
        final Bitmap TEST_BITMAP = BitmapFactory.decodeResource(getActivity().getResources(),
                R.drawable.blue_pushpin);

        // Convert bitmap to byte array
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        TEST_BITMAP.compress(Bitmap.CompressFormat.PNG, 100, bos);
        final byte[] TEST_BITMAP_VALUE = bos.toByteArray();

        final ImageView mImageView = getActivity().findViewById(R.id.imageProfile);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Attempts to manipulate the UI must be performed on a UI thread.
                // Calling this outside runOnUiThread() will cause an exception.
                //
                // You could also use @UiThreadTest, but activity lifecycle methods
                // cannot be called if this annotation is used.
                //set the test bitmap to the image view
                mImageView.setImageBitmap(TEST_BITMAP);
            }
        });

        // Suspends the current thread for 1 second. This is no necessary.
        // But you can see the change on your phone.
        Thread.sleep(2000);

        // Information about a particular kind of Intent that is being monitored.
        // It is required to open your phone screen, otherwise the test will be hanging.
        Instrumentation.ActivityMonitor monitor =
                new Instrumentation.ActivityMonitor(ProfileActivity.class.getName(), null, false);
        getInstrumentation().addMonitor(monitor);
        // Rotate the screen
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getInstrumentation().waitForIdleSync();
        // Updates current activity
        Activity activity = getInstrumentation().waitForMonitor(monitor);

        // Suspends the current thread for 1 second. This is no necessary.
        // But you can see the change on your phone.
        Thread.sleep(2000);

        final ImageView mImageView2 = activity.findViewById(R.id.imageProfile);
        // Get the current bitmap from image view
        Bitmap currentBitMap = ((BitmapDrawable) mImageView2.getDrawable()).getBitmap();

        // Convert bitmap to byte array
        bos = new ByteArrayOutputStream();
        currentBitMap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] currentBitmapValue = bos.toByteArray();

        // Check if these two bitmaps have the same byte values.
        // If the program executes correctly, they should be the same
        assertTrue(java.util.Arrays.equals(TEST_BITMAP_VALUE, currentBitmapValue));
    }

    /**
     * Test to make sure that value of name is persisted across activity restarts.
     *
     * Launches the main activity, sets a name value, clicks the save button, closes the activity,
     * then relaunches that activity. Checks to make sure that the name value match what we
     * set it to.
     */
    public void testNameValuePersistedBetweenLaunches(){
        // implement your test based on the function header

        Activity curActivity = getActivity();    // get current activity

        final EditText mName = curActivity.findViewById(R.id.editName);
        final Button savebtn = curActivity.findViewById(R.id.btnSave);

        curActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                mName.requestFocus();
                mName.setText(TEST_NAME_VALUE);      // set the value
                savebtn.performClick();            // perform click save btn
            }
        });

        curActivity.finish();           // finish current activity
        setActivity(null);

        curActivity = this.getActivity();       // restart
        EditText mName2 = curActivity.findViewById(R.id.editName);
        String curname = mName2.getText().toString();      // get the current value
        assertEquals(TEST_NAME_VALUE, curname);      // compare
    }

    /**
     * Test to make sure that value of email is persisted across activity restarts.
     *
     * Launches the main activity, sets a email value, clicks the save button, closes the activity,
     * then relaunches that activity. Checks to make sure that the email value match what we
     * set it to.
     */
    public void testEmailValuePersistedBetweenLaunches() {
        // implement your test based on the function header

        Activity curActivity = getActivity();    // get current activity

        final EditText mEmail = curActivity.findViewById(R.id.editEmail);
        final Button savebtn = curActivity.findViewById(R.id.btnSave);

        curActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                mEmail.requestFocus();
                mEmail.setText(TEST_EMAIL_VALUE);      // set the value
                savebtn.performClick();            // perform click save btn
            }
        });

        curActivity.finish();           // finish current activity
        setActivity(null);

        curActivity = this.getActivity();       // restart
        EditText mEmail2 = curActivity.findViewById(R.id.editEmail);
        String curEmail = mEmail2.getText().toString();      // get the current value
        assertEquals(TEST_EMAIL_VALUE, curEmail);      // compare

    }

    /**
     * Test to make sure that value of phone is persisted across activity restarts.
     *
     * Launches the main activity, sets a phone value, clicks the save button, closes the activity,
     * then relaunches that activity. Checks to make sure that the phone value match what we
     * set it to.
     */
    public void testPhoneValuePersistedBetweenLaunches() {
        // implement your test based on the function header

        Activity curActivity = getActivity();    // get current activity

        final EditText mphone = curActivity.findViewById(R.id.editPhone);
        final Button savebtn = curActivity.findViewById(R.id.btnSave);

        curActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                mphone.requestFocus();
                mphone.setText(TEST_PHONE_VALUE);      // set the value
                savebtn.performClick();            // perform click save btn
            }
        });

        curActivity.finish();           // finish current activity
        setActivity(null);

        curActivity = this.getActivity();       // restart
        EditText mphone2 = curActivity.findViewById(R.id.editPhone);
        String curphone = mphone2.getText().toString();      // get the current value
        assertEquals(TEST_PHONE_VALUE, curphone);      // compare

    }

    /**
     * Test to make sure that value of gender is persisted across activity restarts.
     *
     * Launches the main activity, sets a gender value, clicks the save button, closes the activity,
     * then relaunches that activity. Checks to make sure that the gender value match what we
     * set it to.
     */
    public void testGenderValuePersistedBetweenLaunches() {
        // implement your test based on the function header
        Activity curActivity = getActivity();    // get current activity

        final RadioGroup mgender = curActivity.findViewById(R.id.radioGender);
        final Button savebtn = curActivity.findViewById(R.id.btnSave);

        curActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                mgender.requestFocus();
                mgender.check(mgender.getChildAt(TEST_GENDER_VALUE).getId());
                savebtn.performClick();            // perform click save btn
            }
        });

        curActivity.finish();           // finish current activity
        setActivity(null);

        curActivity = this.getActivity();       // restart
        RadioGroup mgender2 = curActivity.findViewById(R.id.radioGender);

        int curBtn = mgender2.getCheckedRadioButtonId();      // get the current value
        Button radioButton = mgender2.findViewById(curBtn);
        int idx = mgender2.indexOfChild(radioButton);

        assertEquals(TEST_GENDER_VALUE, idx);      // compare

    }

    /**
     * Test to make sure that value of class is persisted across activity restarts.
     *
     * Launches the main activity, sets a class value, clicks the save button, closes the activity,
     * then relaunches that activity. Checks to make sure that the class value match what we
     * set it to.
     */
    public void testClassValuePersistedBetweenLaunches() {
        // implement your test based on the function header
        Activity curActivity = getActivity();    // get current activity

        final EditText mclass = curActivity.findViewById(R.id.editClass);
        final Button savebtn = curActivity.findViewById(R.id.btnSave);

        curActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                mclass.requestFocus();
                mclass.setText(TEST_CLASS_VALUE);      // set the value
                savebtn.performClick();            // perform click save btn
            }
        });

        curActivity.finish();           // finish current activity
        setActivity(null);

        curActivity = this.getActivity();       // restart
        EditText mclass2 = curActivity.findViewById(R.id.editClass);
        String curclass = mclass2.getText().toString();      // get the current value
        assertEquals(TEST_CLASS_VALUE, curclass);      // compare
        
        
    }

    /**
     * Test to make sure that value of major is persisted across activity restarts.
     *
     * Launches the main activity, sets a major value, clicks the save button, closes the activity,
     * then relaunches that activity. Checks to make sure that the major value match what we
     * set it to.
     */
    public void testMajorValuePersistedBetweenLaunches() {
        // implement your test based on the function header
        Activity curActivity = getActivity();    // get current activity

        final EditText mmajor = curActivity.findViewById(R.id.editMajor);
        final Button savebtn = curActivity.findViewById(R.id.btnSave);

        curActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                mmajor.requestFocus();
                mmajor.setText(TEST_MAJOR_VALUE);      // set the value
                savebtn.performClick();            // perform click save btn
            }
        });

        curActivity.finish();           // finish current activity
        setActivity(null);

        curActivity = this.getActivity();       // restart
        EditText mmajor2 = curActivity.findViewById(R.id.editMajor);
        String curMajor = mmajor2.getText().toString();      // get the current value
        assertEquals(TEST_MAJOR_VALUE, curMajor);      // compare

    }

    /**
     * Test to make sure that image is persisted across activity restarts.
     *
     * Launches the main activity, sets an image, clicks the save button, closes the activity,
     * then relaunches that activity. Checks to make sure that the image matches what we
     * set it to.
     */
    public void testImagePersistedBetweenLaunches() throws InterruptedException {
        // implement your test based on the function header

        Activity curActivity = getActivity();    // get current activity

        final Bitmap TEST_BITMAP = BitmapFactory.decodeResource(getActivity().getResources(),
                R.drawable.blue_pushpin);
        final Button savebtn = curActivity.findViewById(R.id.btnSave);


        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        TEST_BITMAP.compress(Bitmap.CompressFormat.PNG, 100, bos);
        final byte[] TEST_BITMAP_VALUE = bos.toByteArray();          // Convert bitmap to byte array

        final ImageView mImageView = getActivity().findViewById(R.id.imageProfile);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mImageView.setImageBitmap(TEST_BITMAP);     //set the test bitmap to the image view
                savebtn.performClick();
            }
        });

        Thread.sleep(2000);

        curActivity.finish();           // finish current activity
        setActivity(null);
        curActivity = this.getActivity();       // restart

        final ImageView mImageView2 = curActivity.findViewById(R.id.imageProfile);
        Bitmap currentBitMap = ((BitmapDrawable) mImageView2.getDrawable()).getBitmap();   // Get the current bitmap from image view


        // Convert bitmap to byte array
        bos = new ByteArrayOutputStream();
        currentBitMap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] currentBitmapValue = bos.toByteArray();

        assertTrue(java.util.Arrays.equals(TEST_BITMAP_VALUE, currentBitmapValue));  // compare
    }


}
