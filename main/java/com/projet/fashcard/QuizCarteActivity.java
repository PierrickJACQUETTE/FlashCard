package com.projet.fashcard;

import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class QuizCarteActivity extends MenuActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private long index;
    private SimpleCursorAdapter adapter;
    private LoaderManager manager;

    private TextView question;
    private EditText reponse;
    private Button verifier;
    private Button difficile;
    private Timer timer;
    private MyTimerTask myTimerTask;

    private String diff[];
    private ArrayList<Integer> ques;
    private int pos = 0, ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_carte);
        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if (bd != null) {
            index = bd.getLong("index");
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ques = new ArrayList<>();
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

    public boolean checkDate(String d, int val) {
        try {
            String format = "dd MM yyyy";
            SimpleDateFormat formater = new SimpleDateFormat(format);
            Date d2 = new java.util.Date();
            String date = formater.format(d2);

            Date date1 = formater.parse(date);
            Date date2 = formater.parse(d);
            long diff = date2.getTime() - date1.getTime();
            return (TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) % val) == 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void verifier(View view) {
        String giv = reponse.getText().toString();
        String rep = adapter.getCursor().getString(2);
        if (giv.equals(rep)) {
            Toast toast = Toast.makeText(this, "Bonne réponse !", Toast.LENGTH_SHORT);
            toast.show();
            difficile.setEnabled(true);
            verifier.setEnabled(false);
        } else {
            Toast toast = Toast.makeText(this, "Faux, la bonne réponse est " + rep, Toast.LENGTH_SHORT);
            toast.show();
            difficile.setEnabled(true);
            verifier.setEnabled(false);
        }
         LienProvider lien = new LienProvider(this);
        int id = adapter.getCursor().getInt(0);
        lien.updateCarteDate(id);
        ques.remove(pos);
    }

    public void difficile(View view) {
        difficile.setEnabled(false);
        verifier.setEnabled(true);
        reponse.setText("");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.ajouter_carte_tv_choice)
                .setItems(R.array.difficult, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        LienProvider lien = new LienProvider(getApplicationContext());
                        int id = adapter.getCursor().getInt(0);
                        lien.updateCarteNiveau(id, diff[which]);
                        if (!ques.isEmpty()) {
                            affiche();
                        } else {
                            Toast toast = Toast.makeText(getApplicationContext(), "Session quotidienne finie", Toast.LENGTH_SHORT);
                            toast.show();
                            if (timer != null) {
                                timer.cancel();
                            }
                            finish();
                        }

                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void affiche() {
        if (!ques.isEmpty()) {
            pos = (int) (Math.random() * ques.size());
            adapter.getCursor().moveToPosition(ques.get(pos) - ref);
            question.setText(adapter.getCursor().getString(1));
            if (timer != null) {
                timer.cancel();
            }
            timer = new Timer();
            myTimerTask = new MyTimerTask();
            SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            timer.schedule(myTimerTask, 1000 * Integer.parseInt(SP.getString("timer", "30")));
        } else {
            Toast toast = Toast.makeText(this, "Il n'y a pas de carte à réviser aujourd'hui", Toast.LENGTH_SHORT);
            toast.show();
            finish();
        }

    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri;
        Uri.Builder builder = new Uri.Builder();
        uri = builder.scheme("content").authority(getString(R.string.authority)).appendPath("carte_table").build();
        return new CursorLoader(this, uri, new String[]{"_id", "question", "reponse", "niveau", "dateView"}, "jeu_id= " + index, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        adapter.swapCursor(data);
        Cursor c = adapter.getCursor();
        int i = 0;
        boolean first = true;
        while (c.moveToNext()) {
            if (first) {
                ref = c.getInt(0);
                first = false;
            }
            String tmp = c.getString(3);
            i = Integer.parseInt(c.getString(0));
            if (tmp.equals("Difficile")) {
                ques.add(i);
            } else if (tmp.equals("Moyenne") && checkDate(c.getString(4), 2)) {
                ques.add(i);
            } else if (tmp.equals("Facile") && checkDate(c.getString(4), 4)) {
                ques.add(i);
            }
        }
        affiche();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    verifier(null);
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        if (timer != null) {
            timer.cancel();
        }
        super.onDestroy();
    }
}
