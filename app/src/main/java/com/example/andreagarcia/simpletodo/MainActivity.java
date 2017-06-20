package com.example.andreagarcia.simpletodo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //declaring stateful objects here; will be null before onCreate is called
    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //reference to ListView created with layout
        lvItems = (ListView) findViewById(R.id.lvItems);
        //initialize items list
        readItems();
        //initialize adapter using items list
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        //wire adapter to the view
        lvItems.setAdapter(itemsAdapter);

        //mock items for list
        //items.add("First todo item");
        //items.add("Second todo item");

        //setup listener on creation
        setupListViewListener();
    }
    //return file that has data stored
    private File getDataFile() {
        return new File(getFilesDir(), "todo.txt");
    }
    //read items from file system
    private void readItems() {
        try {
            //create array using content in file
            items = new ArrayList<String>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch(IOException e) {
            //print effor to the console
            e.printStackTrace();
            //load an empty list
            items = new ArrayList<>();
        }
    }
    //write tiems to filesystem
    private void writeItems() {
        try {
            //save item list as line-delimited text file
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            //print error to console
            e.printStackTrace();
        }
    }
    private void setupListViewListener() {
        //set ListView's itemLongClickListener
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //remove item in list @ index given by position
                items.remove(position);
                //notify the adapter that dataset has changed
                itemsAdapter.notifyDataSetChanged();
                //store updated list
                writeItems();
                Log.i("MainActivity", "Removed item "+position);
                //return true to tell framework that long click was consumed
                return true;
            }
        });
    }

    public void onAddItem(View v) {
        //obtain reference to EditText
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        //get EditText content as string
        String itemText = etNewItem.getText().toString();
        //add item to list via adapter
        itemsAdapter.add(itemText);
        //store updated list
        writeItems();
        //clear EditText
        etNewItem.setText("");
        //notification for user
        Toast.makeText(getApplicationContext(), "Item added to list", Toast.LENGTH_SHORT).show();
    }
}
