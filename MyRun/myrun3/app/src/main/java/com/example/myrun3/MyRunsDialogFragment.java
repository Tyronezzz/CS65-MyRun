/*
 * @author  Tao Hou
 * @version 1.0
 * @since   2019-04-03
 */

package com.example.myrun3;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

public class MyRunsDialogFragment extends DialogFragment {

    public static final int DIALOG_ID_PHOTO_PICKER_REGISTER = 1;       // dialog IDs
//    public static final int DIALOG_ID_PHOTO_PICKER_PROFILE = 2;       // dialog IDs

    public static final int ID_PHOTO_PICKER_FROM_CAMERA = 0;    // For photo picker selection:
    public static final int ID_GALLERY_PICKER_FROM_CAMERA = 1;    // For photo picker selection:
    private static final String DIALOG_ID_KEY = "dialog_id";

    public static MyRunsDialogFragment newInstance(int dialog_id) {
        MyRunsDialogFragment frag = new MyRunsDialogFragment();
        Bundle args = new Bundle();
        args.putInt(DIALOG_ID_KEY, dialog_id);
        frag.setArguments(args);
        return frag;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        assert getArguments() != null;
        int dialog_id = getArguments().getInt(DIALOG_ID_KEY);
        final Activity parent = getActivity();
        AlertDialog.Builder builder;
        DialogInterface.OnClickListener dlistener;

        switch (dialog_id)
        {
            case DIALOG_ID_PHOTO_PICKER_REGISTER:        // take picture from camera in register
                builder = new AlertDialog.Builder(parent);
                builder.setTitle("Profile Picture Picker");
                dlistener = new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int item)
                    {
                        assert parent != null;
                        ((RegisterActivity) parent)
                                .onPhotoPickerItemSelected(item);
                    }
                };

                builder.setItems(R.array.ui_profile_photo_picker_items, dlistener);
                return builder.create();

//            case DIALOG_ID_PHOTO_PICKER_PROFILE:        // take picture from camera in edit profile
//               builder = new AlertDialog.Builder(parent);
//                builder.setTitle("Profile Picture Picker");
//                dlistener = new DialogInterface.OnClickListener(){
//                    public void onClick(DialogInterface dialog, int item)
//                    {
//                        assert parent != null;
//                        ((ProfileActivity) parent)
//                                .onPhotoPickerItemSelected(item);
//                    }
//                };
//
//                builder.setItems(R.array.ui_profile_photo_picker_items, dlistener);
//                return builder.create();


            default:
                return null;
        }
    }

}
