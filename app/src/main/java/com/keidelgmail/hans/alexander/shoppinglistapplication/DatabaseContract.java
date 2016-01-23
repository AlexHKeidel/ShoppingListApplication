package com.keidelgmail.hans.alexander.shoppinglistapplication;

import android.provider.BaseColumns;

/**
 * Created by Borgelman on 19/01/2016.
 * followed tutorial on
 * http://developer.android.com/training/basics/data-storage/databases.html
 */
public final class DatabaseContract {
    //to prevent someone from accidentally instantiating the contract class give it an empty constructor
    public DatabaseContract(){}

    /*  inner class that defines the table contents
        BaseColumns interface gives this class a primary key field called _ID
    */
    public static abstract class CategoryFeeder implements BaseColumns{
        public static final String TABLE_NAME = "CategoryTable";
        public static final String CATEGORIES_COLUMN = "Category";
    }

    public static abstract  class ItemTableFeeder implements BaseColumns {
        public static final String TABLE_NAME = "ItemTable";
        public static final String CATEGORY_ID = "CategoryID"; //Foreign key
        public static final String ITEM_NAME = "Item";
        public static final String ITEM_QUANTITY = "Quantity";
    }
}
