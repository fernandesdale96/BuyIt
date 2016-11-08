package DB;

/**
 * Created by aksharma2 on 08-11-2016.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ProductDBHandler extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "products.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_PRODUCTS = "products";
    public static final String COLUMN_ID = "prodId";
    public static final String COLUMN_NAME = "prodName";
    public static final String COLUMN_PRICE = "prodPrice";
    public static final String COLUMN_DISTANCE= "prodDist";
    public static final String COLUMN_STORE= "prodStore";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_PRODUCTS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_PRICE + " REAL, " +
                    COLUMN_DISTANCE + " REAL, " +
                    COLUMN_STORE + " TEXT, " +
                    ")";

    public ProductDBHandler(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+TABLE_PRODUCTS);
        db.execSQL(TABLE_CREATE);
    }

}
