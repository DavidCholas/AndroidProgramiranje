package com.example.andriodproekt;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "Baza";
    public static final String TABLE_USER = "users";
    private static final String COLUMN_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_CHOICE = "userChoice";

    private static final String TABLE_DRIVER = "drivers";
    private static final String COLUMN_DRIVER_ID = "driver_id";
    private static final String COLUMN_DRIVER_USERNAME = "driver_username";
    private static final String COLUMN_DRIVER_PASSWORD = "driver_password";

    public static final String CREATE_DRIVERS_TABLE = "CREATE TABLE IF NOT EXISTS drivers (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "username TEXT, " +
            "password TEXT, " +
            "car_details TEXT" +
            ", license_credentials TEXT" +
            ", free_time TEXT"
            + "+)";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //  users table
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USERNAME + " TEXT, "
                + COLUMN_PASSWORD + " TEXT, "
                + COLUMN_CHOICE + " TEXT "
                + ")";

        String CREATE_DRIVER_TABLE = "CREATE TABLE " + TABLE_DRIVER + "("
                + COLUMN_DRIVER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_DRIVER_USERNAME + " TEXT, "
                + COLUMN_DRIVER_PASSWORD + " TEXT, "
                + "car_details TEXT, "
                + "license_credentials TEXT, "
                + "free_time TEXT"
                + ")";

        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_DRIVER_TABLE);
    }
    private boolean columnExists(SQLiteDatabase db, String tableName, String columnName) {
        Cursor cursor = db.rawQuery("PRAGMA table_info(" + tableName + ")", null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int columnIndex = cursor.getColumnIndex("name");

                if (columnIndex >= 0) {
                    String column = cursor.getString(columnIndex);

                    if (column.equals(columnName)) {
                        cursor.close();
                        return true;
                    }
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return false;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if exists
        if(oldVersion < 2) {

            if (!columnExists(db, TABLE_DRIVER, "car_details")) {
                String ALTER_TABLE = "ALTER TABLE " + TABLE_DRIVER + " ADD COLUMN car_details TEXT";
                db.execSQL(ALTER_TABLE);
            }

            if (!columnExists(db, TABLE_DRIVER, "license_credentials")) {
                String ALTER_TABLE = "ALTER TABLE " + TABLE_DRIVER + " ADD COLUMN license_credentials TEXT";
                db.execSQL(ALTER_TABLE);
            }

            if (!columnExists(db, TABLE_DRIVER, "free_time")) {
                String ALTER_TABLE = "ALTER TABLE " + TABLE_DRIVER + " ADD COLUMN free_time TEXT";
                db.execSQL(ALTER_TABLE);
            }
            String ALTER_TABLE = "ALTER TABLE drivers ADD COLUMN username TEXT";
            db.execSQL(ALTER_TABLE);

            db.execSQL("ALTER TABLE " + TABLE_USER + " RENAME TO temp_users");
            String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_USERNAME + " TEXT, "
                    + COLUMN_PASSWORD + " TEXT, "
                    + "userChoice TEXT" + ")";
            db.execSQL(CREATE_USER_TABLE);


            db.execSQL("INSERT INTO " + TABLE_USER + " (" +
                    COLUMN_ID + ", " + COLUMN_USERNAME + ", " + COLUMN_PASSWORD + ", " + "userChoice" + ") " +
                    "SELECT " + COLUMN_ID + ", " + COLUMN_USERNAME + ", " + COLUMN_PASSWORD + ", " + "user_choice" +
                    " FROM temp_users");

            db.execSQL("DROP TABLE IF EXISTS temp_users");
        }
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DRIVER);
        // Create new table
        onCreate(db);
    }


    // insert nov user vo bazata
    public boolean addUser(String username, String password,String userChoice) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_CHOICE, userChoice);
        long result = db.insert(TABLE_USER, null, values);
        db.close();
        return result != -1;
    }

    // dali postoi user vo bazata
    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USER + " WHERE " + COLUMN_USERNAME + " =? AND " + COLUMN_PASSWORD + " =? ";
        Cursor cursor = db.rawQuery(query, new String[]{username, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    public List<User> getDrivers(){
        List<User> drivers = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USER + " WHERE " + COLUMN_CHOICE + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{"driver"});

        if (cursor != null && cursor.moveToFirst()) {

            int usernameIndex = cursor.getColumnIndex(COLUMN_USERNAME);
            int passwordIndex = cursor.getColumnIndex(COLUMN_PASSWORD);
            int userChoiceIndex = cursor.getColumnIndex(COLUMN_CHOICE);

            if (usernameIndex >= 0 && passwordIndex >= 0 && userChoiceIndex >= 0) {
                do {
                    String username = cursor.getString(usernameIndex);
                    String password = cursor.getString(passwordIndex);
                    String userChoice = cursor.getString(userChoiceIndex);

                    User user = new User(username, password, userChoice);
                    drivers.add(user);
                } while (cursor.moveToNext());
            } else {
                Log.e("DBHelper", "Columns not found in database query");
            }
        }

        cursor.close();
        db.close();

        return drivers;
    }

    public void addDriversToDriverTable(List<User> drivers) {
        SQLiteDatabase db = this.getWritableDatabase();

        for (User user : drivers) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_USERNAME, user.getUsername());
            values.put(COLUMN_PASSWORD, user.getPassword());
            long newRowId = db.insert(TABLE_DRIVER, null, values);
            if (newRowId == -1) {
                Log.d("DBHelper", "Failed to add driver: " + user.getUsername());
            } else {
                Log.d("DBHelper", "Driver added: " + user.getUsername());
            }
        }

        db.close();
    }

    public String getUserChoice(String username) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT " + COLUMN_CHOICE + " FROM " + TABLE_USER + " WHERE " + COLUMN_USERNAME + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username});

        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(COLUMN_CHOICE);

            if (columnIndex >= 0) {
                String userChoice = cursor.getString(columnIndex);
                cursor.close();
                return userChoice;
            } else {
                cursor.close();
                return null;
            }
        }

        cursor.close();
        return null;

    }

    public boolean validateUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_USER + " WHERE " + COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username, password});

        boolean isValid = cursor != null && cursor.moveToFirst();
        cursor.close();
        return isValid;
    }

    public boolean checkUserExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USER + " WHERE " + COLUMN_USERNAME + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    public List<User> getAllDrivers() {
        List<User> driverList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT " + COLUMN_USERNAME + ", " + "car_details, license_credentials, free_time FROM " + TABLE_USER;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String username = cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME));
                @SuppressLint("Range") String carDetails = cursor.getString(cursor.getColumnIndex("car_details"));
                @SuppressLint("Range") String licenseCredentials = cursor.getString(cursor.getColumnIndex("license_credentials"));
                @SuppressLint("Range") String freeTime = cursor.getString(cursor.getColumnIndex("free_time"));

                User driver = new User(username, carDetails, licenseCredentials, freeTime);
                driverList.add(driver);

            } while (cursor.moveToNext());
        }

        cursor.close();
        return driverList;
    }
    public User getDriverInfo(String username) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM drivers WHERE username = ?", new String[]{username});

        if (cursor != null && cursor.moveToFirst()) {
            // Retrieve the driver's details from the cursor
            @SuppressLint("Range") String carDetails = cursor.getString(cursor.getColumnIndex("car_details"));
            @SuppressLint("Range") String licenseCredentials = cursor.getString(cursor.getColumnIndex("license_credentials"));
            @SuppressLint("Range") String freeTime = cursor.getString(cursor.getColumnIndex("free_time"));
            @SuppressLint("Range")String Username = cursor.getString(cursor.getColumnIndex("username"));

            cursor.close();
            return new User(username, carDetails, licenseCredentials, freeTime);
        }

        cursor.close();
        return null;
    }


}