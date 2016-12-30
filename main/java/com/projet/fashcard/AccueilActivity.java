package com.projet.fashcard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class AccueilActivity extends AppCompatActivity {

    private static String TAG = "In_AccueilActivity";
    private static Toast toast;
    private Button jeu;
    private Button create;
    private Button option;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = new Intent(this, NotificationService.class);
        startService(intent);

        jeu = (Button) findViewById(R.id.button_apprendre);
        create = (Button) findViewById(R.id.button_create);
        option = (Button) findViewById(R.id.button_opt);
    }

    protected void toJeu(View view) {
        Log.d(TAG, "toJeu: ");
        Intent intent = new Intent(this, ListeActivity.class);
        startActivity(intent);
    }

    protected void toCreate(View view) {
        Intent intent = new Intent(this, OutilCreateSuppActivity.class);
        startActivity(intent);
    }

    protected void toOption(View view) {
        Log.d(TAG, "toOption: ");
        Intent intent = new Intent(this, OptionActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.miAppr:
                intent = new Intent(this, ListeActivity.class);
                startActivity(intent);
                return true;
            case R.id.miCreate:
                intent = new Intent(this, OutilCreateSuppActivity.class);
                startActivity(intent);
                return true;
            case R.id.miPref:
                intent = new Intent(this, OptionActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
