package com.rinsarz.braintrainer;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;

public class RecordsActivity extends AppCompatActivity {

    ListView recordsView;
    ArrayAdapter arrayAdapter;
    ArrayList <String> stringRecords = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);

        recordsView = (ListView)findViewById(R.id.recordsListView);
        recordsView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(RecordsActivity.this)
                        .setIcon(android.R.drawable.ic_menu_delete)
                        .setTitle("Delete record?")
                        .setMessage("Do you want to delete this record?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                GameSettings.RECORDS.remove(position);
                                loadStringRecords(GameSettings.RECORDS);
                                arrayAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                return false;
            }
        });

        stringRecords = new ArrayList<>();
        loadStringRecords(GameSettings.RECORDS);
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, stringRecords);
        recordsView.setAdapter(arrayAdapter);
    }

    public void loadStringRecords(ArrayList<Record> records){
        stringRecords.clear();
        for (Record r:records){
            stringRecords.add(r.toString());
        }
    }
}
