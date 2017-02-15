package Adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.Blob;
import java.util.ArrayList;

import pojo.ShoppingCartResults;
import pojo.topcatresults;

/**
 * Created by GMSoft on 2/1/2017.
 */

public class DbHelpertopcat extends SQLiteOpenHelper
{
    public static final String DATABASE_NAME = "bhimartcategory.db";
    public static final int VERSION = 1;

    public static final String TABLE_NAME = "topcategory";
    public static final String CATEGORY_ID = "category_id";
    public static final String CATEGORY_NAME = "category_name";
    public static final String CATEGORY_IMG="category_img";
    public static final String LATITUDE="latitude";
    public static final String LANGTITUDE="langtitude";
    topcatresults obj;
    SQLiteDatabase mDb;

    String CREATE_TABLE = "create table " + TABLE_NAME + "("
            +CATEGORY_ID + " INTEGER,"
            +CATEGORY_NAME + " TEXT,"
            +CATEGORY_IMG + " TEXT,"
            +LATITUDE + " TEXT,"
            +LANGTITUDE + " TEXT"
            + ");";


    public DbHelpertopcat(Context context)
    {
        super(context, DATABASE_NAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db)
    {

        db.execSQL(CREATE_TABLE);
//        db.execSQL(CREATE_TABLE_HISTORY);
        Log.d("sharath1", "Table created" + CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        this.onCreate(db);
    }

    public boolean insertcat(int catid, String catname, String  catimg, String lat, String lang)
    {
        mDb = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CATEGORY_ID,catid);
        cv.put(CATEGORY_NAME, catname);
        cv.put(CATEGORY_IMG,catimg);
        cv.put(LATITUDE,lat);
        cv.put(LANGTITUDE,lang);
        long k=mDb.insert(TABLE_NAME, null, cv);
        Log.d("valueinserted-", "" + k);
        mDb.close(); // Closing database connection
        return true;
    }

    public  ArrayList<topcatresults> getResultscat()
    {

        String[] columns=new String[]{CATEGORY_ID,CATEGORY_NAME,CATEGORY_IMG,LATITUDE,LANGTITUDE};

        ArrayList<topcatresults> arraylist = new ArrayList<topcatresults>();

        mDb = this.getReadableDatabase();

        Cursor cur = mDb.query(TABLE_NAME, columns, null, null, null, null, CATEGORY_ID + " DESC");

        for(cur.moveToLast();!cur.isBeforeFirst();cur.moveToPrevious())
        {
            int col= cur.getInt(0);
            String col1= cur.getString(1);
            String col2= cur.getString(2);
            String col3=cur.getString(3);
            String col4=cur.getString(4);
            obj = new topcatresults(col,col1,col2,col3,col4);


            arraylist.add(obj);
//        cur.close();
            mDb.close();
        }

        return arraylist;
    }

    public int deletecat(String id) {
        mDb = this.getWritableDatabase();
        mDb.delete(TABLE_NAME, CATEGORY_ID + " = ? ", new String[]{id});
        mDb.close();
        Log.d("akshay ", "DEleted ");
        return 0;
    }

    public void deleteAllcat()
    {
        mDb= this.getWritableDatabase();
        mDb.execSQL("delete from " + TABLE_NAME);
        mDb.close();
        Log.d("sg ", "table deleted ");
    }
    public  boolean CheckIsDataAlreadyInDBorNotcat(String fieldValue) {
        // SQLiteDatabase sqldb = EGLifeStyleApplication.sqLiteDatabase;
        mDb = this.getReadableDatabase();
        String Query = "Select * from " + TABLE_NAME +" where " + CATEGORY_ID + " = " + fieldValue;
        Cursor cursor = mDb.rawQuery(Query, null);
        if(cursor.getCount() <= 0)
        {
            cursor.close();
            return false;


        }
        cursor.close();
        return true;
    }



}


