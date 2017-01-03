package com.projet.fashcard;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LienProvider {

    private static final String authority = "com.project.fcContentProvider";
    private Context context;
    private ContentResolver resolver;
    private ContentValues values;
    private Uri.Builder builder;

    public LienProvider(Context context) {
        this.context = context;
        this.resolver = context.getContentResolver();
        this.values = new ContentValues();
        this.builder = new Uri.Builder();
    }

    private String date() {
        String format = "dd MM yyyy";
        SimpleDateFormat formater = new SimpleDateFormat(format);
        Date d = new java.util.Date();
        return formater.format(d);
    }

    protected Uri insertLienCarte(String question, String reponse, Long id) {
        String niveau = "Difficile";
        values.put("question", question);
        values.put("reponse", reponse);
        values.put("niveau", niveau);
        values.put("jeu_id", id);
        values.put("dateView", date());
        builder.scheme("content").authority(authority).appendPath("carte_table");
        Uri uri = builder.build();
        return resolver.insert(uri, values);
    }

    protected Uri insertLienJeu(String nom, String date) {
        values.put("nom", nom);
        values.put("lastView", date);
        builder.scheme("content").authority(authority).appendPath("jeu_table");
        Uri uri = builder.build();
        return resolver.insert(uri, values);
    }

    protected Uri insertLienJeu(String nom) {
        return insertLienJeu(nom, date());
    }

    protected int updateJeuDate(long index) {
        values.put("lastView", date());
        builder.scheme("content").authority(authority).appendPath("update_jeu_date");
        Uri uri = builder.build();
        return resolver.update(uri, values, "_id= " + index, null);
    }

    protected int updateCarteDate(int id) {
        values.put("dateView", date());
        return updateCarte(id);
    }

    protected int updateCarteNiveau(int id, String difficulté) {
        values.put("niveau", difficulté);
        return updateCarte(id);
    }

    private int updateCarte(int id) {
        builder.scheme("content").authority(authority).appendPath("update_level");
        Uri uri = builder.build();
        return resolver.update(uri, values, "_id= " + id, null);
    }

    protected int deleteJeu(long position) {
        builder.scheme("content").authority(authority).appendPath("supp_jeu");
        ContentUris.appendId(builder, position);
        Uri uri = builder.build();
        return resolver.delete(uri, null, null);
    }
}
