package Adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import pojo.ShoppingCartResults;


/**
 * Created by pc on 4/12/15.
 */
public class DbHelper extends SQLiteOpenHelper
{
    public static final String DATABASE_NAME = "closebuy.db";
    public static final int VERSION = 1;

    public static final String TABLE_NAME = "ShoppingCart";
    public static final String PRODUCT_ID = "Product_id";
    public static final String PRODUCT_QUANTITY = "product_quantity";
    public static final String PRODUCT_PRICE="product_price";
    public static final String VENDOR_QTY="vendorqty";
    public static final String PRODUCT_NAME="productname";
    public static final String PRODUCT_IMAGE="productimage";
    public static final String SHOP_NAME="shopname";
    ShoppingCartResults obj;
    SQLiteDatabase mDb;

    String CREATE_TABLE = "create table " + TABLE_NAME + "("
            +PRODUCT_ID + " TEXT PRIMARY KEY,"
            +PRODUCT_QUANTITY + " INTEGER,"
            +PRODUCT_PRICE + " INTEGER,"
            +VENDOR_QTY + " INTEGER,"
            +PRODUCT_NAME + " TEXT,"
            +PRODUCT_IMAGE + " TEXT,"
            +SHOP_NAME + " TEXT"
            + ");";


    public DbHelper(Context context)
    {
        super(context, DATABASE_NAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db)
    {

        db.execSQL(CREATE_TABLE);
//        db.execSQL(CREATE_TABLE_HISTORY);
        Log.d("sharath", "Table created" + CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        this.onCreate(db);
    }

    public boolean insert(String pid , int qty, int price,int vqty,String name,String image,String shopname)
    {
        mDb = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(PRODUCT_ID,pid);
        cv.put(PRODUCT_QUANTITY, qty);
        cv.put(PRODUCT_PRICE,price);
        cv.put(VENDOR_QTY,vqty);
        cv.put(PRODUCT_NAME, name);
        cv.put(PRODUCT_IMAGE,image);
        cv.put(SHOP_NAME,shopname);
        long k=mDb.insert(TABLE_NAME, null, cv);
        Log.d("valueinserted-", "" + k);
        mDb.close(); // Closing database connection
        return true;
    }

    public int getProductQuantity(String pid) {
        mDb = this.getReadableDatabase();
        Cursor cursor;

        try {
            cursor = mDb.rawQuery("select " + "*" + " from " + TABLE_NAME + " where " + PRODUCT_ID + "=" + pid, null);
            if (cursor != null)
                cursor.moveToFirst();
            Log.d("akshay--",cursor.getString(0));
            return Integer.parseInt(cursor.getString(0));

        } catch (Exception e) {
            //Log.d("akshay--",e.toString());
           // e.printStackTrace();
        } finally {
            mDb.close();
        }
        return 0;
    }
    public ArrayList<ShoppingCartResults> getResults()
    {

        String[] columns=new String[]{PRODUCT_ID,PRODUCT_QUANTITY,PRODUCT_PRICE,VENDOR_QTY,PRODUCT_NAME,PRODUCT_IMAGE,SHOP_NAME};

        ArrayList<ShoppingCartResults> arraylist = new ArrayList<ShoppingCartResults>();

        mDb = this.getReadableDatabase();

        Cursor cur = mDb.query(TABLE_NAME, columns, null, null, null, null, null);

        for(cur.moveToLast();!cur.isBeforeFirst();cur.moveToPrevious())
        {
            String col= cur.getString(0);
            Integer col1= cur.getInt(1);
            Integer col2= cur.getInt(2);
            Integer col3= cur.getInt(3);
            String col4= cur.getString(4);
            String col5= cur.getString(5);
            String col6= cur.getString(6);
            obj = new ShoppingCartResults(col,col1,col2,col3,col4,col5,col6);


            arraylist.add(obj);
//        cur.close();
            mDb.close();
        }

        return arraylist;
    }

    public int delete(String id) {
        mDb = this.getWritableDatabase();
        mDb.delete(TABLE_NAME, PRODUCT_ID + " = ? ", new String[]{id});
        mDb.close();
        Log.d("akshay ", "DEleted ");
        return 0;
    }

    public void deleteAll()
    {
        mDb= this.getWritableDatabase();
        mDb.execSQL("delete from " + TABLE_NAME);
        mDb.close();
        Log.d("sg ", "table deleted ");
    }
    public  boolean CheckIsDataAlreadyInDBorNot(String fieldValue) {
       // SQLiteDatabase sqldb = EGLifeStyleApplication.sqLiteDatabase;
        mDb = this.getReadableDatabase();
        String Query = "Select * from " + TABLE_NAME + " where " + PRODUCT_ID + " = " + fieldValue;
        Cursor cursor = mDb.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;


        }
        cursor.close();
        return true;
    }

    public boolean updateqty (String pid,int qty) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(PRODUCT_QUANTITY, qty);
        db.update("ShoppingCart", cv, "Product_id = ? ", new String[] { (pid) } );
        return true;
    }


}
