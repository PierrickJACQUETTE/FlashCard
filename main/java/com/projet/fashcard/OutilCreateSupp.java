package com.projet.fashcard;

import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class OutilCreateSupp extends AppCompatActivity implements ListeJeuxFragment.OnFragmentInteractionListener {

    private static String authority = "com.project.fcContentProvider";
    private Button buttonPlus;
    private Button buttonDowload;

    private final Context context = this;
    private FragmentManager manager;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outil_create_supp);
        buttonPlus = (Button) findViewById(R.id.buttonPlus);
        buttonDowload = (Button) findViewById(R.id.buttonDowload);
        manager = getSupportFragmentManager();
        fragment = manager.findFragmentById(R.id.liste_fragment);
        if (fragment == null) {
            fragment = ListeJeuxFragment.newInstance();
            manager.beginTransaction().add(R.id.liste_fragment, fragment).addToBackStack("debut").commit();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }


    public void ajouterJeu(View view) {
        Intent intent = new Intent(this, AjouterJeu.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == AjouterJeu.RESULT_OK) {
                finish();
            }
        }
    }

    public void download(View view) {
        Intent intent = new Intent(this, DownloadManagerActivity.class);
        startService(intent);
        fragment = ListeJeuxFragment.newInstance();
        manager.beginTransaction().replace(R.id.liste_fragment, fragment).addToBackStack("debut").commit();
    }

    public void supp(long position) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("content").authority(authority).appendPath("supp_jeu");
        ContentUris.appendId(builder, position);
        Uri uri = builder.build();
        int res = getContentResolver().delete(uri, null, null);
        fragment = ListeJeuxFragment.newInstance();
        manager.beginTransaction().replace(R.id.liste_fragment, fragment).addToBackStack("debut").commit();
    }

    @Override
    public void onJeuxSelection(long index) {
        final long position = index;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.outilCreateSupp_dialog_title);
        builder.setPositiveButton(R.string.outilCreateSupp_dialog_supp, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                supp(position);
            }
        });
        builder.setNegativeButton(R.string.outilCreateSupp_dialog_add, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                Intent intent = new Intent(OutilCreateSupp.this, AjouterCarte.class);
                intent.putExtra("id", position);
                startActivity(intent);
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }
}
