package com.projet.fashcard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AjouterCarteActivity extends MenuActivity {

    private Button ajouter;
    private EditText reponse;
    private EditText question;
    private Long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_carte);
        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if (bd != null) {
            id = bd.getLong("id");
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ajouter = (Button) findViewById(R.id.bajouter);
        reponse = (EditText) findViewById(R.id.etReponse);
        question = (EditText) findViewById(R.id.etQuestion);
    }


    public void ajouter(View view) {

        if (reponse.getText().toString().trim().length() != 0 && question.getText().toString().trim().length() != 0) {
            LienProvider lien = new LienProvider(this);
            lien.insertLienCarte(question.getText().toString(), reponse.getText().toString(), id);

            question.getText().clear();
            reponse.getText().clear();
            setResult(Activity.RESULT_OK);
            finish();
        } else {
            Toast toast = Toast.makeText(this, "Les 2 champs doivent être remplies", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
