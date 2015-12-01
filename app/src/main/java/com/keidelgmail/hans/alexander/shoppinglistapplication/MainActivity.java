package com.keidelgmail.hans.alexander.shoppinglistapplication;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

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
    //private CustomExpandableListAdapter myAdapter; //using custom class
    private TextView exampleTextView;

    private List<String> itemsList;
    private Map<String, List<String>> itemCollections;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialiseList();
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
    private void initialiseList(){
        myListView = (ExpandableListView) findViewById(R.id.myShoppingList); //find the expandable list view and set it so we can change it
        createExampleItemList();
        final ExpandableListAdapter myAdapter = new CustomExpandableListAdapter(this, itemsList, itemCollections);
        myListView.setAdapter(myAdapter);
        myListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                final String selected = (String) myAdapter.getChild(
                        groupPosition, childPosition);
                Toast.makeText(getBaseContext(), selected, Toast.LENGTH_LONG)
                        .show();

                return true;
            }
        });

    }

    private void createExampleItemList(){

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
}
