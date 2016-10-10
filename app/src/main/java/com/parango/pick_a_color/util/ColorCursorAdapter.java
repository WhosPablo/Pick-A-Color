package com.parango.pick_a_color.util;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.parango.pick_a_color.R;
import com.parango.pick_a_color.db.ColorTable;



public class ColorCursorAdapter extends CursorAdapter {

    // Fields from the database (projection)
    // Must include the _id column for the adapter to work
    static public final int ID         = 0;
    static public final int NAME       = 1;
    static public final int HEX        = 2;
    static public final int HUE        = 3;
    static public final int SATURATION = 4;
    static public final int VALUE      = 5;

    static public final String[] PROJECTION
            = new String[] {
            ColorTable.COLUMN_ID,
            ColorTable.COLUMN_NAME,
            ColorTable.COLUMN_HEX,
            ColorTable.COLUMN_HUE,
            ColorTable.COLUMN_SATURATION,
            ColorTable.COLUMN_VALUE
    };

    static public final String[] ORDER_BY
            = new String[] {
            ColorTable.COLUMN_NAME,

            ColorTable.COLUMN_HUE + "," +
            ColorTable.COLUMN_SATURATION + "," +
            ColorTable.COLUMN_VALUE,

            ColorTable.COLUMN_HUE + "," +
            ColorTable.COLUMN_VALUE + "," +
            ColorTable.COLUMN_SATURATION,

            ColorTable.COLUMN_SATURATION + "," +
            ColorTable.COLUMN_HUE + "," +
            ColorTable.COLUMN_VALUE,

            ColorTable.COLUMN_SATURATION + "," +
            ColorTable.COLUMN_VALUE + "," +
            ColorTable.COLUMN_HUE,

            ColorTable.COLUMN_VALUE + "," +
            ColorTable.COLUMN_HUE + "," +
            ColorTable.COLUMN_SATURATION,

            ColorTable.COLUMN_VALUE + "," +
            ColorTable.COLUMN_SATURATION + "," +
            ColorTable.COLUMN_HUE,

            };




    private static class ViewHolder {
        TextView preview;
        TextView name;
        TextView hexcode;
    }

    private LayoutInflater mInflater;

    public ColorCursorAdapter( Context context, Cursor cursor, int flags ) {
        super(context, cursor, flags);

        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View row = mInflater.inflate(R.layout.details_row, parent, false);

        // cache the row's Views in a ViewHolder
        ViewHolder viewHolder = new ViewHolder();

        viewHolder.preview = (TextView) row.findViewById( R.id.detail_preview );
        viewHolder.name = (TextView) row.findViewById( R.id.color_name );
        viewHolder.hexcode = (TextView) row.findViewById( R.id.rgb_hex_code );
        row.setTag( viewHolder );


        return row;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = updateViewHolderValues(view, cursor);
    }

    private ViewHolder updateViewHolderValues(View view, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        //Log.d("AdapterValues:", cursor.getString( NAME ) + "  " + cursor.getString( HEX ));

        viewHolder.name.setText( cursor.getString( NAME ) );

        String hexColor = cursor.getString(HEX);
        viewHolder.hexcode.setText( hexColor);
        viewHolder.preview.setBackgroundColor(Color.parseColor(hexColor));

        return viewHolder;
    }

    @Override
    public void changeCursor(Cursor newCursor) {
        newCursor.close();
    }
}
