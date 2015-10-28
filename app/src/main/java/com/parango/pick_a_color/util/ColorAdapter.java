package com.parango.pick_a_color.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.parango.pick_a_color.R;

import java.util.ArrayList;
import java.util.List;


public class ColorAdapter extends ArrayAdapter<HSVColor> {

    private List<HSVColor> colorList;

    private static class ViewHolder {
        TextView preview;
    }

    public ColorAdapter(Context context, ArrayList<HSVColor> objects){
        super(context, 0,  objects);
        this.colorList = objects;
    }

    public View getView( int position, View convertView, ViewGroup parent){


        ViewHolder viewHolder; // cache Views to avoid future lookups


        // check if an existing View is being recycled; if not
        // inflate a new one
        if( convertView == null ){

            viewHolder = new ViewHolder();

            convertView = LayoutInflater.from( getContext() ).inflate( R.layout.color_row, parent, false );

            viewHolder.preview = (TextView) convertView.findViewById( R.id.color_preview);

            //cache the Views
            convertView.setTag( viewHolder );
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        // populate the View with data
        HSVColor color = getItem( position );
        float start = color.getHueStart();
        //Log.d("Start: ", String.valueOf(start));

        float end = color.getHueEnd();
        //Log.d("End: ", String.valueOf(end));

        int arraysize = (int)(end-start);

        if (arraysize<0){
            arraysize = -1*arraysize;
        }

        if(start<0){
            start = 360+start;
        }
        //Log.d("ArraySize", String.valueOf(arraysize));

        float saturation = color.getSaturation();
        float value = color.getValue();
        float [] hsvStart = {start,saturation,value};
        float [] hsvEnd = {end, saturation, value};

        int colorStart = Color.HSVToColor(hsvStart);
        int colorEnd = Color.HSVToColor(hsvEnd);

        int colorArray[] = new int[arraysize];

        for (int i = 0; i<arraysize; i++){
            colorArray[i]= Color.HSVToColor(new float[]{((start+i)%360), saturation, value});
        }

        GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colorArray);


        viewHolder.preview.setBackground(drawable);

        return convertView;
    }



}
