package co.hopeorbits.buyer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import co.hopeorbits.holder.StoreListHolder;

public class DBHelper extends SQLiteOpenHelper {
    Cursor c;
    boolean contain = false;
    String t_date, t_name;
    String itemId, itemQuantity;

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
                        "(id integer primary key, pageId text,pageName text, catId text,catName text, productId text,productName text,quantity text,size text,price text,itemImage text)"
        );


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }

    public boolean addTocart(String PageId, String PageName, String CatId, String CatName, String ItemId, String ItemName, String Quantity, String Size, String Price,String ItemImage) {
        c = getcartItemData(ItemId);
        int length = c.getCount();
        if (length > 0) {
            if (c != null) {
                c.moveToFirst();
            }
            do {
                try {

                    itemId = c.getString(c.getColumnIndex("productId"));
                    itemQuantity = c.getString(c.getColumnIndex("quantity"));
                    int quan = Integer.parseInt(Quantity) + Integer.parseInt(itemQuantity);
                    SQLiteDatabase db = this.getWritableDatabase();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("quantity", quan);
                    db.update("add_cart", contentValues, "productId='" + ItemId+"'", null);

                } catch (IndexOutOfBoundsException e) {

                } catch (OutOfMemoryError e) {

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (c.moveToNext());
        } else {
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
            values.put("itemImage", ItemImage);
            db.insert("add_cart", null, values);
        }
        return true;
    }

    public boolean update_cart(String itemId, String itemQuant,String itemPrice) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("quantity", itemQuant);
        contentValues.put("price", itemPrice);
        db.update("add_cart", contentValues, "productId='" + itemId+"'", null);
        return true;
    }
    public Cursor getcartItemData(String productId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select productId , quantity from add_cart where productId='" + productId + "'", null);
        return res;
    }


    public boolean addTocartdummy(String PageId, String PageName, String CatId, String CatName, String ItemId, String ItemName, String Quantity, String Size, String Price) {

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
        db.insert("add_cartdummy", null, values);

        return true;
    }

    public Cursor getallcartData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select pageName , pageId from add_cart group by pageId", null);
        return res;
    }

    public Cursor getallcartDataDummy() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select pageName , pageId from add_cartdummy group by pageId", null);
        return res;
    }

    public Cursor getallcartItemData(String pageId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from add_cart where pageId=" + "'" + pageId + "'", null);
        return res;
    }

    public Cursor getallcartItemDataDummy(String pageId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from add_cartdummy where pageId=" + "'" + pageId + "'", null);
        return res;
    }

    public boolean delete(ArrayList<StoreListHolder> selecteditemsId) {
        SQLiteDatabase db = this.getReadableDatabase();
        for (int i = 0; i < selecteditemsId.size(); i++) {
            db.delete("add_cart", "productId ='" + selecteditemsId.get(i).getItemID() + "'", null);
        }
        return true;
    }
    public boolean remove(ArrayList<String> selecteditems) {
        SQLiteDatabase db = this.getReadableDatabase();
        for (int i = 0; i < selecteditems.size(); i++) {
            db.delete("add_cart", "productId ='" + selecteditems.get(i) + "'", null);
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
        db.update("employee_attandance", contentValues, "emp_id='" + Employee_id+"'", null);
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