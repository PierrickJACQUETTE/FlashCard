package com.projet.fashcard;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AjouterJeu extends AppCompatActivity {

    private static String authority = "com.project.fcContentProvider";
    private Button ajouter;
    private EditText nom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_jeu);
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
        String n = nom.getText().toString();
        ContentValues values = new ContentValues();
        values.put("nom", n);
        ContentResolver resolver = getContentResolver();
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("content").authority(authority).appendPath("jeu_table");
        Uri uri = builder.build();
        uri = resolver.insert(uri, values);
        nom.getText().clear();
        setResult(Activity.RESULT_OK);
        finish();
    }


}
