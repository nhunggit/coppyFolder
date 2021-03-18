package com.example.coppyfolder;

import android.content.Context;
import android.database.Cursor;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="secure.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME="TABLE_SECURE";
    public static final String COLUMN_PATH_ENCRYPT="PATH_ENCRYPT";
    public static final String COLUMN_PATH_DECRYPT="PATH_ENCRYPT";
    private static final String SQL_CREATE_TABLE_QUERY="CREATE TABLE " + TABLE_NAME+" ( " + COLUMN_PATH_ENCRYPT + " VARCHAR (255) NOT NULL, " + COLUMN_PATH_DECRYPT  + " VARCHAR (255) NOT NULL TEXT )";

    public Database(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

//    public List<Path> getAllProducts() {
//
//        List<Path> products = new ArrayList<>();
//
//
//        SQLiteDatabase db = new SQLiteDatabase()
//        Cursor cursor = db.rawQuery("SELECT id, name, price from product", null);
//
//        //Đến dòng đầu của tập dữ liệu
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()) {
//            int productID = cursor.getInt(0);
//            String productName = cursor.getString(1);
//            int productPrice = cursor.getInt(2);
//
//            products.add(new Product(productID, productName, productPrice));
//            cursor.moveToNext();
//        }
//
//        cursor.close();
//
//        return products;
//    }
//    //Lấy một SP biết ID
//    public Product getProductByID(int ID) {
//        Product product = null;
//        SQLiteDatabase db = getReadableDatabase();
//        Cursor cursor = db.rawQuery("SELECT id, name, price from product where id = ?",
//                new String[]{ID + ""});
//
//        if (cursor.getCount() > 0) {
//            cursor.moveToFirst();
//            int productID = cursor.getInt(0);
//            String productName = cursor.getString(1);
//            int productPrice = cursor.getInt(2);
//            product = new Product(productID, productName, productPrice);
//        }
//        cursor.close();
//        return product;
//    }
//
//    //Cập nhật
//    void updateProduct(Product product) {
//        SQLiteDatabase db = getWritableDatabase();
//        db.execSQL("UPDATE product SET name=?, price = ? where id = ?",
//                new String[]{product.name, product.price + "", product.productID + ""});
//    }
//
//    //Chèn mới
//    void insertProduct(Product product) {
//        SQLiteDatabase db = getWritableDatabase();
//        db.execSQL("INSERT INTO product (name, price ) VALUES (?,?)",
//                new String[]{product.name, product.price + ""});
//    }
//
//    //Xoá sản phẩm khỏi DB
//    void deleteProductByID(int ProductID) {
//        SQLiteDatabase db = getWritableDatabase();
//        db.execSQL("DELETE FROM product where id = ?", new String[]{String.valueOf(ProductID)});
//    }
}
