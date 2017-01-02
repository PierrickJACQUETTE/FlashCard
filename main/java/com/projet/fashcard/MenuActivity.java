package com.projet.fashcard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MenuActivity extends AppCompatActivity {

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
