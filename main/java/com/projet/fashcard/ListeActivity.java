package com.projet.fashcard;


import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ListeActivity extends AppCompatActivity implements ListeJeuxFragment.OnFragmentInteractionListener {

    private FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste);
        manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.liste_fragment);
        if(fragment == null){
            fragment = ListeJeuxFragment.newInstance();
            manager.beginTransaction().add(R.id.liste_fragment, fragment).commit();
        }
    }

    protected void goToJeux(int index){
        //en attente de chercher dans la database
        Intent intent = new Intent(this, AccueilActivity.class);
        startActivity(intent);
    }

    @Override
    public void onJeuxSelection(int index) {
        goToJeux(index);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {}
}
