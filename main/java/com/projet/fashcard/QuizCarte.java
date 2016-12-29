package com.projet.fashcard;

import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class QuizCarte extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static String authority = "com.project.fcContentProvider";
    private long index;
    private SimpleCursorAdapter adapter;
    private LoaderManager manager;

    private TextView question;
    private EditText reponse;
    private Button verifier;
    private Button difficile;

    String diff[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_carte);
        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if (bd != null) {
            index = bd.getLong("index");
        }
        adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, null, new String[]{"question"}, new int[]{android.R.id.text1}, 0);

        diff = getResources().getStringArray(R.array.difficult);

        reponse = (EditText) findViewById(R.id.quiz_reponse);
        question = (TextView) findViewById(R.id.quiz_question);
        verifier = (Button) findViewById(R.id.verifier);
        difficile = (Button) findViewById(R.id.difficile);
        difficile.setEnabled(false);
        manager = getLoaderManager();
        manager.initLoader(0, null, this);
    }

    public void verifier(View view) {
        String giv = reponse.getText().toString();
        String rep = adapter.getCursor().getString(2);
        if(giv.equals(rep)){
            Toast toast = Toast.makeText(this, "Bonne réponse !", Toast.LENGTH_SHORT);
            toast.show();
            difficile.setEnabled(true);
            verifier.setEnabled(false);
        } else {
            Toast toast = Toast.makeText(this, "Faux, la bonne réponse est "+rep, Toast.LENGTH_SHORT);
            toast.show();
            difficile.setEnabled(true);
            verifier.setEnabled(false);
        }
    }

    public void change(String newLevel){
        int id = adapter.getCursor().getInt(0);
        ContentResolver resolver = getContentResolver();
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("content").authority(authority).appendPath("update_level");
        ContentValues newValues = new ContentValues();
        newValues.put("niveau", newLevel);
        Uri uri = builder.build();
        int c = resolver.update(uri, newValues, "_id= " + id, null);
    }

    public void difficile(View view) {
        difficile.setEnabled(false);
        verifier.setEnabled(true);
        reponse.setText("");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.ajouter_carte_tv_choice)
                .setItems(R.array.difficult, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        change(diff[which]);
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();

        if (adapter.getCursor().moveToNext()) {
            affiche();
        } else {
            finish();
        }
    }

    public void affiche() {
        question.setText(adapter.getCursor().getString(1));
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri;
        Uri.Builder builder = new Uri.Builder();
        uri = builder.scheme("content").authority(authority).appendPath("carte_table").build();
        Log.d("ci", "onCreateLoader: ");
        return new CursorLoader(this, uri, new String[]{"_id", "question", "reponse", "niveau"}, "jeu_id= " + index, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
        adapter.getCursor().moveToFirst();
        affiche();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
