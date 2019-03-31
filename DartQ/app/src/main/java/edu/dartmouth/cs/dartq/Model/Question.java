package edu.dartmouth.cs.dartq.Model;
//import android.util.Log;

public class Question {
    private int mTextResId;
    private boolean mAnswerTrue;
    private boolean ischeat;
//    private static final String TAG = "Question.lifecycle";

    public Question(int textResId, boolean answerTrue, boolean is_cheat) {
        mTextResId = textResId;
        mAnswerTrue = answerTrue;
        ischeat = is_cheat;
//        Log.d(TAG, "setfalse!!!!!!!!!!!!");
    }

    public int getTextResId() {
        return mTextResId;
    }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    public void setcheat(boolean is_cheat)
    {
        ischeat = is_cheat;
//        Log.d(TAG, "set?????!!!!!!!!!!!!" + String.valueOf(is_cheat));
    }

    public boolean getcheat()
    {
        return ischeat;
    }

}
