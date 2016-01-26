package com.keidelgmail.hans.alexander.shoppinglistapplication;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * see http://theopentutorials.com/tutorials/android/listview/android-expandable-list-view-example/
 * for reference
 */
public class MainActivity extends ActionBarActivity {


    private ExpandableListView myListView;
    private CustomExpandableListAdapter myAdapter; //using custom class
    private TextView exampleTextView;
    private List<String> itemsList;
    private Map<String, List<String>> itemCollections;

    private SQLiteDatabase myDb = null;
    private MyDatabaseHelper myDbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initialiseList();
        setupDatabase();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(this, "Settings are not currently supported.", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * initialise the ExpandableListView on this activity
     */
    private void initialiseList() {
        myListView = (ExpandableListView) findViewById(R.id.myShoppingList); //find the expandable list view and set it so we can change it
        createExampleItemList(); //populate with pre-set data
        myAdapter = new CustomExpandableListAdapter(this, itemsList, itemCollections); //create the adapter for the list, holding all the data
        myListView.setAdapter(myAdapter);
        myListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                final String selected = (String) myAdapter.getChild(groupPosition, childPosition);
                Toast.makeText(getBaseContext(), selected, Toast.LENGTH_LONG).show();
                return true;
            }
        });
    }


    /**
     * Create an example list with different categories and sub-items
     */
    private void createExampleItemList() {

        itemsList = new ArrayList<String>();

        itemsList.add("Meats");
        List<String> Meats = new ArrayList<>();
        Meats.add("Chicken Breast");
        Meats.add("Beef Mince");

        itemsList.add("Vegetables");
        List<String> Vegetables = new ArrayList<>();
        Vegetables.add("Cucumber");
        Vegetables.add("Tomatoes");
        Vegetables.add("Onions");

        itemsList.add("Others");
        List<String> Others = new ArrayList<>();
        Others.add("Toothpaste");

        itemCollections = new LinkedHashMap<String, List<String>>();
        itemCollections.put(itemsList.get(0), Meats);
        itemCollections.put(itemsList.get(1), Vegetables);
        itemCollections.put(itemsList.get(2), Others);
    }

    private boolean checkIfDbExists(){
        try {
            File dbcheck = getApplicationContext().getDatabasePath("Shopping.db");
            if (dbcheck.exists()) {
                System.out.println("DB already exists.");
                Toast.makeText(this, "DB already exists", Toast.LENGTH_SHORT).show();
                return true;
            }
        } catch (NullPointerException nex) {
            System.out.println("Nullpointer when trying to find the existing db file");
            nex.printStackTrace();
            return false;
        }
        return false; //db does not exist
    }

    private boolean setupDatabase() {
        try {
            if(checkIfDbExists()){
                return true;
            }
            System.out.println("DB will be created.");
            Toast.makeText(this, "DB will be created.", Toast.LENGTH_LONG).show();
            myDbHelper = new MyDatabaseHelper(getBaseContext());
            myDb = myDbHelper.getWritableDatabase(); //Gets the data repository in write mode
            //Create a new map of values, where column names are keys
            ContentValues categoryValues = new ContentValues();
            categoryValues.put(DatabaseContract.CategoryFeeder.CATEGORIES_COLUMN, "Vegetables");
            categoryValues.put(DatabaseContract.CategoryFeeder.CATEGORIES_COLUMN, "Meats");
            categoryValues.put(DatabaseContract.CategoryFeeder.CATEGORIES_COLUMN, "Others");

            try {
                //Insert the new row, returning the primary key value of the new row
                myDb.insert(DatabaseContract.CategoryFeeder.TABLE_NAME, null, categoryValues);
            } catch (android.database.sqlite.SQLiteConstraintException ex) {
                System.out.println("Tried adding an existing row to a unique table");
                ex.printStackTrace();
            }
            ContentValues itemValues = new ContentValues();
            String query = "SELECT " + DatabaseContract.CategoryFeeder.CATEGORIES_COLUMN + " FROM " + DatabaseContract.CategoryFeeder.TABLE_NAME + " WHERE " + DatabaseContract.CategoryFeeder.CATEGORIES_COLUMN + " = ?";
            System.out.println(query);
            Cursor c = myDb.rawQuery(query, new String[]{"Meats"});
            if (c != null && c.moveToFirst()) {
                System.out.println(c.getString(1));
            }
            c.moveToFirst();
            itemValues.put(DatabaseContract.ItemTableFeeder.CATEGORY_ID, "Meats");
            itemValues.put(DatabaseContract.ItemTableFeeder.ITEM_NAME, "Beef");
            itemValues.put(DatabaseContract.ItemTableFeeder.ITEM_QUANTITY, 2);
            myDb.insert(DatabaseContract.ItemTableFeeder.TABLE_NAME, null, itemValues);
            c.close();

            String testquery = "SELECT " + DatabaseContract.ItemTableFeeder.ITEM_NAME + " FROM " + DatabaseContract.ItemTableFeeder.TABLE_NAME + " WHERE " + DatabaseContract.ItemTableFeeder.ITEM_NAME + " = 'Beef'";
            System.out.println(testquery);
            Cursor c1 = myDb.rawQuery(testquery, null);
            if(c1 != null && c1.moveToFirst()){
                //Toast.makeText(this, "Cursor c1 count: " + c1.getCount(), Toast.LENGTH_LONG).show();
                Toast.makeText(this, "Query for Beef returns: " + c1.getString(1), Toast.LENGTH_LONG).show();
            }

            c1.close();

            //System.out.println("New Row ID = " + newRowId);
            //Toast.makeText(this, "New Row ID = " + newRowId, Toast.LENGTH_SHORT).show();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


}
