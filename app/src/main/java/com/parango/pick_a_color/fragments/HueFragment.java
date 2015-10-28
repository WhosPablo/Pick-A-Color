package com.parango.pick_a_color.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.parango.pick_a_color.R;
import com.parango.pick_a_color.util.ColorAdapter;
import com.parango.pick_a_color.util.HSVColor;

import java.util.ArrayList;


public class HueFragment
        extends Fragment {

    public final static String ARG_SWATCH_NUM = "hueSwatchNum";
    public final static String ARG_CENTRAL_HUE = "centralHue";

    private int mSwatchNum;
    private int mCentralHue;
    private ListView mColorList;
    private OnHueSelectedListener mCallback;


    public interface OnHueSelectedListener {
        public void onHueSelected(float hueStart, float hueEnd);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);


        mSwatchNum = getActivity().getPreferences(Activity.MODE_PRIVATE).getInt(ARG_SWATCH_NUM, 10);
        mCentralHue = getActivity().getPreferences(Activity.MODE_PRIVATE).getInt(ARG_CENTRAL_HUE,0);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        try {
            mCallback = ( OnHueSelectedListener ) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.color_main, container, false);
        // Inflate the layout for this fragment
        mColorList = (ListView) view.findViewById(R.id.list);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();



        Bundle args = getArguments();
        if (args != null) {
            // Set color_row based on argument passed in
            mSwatchNum = args.getInt(ARG_SWATCH_NUM, mSwatchNum);
            mCentralHue = args.getInt(ARG_CENTRAL_HUE, mCentralHue);
        }


        mColorList.setDividerHeight(2);
        fillData( mSwatchNum, mCentralHue);
        setListListener( mColorList );

        Button configButton = (Button) getActivity().findViewById(R.id.button);
        configButton.setText(R.string.hue_preferences);
        setButtonListener( configButton );

    }

    private void fillData(int swatchNum,
                          int centralHue){
        ArrayList<HSVColor> colorList = new ArrayList<>();
        float swatches = 360/swatchNum;
        float plmin = swatches/2;
        for (int i = 0; i < swatchNum; i++) {
            float start = centralHue + (i*swatches) - plmin;
            float end = centralHue +(i*swatches) + plmin;
            colorList.add(new HSVColor(start, end , 1f , 1f));

        }
        mColorList.setAdapter(new ColorAdapter(getActivity(), colorList));
    }

    private void setListListener(ListView lv ){
        lv.setOnItemClickListener( new ListView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Notify the parent activity of selected item
                HSVColor color = (HSVColor) parent.getItemAtPosition( position );
                float hueStart =  color.getHueStart();
                float hueEnd =  color.getHueEnd();
                mCallback.onHueSelected(hueStart, hueEnd);

            }
        });
    }

    private void setButtonListener( Button button){

        button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                LayoutInflater inflater =
                        (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.hue_config_dialog,
                        (ViewGroup) getActivity().findViewById(R.id.hue_config_dialog));

                // **********
                // Central Hue seekBar
                // **********
                final TextView centralHueProgressView = (TextView) layout.findViewById(R.id.central_hue_progress);
                centralHueProgressView.setText(String.valueOf(mCentralHue));
                final TextView centralHuePreview = (TextView) layout.findViewById(R.id.central_hue_preview);
                centralHuePreview.setBackgroundColor(Color.HSVToColor(new float[]{(float)mCentralHue, 1f, 1f}));

                SeekBar centralHueSeekBar = (SeekBar) layout.findViewById(R.id.central_hue_seekBar);
                centralHueSeekBar.setMax(360);
                centralHueSeekBar.setProgress(mCentralHue);
                centralHueSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                        mCentralHue = progressValue;
                        centralHueProgressView.setText(String.valueOf(mCentralHue));
                        centralHuePreview.setBackgroundColor(Color.HSVToColor(new float[]{(float)mCentralHue, 1f, 1f}));
                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {}
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {}
                });



                // **********
                // Number of swatches seekBar
                // **********
                final TextView swatchProgressView = (TextView) layout.findViewById(R.id.swatch_num_progress);
                swatchProgressView.setText(String.valueOf(mSwatchNum));

                SeekBar swatchSeekBar = (SeekBar) layout.findViewById(R.id.swatch_num_seekBar);
                swatchSeekBar.setMax(35);
                swatchSeekBar.setProgress(mSwatchNum);
                swatchSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                        mSwatchNum = progressValue+1;
                        swatchProgressView.setText(String.valueOf(mSwatchNum));
                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {}
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {}
                });







                // **********
                // finish dialog
                // **********

                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setView(layout);
                dialog.setTitle(R.string.hue_preferences);
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fillData(mSwatchNum, mCentralHue);
                    }
                });
                dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();



            }
        });
    }





    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current article selection in case we need to recreate the fragment
        outState.putInt(ARG_SWATCH_NUM, mSwatchNum);
        outState.putInt(ARG_CENTRAL_HUE, mCentralHue);
    }

    @Override
    public void onPause(){
        super.onPause();

        SharedPreferences.Editor editor = getActivity().getPreferences(Activity.MODE_PRIVATE).edit();
        editor.putInt(ARG_SWATCH_NUM, mSwatchNum);
        editor.putInt(ARG_CENTRAL_HUE, mCentralHue);
        editor.apply();
    }

//    @Override
//    public void onListItemClick(ListView l, View v, int position, long id) {
//        // Notify the parent activity of selected item
//        HSVColor color = (HSVColor) getListAdapter().getItem(position);
//
//        float passStart =  color.getHueStart();
//        float passEnd =  color.getHueEnd();
//        mCallback.onHueSelected(passStart,passEnd);
//
//    }
//
//
//
//    private void updateCurrentList( int swatchNum, int offset ){
//        mColorList = new ArrayList<>();
//        float swatches = 360/swatchNum;
//        float count = 0;
//        for (int i = 0; i < swatchNum; i++) {
//            mColorList.add(new HSVColor(((swatches * count) - offset), ((swatches * ++count) - offset) , 1f , 1f));
//
//        }
//        setListAdapter(new ColorAdapter(getActivity(), mColorList));
//    }






}
