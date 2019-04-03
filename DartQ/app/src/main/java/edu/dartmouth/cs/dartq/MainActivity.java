package edu.dartmouth.cs.dartq;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import edu.dartmouth.cs.dartq.Model.Question;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity.lifecycle";
    private static final String KEY_INDEX = "current_index";
    private static final int REQUEST_CODE_CHEAT = 0;
    private static final String KEY_ISCHEAT = "is_cheat";
    private boolean mIsCheater;
    private TextView mQuestionTextView;
    private int mCurrentIndex;

    Button mTrueButton;
    Button mFalseButton;
    Button mNextButton;
    Button mPrevButton;
    Button mCheatButton;

    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.is_there_hope, true, false),
            new Question(R.string.brexit, false, false),
            new Question(R.string.ivy, false, false),
            new Question(R.string.campbell, true, false),
            new Question(R.string.soccer, true, false),
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate()");

        mTrueButton = findViewById(R.id.true_button);
        mFalseButton = findViewById(R.id.false_button);
        mNextButton = findViewById(R.id.next_button);
        mPrevButton = findViewById(R.id.previous_button);
        mCheatButton = findViewById(R.id.cheat_button);
        mQuestionTextView = findViewById(R.id.question);

//        Boolean ttmp = getIntent().getBooleanExtra("answer_is_shown", false);
//        Log.d(TAG, "here" + Boolean.toString(ttmp));



        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mIsCheater = savedInstanceState.getBoolean(KEY_ISCHEAT, false);
        } else {
            mCurrentIndex = 0;
            mIsCheater = false;
        }

        mQuestionTextView.setText(mQuestionBank[mCurrentIndex].getTextResId());

        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(true);
            }
        });

        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(false);
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIsCheater = false;
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                mQuestionTextView.setText(mQuestionBank[mCurrentIndex].getTextResId());

            }
        });

        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mIsCheater = false;

                if (mCurrentIndex == 0) {
                    mCurrentIndex = mQuestionBank.length - 1;
                } else {
                    mCurrentIndex = mCurrentIndex - 1;
                }
                mQuestionTextView.setText(mQuestionBank[mCurrentIndex].getTextResId());

            }
        });

        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // start the CheatActivity
                //Intent intent = new Intent(MainActivity.this, CheatActivity.class);
                Intent intent = CheatActivity.newIntent(MainActivity.this, mQuestionBank[mCurrentIndex].isAnswerTrue());
                //startActivity(intent);
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
            }
        });


    }

    // Lifecycle call back methods

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart()");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_INDEX, mCurrentIndex);
        outState.putBoolean(KEY_ISCHEAT, mIsCheater);
        //outState.putA
        Log.d(TAG, "onSaveInstanceState()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop()");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");

    }


    // some helpers

    private void checkAnswer(boolean answer) {
        boolean answerTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        boolean isCheat = mQuestionBank[mCurrentIndex].getcheat();

        Log.d(TAG, "here" + Boolean.toString(isCheat));
        Log.d(TAG, "bINDEX!!!"+ String.valueOf(mCurrentIndex) );

        if (mIsCheater || isCheat) {

            Toast.makeText(MainActivity.this,
                    "Cheating is Wrong!",
                    Toast.LENGTH_LONG).show();

        } else {

            if (answer == answerTrue) {
                Toast.makeText(MainActivity.this,
                        "Correct",
                        Toast.LENGTH_LONG).show();

            } else {

                Toast.makeText(MainActivity.this,
                        "Try again",
                        Toast.LENGTH_LONG).show();
            }
        }

    }

    // This callback runs when you click the back button on the CheatActivity

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        String tmp = data.getStringExtra("answer_is_shown");
//        Log.d(TAG, tmp);

        if ((resultCode == RESULT_OK) && (requestCode == REQUEST_CODE_CHEAT)) {
            if (data != null) {
                mIsCheater = CheatActivity.wasAnswerShown(data);

//                mCurrentIndex = data.getIntExtra(KEY_INDEX, 0);
                mQuestionBank[mCurrentIndex].setcheat(mIsCheater);
            }
        }




//        Log.d(TAG, "back!!!"+ String.valueOf(resultCode) );
    }

}
