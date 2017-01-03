package com.projet.fashcard;

import android.Manifest;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class OutilCreateSuppActivity extends MenuActivity implements ListeJeuxFragment.OnFragmentInteractionListener {

    private final int REQUEST_UN = 1;
    private String permission;
    private Button buttonPlus;
    private Button buttonDowload;
    private FragmentManager manager;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outil_create_supp);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        Intent intent = new Intent(this, AjouterJeuActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == AjouterJeuActivity.RESULT_OK) {
                finish();
            }
        }
    }

    public void supp(long position) {
        LienProvider lien = new LienProvider(this);
        lien.deleteJeu(position);
        fragment = ListeJeuxFragment.newInstance();
        manager.beginTransaction().replace(R.id.liste_fragment, fragment).addToBackStack("debut").commit();
    }

    @Override
    public void onJeuxSelection(long index) {
        final long position = index;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.outilCreateSupp_dialog_title);
        builder.setPositiveButton(R.string.outilCreateSupp_dialog_supp, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                supp(position);
            }
        });
        builder.setNegativeButton(R.string.outilCreateSupp_dialog_add, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                Intent intent = new Intent(OutilCreateSuppActivity.this, AjouterCarteActivity.class);
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void download(View view) {
        permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        ArrayList<String> permNonAccordé = new ArrayList<String>();
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), permission) != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(permission) == true) {
                explain();
            } else {
                askForPermission();
            }
        } else {
            startDownload();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void askForPermission() {
        requestPermissions(new String[]{permission}, REQUEST_UN);
    }


    public void startDownload() {
        Intent intent = new Intent(this, DownloadManagerService.class);
        startService(intent);
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_UN) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startDownload();
            } else if (shouldShowRequestPermissionRationale(permissions[0]) == false) {
                displayOptions();
            } else {
                explain();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void displayOptions() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.outil_create_supp_param));
        builder.setPositiveButton("Paramètres", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            public void onClick(DialogInterface dialog, int id) {
                final Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                final Uri uri = Uri.fromParts("package", getApplication().getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void explain() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.outil_create_supp_perm));
        builder.setPositiveButton(getString(R.string.ajouter_add), new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            public void onClick(DialogInterface dialog, int id) {
                askForPermission();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
