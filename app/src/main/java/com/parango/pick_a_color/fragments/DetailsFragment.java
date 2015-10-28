package com.parango.pick_a_color.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.parango.pick_a_color.R;
import com.parango.pick_a_color.content_provider.ColorContentProvider;
import com.parango.pick_a_color.db.ColorTable;
import com.parango.pick_a_color.util.ColorCursorAdapter;


public class DetailsFragment
        extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor>,
                    PreferencesDialogFragment.OnPreferencesSelectedListener{




    public final static String ARG_HUE_START = "hueStart";
    public final static String ARG_HUE_END = "hueEnd";
    public final static String ARG_SATURATION = "saturation";
    public final static String ARG_VALUE = "value";
    public final static String ORDER_PREFERENCES_INDEX = "orderPrefIndex";

    //color values selected
    private float mHueStart;
    private float mHueEnd;
    private float mSaturation;
    private float mValue;
    private int mPrefsIndex;

    // private Cursor cursor;
    private ColorCursorAdapter mAdapter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mPrefsIndex = getActivity().getPreferences(Activity.MODE_PRIVATE).getInt(ORDER_PREFERENCES_INDEX, 1);
        mAdapter = new ColorCursorAdapter( getActivity(), // context
                null, // cursor
                0     // flags
        );
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.color_main, container, false);
    }



    @Override
    public void onStart() {
        super.onStart();

        Bundle args = getArguments();
        if (args != null) {
            // Set article based on argument passed in
            mPrefsIndex = args.getInt(ORDER_PREFERENCES_INDEX, mPrefsIndex);
            mHueStart = args.getFloat(ARG_HUE_START, 0);
            mHueEnd = args.getFloat(ARG_HUE_END, 360);
            mSaturation = args.getFloat(ARG_SATURATION, 1);
            mValue = args.getFloat(ARG_VALUE, 1);

            //Log.d("Args:", " HueStart: " + mHueStart + " HueEnd: " + mHueEnd + " Sat:" + mSaturation + " Val: " + mValue);
        }

        ListView lv = (ListView) getActivity().findViewById(R.id.list);
        lv.setDividerHeight(2);
        fillData( lv );
        setListListener(lv );

        Button configButton = (Button) getActivity().findViewById(R.id.button);
        configButton.setText(R.string.config_sort_order);
        setButtonListener(configButton);

    }

    private void fillData( ListView lv) {
        Bundle bundle = new Bundle();
        bundle.putInt(ORDER_PREFERENCES_INDEX, mPrefsIndex);
        getLoaderManager().restartLoader(0, bundle, this );

        lv.setAdapter(mAdapter);
    }

    private void setListListener(ListView lv ){
        lv.setOnItemClickListener( new ListView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Cursor colorCursor = (Cursor) parent.getItemAtPosition(position );

                String toastText = "Name: " + colorCursor.getString(ColorCursorAdapter.NAME) +" \n"
                        + "Hue: " + colorCursor.getString(ColorCursorAdapter.HUE)+ (char)0x00B0 + " \n"
                        + "Saturation: " + colorCursor.getString(ColorCursorAdapter.SATURATION) + " \n"
                        + "Value: " + colorCursor.getString(ColorCursorAdapter.VALUE);

                Toast.makeText(getActivity(),toastText, Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void setButtonListener( Button button){

        final PreferencesDialogFragment.OnPreferencesSelectedListener listener = this;

        button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PreferencesDialogFragment configDialogFragment = new PreferencesDialogFragment();
                configDialogFragment.setPreferences(ColorCursorAdapter.ORDER_BY);
                configDialogFragment.setOnPreferencesSelectedListener( listener );
                configDialogFragment.setSelectedPreference(mPrefsIndex);
                configDialogFragment.show(getFragmentManager(), "Dialog");
            }
        });
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current article selection in case we need to recreate the fragment
        outState.putInt(ORDER_PREFERENCES_INDEX, mPrefsIndex);
        outState.putFloat(ARG_HUE_START, mHueStart);
        outState.putFloat(ARG_HUE_END, mHueEnd);
        outState.putFloat(ARG_SATURATION, mSaturation);
        outState.putFloat(ARG_VALUE, mValue);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String hueStart = String.valueOf(mHueStart);
        String hueEnd = String.valueOf(mHueEnd);
        String selection;

        if(mHueStart<0){
            hueStart = String.valueOf(360 + mHueStart);
            selection =
                    "("+ColorTable.COLUMN_HUE + ">? OR " + ColorTable.COLUMN_HUE + "<? ) AND " +
                    ColorTable.COLUMN_SATURATION + ">? AND " + ColorTable.COLUMN_SATURATION + "<? AND " +
                    ColorTable.COLUMN_VALUE + ">? AND " + ColorTable.COLUMN_VALUE + "<?";

        } else {
            selection =
                    ColorTable.COLUMN_HUE + ">? AND " + ColorTable.COLUMN_HUE + "<? AND " +
                    ColorTable.COLUMN_SATURATION + ">? AND " + ColorTable.COLUMN_SATURATION + "<? AND " +
                    ColorTable.COLUMN_VALUE + ">? AND " + ColorTable.COLUMN_VALUE + "<?";
        }


        String saturationStart = String.valueOf(mSaturation - (float).15);
        String saturationEnd = String.valueOf(mSaturation + (float).15);

        String valueStart = String.valueOf(mValue - (float).15);
        String valueEnd = String.valueOf(mValue + (float).15);

        //select hue>0 and hue<30 and saturation>x and saturation<y and value>x and value<y

        String[] selectionArgs = {
                hueStart, hueEnd,
                saturationStart, saturationEnd,
                valueStart, valueEnd
                };

        String orderPrefs = null;

        if (args != null){
            int prefIndex = args.getInt(ORDER_PREFERENCES_INDEX, 0);
            orderPrefs = ColorCursorAdapter.ORDER_BY[prefIndex];
        }




        return new CursorLoader( getActivity(), // context
                ColorContentProvider.CONTENT_URI, // content provider uri
                ColorCursorAdapter.PROJECTION, // projection
                selection,  // selection
                selectionArgs,  // selection arguments
                orderPrefs );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // data is not available anymore, delete reference
        mAdapter.swapCursor(null);
    }


    @Override
    public void updatePreferences(int i) {
        Bundle bundle = new Bundle();
        mPrefsIndex = i;
        bundle.putInt(ORDER_PREFERENCES_INDEX, i);

        getLoaderManager().restartLoader(0, bundle, this );
    }


    @Override
    public void onPause(){
        super.onPause();

        SharedPreferences.Editor editor = getActivity().getPreferences(Activity.MODE_PRIVATE).edit();
        editor.putInt(ORDER_PREFERENCES_INDEX, mPrefsIndex);
        editor.apply();
    }
}
