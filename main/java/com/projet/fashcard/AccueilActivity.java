package com.projet.fashcard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class AccueilActivity extends AppCompatActivity {

    private static String TAG = "In_AccueilActivity";

    private static Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);
    }

    protected void toJeu(View view){
        Log.d(TAG, "toJeu: ");
        Intent intent = new Intent(this, ListeActivity.class);
        startActivity(intent);
        //toast = Toast.makeText(this, "En cours de développement", Toast.LENGTH_SHORT);
        //toast.show();
    }

    protected void toCreate(View view){
        Log.d(TAG, "toCreate: ");
        toast = Toast.makeText(this, "En cours de développement", Toast.LENGTH_SHORT);
        toast.show();
    }

    protected void toOption(View view){
        Log.d(TAG, "toOption: ");
        toast = Toast.makeText(this, "En cours de développement", Toast.LENGTH_SHORT);
        toast.show();
    }
}
