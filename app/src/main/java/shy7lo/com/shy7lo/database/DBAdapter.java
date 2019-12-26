package shy7lo.com.shy7lo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import shy7lo.com.shy7lo.model.Wishlist;
import shy7lo.com.shy7lo.utils.LogUtils;

public class DBAdapter {

    public static final String DATABASE_NAME = "shy7lo.db";
    static final int version = 3;
    private static final String TB_WISHLIST = "tb_wishlist";
    private static final String TB_SHOPPING = "tv_shopping";

    //wishlist
    private static final String DB_ID = "db_id";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String SKU = "sku";
    private static final String PRODUCTID = "productId";
    private static final String TYPEID = "typeId";
    private static final String QTY = "qty";
    private static final String PRICE = "price";
    private static final String SPECIAL_PRICE = "special_price";
    private static final String SPECIAL_FROM_DATE = "special_from_date";
    private static final String SPECIAL_TO_DATE = "special_to_date";
    private static final String IMAGE = "image";
    private static final String BRAND = "brand";
    private static final String RATING = "rating";
    private static final String ISGUEST = "isGuest";
    private static final String TOKEN = "token";
    private static final String SOFT_DELETE = "soft_delete";
    private static final String IS_ON_SERVER = "is_on_server";
    private static final String SIZE = "size";

