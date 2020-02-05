package com.example.srttata.appointmant.alerm;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.srttata.appointmant.alerm.modelclass.LocalArray;

import java.util.ArrayList;
import java.util.List;

public class LocalData  extends SQLiteOpenHelper {

    private static String DB_NAME = "sear";
    private static int DB_VERSION = 1;
    private static final String SERIAL_NO = "serial_no";
    private static final String TABLE_NAME = "search";
    private static final String TIME = "time";
    private static final String NAME = "name";
    private static final String ORDER_NO = "order_no";
    private static final String ID = "ID";

    private static final String SYNC = "sync";
    private static final String ADDRESS = "address";
    private static final String MOBILE_NUMBER = "mobile";
    public LocalData(Context context ) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME
                + "(" + SERIAL_NO +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME +
                " VARCHAR, " + TIME +
                " VARCHAR, " + ORDER_NO+
                " VARCHAR, " + ID +
                " VARCHAR, "+SYNC +
                " VARCHAR, "+ADDRESS +
                " VARCHAR, "+MOBILE_NUMBER +
                " VARCHAR "+
                ");";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);
    }

    public void addItems(String name, String time, String orderNo, String uniqueId, String value, String address, String mobileNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, name);
        contentValues.put(TIME,time);
        contentValues.put(ORDER_NO,orderNo);
        contentValues.put(ID,uniqueId);
        contentValues.put(SYNC,value);
        contentValues.put(ADDRESS,address);
        contentValues.put(MOBILE_NUMBER,mobileNumber);
        db.insert(TABLE_NAME, null, contentValues);
        db.close();
    }
    public List<LocalArray> getDetails(String id)
    {
        List<LocalArray> list = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                LocalArray array =new LocalArray();
                array.setTime(cursor.getString(2));
                array.setOrdernumber(cursor.getString(3));
                array.setName(cursor.getString(1));
                array.setId(cursor.getString(4));
                array.setValue(cursor.getString(5));
                array.setAddress(cursor.getString(6));
                array.setMobilenumber(cursor.getString(7));
                if (cursor.getString(4).equals(id))
                list.add(array);
            } while (cursor.moveToNext());
        }
        return list;
    }
    public List<LocalArray> getAllDetails()
    {
        List<LocalArray> list = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                LocalArray array =new LocalArray();
                array.setTime(cursor.getString(2));
                array.setOrdernumber(cursor.getString(3));
                array.setName(cursor.getString(1));
                array.setId(cursor.getString(4));
                array.setValue(cursor.getString(5));
                array.setAddress(cursor.getString(6));
                array.setMobilenumber(cursor.getString(7));
                if (cursor.getString(5).equals("false"))
                    list.add(array);

            } while (cursor.moveToNext());
        }
        return list;
    }
    void updateValue(String id, String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SYNC, value);
        db.update(TABLE_NAME, contentValues, ID + " = ?",
                new String[]{String.valueOf(id)});
    }
    public Cursor getData(String id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
//        String query = "SELECT * FROM search WHERE order_no = " + "'"+ id +"'" + " AND name = " + "'"+ name + "'";
        String query = "SELECT * FROM search WHERE ID = " + "'"+ id +"'";
        Cursor  cursor = db.rawQuery(query,null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
    int getIdForString(String str) {
        int res;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[] { SERIAL_NO,
                }, NAME + "=?",
                new String[] { str }, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            res = cursor.getInt(cursor.getColumnIndex(SERIAL_NO));
        }
        else {
            res = -1;
        }
        if (cursor != null) {
            cursor.close();
        }
        return res;
    }
    public Integer deleteId(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("search",
                "ID = ? ",
                new String[]{id});
    }

    public void deleteItem(String item) {
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = "ID=?";
        String[] whereArgs = {item};
        db.delete("order_no", whereClause, whereArgs);
    }
    public  void deleteTables()
    {

        SQLiteDatabase db = this.getWritableDatabase();
        String sql=  "DELETE FROM "+ TABLE_NAME ;
        db.execSQL(sql);
        db.close();

    }

}