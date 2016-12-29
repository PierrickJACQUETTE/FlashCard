package com.projet.fashcard;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ListeActivity extends AppCompatActivity implements ListeJeuxFragment.OnFragmentInteractionListener {

    private FragmentManager manager;
    private static String authority = "com.project.fcContentProvider";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste);
        manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.liste_fragment);
        if (fragment == null) {
            fragment = ListeJeuxFragment.newInstance();
            manager.beginTransaction().add(R.id.liste_fragment, fragment).commit();
        }
    }

    protected void goToJeux(long index) {
        String format = "dd MM yyyy";
        SimpleDateFormat formater = new SimpleDateFormat(format);
        Date d = new java.util.Date();
        String date = formater.format(d);
        ContentResolver resolver = getContentResolver();
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("content").authority(authority).appendPath("update_jeu_date");
        ContentValues newValues = new ContentValues();
        newValues.put("lastView", date);
        Uri uri = builder.build();
        int c = resolver.update(uri, newValues, "_id= " + index, null);
        Intent intent = new Intent(this, QuizCarte.class);
        intent.putExtra("index", index);
        startActivity(intent);
    }

    @Override
    public void onJeuxSelection(long index) {
        goToJeux(index);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }
}
