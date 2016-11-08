package DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.dayle_fernandes.final_project.ProductInfo;
import com.google.android.gms.analytics.ecommerce.Product;

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
}
