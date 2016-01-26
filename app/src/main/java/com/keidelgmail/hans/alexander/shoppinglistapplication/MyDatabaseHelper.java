package com.keidelgmail.hans.alexander.shoppinglistapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Borgelman on 19/01/2016.
 * See link for reference to the used tutorial
 * http://developer.android.com/training/basics/data-storage/databases.html#DbHelper
 */
public class MyDatabaseHelper extends SQLiteOpenHelper {
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_CATEGORY_ENTRIES = "CREATE TABLE " + DatabaseContract.CategoryFeeder.TABLE_NAME + " (" + DatabaseContract.CategoryFeeder._ID + " INTEGER PRIMARY KEY," + DatabaseContract.CategoryFeeder.CATEGORIES_COLUMN + TEXT_TYPE + " UNIQUE);";
    private static final String SQL_CREATE_ITEM_ENTRIES = "CREATE TABLE " + DatabaseContract.ItemTableFeeder.TABLE_NAME + " (" + DatabaseContract.ItemTableFeeder._ID +  " INTEGER PRIMARY KEY," + DatabaseContract.ItemTableFeeder.ITEM_NAME + TEXT_TYPE + COMMA_SEP + DatabaseContract.ItemTableFeeder.ITEM_QUANTITY + INTEGER_TYPE + COMMA_SEP + DatabaseContract.ItemTableFeeder.CATEGORY_ID + " INTEGER," + " FOREIGN KEY ( " + DatabaseContract.ItemTableFeeder.CATEGORY_ID + ") REFERENCES " + DatabaseContract.CategoryFeeder.TABLE_NAME + " (" + DatabaseContract.CategoryFeeder._ID + "));";
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + DatabaseContract.CategoryFeeder.TABLE_NAME + COMMA_SEP + DatabaseContract.ItemTableFeeder.TABLE_NAME;

    private static int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Shopping.db";

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public MyDatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_CATEGORY_ENTRIES); //create category table
        db.execSQL(SQL_CREATE_ITEM_ENTRIES); //create item table, make sure category table is created first as it gives a foreign key to the item table
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Upgrade policy at the moment is to delete all entries and start over
        db.execSQL(SQL_DELETE_ENTRIES); //delete all entries
        onCreate(db); //start over
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
        onUpgrade(db, oldVersion, newVersion); //just upgrade with new version number
    }
}
