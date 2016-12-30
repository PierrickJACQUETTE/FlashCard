package com.projet.fashcard;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AjouterJeuActivity extends AppCompatActivity {

    private static String authority = "com.project.fcContentProvider";
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
        String format = "dd MM yyyy";
        SimpleDateFormat formater = new SimpleDateFormat(format);
        Date d = new java.util.Date();
        String date = formater.format(d);
        String n = nom.getText().toString();
        ContentValues values = new ContentValues();
        values.put("nom", n);
        values.put("lastView", date);
        ContentResolver resolver = getContentResolver();
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("content").authority(authority).appendPath("jeu_table");
        Uri uri = builder.build();
        uri = resolver.insert(uri, values);
        nom.getText().clear();
        setResult(Activity.RESULT_OK);
        finish();
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
                finish();
                return true;
            case R.id.miCreate:
                intent = new Intent(this, OutilCreateSuppActivity.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.miPref:
                intent = new Intent(this, OptionActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
