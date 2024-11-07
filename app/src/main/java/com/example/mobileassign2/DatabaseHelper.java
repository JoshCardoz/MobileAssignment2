package com.example.mobileassign2;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ADDRESS_LOCATIONS";
    private SQLiteDatabase database; // Add this field declaration

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 3);
        database = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS LOCATIONS" +
                        "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "ADDRESS VARCHAR(255) NOT NULL, " +
                        "LATITUDE DECIMAL(9,6) NOT NULL, " +
                        "LONGITUDE DECIMAL(9,6) NOT NULL)"
        );

        insertDefaultData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS LOCATIONS");
        onCreate(db);
    }

    @SuppressLint("Range")
    public Cursor getEntries() {
        return database.rawQuery("SELECT * FROM LOCATIONS", null);
    }

    public void addEntry(String address, double longitude, double latitude) {
        ContentValues values = new ContentValues();
        values.put("ADDRESS", address);
        values.put("LATITUDE", latitude);
        values.put("LONGITUDE", longitude);
        database.insert("LOCATIONS", null, values);
    }

    public void deleteEntry(int id) {
        database.delete("LOCATIONS", "ID = ?", new String[]{String.valueOf(id)});
    }

    @SuppressLint("Range")
    public Cursor search(String searchTerm) {
        String query = "SELECT * FROM LOCATIONS WHERE LOWER(ADDRESS) LIKE LOWER(?)";
        return database.rawQuery(query, new String[]{"%" + searchTerm + "%"});
    }

    public boolean updateEntry(int id, String address, double latitude, double longitude) {
        ContentValues values = new ContentValues();
        values.put("ADDRESS", address);
        values.put("LATITUDE", latitude);
        values.put("LONGITUDE", longitude);
        int rowsUpdated = database.update("LOCATIONS", values, "ID = ?", new String[]{String.valueOf(id)});
        return rowsUpdated > 0;
    }

    private void insertDefaultData(SQLiteDatabase db) {
        ContentValues values = new ContentValues();

        String[] addresses = {
                "123 Queen St W, Toronto", "456 King St W, Toronto", "789 Dundas St W, Toronto",
                "101 Bay St, Toronto", "202 College St, Toronto", "303 Yonge St, Toronto",
                "404 Front St W, Toronto", "505 Spadina Ave, Toronto", "606 Bathurst St, Toronto",
                "707 St Clair Ave W, Toronto", "808 Bloor St W, Toronto", "909 Finch Ave W, Toronto",
                "1010 University Ave, Toronto", "1111 Avenue Rd, Toronto", "1212 Eglinton Ave W, Toronto",
                "1313 Dufferin St, Toronto", "1414 Wilson Ave, Toronto", "1515 St Clair Ave E, Toronto",
                "1616 Kingston Rd, Toronto", "1717 Lawrence Ave E, Toronto", "1818 Victoria Park Ave, Toronto",
                "1919 Sheppard Ave E, Toronto", "2020 Don Mills Rd, Toronto", "2121 Bayview Ave, Toronto",
                "2222 Mount Pleasant Rd, Toronto", "2323 Yonge St, Toronto", "2424 Danforth Ave, Toronto",
                "2525 Queen St E, Toronto", "2626 Lake Shore Blvd W, Toronto", "2727 Kipling Ave, Toronto",
                "2828 Jane St, Toronto", "2929 Keele St, Toronto", "3030 Weston Rd, Toronto",
                "3131 Steeles Ave W, Toronto", "3232 Finch Ave E, Toronto", "3333 Kennedy Rd, Toronto",
                "3434 McCowan Rd, Toronto", "3535 Brimley Rd, Toronto", "3636 Midland Ave, Toronto",
                "3737 Pharmacy Ave, Toronto", "3838 Ellesmere Rd, Toronto", "3939 Lawrence Ave E, Toronto",
                "4040 Markham Rd, Toronto", "4141 Morningside Ave, Toronto", "4242 Port Union Rd, Toronto",
                "4343 Kingston Rd, Toronto", "4444 Eglinton Ave E, Toronto", "4545 York Mills Rd, Toronto",
                "4646 Sheppard Ave W, Toronto", "4747 Leslie St, Toronto", "4848 Bathurst St, Toronto",
                "4949 Dufferin St, Toronto", "5050 Steeles Ave E, Toronto", "5151 Don Mills Rd, Toronto",
                "5252 Victoria Park Ave, Toronto", "5353 Lawrence Ave W, Toronto", "5454 Rogers Rd, Toronto",
                "5555 St Clair Ave W, Toronto", "5656 Davenport Rd, Toronto", "5757 Dupont St, Toronto",
                "5858 Bloor St W, Toronto", "5959 The Queensway, Toronto", "6060 Evans Ave, Toronto",
                "6161 Dixon Rd, Toronto", "6262 Rexdale Blvd, Toronto", "6363 Albion Rd, Toronto",
                "6464 Islington Ave, Toronto", "6565 Martin Grove Rd, Toronto", "6666 Burnhamthorpe Rd, Toronto",
                "6767 Mill Rd, Toronto", "6868 Royal York Rd, Toronto", "6969 Brown's Line, Toronto",
                "7070 Coxwell Ave, Toronto", "7171 Woodbine Ave, Toronto", "7272 Birchmount Rd, Toronto",
                "7373 Warden Ave, Toronto", "7474 Bellamy Rd, Toronto", "7575 Brimley Rd, Toronto",
                "7676 Progress Ave, Toronto", "7777 Neilson Rd, Toronto", "7878 Sewells Rd, Toronto",
                "7979 Old Kingston Rd, Toronto", "8080 Military Trail, Toronto", "8181 Conlins Rd, Toronto",
                "8282 Meadowvale Rd, Toronto", "8383 Sheppard Ave E, Toronto", "8484 Bridletowne Circle, Toronto",
                "8585 Huntingwood Dr, Toronto", "8686 Brian Dr, Toronto", "8787 Gordon Baker Rd, Toronto",
                "8888 McNicoll Ave, Toronto", "8989 Steeles Ave E, Toronto", "9090 Finch Ave E, Toronto",
                "9191 Sandhurst Circle, Toronto", "9292 Kennedy Rd, Toronto", "9393 Birchmount Rd, Toronto",
                "9494 Warden Ave, Toronto", "9595 Pharmacy Ave, Toronto", "9696 Victoria Park Ave, Toronto",
                "9797 Don Mills Rd, Toronto", "9898 Leslie St, Toronto", "9999 Bayview Ave, Toronto",
                "10000 Yonge St, Toronto"
        };

        double[] latitudes = {
                43.6532, 43.6468, 43.6552, 43.6488, 43.6579, 43.6612, 43.6428, 43.6598, 43.6666,
                43.6812, 43.6629, 43.7841, 43.6579, 43.6879, 43.6999, 43.6688, 43.7199, 43.6788,
                43.6888, 43.7488, 43.7199, 43.7677, 43.7388, 43.7299, 43.7088, 43.7166, 43.6833,
                43.6688, 43.6288, 43.7122, 43.6999, 43.7299, 43.7066, 43.7944, 43.7855, 43.7788,
                43.7844, 43.7733, 43.7788, 43.7733, 43.7677, 43.7577, 43.7844, 43.7788, 43.7922,
                43.7677, 43.7255, 43.7455, 43.7844, 43.7677, 43.7577, 43.7399, 43.7944, 43.7844,
                43.7677, 43.7122, 43.6922, 43.6833, 43.6733, 43.6633, 43.6579, 43.6488, 43.6429,
                43.6922, 43.6833, 43.6733, 43.6633, 43.7122, 43.7066, 43.7011, 43.6955, 43.6899,
                43.6844, 43.6788, 43.6733, 43.7122, 43.7066, 43.7011, 43.6955, 43.6899, 43.7122,
                43.7066, 43.7011, 43.6955, 43.7299, 43.7844, 43.7788, 43.7733, 43.7677, 43.7622,
                43.7566, 43.7511, 43.7455, 43.7399, 43.7344, 43.7288, 43.7233, 43.7177, 43.7122,
                43.7066, 43.7011, 43.6955, 43.6899, 43.6844, 43.6788, 43.6733, 43.6677, 43.6622
        };

        double[] longitudes = {
                -79.3832, -79.3926, -79.4129, -79.3806, -79.4023, -79.3834, -79.3972, -79.3975, -79.4066,
                -79.4337, -79.4273, -79.4677, -79.3903, -79.4088, -79.4266, -79.4344, -79.4522, -79.3488,
                -79.2844, -79.2733, -79.3188, -79.3344, -79.3466, -79.3877, -79.3966, -79.4033, -79.3244,
                -79.3033, -79.4788, -79.5733, -79.5122, -79.4677, -79.5322, -79.5488, -79.2922, -79.2833,
                -79.2744, -79.2655, -79.2877, -79.2988, -79.2766, -79.2677, -79.2588, -79.2499, -79.2411,
                -79.2322, -79.2233, -79.3455, -79.3366, -79.3277, -79.4466, -79.4377, -79.3188, -79.3466,
                -79.3344, -79.4088, -79.3999, -79.3911, -79.4733, -79.4644, -79.4555, -79.4466, -79.4377,
                -79.4977, -79.4888, -79.4799, -79.4711, -79.5733, -79.5644, -79.5555, -79.5466, -79.5377,
                -79.5288, -79.5199, -79.5111, -79.3188, -79.3099, -79.3011, -79.2922, -79.2833, -79.2744,
                -79.2655, -79.2566, -79.2477, -79.2388, -79.2299, -79.2211, -79.2122, -79.2033, -79.1944,
                -79.1855, -79.1766, -79.1677, -79.1588, -79.1499, -79.1411, -79.1322, -79.1233, -79.1144,
                -79.1055, -79.0966, -79.0877, -79.0788, -79.0699, -79.0611, -79.0522, -79.0433, -79.0344
        };

        // Insert all locations into database
        for (int i = 0; i < addresses.length; i++) {
            values.put("ADDRESS", addresses[i]);
            values.put("LATITUDE", latitudes[i]);
            values.put("LONGITUDE", longitudes[i]);
            db.insert("LOCATIONS", null, values);
            values.clear();
        }
    }

}