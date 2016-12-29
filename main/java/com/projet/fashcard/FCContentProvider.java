package com.projet.fashcard;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class FCContentProvider extends ContentProvider {

    private static String authority = "com.project.fcContentProvider";
    private static final int INSERTJEU = 1;
    private static final int INSERTCARTE = 2;
    private static final int SUPPJEU = 3;
    private static final int UPDATELEVEL = 4;
    private static final int UPDATEJEUDATE = 5;

    private FashCardSQLite helper;

    private static final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        matcher.addURI(authority, "jeu_table", INSERTJEU);
        matcher.addURI(authority, "carte_table", INSERTCARTE);
        matcher.addURI(authority, "supp_jeu/*", SUPPJEU);
        matcher.addURI(authority, "update_level", UPDATELEVEL);
        matcher.addURI(authority, "update_jeu_date", UPDATEJEUDATE);
    }

    public FCContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int code = matcher.match(uri);
        int i;
        switch (code) {
            case SUPPJEU:
                long id = ContentUris.parseId(uri);
                db.delete("carte_table", "jeu_id= " + id, null);
                i = db.delete("jeu_table", "_id= " + id, null);
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
        return i;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int code = matcher.match(uri);
        long id = 0;
        Uri.Builder builder = new Uri.Builder();
        switch (code) {
            case INSERTJEU:
                id = db.insert("jeu_table", null, values);
                builder.appendPath("jeu_table");
                break;
            case INSERTCARTE:
                id = db.insert("carte_table", null, values);
                builder.appendPath("carte_table");
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
        builder.authority(authority);
        builder = ContentUris.appendId(builder, id);
        return builder.build();
    }

    @Override
    public boolean onCreate() {
        helper = FashCardSQLite.getInstance(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = helper.getReadableDatabase();
        int code = matcher.match(uri);
        Cursor cursor;
        switch (code) {
            case INSERTJEU:
                cursor = db.query("jeu_table", projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case INSERTCARTE:
                cursor = db.query("carte_table", projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int code = matcher.match(uri);
        int updateCount;
        switch (code) {
            case UPDATELEVEL:
                updateCount = db.update("carte_table", values, selection, selectionArgs);
                break;
            case UPDATEJEUDATE:
                updateCount = db.update("jeu_table", values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented");

        }
        return updateCount;
    }
}
