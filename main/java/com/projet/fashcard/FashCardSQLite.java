package com.projet.fashcard;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FashCardSQLite extends SQLiteOpenHelper {

    private static int VERSION = 9;
    private static FashCardSQLite instance;
    private static String bd = "FCBD";
    private String carte_table = "create table carte_table( " +
            "question varchar(50) not null," +
            "reponse varchar(50) not null," +
            "niveau varchar(30) not null," +
            "dateView date not null," +
            "jeu_id int reference jeu_table," +
            "_id integer primary key" +
            ");";
    private String jeu_table = "create table jeu_table( " +
            "nom varchar(50) not null," +
            "lastView date not null," +
            "_id integer primary key" +
            ");";

    public static FashCardSQLite getInstance(Context context) {
        if (instance == null) {
            instance = new FashCardSQLite(context);
        }
        return instance;
    }

    private FashCardSQLite(Context context) {
        super(context, bd, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(jeu_table);
        db.execSQL(carte_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            db.execSQL("drop table if exists carte_table ;");
            db.execSQL("drop table if exists jeu_table ;");
            onCreate(db);
        }
    }
}