    //Shopping
    private static final String ITEM_ID = "item_id";
    private static final String PRODUCT_TYPE = "product_type";
    private static final String STOCK_QTY = "stock_qty";
    private static final String STOCK_STATUS = "stock_status";
    private static final String PRICE_EXCL_TAX = "price_excl_tax";
    private static final String MSG = "msg";
    private static final String COLOR = "color";
    private static final String ATTRIBUTE_ID = "attribute_id";
    private static final String VALUE_INDEX = "value_index";
    private static final String OPTION_LABLE = "option_lable";
    private static final String OPTION_VALUE = "option_value";


    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    SimpleDateFormat displayDateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());

    private static final String WISHLIST_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "
            + TB_WISHLIST + "("
            + DB_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
            + ID + " TEXT,"
            + NAME + " TEXT,"
            + DESCRIPTION + " TEXT,"
            + SKU + " TEXT,"
            + PRODUCTID + " TEXT,"
            + TYPEID + " TEXT,"
            + QTY + " INTEGER,"
            + PRICE + " REAL,"
            + SPECIAL_PRICE + " REAL,"
            + SPECIAL_FROM_DATE + " TEXT,"
            + SPECIAL_TO_DATE + " TEXT,"
            + IMAGE + " TEXT,"
            + BRAND + " TEXT,"
            + RATING + " INTEGER,"
            + ISGUEST + " TEXT,"
            + TOKEN + " TEXT,"
            + SOFT_DELETE + " TEXT,"
            + IS_ON_SERVER + " TEXT,"
            + SIZE + " TEXT,"
            + STOCK_STATUS + " TEXT,"
            + STOCK_QTY + " INTEGER"
            + ");";


    private static Context mContext;
    private static DatabaseHelper DBHelper;
    static SQLiteDatabase db;

    public DBAdapter(Context ctx) {

        mContext = ctx;
//        DBHelper = new DatabaseHelper(mContext);
        DBHelper = DatabaseHelper.getInstance(mContext);

    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        private static DatabaseHelper mInstance = null;

        public static DatabaseHelper getInstance(Context ctx) {
            if (mInstance == null) {
//                mInstance = new DatabaseHelper(ctx.getApplicationContext());
                mInstance = new DatabaseHelper(ctx);
            }
            return mInstance;
        }

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            System.out.println("onCreate DatabaseHelper");

            try {

                if (!isDatabaseAvailable()) {
                    copyDataBase();
                }
                LogUtils.e("", "WISHLIST_CREATE_TABLE::" + WISHLIST_CREATE_TABLE);
                db.execSQL(WISHLIST_CREATE_TABLE);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w("vegicle", "upgrade database from version" + oldVersion
                    + "to" + newVersion + ",witch will distroy all data");
//            db.execSQL("DROP TABLE IF EXISTS '" + TB_WISHLIST + "'");
//            onCreate(db);
            if (newVersion == 2) {
                db.execSQL(WISHLIST_CREATE_TABLE);
            } else if (newVersion == 3) {
                db.execSQL("ALTER TABLE " + TB_WISHLIST + " ADD COLUMN " + STOCK_STATUS + " TEXT DEFAULT 1");
                db.execSQL("ALTER TABLE " + TB_WISHLIST + " ADD COLUMN " + STOCK_QTY + " INTEGER DEFAULT 1");
            }
        }
    }

    public static void copyDataBase() {
        try {

            getDatabaseFolder();

            OutputStream databaseOutputStream = new FileOutputStream(
                    getDatabaseFilePath());
            InputStream databaseInputStream = mContext.getAssets().open(
                    DATABASE_NAME);

            byte[] buffer = new byte[1024];
            while ((databaseInputStream.read(buffer)) > 0) {
                databaseOutputStream.write(buffer);
            }
            databaseInputStream.close();
            databaseOutputStream.flush();
            databaseOutputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void getDatabaseFolder() {
        String outputDatabasePath = getDatabaseFilePath().replace(
                DATABASE_NAME, "");
        File outputDatabaseFile = new File(outputDatabasePath);
        System.out.println("outputDatabaseFile::"
                + outputDatabaseFile.getAbsolutePath());
        if (!outputDatabaseFile.exists()) {
            outputDatabaseFile.mkdirs();
        }

    }

    public static boolean isDatabaseAvailable() {
        return mContext.getDatabasePath(DATABASE_NAME).exists();
    }

    public static String getDatabaseFilePath() {
        return mContext.getDatabasePath(DATABASE_NAME).getAbsolutePath();
    }

    public void open() throws SQLException {

        close();

        String mPath = mContext.getDatabasePath(DATABASE_NAME).getPath();
//        db = SQLiteDatabase.openDatabase(mPath, null,
//                SQLiteDatabase.CREATE_IF_NECESSARY);
//        db = SQLiteDatabase.openDatabase(mPath, null,
//                SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        db = DBHelper.getWritableDatabase();
    }

    public void close() {
        try {
            if (db != null) {
                db.close();
            } else {
                Log.e("", "db is null so not close");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // Project Query

    public boolean addWishItem(Wishlist.WishlistData mWishlistData) {
        LogUtils.e("", "addWishItem::" + mWishlistData.getSku());

        boolean addWishItem = false;
        open();

        ContentValues addRValues = new ContentValues();
        addRValues.put(ID, mWishlistData.getId());
        addRValues.put(NAME, mWishlistData.getName());
//        addRValues.put(DESCRIPTION, mWishlistData.getDescription());
        addRValues.put(DESCRIPTION, new Gson().toJson(mWishlistData.configurableAttributes));
        addRValues.put(SKU, mWishlistData.getSku());
        addRValues.put(PRODUCTID, mWishlistData.getProductId());
        addRValues.put(TYPEID, mWishlistData.getTypeId());
        addRValues.put(QTY, mWishlistData.getQty());
        addRValues.put(PRICE, mWishlistData.getPrice());
        addRValues.put(SPECIAL_PRICE, mWishlistData.getSpecial_price());
        addRValues.put(SPECIAL_FROM_DATE, mWishlistData.getSpecial_from_date());
        addRValues.put(SPECIAL_TO_DATE, mWishlistData.getSpecial_to_date());
        addRValues.put(IMAGE, mWishlistData.getThumbNail());
        addRValues.put(BRAND, mWishlistData.getBrand());
        addRValues.put(RATING, mWishlistData.getRating());
        addRValues.put(ISGUEST, mWishlistData.getIsGuest());
        addRValues.put(TOKEN, mWishlistData.getToken());
        addRValues.put(SOFT_DELETE, "0");
        addRValues.put(SIZE, mWishlistData.getSize());
        addRValues.put(IS_ON_SERVER, mWishlistData.getIs_on_server());
        addRValues.put(STOCK_STATUS, mWishlistData.getStockStatus());
//        addRValues.put(STOCK_QTY, mWishlistData.getStockQty());
        addRValues.put(STOCK_QTY, "1");

        try {

            Long addId = db.insert(TB_WISHLIST, "", addRValues);
            LogUtils.e("", "addId::" + addId + " Item:" + mWishlistData.getName() + " sku:" + mWishlistData.getSku());
            if (addId == -1) {
                addWishItem = false;
            } else {
                addWishItem = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }

        return addWishItem;
    }

    public boolean updateWishItem(Wishlist.WishlistData mWishlistData) {
        LogUtils.e("", "updateWishItem::" + mWishlistData.getSku());
        boolean updateWishItem = false;
        open();

        ContentValues addRValues = new ContentValues();
        addRValues.put(ID, mWishlistData.getId());
        addRValues.put(NAME, mWishlistData.getName());
//        addRValues.put(DESCRIPTION, mWishlistData.getDescription());
        addRValues.put(DESCRIPTION, new Gson().toJson(mWishlistData.configurableAttributes));
//        addRValues.put(SKU, mWishlistData.getSku());
        addRValues.put(PRODUCTID, mWishlistData.getProductId());
        addRValues.put(TYPEID, mWishlistData.getTypeId());
        addRValues.put(QTY, mWishlistData.getQty());
        addRValues.put(PRICE, mWishlistData.getPrice());
        addRValues.put(SPECIAL_PRICE, mWishlistData.getSpecial_price());
        addRValues.put(SPECIAL_FROM_DATE, mWishlistData.getSpecial_from_date());
        addRValues.put(SPECIAL_TO_DATE, mWishlistData.getSpecial_to_date());
        addRValues.put(IMAGE, mWishlistData.getThumbNail());
        addRValues.put(BRAND, mWishlistData.getBrand());
        addRValues.put(RATING, mWishlistData.getRating());
        addRValues.put(ISGUEST, mWishlistData.getIsGuest());
        addRValues.put(TOKEN, mWishlistData.getToken());
        addRValues.put(STOCK_STATUS, mWishlistData.getStockStatus());
//        addRValues.put(STOCK_QTY, mWishlistData.getStockQty());
        addRValues.put(STOCK_QTY, "1");

        try {

            int updateId = db.update(TB_WISHLIST, addRValues, SKU + " = ?",
                    new String[]{mWishlistData.getSku()});
            LogUtils.e("", "updateId::" + updateId + " Item:" + mWishlistData.getName() + " sku:" + mWishlistData.getSku());
            if (updateId == -1) {
                updateWishItem = false;
            } else {
                updateWishItem = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }

        return updateWishItem;

    }

    public boolean updateTokenWishlist(String token) {

        boolean updateWishItem = false;
        open();

        ContentValues addRValues = new ContentValues();
        addRValues.put(TOKEN, token);

        try {

            int updateId = db.update(TB_WISHLIST, addRValues, ISGUEST + " = ?",
                    new String[]{"1"});
            LogUtils.e("", "updateId::");
            if (updateId == -1) {
                updateWishItem = false;
            } else {
                updateWishItem = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }

        return updateWishItem;
    }

    public ArrayList<Wishlist.WishlistData> getAllWishList() {

        ArrayList<Wishlist.WishlistData> alWishlist = new ArrayList<Wishlist.WishlistData>();

        open();

        String query = "SELECT * FROM " + TB_WISHLIST + " WHERE " + SOFT_DELETE + " != '1'";
        LogUtils.e("", "query::" + query);
        Cursor cursor = db.rawQuery(query, null);

        try {

            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                Wishlist.WishlistData mWishlistData;
                do {

                    mWishlistData = new Wishlist().new WishlistData();
                    mWishlistData.setId(cursor.getString(cursor
                            .getColumnIndex(ID)));
                    mWishlistData.setName(cursor.getString(cursor
                            .getColumnIndex(NAME)));
                    mWishlistData.setDescription(cursor.getString(cursor
                            .getColumnIndex(DESCRIPTION)));
                    mWishlistData.setSku(cursor.getString(cursor
                            .getColumnIndex(SKU)));
                    mWishlistData.setProductId(cursor.getString(cursor
                            .getColumnIndex(PRODUCTID)));
                    mWishlistData.setTypeId(cursor.getString(cursor
                            .getColumnIndex(TYPEID)));
                    mWishlistData.setQty(cursor.getInt(cursor
                            .getColumnIndex(QTY)));
                    mWishlistData.setPrice(cursor.getFloat(cursor
                            .getColumnIndex(PRICE)));
                    mWishlistData.setSpecial_price(cursor.getFloat(cursor
                            .getColumnIndex(SPECIAL_PRICE)));
                    mWishlistData.setSpecial_from_date(cursor.getString(cursor
                            .getColumnIndex(SPECIAL_FROM_DATE)));
                    mWishlistData.setSpecial_to_date(cursor.getString(cursor
                            .getColumnIndex(SPECIAL_TO_DATE)));
                    mWishlistData.setThumbNail(cursor.getString(cursor
                            .getColumnIndex(IMAGE)));
                    mWishlistData.setBrand(cursor.getString(cursor
                            .getColumnIndex(BRAND)));
                    mWishlistData.setRating(cursor.getInt(cursor
                            .getColumnIndex(RATING)));
                    mWishlistData.setRating(cursor.getInt(cursor
                            .getColumnIndex(RATING)));
                    mWishlistData.setIsGuest(cursor.getString(cursor
                            .getColumnIndex(ISGUEST)));
                    mWishlistData.setToken(cursor.getString(cursor
                            .getColumnIndex(TOKEN)));
                    mWishlistData.setToken(cursor.getString(cursor
                            .getColumnIndex(TOKEN)));
                    mWishlistData.setSoft_delete(cursor.getString(cursor
                            .getColumnIndex(SOFT_DELETE)));
                    mWishlistData.setSize(cursor.getString(cursor
                            .getColumnIndex(SIZE)));
                    mWishlistData.setStockStatus(cursor.getString(cursor
                            .getColumnIndex(STOCK_STATUS)));
                    mWishlistData.setStockQty(cursor.getInt(cursor
                            .getColumnIndex(STOCK_QTY)));


                    alWishlist.add(mWishlistData);
                } while (cursor.moveToNext());
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            close();
        }

        LogUtils.e("", "getAllWishList size::" + alWishlist.size());


        return alWishlist;

    }

    public ArrayList<Wishlist.WishlistData> getGeustWishList() {

        ArrayList<Wishlist.WishlistData> alWishlist = new ArrayList<Wishlist.WishlistData>();

        open();

        String query = "SELECT * FROM " + TB_WISHLIST + " WHERE " + ISGUEST + " = '1' AND " + SOFT_DELETE + " != '1'";
        LogUtils.e("", "query::" + query);
        Cursor cursor = db.rawQuery(query, null);

        try {

            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                Wishlist.WishlistData mWishlistData;
                do {

                    mWishlistData = new Wishlist().new WishlistData();
                    mWishlistData.setId(cursor.getString(cursor
                            .getColumnIndex(ID)));
                    mWishlistData.setName(cursor.getString(cursor
                            .getColumnIndex(NAME)));
                    mWishlistData.setDescription(cursor.getString(cursor
                            .getColumnIndex(DESCRIPTION)));
                    mWishlistData.setSku(cursor.getString(cursor
                            .getColumnIndex(SKU)));
                    mWishlistData.setProductId(cursor.getString(cursor
                            .getColumnIndex(PRODUCTID)));
                    mWishlistData.setTypeId(cursor.getString(cursor
                            .getColumnIndex(TYPEID)));
                    mWishlistData.setQty(cursor.getInt(cursor
                            .getColumnIndex(QTY)));
                    mWishlistData.setPrice(cursor.getFloat(cursor
                            .getColumnIndex(PRICE)));
                    mWishlistData.setSpecial_price(cursor.getFloat(cursor
                            .getColumnIndex(SPECIAL_PRICE)));
                    mWishlistData.setSpecial_from_date(cursor.getString(cursor
                            .getColumnIndex(SPECIAL_FROM_DATE)));
                    mWishlistData.setSpecial_to_date(cursor.getString(cursor
                            .getColumnIndex(SPECIAL_TO_DATE)));
                    mWishlistData.setThumbNail(cursor.getString(cursor
                            .getColumnIndex(IMAGE)));
                    mWishlistData.setBrand(cursor.getString(cursor
                            .getColumnIndex(BRAND)));
                    mWishlistData.setRating(cursor.getInt(cursor
                            .getColumnIndex(RATING)));
                    mWishlistData.setRating(cursor.getInt(cursor
                            .getColumnIndex(RATING)));
                    mWishlistData.setIsGuest(cursor.getString(cursor
                            .getColumnIndex(ISGUEST)));
                    mWishlistData.setToken(cursor.getString(cursor
                            .getColumnIndex(TOKEN)));
                    mWishlistData.setToken(cursor.getString(cursor
                            .getColumnIndex(TOKEN)));
                    mWishlistData.setSoft_delete(cursor.getString(cursor
                            .getColumnIndex(SOFT_DELETE)));
                    mWishlistData.setSize(cursor.getString(cursor
                            .getColumnIndex(SIZE)));
                    mWishlistData.setStockStatus(cursor.getString(cursor
                            .getColumnIndex(STOCK_STATUS)));
                    mWishlistData.setStockQty(cursor.getInt(cursor
                            .getColumnIndex(STOCK_QTY)));


                    alWishlist.add(mWishlistData);
                } while (cursor.moveToNext());
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            close();
        }

        LogUtils.e("", "getGeustWishList size::" + alWishlist.size());


        return alWishlist;

    }

    public ArrayList<Wishlist.WishlistData> getLocalWishList() {

        ArrayList<Wishlist.WishlistData> alWishlist = new ArrayList<Wishlist.WishlistData>();

        open();

        String query = "SELECT * FROM " + TB_WISHLIST + " WHERE " + IS_ON_SERVER + " = '0' AND " + SOFT_DELETE + " != '1'";
        LogUtils.e("", "query::" + query);
        Cursor cursor = db.rawQuery(query, null);

        try {

            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                Wishlist.WishlistData mWishlistData;
                do {

                    mWishlistData = new Wishlist().new WishlistData();
                    mWishlistData.setId(cursor.getString(cursor
                            .getColumnIndex(ID)));
                    mWishlistData.setName(cursor.getString(cursor
                            .getColumnIndex(NAME)));
                    mWishlistData.setDescription(cursor.getString(cursor
                            .getColumnIndex(DESCRIPTION)));
                    mWishlistData.setSku(cursor.getString(cursor
                            .getColumnIndex(SKU)));
                    mWishlistData.setProductId(cursor.getString(cursor
                            .getColumnIndex(PRODUCTID)));
                    mWishlistData.setTypeId(cursor.getString(cursor
                            .getColumnIndex(TYPEID)));
                    mWishlistData.setQty(cursor.getInt(cursor
                            .getColumnIndex(QTY)));
                    mWishlistData.setPrice(cursor.getFloat(cursor
                            .getColumnIndex(PRICE)));
                    mWishlistData.setSpecial_price(cursor.getFloat(cursor
                            .getColumnIndex(SPECIAL_PRICE)));
                    mWishlistData.setSpecial_from_date(cursor.getString(cursor
                            .getColumnIndex(SPECIAL_FROM_DATE)));
                    mWishlistData.setSpecial_to_date(cursor.getString(cursor
                            .getColumnIndex(SPECIAL_TO_DATE)));
                    mWishlistData.setThumbNail(cursor.getString(cursor
                            .getColumnIndex(IMAGE)));
                    mWishlistData.setBrand(cursor.getString(cursor
                            .getColumnIndex(BRAND)));
                    mWishlistData.setRating(cursor.getInt(cursor
                            .getColumnIndex(RATING)));
                    mWishlistData.setRating(cursor.getInt(cursor
                            .getColumnIndex(RATING)));
                    mWishlistData.setIsGuest(cursor.getString(cursor
                            .getColumnIndex(ISGUEST)));
                    mWishlistData.setToken(cursor.getString(cursor
                            .getColumnIndex(TOKEN)));
                    mWishlistData.setToken(cursor.getString(cursor
                            .getColumnIndex(TOKEN)));
                    mWishlistData.setSoft_delete(cursor.getString(cursor
                            .getColumnIndex(SOFT_DELETE)));
                    mWishlistData.setSize(cursor.getString(cursor
                            .getColumnIndex(SIZE)));
                    mWishlistData.setStockStatus(cursor.getString(cursor
                            .getColumnIndex(STOCK_STATUS)));
                    mWishlistData.setStockQty(cursor.getInt(cursor
                            .getColumnIndex(STOCK_QTY)));


                    alWishlist.add(mWishlistData);
                } while (cursor.moveToNext());
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            close();
        }

        LogUtils.e("", "getLocalWishList size::" + alWishlist.size());


        return alWishlist;

    }

    public ArrayList<Wishlist.WishlistData> getLocalSoftDeleteWishList() {

        ArrayList<Wishlist.WishlistData> alWishlist = new ArrayList<Wishlist.WishlistData>();

        open();

        String query = "SELECT * FROM " + TB_WISHLIST + " WHERE " + ID + " != '" + PRODUCTID + "' AND " + SOFT_DELETE + " = '1'";
        LogUtils.e("", "query::" + query);
        Cursor cursor = db.rawQuery(query, null);

        try {

            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                Wishlist.WishlistData mWishlistData;
                do {

                    mWishlistData = new Wishlist().new WishlistData();
                    mWishlistData.setId(cursor.getString(cursor
                            .getColumnIndex(ID)));
                    mWishlistData.setName(cursor.getString(cursor
                            .getColumnIndex(NAME)));
                    mWishlistData.setDescription(cursor.getString(cursor
                            .getColumnIndex(DESCRIPTION)));
                    mWishlistData.setSku(cursor.getString(cursor
                            .getColumnIndex(SKU)));
                    mWishlistData.setProductId(cursor.getString(cursor
                            .getColumnIndex(PRODUCTID)));
                    mWishlistData.setTypeId(cursor.getString(cursor
                            .getColumnIndex(TYPEID)));
                    mWishlistData.setQty(cursor.getInt(cursor
                            .getColumnIndex(QTY)));
                    mWishlistData.setPrice(cursor.getFloat(cursor
                            .getColumnIndex(PRICE)));
                    mWishlistData.setSpecial_price(cursor.getFloat(cursor
                            .getColumnIndex(SPECIAL_PRICE)));
                    mWishlistData.setSpecial_from_date(cursor.getString(cursor
                            .getColumnIndex(SPECIAL_FROM_DATE)));
                    mWishlistData.setSpecial_to_date(cursor.getString(cursor
                            .getColumnIndex(SPECIAL_TO_DATE)));
                    mWishlistData.setThumbNail(cursor.getString(cursor
                            .getColumnIndex(IMAGE)));
                    mWishlistData.setBrand(cursor.getString(cursor
                            .getColumnIndex(BRAND)));
                    mWishlistData.setRating(cursor.getInt(cursor
                            .getColumnIndex(RATING)));
                    mWishlistData.setRating(cursor.getInt(cursor
                            .getColumnIndex(RATING)));
                    mWishlistData.setIsGuest(cursor.getString(cursor
                            .getColumnIndex(ISGUEST)));
                    mWishlistData.setToken(cursor.getString(cursor
                            .getColumnIndex(TOKEN)));
                    mWishlistData.setToken(cursor.getString(cursor
                            .getColumnIndex(TOKEN)));
                    mWishlistData.setSoft_delete(cursor.getString(cursor
                            .getColumnIndex(SOFT_DELETE)));
                    mWishlistData.setSize(cursor.getString(cursor
                            .getColumnIndex(SIZE)));
                    mWishlistData.setStockStatus(cursor.getString(cursor
                            .getColumnIndex(STOCK_STATUS)));
                    mWishlistData.setStockQty(cursor.getInt(cursor
                            .getColumnIndex(STOCK_QTY)));


                    alWishlist.add(mWishlistData);
                } while (cursor.moveToNext());
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            close();
        }

        LogUtils.e("", "getLocalSoftDeleteWishList size::" + alWishlist.size());


        return alWishlist;

    }

    public boolean updateSoftDeleteWishItem(String sku, String soft_delete) {

        boolean updateWishItem = false;
        open();

        ContentValues addRValues = new ContentValues();
        addRValues.put(SOFT_DELETE, soft_delete);

        try {

            int updateId = db.update(TB_WISHLIST, addRValues, SKU + " = ?",
                    new String[]{sku});
            LogUtils.e("", "updateId::" + updateId);
            if (updateId == -1) {
                updateWishItem = false;
            } else {
                updateWishItem = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }

        return updateWishItem;

    }

    public boolean updateIsOnServerWishItem(String sku, String is_on_server) {

        boolean updateWishItem = false;
        open();

        ContentValues addRValues = new ContentValues();
        addRValues.put(IS_ON_SERVER, is_on_server);

        try {

            int updateId = db.update(TB_WISHLIST, addRValues, SKU + " = ?",
                    new String[]{sku});
            LogUtils.e("", "updateId::" + updateId);
            if (updateId == -1) {
                updateWishItem = false;
            } else {
                updateWishItem = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }

        return updateWishItem;
    }

    public int getWishListCounter() {

        int mWishlistCounter = 0;

        open();
        String query = "SELECT * FROM " + TB_WISHLIST + " WHERE " + SOFT_DELETE + " != '1'";
        Cursor cursor = db.rawQuery(query, null);

        try {

            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                mWishlistCounter = cursor.getCount();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            close();
        }

        return mWishlistCounter;
    }

    public boolean updateGuestWishItem(String sku, String isGuest, String token, String is_on_server) {

        boolean updateWishItem = false;
        open();

        ContentValues addRValues = new ContentValues();
        addRValues.put(ISGUEST, isGuest);
        addRValues.put(TOKEN, token);
        addRValues.put(IS_ON_SERVER, is_on_server);

        try {

            int updateId = db.update(TB_WISHLIST, addRValues, SKU + " = ?",
                    new String[]{sku});
            LogUtils.e("", "updateId::" + updateId);
            if (updateId == -1) {
                updateWishItem = false;
            } else {
                updateWishItem = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }

        return updateWishItem;
    }

    public boolean updateItemSize(String sku, String size) {
        LogUtils.e("", "sku: " + sku + " size::" + size);
        boolean updateWishItem = false;
        open();

        ContentValues addRValues = new ContentValues();
        addRValues.put(SIZE, size);

        try {

            int updateId = db.update(TB_WISHLIST, addRValues, SKU + " = ?",
                    new String[]{sku});
            LogUtils.e("", "updateId::" + updateId);
            if (updateId == -1) {
                updateWishItem = false;
            } else {
                updateWishItem = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }

        return updateWishItem;
    }

    public boolean removeWishItem(String sku) {

        boolean isWishItemDeleted = false;

        open();

        try {

            int rowAffected = db.delete(TB_WISHLIST, SKU + " = ?",
                    new String[]{sku});
            Log.e("", "rowAffected:::" + rowAffected);
            if (rowAffected > 0) {
                isWishItemDeleted = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }

        return isWishItemDeleted;

    }

    public boolean removeAllWishItem() {

        boolean isWishListCleared = false;

        open();

        try {

            int rowAffected = db.delete(TB_WISHLIST, "1", null);
            Log.e("", "rowAffected:::" + rowAffected);
            if (rowAffected > 0) {
                isWishListCleared = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }

        return isWishListCleared;

    }

    public boolean isContainInWishList(String sku) {

        boolean isWishItemFound = false;

        open();
        String query = "SELECT * FROM " + TB_WISHLIST + " WHERE SKU = '" + sku + "'";
        LogUtils.e("", "query::" + query);
        Cursor cursor = db.rawQuery(query, null);
        try {


            if (cursor != null && cursor.getCount() > 0) {
                isWishItemFound = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            close();
        }
        LogUtils.e("", "isWishItemFound::" + isWishItemFound);
        return isWishItemFound;
    }

    public boolean isContainInWishListWithoutSoftDelete(String sku) {

        boolean isWishItemFound = false;

        open();

        String query = "SELECT * FROM " + TB_WISHLIST + " WHERE SKU = '" + sku + "' AND " + SOFT_DELETE + " != '1'";
        LogUtils.e("", "query::" + query);
        Cursor cursor = db.rawQuery(query, null);

        try {

            if (cursor != null && cursor.getCount() > 0) {
                isWishItemFound = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            close();
        }
        LogUtils.e("", "isWishItemFound::" + isWishItemFound);
        return isWishItemFound;
    }

    public boolean isGuestWishItem(String sku) {

        boolean isWishItemFound = false;

        open();

        String query = "SELECT * FROM " + TB_WISHLIST + " WHERE SKU = '" + sku + "' AND " + ISGUEST + " = '1'";
        LogUtils.e("", "query::" + query);
        Cursor cursor = db.rawQuery(query, null);

        try {

            if (cursor != null && cursor.getCount() > 0) {
                isWishItemFound = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            close();
        }
        LogUtils.e("", "isWishItemFound::" + isWishItemFound);
        return isWishItemFound;
    }

    public boolean isWishItemOnServer(String sku) {

        boolean isWishItemFound = false;

        open();
        String query = "SELECT * FROM " + TB_WISHLIST + " WHERE SKU = '" + sku + "' AND " + IS_ON_SERVER + " = '1'";
        LogUtils.e("", "query::" + query);
        Cursor cursor = db.rawQuery(query, null);
        try {

            if (cursor != null && cursor.getCount() > 0) {
                isWishItemFound = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            close();
        }
        LogUtils.e("", "isWishItemFound::" + isWishItemFound);
        return isWishItemFound;
    }


    // Shopping
    // Project Query

//    public boolean addShoppingItemFromDetails(ShoppingBag.Item mShoppingItem) {
//
//        String mSku = TextUtils.isEmpty(mShoppingItem.getParent_sku()) ? mShoppingItem.getSku() : mShoppingItem.getParent_sku();
//
//        boolean addShoppingItem = false;
//        open();
//
//        ContentValues addRValues = new ContentValues();
//        addRValues.put(ITEM_ID, mShoppingItem.getItemId());
//        addRValues.put(NAME, mShoppingItem.getName());
//        addRValues.put(BRAND, mShoppingItem.getBrand());
//        addRValues.put(IMAGE, mShoppingItem.getImageFIle());
//        addRValues.put(SKU, mSku);
//        addRValues.put(PRODUCT_TYPE, mShoppingItem.getProductType());
//        addRValues.put(PRICE, mShoppingItem.getPrice());
//        addRValues.put(SPECIAL_PRICE, mShoppingItem.getSpecial_price());
//        addRValues.put(SPECIAL_FROM_DATE, mShoppingItem.getSpecial_from_date());
//        addRValues.put(SPECIAL_TO_DATE, mShoppingItem.getSpecial_to_date());
//        addRValues.put(PRICE_EXCL_TAX, mShoppingItem.getPrice_excl_tax());
//        addRValues.put(QTY, mShoppingItem.getQty());
//        addRValues.put(STOCK_QTY, mShoppingItem.getStockQty());
//        addRValues.put(STOCK_STATUS, mShoppingItem.getStockStatus());
//        addRValues.put(ATTRIBUTE_ID, mShoppingItem.getAttribute_id());
//        addRValues.put(VALUE_INDEX, mShoppingItem.getValue_index());
//        addRValues.put(MSG, mShoppingItem.getCustom_msg().getMsg());
//        addRValues.put(COLOR, mShoppingItem.getCustom_msg().getColor());
//        addRValues.put(OPTION_LABLE, "Size");
//        addRValues.put(OPTION_VALUE, mShoppingItem.getOption_value());
//        addRValues.put(ISGUEST, mShoppingItem.getIsGuest());
//        addRValues.put(TOKEN, mShoppingItem.getToken());
//        addRValues.put(SOFT_DELETE, "0");
//        addRValues.put(IS_ON_SERVER, mShoppingItem.getIs_on_server());
//
//        try {
//
//            Long addId = db.insert(TB_SHOPPING, "", addRValues);
//            LogUtils.e("", "addShoppingItemID::" + addId + " Item:" + mShoppingItem.getName() + " sku:" + mSku);
//            if (addId == -1) {
//                addShoppingItem = false;
//            } else {
//                addShoppingItem = true;
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            close();
//        }
//
//        return addShoppingItem;
//    }
//
//    public boolean addShoppingItemFromServer(ShoppingBag.Item mShoppingItem) {
//
//        String mSku = TextUtils.isEmpty(mShoppingItem.getParent_sku()) ? mShoppingItem.getSku() : mShoppingItem.getParent_sku();
//
//        if (isSoftDeleteShoppingItem(mSku,
//                mShoppingItem.getProductType(), mShoppingItem.getOption_value())) {
//            return false;
//        }
//
//        boolean addShoppingItem = false;
//        open();
//
//        ContentValues addRValues = new ContentValues();
//        addRValues.put(ITEM_ID, mShoppingItem.getItemId());
//        addRValues.put(NAME, mShoppingItem.getName());
//        addRValues.put(BRAND, mShoppingItem.getBrand());
//        addRValues.put(IMAGE, mShoppingItem.getImageFIle());
//        addRValues.put(SKU, mSku);
//        addRValues.put(PRODUCT_TYPE, mShoppingItem.getProductType());
//        addRValues.put(PRICE, mShoppingItem.getPrice());
//        addRValues.put(SPECIAL_PRICE, mShoppingItem.getSpecial_price());
//        addRValues.put(SPECIAL_FROM_DATE, mShoppingItem.getSpecial_from_date());
//        addRValues.put(SPECIAL_TO_DATE, mShoppingItem.getSpecial_to_date());
//        addRValues.put(PRICE_EXCL_TAX, mShoppingItem.getPrice_excl_tax());
//        addRValues.put(QTY, mShoppingItem.getQty());
//        addRValues.put(STOCK_QTY, mShoppingItem.getStockQty());
//        addRValues.put(STOCK_STATUS, mShoppingItem.getStockStatus());
//        addRValues.put(ATTRIBUTE_ID, mShoppingItem.getAttribute_id());
//        addRValues.put(VALUE_INDEX, mShoppingItem.getValue_index());
//        if (mShoppingItem.getCustom_msg() != null) {
//            addRValues.put(MSG, mShoppingItem.getCustom_msg().getMsg());
//            addRValues.put(COLOR, mShoppingItem.getCustom_msg().getColor());
//        }
//        addRValues.put(OPTION_LABLE, "Size");
//        addRValues.put(OPTION_VALUE, mShoppingItem.getOption_value());
//        addRValues.put(ISGUEST, mShoppingItem.getIsGuest());
//        addRValues.put(TOKEN, mShoppingItem.getToken());
//        addRValues.put(SOFT_DELETE, "0");
//        addRValues.put(IS_ON_SERVER, mShoppingItem.getIs_on_server());
//
//        try {
//
//            Long addId = db.insert(TB_SHOPPING, "", addRValues);
//            LogUtils.e("", "addShoppingItemID::" + addId + " Item:" + mShoppingItem.getName() + " sku:" + mSku);
//            if (addId == -1) {
//                addShoppingItem = false;
//            } else {
//                addShoppingItem = true;
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            close();
//        }
//
//        return addShoppingItem;
//    }
//
//
//    public boolean updateShoppingItemFromServer(ShoppingBag.Item mShoppingItem, String productType, String mSizeLable) {
//
//        String mSku = TextUtils.isEmpty(mShoppingItem.getParent_sku()) ? mShoppingItem.getSku() : mShoppingItem.getParent_sku();
//
//        boolean updateShoppingItem = false;
//        open();
//
//        ContentValues addRValues = new ContentValues();
//        addRValues.put(ITEM_ID, mShoppingItem.getItemId());
//        addRValues.put(NAME, mShoppingItem.getName());
//        addRValues.put(BRAND, mShoppingItem.getBrand());
//        addRValues.put(IMAGE, mShoppingItem.getImageFIle());
//        addRValues.put(PRICE, mShoppingItem.getPrice());
//        addRValues.put(SPECIAL_PRICE, mShoppingItem.getSpecial_price());
//        addRValues.put(SPECIAL_FROM_DATE, mShoppingItem.getSpecial_from_date());
//        addRValues.put(SPECIAL_TO_DATE, mShoppingItem.getSpecial_to_date());
//        addRValues.put(PRICE_EXCL_TAX, mShoppingItem.getPrice_excl_tax());
//        addRValues.put(QTY, mShoppingItem.getQty());
//        addRValues.put(STOCK_QTY, mShoppingItem.getStockQty());
//        addRValues.put(STOCK_STATUS, mShoppingItem.getStockStatus());
//        if (mShoppingItem.getCustom_msg() != null) {
//            addRValues.put(MSG, mShoppingItem.getCustom_msg().getMsg());
//            addRValues.put(COLOR, mShoppingItem.getCustom_msg().getColor());
//        }
//        addRValues.put(ISGUEST, mShoppingItem.getIsGuest());
//        addRValues.put(TOKEN, mShoppingItem.getToken());
//        addRValues.put(IS_ON_SERVER, mShoppingItem.getIs_on_server());
//
//        try {
//            int updateId;
//            if (productType.equalsIgnoreCase("simple")) {
//                updateId = db.update(TB_SHOPPING, addRValues, SKU + " = ?",
//                        new String[]{mSku});
//            } else {
//                updateId = db.update(TB_SHOPPING, addRValues, SKU + " = ? AND " + PRODUCT_TYPE + " = ? AND " + OPTION_VALUE + " = ?",
//                        new String[]{mSku, productType, mSizeLable});
//            }
//
//            LogUtils.e("", "updateId::" + updateId + " Item:" + mShoppingItem.getName() + " sku:" + mSku);
//            if (updateId == -1) {
//                updateShoppingItem = false;
//            } else {
//                updateShoppingItem = true;
//            }
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            close();
//        }
//
//        return updateShoppingItem;
//    }
//
//    public ArrayList<ShoppingBag.Item> getAllShoppingBagList() {
//
//        ArrayList<ShoppingBag.Item> alShoppingBagList = new ArrayList<ShoppingBag.Item>();
//
//        open();
//
//        String query = "SELECT * FROM " + TB_SHOPPING + " WHERE " + SOFT_DELETE + " != '1'";
//        LogUtils.e("", "query::" + query);
//        Cursor cursor = db.rawQuery(query, null);
//
//        try {
//
//            if (cursor != null && cursor.getCount() > 0) {
//                cursor.moveToFirst();
//                ShoppingBag.Item mShoppingItem;
//                do {
//
//                    mShoppingItem = new ShoppingBag().new Item();
//                    mShoppingItem.setItemId(cursor.getInt(cursor
//                            .getColumnIndex(ITEM_ID)));
//                    mShoppingItem.setName(cursor.getString(cursor
//                            .getColumnIndex(NAME)));
//                    mShoppingItem.setBrand(cursor.getString(cursor
//                            .getColumnIndex(BRAND)));
//                    mShoppingItem.setImageFIle(cursor.getString(cursor
//                            .getColumnIndex(IMAGE)));
//                    mShoppingItem.setSku(cursor.getString(cursor
//                            .getColumnIndex(SKU)));
//                    mShoppingItem.setProductType(cursor.getString(cursor
//                            .getColumnIndex(PRODUCT_TYPE)));
//                    mShoppingItem.setPrice(cursor.getFloat(cursor
//                            .getColumnIndex(PRICE)));
//                    mShoppingItem.setSpecial_price(cursor.getFloat(cursor
//                            .getColumnIndex(SPECIAL_PRICE)));
//                    mShoppingItem.setSpecial_from_date(cursor.getString(cursor
//                            .getColumnIndex(SPECIAL_FROM_DATE)));
//                    mShoppingItem.setSpecial_to_date(cursor.getString(cursor
//                            .getColumnIndex(SPECIAL_TO_DATE)));
//                    mShoppingItem.setPrice_excl_tax(cursor.getFloat(cursor
//                            .getColumnIndex(PRICE_EXCL_TAX)));
//                    mShoppingItem.setQty(cursor.getInt(cursor
//                            .getColumnIndex(QTY)));
//                    mShoppingItem.setStockQty(cursor.getInt(cursor
//                            .getColumnIndex(STOCK_QTY)));
//                    mShoppingItem.setAttribute_id(cursor.getString(cursor
//                            .getColumnIndex(ATTRIBUTE_ID)));
//                    mShoppingItem.setValue_index(cursor.getString(cursor
//                            .getColumnIndex(VALUE_INDEX)));
//                    ShoppingBag.CustomMsg mCustomMsg = new ShoppingBag().new CustomMsg();
//                    mCustomMsg.setMsg(cursor.getString(cursor
//                            .getColumnIndex(MSG)));
//                    mCustomMsg.setColor(cursor.getString(cursor
//                            .getColumnIndex(COLOR)));
//                    mShoppingItem.setCustom_msg(mCustomMsg);
//                    mShoppingItem.setOption_lable(cursor.getString(cursor
//                            .getColumnIndex(OPTION_LABLE)));
//                    mShoppingItem.setOption_value(cursor.getString(cursor
//                            .getColumnIndex(OPTION_VALUE)));
//                    mShoppingItem.setIsGuest(cursor.getString(cursor
//                            .getColumnIndex(ISGUEST)));
//                    mShoppingItem.setToken(cursor.getString(cursor
//                            .getColumnIndex(TOKEN)));
//                    mShoppingItem.setSoft_delete(cursor.getString(cursor
//                            .getColumnIndex(SOFT_DELETE)));
//                    mShoppingItem.setIs_on_server(cursor.getString(cursor
//                            .getColumnIndex(IS_ON_SERVER)));
//
//                    alShoppingBagList.add(mShoppingItem);
//                } while (cursor.moveToNext());
//            }
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (cursor != null && !cursor.isClosed()) {
//                cursor.close();
//            }
//            close();
//        }
//
//        LogUtils.e("", "getAllShoppingBagList size::" + alShoppingBagList.size());
//
//
//        return alShoppingBagList;
//
//    }
//
//    public ArrayList<ShoppingBag.Item> getLocalShoppingBagList() {
//
//        ArrayList<ShoppingBag.Item> alShoppingBagList = new ArrayList<ShoppingBag.Item>();
//
//        open();
//
//        String query = "SELECT * FROM " + TB_SHOPPING + " WHERE " + IS_ON_SERVER + " = '0' AND " + SOFT_DELETE + " != '1'";
//        LogUtils.e("", "query::" + query);
//        Cursor cursor = db.rawQuery(query, null);
//
//        try {
//
//            if (cursor != null && cursor.getCount() > 0) {
//                cursor.moveToFirst();
//                ShoppingBag.Item mShoppingItem;
//                do {
//
//                    mShoppingItem = new ShoppingBag().new Item();
//                    mShoppingItem.setItemId(cursor.getInt(cursor
//                            .getColumnIndex(ITEM_ID)));
//                    mShoppingItem.setName(cursor.getString(cursor
//                            .getColumnIndex(NAME)));
//                    mShoppingItem.setBrand(cursor.getString(cursor
//                            .getColumnIndex(BRAND)));
//                    mShoppingItem.setImageFIle(cursor.getString(cursor
//                            .getColumnIndex(IMAGE)));
//                    mShoppingItem.setSku(cursor.getString(cursor
//                            .getColumnIndex(SKU)));
//                    mShoppingItem.setProductType(cursor.getString(cursor
//                            .getColumnIndex(PRODUCT_TYPE)));
//                    mShoppingItem.setPrice(cursor.getFloat(cursor
//                            .getColumnIndex(PRICE)));
//                    mShoppingItem.setSpecial_price(cursor.getFloat(cursor
//                            .getColumnIndex(SPECIAL_PRICE)));
//                    mShoppingItem.setSpecial_from_date(cursor.getString(cursor
//                            .getColumnIndex(SPECIAL_FROM_DATE)));
//                    mShoppingItem.setSpecial_to_date(cursor.getString(cursor
//                            .getColumnIndex(SPECIAL_TO_DATE)));
//                    mShoppingItem.setPrice_excl_tax(cursor.getFloat(cursor
//                            .getColumnIndex(PRICE_EXCL_TAX)));
//                    mShoppingItem.setQty(cursor.getInt(cursor
//                            .getColumnIndex(QTY)));
//                    mShoppingItem.setStockQty(cursor.getInt(cursor
//                            .getColumnIndex(STOCK_QTY)));
//                    mShoppingItem.setAttribute_id(cursor.getString(cursor
//                            .getColumnIndex(ATTRIBUTE_ID)));
//                    mShoppingItem.setValue_index(cursor.getString(cursor
//                            .getColumnIndex(VALUE_INDEX)));
//                    ShoppingBag.CustomMsg mCustomMsg = new ShoppingBag().new CustomMsg();
//                    mCustomMsg.setMsg(cursor.getString(cursor
//                            .getColumnIndex(MSG)));
//                    mCustomMsg.setColor(cursor.getString(cursor
//                            .getColumnIndex(COLOR)));
//                    mShoppingItem.setCustom_msg(mCustomMsg);
//                    mShoppingItem.setOption_lable(cursor.getString(cursor
//                            .getColumnIndex(OPTION_LABLE)));
//                    mShoppingItem.setOption_value(cursor.getString(cursor
//                            .getColumnIndex(OPTION_VALUE)));
//                    mShoppingItem.setIsGuest(cursor.getString(cursor
//                            .getColumnIndex(ISGUEST)));
//                    mShoppingItem.setToken(cursor.getString(cursor
//                            .getColumnIndex(TOKEN)));
//                    mShoppingItem.setSoft_delete(cursor.getString(cursor
//                            .getColumnIndex(SOFT_DELETE)));
//                    mShoppingItem.setIs_on_server(cursor.getString(cursor
//                            .getColumnIndex(IS_ON_SERVER)));
//
//                    alShoppingBagList.add(mShoppingItem);
//
//                } while (cursor.moveToNext());
//            }
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (cursor != null && !cursor.isClosed()) {
//                cursor.close();
//            }
//            close();
//        }
//
//        LogUtils.e("", "getLocalShoppingBagList size::" + alShoppingBagList.size());
//
//
//        return alShoppingBagList;
//
//    }
//
//    public ArrayList<ShoppingBag.Item> getLocalSoftDeleteShoppingList() {
//
//        ArrayList<ShoppingBag.Item> alShoppingBagList = new ArrayList<ShoppingBag.Item>();
//
//        open();
//
//        String query = "SELECT * FROM " + TB_SHOPPING + " WHERE " + IS_ON_SERVER + " = '1' AND " + SOFT_DELETE + " = '1'";
//        LogUtils.e("", "query::" + query);
//        Cursor cursor = db.rawQuery(query, null);
//
//        try {
//
//            if (cursor != null && cursor.getCount() > 0) {
//                cursor.moveToFirst();
//                ShoppingBag.Item mShoppingItem;
//                do {
//
//                    mShoppingItem = new ShoppingBag().new Item();
//                    mShoppingItem.setItemId(cursor.getInt(cursor
//                            .getColumnIndex(ITEM_ID)));
//                    mShoppingItem.setName(cursor.getString(cursor
//                            .getColumnIndex(NAME)));
//                    mShoppingItem.setBrand(cursor.getString(cursor
//                            .getColumnIndex(BRAND)));
//                    mShoppingItem.setImageFIle(cursor.getString(cursor
//                            .getColumnIndex(IMAGE)));
//                    mShoppingItem.setSku(cursor.getString(cursor
//                            .getColumnIndex(SKU)));
//                    mShoppingItem.setProductType(cursor.getString(cursor
//                            .getColumnIndex(PRODUCT_TYPE)));
//                    mShoppingItem.setPrice(cursor.getFloat(cursor
//                            .getColumnIndex(PRICE)));
//                    mShoppingItem.setSpecial_price(cursor.getFloat(cursor
//                            .getColumnIndex(SPECIAL_PRICE)));
//                    mShoppingItem.setSpecial_from_date(cursor.getString(cursor
//                            .getColumnIndex(SPECIAL_FROM_DATE)));
//                    mShoppingItem.setSpecial_to_date(cursor.getString(cursor
//                            .getColumnIndex(SPECIAL_TO_DATE)));
//                    mShoppingItem.setPrice_excl_tax(cursor.getFloat(cursor
//                            .getColumnIndex(PRICE_EXCL_TAX)));
//                    mShoppingItem.setQty(cursor.getInt(cursor
//                            .getColumnIndex(QTY)));
//                    mShoppingItem.setStockQty(cursor.getInt(cursor
//                            .getColumnIndex(STOCK_QTY)));
//                    mShoppingItem.setAttribute_id(cursor.getString(cursor
//                            .getColumnIndex(ATTRIBUTE_ID)));
//                    mShoppingItem.setValue_index(cursor.getString(cursor
//                            .getColumnIndex(VALUE_INDEX)));
//                    ShoppingBag.CustomMsg mCustomMsg = new ShoppingBag().new CustomMsg();
//                    mCustomMsg.setMsg(cursor.getString(cursor
//                            .getColumnIndex(MSG)));
//                    mCustomMsg.setColor(cursor.getString(cursor
//                            .getColumnIndex(COLOR)));
//                    mShoppingItem.setCustom_msg(mCustomMsg);
//                    mShoppingItem.setOption_lable(cursor.getString(cursor
//                            .getColumnIndex(OPTION_LABLE)));
//                    mShoppingItem.setOption_value(cursor.getString(cursor
//                            .getColumnIndex(OPTION_VALUE)));
//                    mShoppingItem.setIsGuest(cursor.getString(cursor
//                            .getColumnIndex(ISGUEST)));
//                    mShoppingItem.setToken(cursor.getString(cursor
//                            .getColumnIndex(TOKEN)));
//                    mShoppingItem.setSoft_delete(cursor.getString(cursor
//                            .getColumnIndex(SOFT_DELETE)));
//                    mShoppingItem.setIs_on_server(cursor.getString(cursor
//                            .getColumnIndex(IS_ON_SERVER)));
//
//                    alShoppingBagList.add(mShoppingItem);
//                } while (cursor.moveToNext());
//            }
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (cursor != null && !cursor.isClosed()) {
//                cursor.close();
//            }
//            close();
//        }
//
//        LogUtils.e("", "getLocalSoftDeleteShoppingList size::" + alShoppingBagList.size());
//
//
//        return alShoppingBagList;
//
//    }
//
//    public boolean updateIsOnServerShoppingFromDetail(String sku, String item_id, String product_type, String attribute_id, String value_index, String is_on_server) {
//
//        boolean updateWishItem = false;
//        open();
//
//        ContentValues addRValues = new ContentValues();
//        addRValues.put(IS_ON_SERVER, is_on_server);
//        if (!TextUtils.isEmpty(item_id)) {
//            addRValues.put(ITEM_ID, item_id);
//        }
//
//        try {
//
//            int updateId;
//
//            if (!TextUtils.isEmpty(product_type) && product_type.equalsIgnoreCase("simple")) {
//                updateId = db.update(TB_SHOPPING, addRValues, SKU + " = ?",
//                        new String[]{sku});
//            } else {
//                updateId = db.update(TB_SHOPPING, addRValues, SKU + " = ? AND " + ATTRIBUTE_ID + " = ? AND " + VALUE_INDEX + " = ?",
//                        new String[]{sku, attribute_id, value_index});
//            }
//            LogUtils.e("", "updateId::" + updateId);
//            if (updateId == -1) {
//                updateWishItem = false;
//            } else {
//                updateWishItem = true;
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            close();
//        }
//
//        return updateWishItem;
//    }
//
//    public boolean isSoftDeleteShoppingItem(String sku, String product_type, String mSizeLable) {
//
//        boolean isShoppingItemFound = false;
//
//        open();
//        String query = "";
//        if (!TextUtils.isEmpty(product_type) && product_type.equalsIgnoreCase("simple")) {
//            query = "SELECT * FROM " + TB_SHOPPING + " WHERE SKU = '" + sku + "' AND " + SOFT_DELETE + " = '1'";
//        } else {
//            query = "SELECT * FROM " + TB_SHOPPING + " WHERE SKU = '" + sku + "' AND " + PRODUCT_TYPE + " = '" + product_type + "' AND " + OPTION_VALUE + " = '" + mSizeLable + "' AND " + SOFT_DELETE + " = '1'";
//        }
//
//        LogUtils.e("", "query::" + query);
//        Cursor cursor = db.rawQuery(query, null);
//
//        try {
//
//
//            if (cursor != null && cursor.getCount() > 0) {
//                isShoppingItemFound = true;
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (cursor != null && !cursor.isClosed()) {
//                cursor.close();
//            }
//            close();
//        }
//        LogUtils.e("", "isShoppingItemFound::" + isShoppingItemFound);
//        return isShoppingItemFound;
//    }
//
//    public boolean updateQtySoftDelete(String sku, int qty, String soft_delete) {
//
//        boolean updateShoppingItem = false;
//        open();
//
//        ContentValues addRValues = new ContentValues();
//        addRValues.put(QTY, qty);
//        addRValues.put(SOFT_DELETE, soft_delete);
//        addRValues.put(IS_ON_SERVER, "0");
//
//        try {
//
//            int updateId = db.update(TB_SHOPPING, addRValues, SKU + " = ?",
//                    new String[]{sku});
//
//            LogUtils.e("", "updateQtySoftDelete updateId::" + updateId);
//            if (updateId == -1) {
//                updateShoppingItem = false;
//            } else {
//                updateShoppingItem = true;
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            close();
//        }
//
//        return updateShoppingItem;
//    }
//
//    public boolean updateQtyShoppingItem(int qty, String sku, String product_type, String mSizeLable) {
//
//        boolean updateShoppingItem = false;
//        open();
//
//        String query = "";
//
//        if (!TextUtils.isEmpty(product_type) && product_type.equalsIgnoreCase("simple")) {
//            query = "UPDATE " + TB_SHOPPING + " SET " + QTY + " = '" + qty + "' WHERE SKU = '" + sku + "'";
//        } else {
//            query = "UPDATE " + TB_SHOPPING + " SET " + QTY + " = '" + qty + "' WHERE SKU = '" + sku
//                    + "' AND " + PRODUCT_TYPE + " = '" + product_type + "' AND " + OPTION_VALUE + " = '" + mSizeLable + "'";
//        }
//
//        Cursor cursor = db.rawQuery(query, null);
//
//        try {
//
//
//            if (cursor != null && cursor.getCount() > 0) {
//                updateShoppingItem = true;
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (cursor != null && !cursor.isClosed()) {
//                cursor.close();
//            }
//            close();
//        }
//
//        return updateShoppingItem;
//    }
//
//    public boolean increaseQtyAndServer(String sku, String product_type, String mSizeLable) {
//
//        boolean isIncreaseQty = false;
//        open();
//        String query = "";
//
//        if (!TextUtils.isEmpty(product_type) && product_type.equalsIgnoreCase("simple")) {
//            query = "UPDATE " + TB_SHOPPING + " SET " + QTY + " = qty+1 , " + IS_ON_SERVER + " = 0 WHERE SKU = '" + sku + "'";
//        } else {
//            query = "UPDATE " + TB_SHOPPING + " SET " + QTY + " = qty+1 , " + IS_ON_SERVER + " = 0 WHERE SKU = '" + sku
//                    + "' AND " + PRODUCT_TYPE + " = '" + product_type + "' AND " + OPTION_VALUE + " = '" + mSizeLable + "'";
//        }
//        Cursor cursor = db.rawQuery(query, null);
//        try {
//
//
//            if (cursor != null && cursor.getCount() > 0) {
//                isIncreaseQty = true;
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (cursor != null && !cursor.isClosed()) {
//                cursor.close();
//            }
//            close();
//        }
//
//        return isIncreaseQty;
//    }
//
//    public boolean decreaseQtyAndServer(String sku, String product_type, String mSizeLable) {
//
//        boolean isDecreaseQty = false;
//        open();
//
//        String query = "";
//
//        if (!TextUtils.isEmpty(product_type) && product_type.equalsIgnoreCase("simple")) {
//            query = "UPDATE " + TB_SHOPPING + " SET " + QTY + " = qty-1 , " + IS_ON_SERVER + " = 0 WHERE SKU = '" + sku + "'";
//        } else {
//            query = "UPDATE " + TB_SHOPPING + " SET " + QTY + " = qty-1 , " + IS_ON_SERVER + " = 0 WHERE SKU = '" + sku
//                    + "' AND " + PRODUCT_TYPE + " = '" + product_type + "' AND " + OPTION_VALUE + " = '" + mSizeLable + "'";
//        }
//        LogUtils.e("", "query::" + query);
//        Cursor cursor = db.rawQuery(query, null);
//
//        try {
//
//
//            if (cursor != null && cursor.getCount() > 0) {
//                isDecreaseQty = true;
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (cursor != null && !cursor.isClosed()) {
//                cursor.close();
//            }
//            close();
//        }
//        LogUtils.e("", "isDecreaseQty:" + isDecreaseQty);
//        return isDecreaseQty;
//    }
//
//    public boolean softDeleteShoppingItem(String sku, String product_type, String mSizeLable) {
//
//        boolean isDeleteSoftShoppingItem = false;
//        open();
//        String query = "";
//
//        if (!TextUtils.isEmpty(product_type) && product_type.equalsIgnoreCase("simple")) {
//            query = "UPDATE " + TB_SHOPPING + " SET " + SOFT_DELETE + " = 1 WHERE SKU = '" + sku + "'";
//        } else {
//            query = "UPDATE " + TB_SHOPPING + " SET " + SOFT_DELETE + " = 1 WHERE SKU = '" + sku
//                    + "' AND " + PRODUCT_TYPE + " = '" + product_type + "' AND " + OPTION_VALUE + " = '" + mSizeLable + "'";
//        }
//        LogUtils.e("", "query::" + query);
//        Cursor cursor = db.rawQuery(query, null);
//        try {
//
//
//            if (cursor != null && cursor.getCount() > 0) {
//                isDeleteSoftShoppingItem = true;
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (cursor != null && !cursor.isClosed()) {
//                cursor.close();
//            }
//            close();
//        }
//        LogUtils.e("", "isDeleteSoftShoppingItem:" + isDeleteSoftShoppingItem);
//        return isDeleteSoftShoppingItem;
//    }
//
//    public boolean ifShoppingItemAvailable(String sku, String product_type, String mSizeLable) {
//        boolean isShoppingItemFound = false;
//
//        open();
//        String query = "";
//        if (!TextUtils.isEmpty(product_type) && product_type.equalsIgnoreCase("simple")) {
//            query = "SELECT * FROM " + TB_SHOPPING + " WHERE SKU = '" + sku + "' ";
//        } else {
//            query = "SELECT * FROM " + TB_SHOPPING + " WHERE SKU = '" + sku + "' AND " + PRODUCT_TYPE + " = '" + product_type + "' AND " + OPTION_VALUE + " = '" + mSizeLable + "'";
//        }
//        LogUtils.e("", "query::" + query);
//        Cursor cursor = db.rawQuery(query, null);
//        try {
//
//
//            if (cursor != null && cursor.getCount() > 0) {
//                isShoppingItemFound = true;
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (cursor != null && !cursor.isClosed()) {
//                cursor.close();
//            }
//            close();
//        }
//        LogUtils.e("", "ifShoppingItemAvailable qty::" + isShoppingItemFound);
//        return isShoppingItemFound;
//    }
//
//    public ShoppingBag.Item getShoppingItemBySku(String sku, String product_type, String mSizeLable) {
//
//        ShoppingBag.Item mShoppingItem = null;
//
//        open();
//
//        String query = "";
//        if (!TextUtils.isEmpty(product_type) && product_type.equalsIgnoreCase("simple")) {
//            query = "SELECT * FROM " + TB_SHOPPING + " WHERE SKU = '" + sku + "' ";
//        } else {
//            query = "SELECT * FROM " + TB_SHOPPING + " WHERE SKU = '" + sku + "' AND " + PRODUCT_TYPE + " = '" + product_type + "' AND " + OPTION_VALUE + " = '" + mSizeLable + "'";
//        }
//        Cursor cursor = db.rawQuery(query, null);
//
//        try {
//
//
//            if (cursor != null && cursor.getCount() > 0) {
//                cursor.moveToFirst();
//
//                mShoppingItem = new ShoppingBag().new Item();
//                mShoppingItem.setItemId(cursor.getInt(cursor
//                        .getColumnIndex(ITEM_ID)));
//                mShoppingItem.setName(cursor.getString(cursor
//                        .getColumnIndex(NAME)));
//                mShoppingItem.setBrand(cursor.getString(cursor
//                        .getColumnIndex(BRAND)));
//                mShoppingItem.setImageFIle(cursor.getString(cursor
//                        .getColumnIndex(IMAGE)));
//                mShoppingItem.setSku(cursor.getString(cursor
//                        .getColumnIndex(SKU)));
//                mShoppingItem.setProductType(cursor.getString(cursor
//                        .getColumnIndex(PRODUCT_TYPE)));
//                mShoppingItem.setPrice(cursor.getFloat(cursor
//                        .getColumnIndex(PRICE)));
//                mShoppingItem.setSpecial_price(cursor.getFloat(cursor
//                        .getColumnIndex(SPECIAL_PRICE)));
//                mShoppingItem.setSpecial_from_date(cursor.getString(cursor
//                        .getColumnIndex(SPECIAL_FROM_DATE)));
//                mShoppingItem.setSpecial_to_date(cursor.getString(cursor
//                        .getColumnIndex(SPECIAL_TO_DATE)));
//                mShoppingItem.setPrice_excl_tax(cursor.getFloat(cursor
//                        .getColumnIndex(PRICE_EXCL_TAX)));
//                mShoppingItem.setQty(cursor.getInt(cursor
//                        .getColumnIndex(QTY)));
//                mShoppingItem.setStockQty(cursor.getInt(cursor
//                        .getColumnIndex(STOCK_QTY)));
//                mShoppingItem.setAttribute_id(cursor.getString(cursor
//                        .getColumnIndex(ATTRIBUTE_ID)));
//                mShoppingItem.setValue_index(cursor.getString(cursor
//                        .getColumnIndex(VALUE_INDEX)));
//                ShoppingBag.CustomMsg mCustomMsg = new ShoppingBag().new CustomMsg();
//                mCustomMsg.setMsg(cursor.getString(cursor
//                        .getColumnIndex(MSG)));
//                mCustomMsg.setColor(cursor.getString(cursor
//                        .getColumnIndex(COLOR)));
//                mShoppingItem.setCustom_msg(mCustomMsg);
//                mShoppingItem.setOption_lable(cursor.getString(cursor
//                        .getColumnIndex(OPTION_LABLE)));
//                mShoppingItem.setOption_value(cursor.getString(cursor
//                        .getColumnIndex(OPTION_VALUE)));
//                mShoppingItem.setIsGuest(cursor.getString(cursor
//                        .getColumnIndex(ISGUEST)));
//                mShoppingItem.setToken(cursor.getString(cursor
//                        .getColumnIndex(TOKEN)));
//                mShoppingItem.setSoft_delete(cursor.getString(cursor
//                        .getColumnIndex(SOFT_DELETE)));
//                mShoppingItem.setIs_on_server(cursor.getString(cursor
//                        .getColumnIndex(IS_ON_SERVER)));
//
//            }
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (cursor != null && !cursor.isClosed()) {
//                cursor.close();
//            }
//            close();
//        }
//
//        LogUtils.e("", "getShoppingItemBySku size::" + sku);
//
//
//        return mShoppingItem;
//
//    }
//
//    public boolean isContainInWishList(String sku, String product_type, String mSizeLable) {
//
//        boolean isShoppingItemFound = false;
//
//        open();
//        String query = "";
//        if (!TextUtils.isEmpty(product_type) && product_type.equalsIgnoreCase("simple")) {
//            query = "SELECT * FROM " + TB_SHOPPING + " WHERE SKU = '" + sku + "'";
//        } else {
//            query = "SELECT * FROM " + TB_SHOPPING + " WHERE SKU = '" + sku + "' AND " + PRODUCT_TYPE + " = '" + product_type + "' AND " + OPTION_VALUE + " = '" + mSizeLable + "'";
//        }
//        LogUtils.e("", "query::" + query);
//        Cursor cursor = db.rawQuery(query, null);
//
//        try {
//
//            if (cursor != null && cursor.getCount() > 0) {
//                isShoppingItemFound = true;
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (cursor != null && !cursor.isClosed()) {
//                cursor.close();
//            }
//            close();
//        }
//        LogUtils.e("", "isShoppingItemFound::" + isShoppingItemFound);
//        return isShoppingItemFound;
//    }
//
//    public boolean deleteShoppingItem(String sku, String product_type, String mSizeLable) {
//
//        boolean isShoppingItemDeleted = false;
//
//        open();
//
//        try {
//            int rowAffected = -1;
//            if (!TextUtils.isEmpty(product_type) && product_type.equalsIgnoreCase("simple")) {
//                rowAffected = db.delete(TB_SHOPPING, SKU + " = ?",
//                        new String[]{sku});
//            } else {
//                rowAffected = db.delete(TB_SHOPPING, SKU + " = ? AND " + PRODUCT_TYPE + " = ? AND " + OPTION_VALUE + " = ?",
//                        new String[]{sku, product_type, mSizeLable});
//            }
//
//            Log.e("", "rowAffected:::" + rowAffected);
//            if (rowAffected > 0) {
//                isShoppingItemDeleted = true;
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            close();
//        }
//
//        return isShoppingItemDeleted;
//
//    }
//
//    public boolean removeAllShoppingItem() {
//
//        boolean isShoplistCleared = false;
//
//        open();
//
//        try {
//
//            int rowAffected = db.delete(TB_SHOPPING, "1", null);
//            Log.e("", "rowAffected:::" + rowAffected);
//            if (rowAffected > 0) {
//                isShoplistCleared = true;
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            close();
//        }
//        LogUtils.e("", "isShoplistCleared::" + isShoplistCleared);
//        return isShoplistCleared;
//
//    }
//
//    public boolean updateTokenShopping(String token) {
//
//        boolean updateShoppingItem = false;
//        open();
//
//        ContentValues addRValues = new ContentValues();
//        addRValues.put(TOKEN, token);
//
//        try {
//
//            int updateId = db.update(TB_SHOPPING, addRValues, ISGUEST + " = ?",
//                    new String[]{"1"});
//            LogUtils.e("", "updateId::");
//            if (updateId == -1) {
//                updateShoppingItem = false;
//            } else {
//                updateShoppingItem = true;
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            close();
//        }
//
//        return updateShoppingItem;
//    }
//
//    public boolean isShoppingItemOnServer(String sku, String product_type, String mSizeLable) {
//
//        boolean isShoppingItemFound = false;
//
//        open();
//        String query = "";
//        if (!TextUtils.isEmpty(product_type) && product_type.equalsIgnoreCase("simple")) {
//            query = "SELECT * FROM " + TB_SHOPPING + " WHERE SKU = '" + sku + "' AND " + IS_ON_SERVER + " = 1";
//        } else {
//            query = "SELECT * FROM " + TB_SHOPPING + " WHERE SKU = '" + sku + "' AND " + PRODUCT_TYPE + " = '" + product_type + "' AND " + OPTION_VALUE + " = '" + mSizeLable + "' AND " + IS_ON_SERVER + " = 1";
//        }
//        Cursor cursor = db.rawQuery(query, null);
//
//        try {
//
//
//            if (cursor != null && cursor.getCount() > 0) {
//                isShoppingItemFound = true;
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (cursor != null && !cursor.isClosed()) {
//                cursor.close();
//            }
//            close();
//        }
//        LogUtils.e("", "isShoppingItemFound::" + isShoppingItemFound);
//        return isShoppingItemFound;
//    }
//
//    public boolean isSyncAvailable() {
//
//        boolean isSyncShoppingItemFound = false;
//
//        open();
//        String query = "SELECT * FROM " + TB_SHOPPING + " WHERE " + IS_ON_SERVER + " != 1 OR " + SOFT_DELETE + " = 1";
//        Cursor cursor = db.rawQuery(query, null);
//        try {
//
//
//            if (cursor != null && cursor.getCount() > 0) {
//                isSyncShoppingItemFound = true;
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (cursor != null && !cursor.isClosed()) {
//                cursor.close();
//            }
//            close();
//        }
//        LogUtils.e("", "isSyncShoppingItemFound::" + isSyncShoppingItemFound);
//        return isSyncShoppingItemFound;
//    }

}
