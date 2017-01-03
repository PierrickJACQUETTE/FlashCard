package com.projet.fashcard;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;

public class ListeActivity extends MenuActivity implements ListeJeuxFragment.OnFragmentInteractionListener {

    private FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.liste_fragment);
        if (fragment == null) {
            fragment = ListeJeuxFragment.newInstance();
            manager.beginTransaction().add(R.id.liste_fragment, fragment).commit();
        }
    }

    protected void goToJeux(long index) {
        LienProvider lien = new LienProvider(this);
        lien.updateJeuDate(index);

        Intent intent = new Intent(this, QuizCarteActivity.class);
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
