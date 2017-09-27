package com.example.tabishhussain.hopeorbits.buyer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    Cursor c;
    boolean contain = false;
    String t_date, t_name;

    public DBHelper(Context context) {
        super(context, "MyDBName.db", null, 1);
    }

    String selectQuery;
    byte[] img;

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table add_cart " +
                        "(id integer primary key, pageId text,pageName text, catId text,catName text, productId text,productName text,quantity text,size text,price text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }

    public boolean addTocart(String PageId, String PageName, String CatId, String CatName, String ItemId, String ItemName, String Quantity, String Size, String Price) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("pageId", PageId);
        values.put("pageName", PageName);
        values.put("catId", CatId);
        values.put("catName", CatName);
        values.put("productId", ItemId);
        values.put("productName", ItemName);
        values.put("quantity", Quantity);
        values.put("size", Size);
        values.put("price", Price);
        db.insert("add_cart", null, values);

        return true;
    }

    public Cursor getallcartData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select pageName , pageId from add_cart", null);
        return res;
    }
    public Cursor getallcartItemData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from add_cart", null);
        return res;
    }


    public boolean add_employee(String F_name, String L_name, String E_mail, String Phone, String Join_date, String Designation, Bitmap Employee_pic, String Emp_Id) {
        if (Employee_pic != null) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            Employee_pic.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            img = bos.toByteArray();
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("F_name", F_name);
        values.put("L_name", L_name);
        values.put("E_mail", E_mail);
        values.put("Phone", Phone);
        values.put("Designation", Designation);
        values.put("Join_date", Join_date);
        values.put("Employee_pic", img);
        values.put("Employee_id", Emp_Id);
        db.insert("add_employee", null, values);

        return true;
    }

    public boolean insert_in_out_time(String Emp_id, String Time_in, String Time_out, String Date, String Designation) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("emp_id", Emp_id);
        values.put("time_in", Time_in);
        values.put("time_out", Time_out);
        values.put("date", Date);
        values.put("Designation", Designation);
        db.insert("employee_attandance", null, values);
        return true;
    }


    public boolean insert_holidays_list(String date, String name) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("date", date);
        values.put("name", name);

        c = getholidays();
        if (c != null) {
            c.moveToFirst();
        }
        if (c.getCount() > 0) {
            do {
                t_date = c.getString(c.getColumnIndex("date"));
                t_name = c.getString(c.getColumnIndex("name"));
                if ((date.equalsIgnoreCase(t_date) || (name.equalsIgnoreCase(t_name)))) {
                    contain = true;
                    break;
                } else if ((t_date.equalsIgnoreCase("")) && (!t_name.equalsIgnoreCase(""))) {
                    contain = true;
                    break;
                }
            } while (c.moveToNext());

            if (!contain) {
                db.insert("holidays_list", null, values);
            } else {
                if (!t_date.equalsIgnoreCase("")) {
                    db.update("holidays_list", values, "date='" + date + "'", null);
                } else {
                    db.update("holidays_list", values, "name='" + name + "'", null);
                }

                contain = false;
            }
        } else {
            db.insert("holidays_list", null, values);
        }


        return true;
    }


    public Cursor getallemployeeData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from add_employee", null);
        return res;
    }

    public Cursor get_emp_details(String Employee_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from add_employee where Employee_id=" + "'" + Employee_id + "'", null);
        return res;
    }


    public ArrayList<String> getallempid() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select Employee_id from add_employee ", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex("Employee_id")));
            res.moveToNext();
        }
        return array_list;
    }


    public Cursor get_emp_timing(String Employee_id, String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from employee_attandance where emp_id=" + "'" + Employee_id + "'" + "and date=" + "'" + date + "'", null);
        return res;
    }

    public boolean update_insert_in_out_time(String Employee_id, String Time_out) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("time_out", Time_out);
        db.update("employee_attandance", contentValues, "emp_id=" + Employee_id, null);
        return true;
    }

    public Cursor get_emp_atten_sheet(String Employee_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from employee_attandance where emp_id=" + "'" + Employee_id + "'", null);
        return res;
    }

    public Cursor getholidays() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from holidays_list", null);
        return res;
    }
//    public Cursor getallnewsData() {
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res = db.rawQuery("select * from holidays_list", null);
//        return res;
//    }
}