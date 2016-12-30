package com.projet.fashcard;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AjouterCarteActivity extends AppCompatActivity {

    private static String authority = "com.project.fcContentProvider";
    private Button ajouter;
    private EditText reponse;
    private EditText question;
    private Long id;
    private final String NIVEAU = "Difficile";

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
            ContentResolver resolver = getContentResolver();
            String q = question.getText().toString();
            String r = reponse.getText().toString();
            ContentValues values = new ContentValues();
            values.put("question", q);
            values.put("reponse", r);
            values.put("niveau", NIVEAU);
            values.put("jeu_id", id);

            String format = "dd MM yyyy";
            SimpleDateFormat formater = new SimpleDateFormat(format);
            Date d = new java.util.Date();
            String date = formater.format(d);
            values.put("dateView", date);

            Uri.Builder builder = new Uri.Builder();
            builder.scheme("content").authority(authority).appendPath("carte_table");
            Uri uri = builder.build();
            uri = resolver.insert(uri, values);
            question.getText().clear();
            reponse.getText().clear();
            setResult(Activity.RESULT_OK);
            finish();
        } else {
            Toast toast = Toast.makeText(this, "Les 2 champs doivent être remplies", Toast.LENGTH_SHORT);
            toast.show();
        }
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
