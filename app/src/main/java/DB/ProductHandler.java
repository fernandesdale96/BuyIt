package DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.dayle_fernandes.final_project.ProductInfo;
import com.google.android.gms.analytics.ecommerce.Product;

import java.util.ArrayList;

/**
 * Created by aksharma2 on 08-11-2016.
 */
public class ProductHandler{

    SQLiteOpenHelper dbhandler;
    SQLiteDatabase database;
    public static final String LOGGER = "PROD_MGMT";

    private final String allColumns[]={
            ProductDBHandler.COLUMN_ID,
            ProductDBHandler.COLUMN_NAME,
            ProductDBHandler.COLUMN_PRICE,
            ProductDBHandler.COLUMN_DISTANCE,
            ProductDBHandler.COLUMN_STORE
    };

    public ProductHandler(Context context){
        dbhandler=new ProductDBHandler(context);
    }

    public void open() {
        Log.i(LOGGER, "Database Opened");
        database = dbhandler.getWritableDatabase();
    }

    public void close() {
        Log.i(LOGGER, "Database Closed");
        dbhandler.close();
    }

    public ProductInfo getProduct(long id){
        Cursor cursor = database.query(ProductDBHandler.TABLE_PRODUCTS,allColumns,ProductDBHandler.COLUMN_ID + "=?",new String[]{String.valueOf(id)},null,null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        ProductInfo pinfo=new ProductInfo(Long.parseLong(cursor.getString(0)), cursor.getString(1),Double.parseDouble(cursor.getString(2)), Double.parseDouble(cursor.getString(3)), cursor.getString(4));
        return pinfo;
    }

    public ProductInfo addProduct(ProductInfo pinfo){
        ContentValues values = new ContentValues();
        values.put(ProductDBHandler.COLUMN_NAME, pinfo.getName());
        values.put(ProductDBHandler.COLUMN_PRICE, pinfo.getPrice());
        values.put(ProductDBHandler.COLUMN_DISTANCE, pinfo.getDistance());
        values.put(ProductDBHandler.COLUMN_STORE, pinfo.getStore());
        long id = database.insert(ProductDBHandler.TABLE_PRODUCTS,null, values);
        pinfo.setId(id);
        return pinfo;
    }

    public ArrayList<ProductInfo> getProducts(){
        Cursor cursor = database.query(ProductDBHandler.TABLE_PRODUCTS,allColumns,null,null,null,null,null);
        ArrayList<ProductInfo>products=new ArrayList<>();
        if(cursor.getCount()>0){
            while(cursor.moveToNext()) {
                ProductInfo pinfo = new ProductInfo();
                pinfo.setId(cursor.getLong(cursor.getColumnIndex(ProductDBHandler.COLUMN_ID)));
                pinfo.setName(cursor.getString(cursor.getColumnIndex(ProductDBHandler.COLUMN_NAME)));
                pinfo.setPrice(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ProductDBHandler.COLUMN_PRICE))));
                pinfo.setDistance(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ProductDBHandler.COLUMN_DISTANCE))));
                pinfo.setStore(cursor.getString(cursor.getColumnIndex(ProductDBHandler.COLUMN_STORE)));
                products.add(pinfo);
            }
        }
        return products;
    }

}
