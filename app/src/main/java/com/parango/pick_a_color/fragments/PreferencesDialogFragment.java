package com.parango.pick_a_color.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;


public class PreferencesDialogFragment extends DialogFragment {


    public interface OnPreferencesSelectedListener {
        public void updatePreferences(int i);
    }

    private OnPreferencesSelectedListener mCallback;
    private String[] mPreferences;
    private int mItem;

    public PreferencesDialogFragment(){
        mPreferences = new String[]{"None Preferences Set"};
        mItem = -1;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        if(mPreferences == null){
            throw new NullPointerException( "Listener for PreferencesDialogFragment must be set before show" );
        }

        // Creating and Building the Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Select sorting order");
        builder.setSingleChoiceItems(mPreferences, mItem, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                mItem = item;

            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mCallback.updatePreferences(mItem);
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });


        return builder.create();
    }

    public void setOnPreferencesSelectedListener(OnPreferencesSelectedListener mCallback) {
        this.mCallback = mCallback;
    }

    public void setPreferences(String[] mPreferences) {
        this.mPreferences = mPreferences;
    }

    public void setSelectedPreference( int item ){
        this.mItem = item;
    }
}
