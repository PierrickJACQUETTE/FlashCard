package com.projet.fashcard;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class AccueilActivity extends AppCompatActivity {

    private static String TAG = "In_AccueilActivity";
    private static Toast toast;
    private Button jeu;
    private Button create;
    private Button option;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_accueil);

        Log.d(TAG, "onCreat22e: ");
        Intent intent = new Intent(this, NotificationService.class);
        startService(intent);
        Log.d(TAG, "onCreate: ");
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
        Intent intent = new Intent(this, OutilCreateSupp.class);
        startActivity(intent);
    }

    protected void toOption(View view) {
        Log.d(TAG, "toOption: ");
        toast = Toast.makeText(this, "En cours de développement", Toast.LENGTH_SHORT);
        toast.show();
    }
}
