package com.parango.pick_a_color.content_provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.parango.pick_a_color.db.ColorDBHelper;
import com.parango.pick_a_color.db.ColorTable;

public class ColorContentProvider extends ContentProvider {

    // database
    private ColorDBHelper database;

    private static final String AUTHORITY
            = "com.parango.pick_a_color.provider";

    private static final String BASE_PATH
            = "colors";


    public static final Uri CONTENT_URI
            = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

    // URI Matcher
    private static final UriMatcher sURIMatcher = new UriMatcher( UriMatcher.NO_MATCH );
    private static final int COLORS = 1;
    static {
        sURIMatcher.addURI(AUTHORITY, BASE_PATH, COLORS);
    }



    @Override
    public boolean onCreate() {
        database = new ColorDBHelper(getContext());
        return false;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;

    }

    @Override
    public String getType(Uri uri) {
       return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // check if the caller has requested a column which does not exists
        ColorTable.validateProjection(projection);

        String print = "";

        for(String a: selectionArgs ){
            print = print+ " " + a;
        }
        //Log.d("ContentProvider", selection);

        //Log.d("ContentProvider", print);

        // Using SQLiteQueryBuilder instead of query() method
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        queryBuilder.setTables( ColorTable.TABLE_COLOR );

        SQLiteDatabase db = database.getWritableDatabase();
        Cursor cursor = queryBuilder.query( db, projection, selection,
                selectionArgs, null, null, sortOrder);

        // notify listeners
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        //Log.d("ContentProviderCursor", cursor.getString( 0 ));

        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        return 0;
    }
}
