package com.example.siddarthshikhar.yomtrainerside;



import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class SQLiteHelper extends SQLiteOpenHelper {

    public static final String CLASS_TABLE = "classtable";
    public static final String COLUMN_STARTTIME = "starttime";
    public static final String COLUMN_ENDTIME = "endtime";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_VENUE = "venue";
    public static final String COLUMN_IFTAKEN = "iftaken";
    public static final String COLUMN_NAME="name";

    public SQLiteHelper(Context context){super(context,"classtable",null,1);}
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + CLASS_TABLE + " ( _ID INTEGER PRIMARY KEY, " + COLUMN_DATE + " TEXT, "
                + COLUMN_STARTTIME + " TEXT, " + COLUMN_IFTAKEN + " INTEGER, "+ COLUMN_ENDTIME + " TEXT, "+COLUMN_VENUE + " TEXT, "+COLUMN_NAME + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}