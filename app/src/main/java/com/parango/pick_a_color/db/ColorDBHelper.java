package com.parango.pick_a_color.db;



import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ColorDBHelper
        extends SQLiteOpenHelper {


    private static final String DB_NAME = "colors.db";
    private static final int DB_VERSION = 1;
    private Context mCtx;

    public ColorDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        mCtx = context;
    }

    @Override
    public void onCreate( SQLiteDatabase database ) {
        ColorTable.onCreate( database );

        boolean dbExist = checkDataBase();

        if(!dbExist){
                Log.d("Database Check:", "Prefilling Database");
                PreFillColorTableThread thread = new PreFillColorTableThread(database);
                thread.start();
        } else {
            Log.d("Database Check:", "Database exits!");
        }

    }



    @Override
    public void onUpgrade( SQLiteDatabase database,
                           int oldVersion,
                           int newVersion) {
        ColorTable.onUpgrade(database, oldVersion, newVersion);
    }



    private boolean checkDataBase(){

        SQLiteDatabase checkDB = null;

        try{
            String myPath = mCtx.getFilesDir().getPath()+"/databases/" + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        }catch(Exception e){

            //database does't exist yet.
        }

        if(checkDB != null){
            checkDB.close();
        }

        return checkDB != null;
    }


    private class PreFillColorTableThread extends Thread{

        SQLiteDatabase database;

        public PreFillColorTableThread (SQLiteDatabase db){
            database = db;
        }

        @Override
        public void run() {

            try {
                BufferedReader input = new BufferedReader(
                        new InputStreamReader(
                                mCtx.getAssets().open("ColorDb.csv")
                        ));
                String inline;

                while((inline = input.readLine()) != null){
                    String splits[] = inline.split(",");
                    ContentValues values = new ContentValues();
                    values.put(ColorTable.COLUMN_NAME, splits[0]);
                    values.put(ColorTable.COLUMN_HEX, splits[1]);
                    values.put(ColorTable.COLUMN_HUE, splits[2]);
                    values.put(ColorTable.COLUMN_SATURATION, splits[3]);
                    values.put(ColorTable.COLUMN_VALUE, splits[4]);
                    database.insertOrThrow(ColorTable.TABLE_COLOR, null, values);

                }
                input.close();

            }catch(Exception e){
                e.printStackTrace();
            }

        }
    }

}

