package com.parango.pick_a_color;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.parango.pick_a_color.content_provider.ColorContentProvider;
import com.parango.pick_a_color.db.ColorDBHelper;
import com.parango.pick_a_color.fragments.DetailsFragment;
import com.parango.pick_a_color.fragments.HueFragment;
import com.parango.pick_a_color.fragments.SaturationFragment;
import com.parango.pick_a_color.fragments.ValueFragment;


public class MainActivity
        extends Activity
        implements HueFragment.OnHueSelectedListener,
        SaturationFragment.OnSaturationSelectedListener,
        ValueFragment.OnValueSelectedListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //create database on activity create
        ColorDBHelper start = new ColorDBHelper(this);
        start.getWritableDatabase();



        if (savedInstanceState != null) {
            return;
        }

        HueFragment firstFragment = new HueFragment();

        firstFragment.setArguments(getIntent().getExtras());

        getFragmentManager().beginTransaction()
                .add(R.id.fragment_container, firstFragment).commit();

    }

    @Override
    public void onHueSelected( float hueStart, float hueEnd ) {

        SaturationFragment saturationFragment = new SaturationFragment();
        Bundle args = new Bundle();
        args.putFloat(SaturationFragment.ARG_HUE_START, hueStart);
        args.putFloat(SaturationFragment.ARG_HUE_END, hueEnd);
        saturationFragment.setArguments(args);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, saturationFragment);
        transaction.addToBackStack(null);

        transaction.commit();

    }

    @Override
    public void onSaturationSelected(float hueStart, float hueEnd, float saturation) {

        ValueFragment valueFragment = new ValueFragment();
        Bundle args = new Bundle();
        args.putFloat(ValueFragment.ARG_HUE_START, hueStart);
        args.putFloat(ValueFragment.ARG_HUE_END, hueEnd);
        args.putFloat(ValueFragment.ARG_SATURATION, saturation);
        valueFragment.setArguments(args);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();


        transaction.replace(R.id.fragment_container, valueFragment);
        transaction.addToBackStack(null);


        transaction.commit();
    }

    @Override
    public void onValueSelected(float hueStart, float hueEnd, float saturation, float value) {
        //Log.d("Values:", " HueStart: " + hueStart + " HueEnd: " + hueEnd + " Sat:" + saturation + " Val: " + value);
        DetailsFragment valueFragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putFloat(DetailsFragment.ARG_HUE_START, hueStart);
        args.putFloat(DetailsFragment.ARG_HUE_END, hueEnd);
        args.putFloat(DetailsFragment.ARG_SATURATION, saturation);
        args.putFloat(DetailsFragment.ARG_VALUE, value);
        valueFragment.setArguments(args);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();


        transaction.replace(R.id.fragment_container, valueFragment);
        transaction.addToBackStack(null);


        transaction.commit();
    }

}
