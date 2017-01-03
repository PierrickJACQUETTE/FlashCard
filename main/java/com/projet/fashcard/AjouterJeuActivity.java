package com.projet.fashcard;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AjouterJeuActivity extends MenuActivity {

    private Button ajouter;
    private EditText nom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_jeu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ajouter = (Button) findViewById(R.id.bajouter);
        ajouter.setEnabled(false);
        nom = (EditText) findViewById(R.id.etNom);
        nom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() != 0) {
                    ajouter.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void ajouter(View view) {
        LienProvider lien = new LienProvider(this);
        lien.insertLienJeu(nom.getText().toString());
        nom.getText().clear();
        setResult(Activity.RESULT_OK);
        finish();
    }
}
