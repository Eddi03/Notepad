package com.android.eddi.notepad;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class CreaNota extends AppCompatActivity {

    EditText editTextTitolo;
    EditText editTextContenuto;
    String titolo, contenuto;
    public static DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crea_nota);

        init();

        dbHelper = new DbHelper(this, getString(R.string.nome_db), null, 2);
        dbHelper.queryData(getString(R.string.sql_query));

        editTextTitolo = findViewById(R.id.editTextTitolo);
        editTextContenuto = findViewById(R.id.editTextContenuto);

    }
    private void init() {
        editTextTitolo = findViewById(R.id.editTextTitolo);
        editTextContenuto = findViewById(R.id.editTextContenuto);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.check) {
            titolo = editTextTitolo.getText().toString().trim().toUpperCase();
            contenuto = editTextContenuto.getText().toString().trim();
            try {
                dbHelper.insertData(titolo, contenuto);
                Toast.makeText(getApplicationContext(), getString(R.string.sql_saved), Toast.LENGTH_SHORT).show();
                editTextTitolo.setText("");
                editTextContenuto.setText("");

                Intent toMainActivity = new Intent(CreaNota.this, MainActivity.class);
                startActivity(toMainActivity);
                //finish();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            Toast.makeText(this, getString(R.string.sql_not_saved), Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}
