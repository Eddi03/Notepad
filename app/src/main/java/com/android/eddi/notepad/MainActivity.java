package com.android.eddi.notepad;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton fab;
    Cursor cursor;
    DbHelper dbHelper;

    ArrayList<String> listItems;
    private ArrayAdapter<String> adapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DbHelper(this, getString(R.string.nome_db), null, 3);
        dbHelper.queryData(getString(R.string.sql_query));

        dbHelper.getReadableDatabase();
        listView = findViewById(R.id.lista);

        listItems = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textview = view.findViewById(android.R.id.text1);
                textview.setTextColor(Color.BLACK);
                textview.setTextSize(18);
                return view;
            }
        };
        listView.setAdapter(adapter);

        fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentCreaNota = new Intent(MainActivity.this, CreaNota.class);
                startActivity(intentCreaNota);
                finish();
            }
        });

        // Dati da SQLite
        cursor = dbHelper.getData(getString(R.string.sql_all_data));
        listItems.clear();

        if (cursor != null) {

            try {
                while (cursor.moveToNext()) {

                    String titolo = cursor.getString(cursor.getColumnIndexOrThrow(getString(R.string.sql_column_titolo)));
                    String contenuto = cursor.getString(cursor.getColumnIndexOrThrow(getString(R.string.sql_column_contenuto)));
                    String risultato = titolo + "\n" + contenuto;
                    listItems.add(risultato);
                }
                adapter.notifyDataSetChanged();
            } catch (Exception e) {
                Toast.makeText(this, getString(R.string.sql_err), Toast.LENGTH_SHORT).show();
            }
        }

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                CharSequence[] items = {getString(R.string.dialog_modifica), getString(R.string.dialog_elimina)};
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);

                dialog.setTitle(getString(R.string.dialog_title));
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 0) {
                            // Modifica
                            Cursor c = dbHelper.getData(getString(R.string.sql_id));
                            ArrayList<Integer> arrID = new ArrayList<>();
                            while (c.moveToNext()) {
                                arrID.add(c.getInt(0));
                            }

                            showDialogUpdate(MainActivity.this, arrID.get(position));

                        } else {
                            // Elimina
                            Cursor c = dbHelper.getData(getString(R.string.sql_id));
                            ArrayList<Integer> arrID = new ArrayList<>();
                            while (c.moveToNext()) {
                                arrID.add(c.getInt(0));
                            }
                            showDialogDelete(arrID.get(position));
                        }
                    }
                });
                dialog.show();
                return false;
            }
        });
    }

    private void showDialogUpdate(Activity activity, final int position) {

        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.layout_update_note);
        dialog.setTitle(getString(R.string.dialog_modifica));

        final EditText editTextTitolo = dialog.findViewById(R.id.editTextUpTit);
        final EditText editTextContenuto = dialog.findViewById(R.id.editTextUpCon);
        final Button buttonUpdate = dialog.findViewById(R.id.buttonUpdate);

        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.95);
        int height = (int) (activity.getResources().getDisplayMetrics().heightPixels * 0.7);
        dialog.getWindow().setLayout(width, height);
        dialog.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    dbHelper.updateData(editTextTitolo.getText().toString().trim().toUpperCase(), editTextContenuto.getText().toString().trim(), position);
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), getString(R.string.toast_aggiornato), Toast.LENGTH_SHORT).show();
                } catch (Exception error) {
                }
                updateListaNote();
            }
        });
    }

    private void updateListaNote() {
        // Dati da SQLite
        Cursor cursor = dbHelper.getData(getString(R.string.sql_all_data));
        listItems.clear();
        while (cursor.moveToNext()) {
            String titolo = cursor.getString(cursor.getColumnIndexOrThrow(getString(R.string.sql_column_titolo)));
            String contenuto = cursor.getString(cursor.getColumnIndexOrThrow(getString(R.string.sql_column_contenuto)));
            String risultato = titolo + "\n" + contenuto;
            listItems.add(risultato);
        }
        adapter.notifyDataSetChanged();
    }

    private void showDialogDelete(final int id) {
        final AlertDialog.Builder dialogDelete = new AlertDialog.Builder(MainActivity.this);

        dialogDelete.setTitle(getString(R.string.dialog_elimina_title));
        dialogDelete.setMessage(getString(R.string.dialog_elimina_msg));
        dialogDelete.setPositiveButton(getString(R.string.dialog_btn_si), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    dbHelper.deleteData(id);
                    Toast.makeText(getApplicationContext(), getString(R.string.dialog_eliminato), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                }
                updateListaNote();
            }
        });

        dialogDelete.setNegativeButton(getString(R.string.dialog_btn_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialogDelete.show();
    }
}