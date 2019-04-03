package edu.dartmouth.cs.dartq;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    private static final String EXTRA_ANSWER_IS_TRUE = "answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN = "answer_is_shown";
    private static final String TAG = "CHEATACTIVITY.lifecycle";
    private boolean  mAnswerIsTrue;
    private static boolean  mAnswerIsShown = false;

    Button mShowAnswer;
    TextView mShowAnswerTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        mShowAnswer = findViewById(R.id.show_answer_button);
        mShowAnswerTextView = findViewById(R.id.show_answer_text_view);
        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

        if(savedInstanceState != null)               // Show the true/false after rotation
        {
            if(mAnswerIsShown)
            {
                if(mAnswerIsTrue)
                {
                    mShowAnswerTextView.setText(R.string.answer_true);
                }
                else
                {
                    mShowAnswerTextView.setText(R.string.answer_false);
                }

                answerShown();     // after rotation, set the cheat value
            }

        }

        mShowAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAnswerIsTrue) {
                    mShowAnswerTextView.setText(R.string.answer_true);
                } else {
                    mShowAnswerTextView.setText(R.string.answer_false);
                }
                answerShown();
                mAnswerIsShown = true;

            }
        });
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putBoolean(EXTRA_ANSWER_SHOWN, mAnswerIsShown);
        super.onSaveInstanceState(outState);
        Log.d(TAG, "SAVED!!!");
    }
    
    private void answerShown() {
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, true);
        setResult(RESULT_OK, data);
    }



    public static Intent newIntent(Context context, boolean answerTrue) {
        Intent intent = new Intent(context, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerTrue);
        return intent;
    }

    public static boolean wasAnswerShown(Intent data) {
        return data.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }

}
