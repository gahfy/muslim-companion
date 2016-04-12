package net.gahfy.muslimcompanion;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DbOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "muslimcompanion.db";
    private static final int DB_VERSION = 11;

    private Context context;
    private static SQLiteDatabase db = null;

    private DbOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    public static synchronized SQLiteDatabase getDb(Context context){
        if(db == null){
            DbOpenHelper openHelper = new DbOpenHelper(context);
            db = openHelper.getWritableDatabase();
        }
        return db;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        executeSqlFileFromAssets("create_database.jpg", db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion < 10){
            db.execSQL("DROP TABLE IF EXISTS cities;");
            db.execSQL("DROP TABLE IF EXISTS alternateNames;");
            db.execSQL("DROP TABLE IF EXISTS quran;");
            db.execSQL("VACUUM");
            this.onCreate(db);
        }
        else{
            if(oldVersion < 11){
                executeSqlFileFromAssets("update_10_11.jpg", db);
            }
        }
    }

    private void executeSqlFileFromAssets(String filename, SQLiteDatabase db){
        try {
            InputStream is = context.getAssets().open(filename);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int nRead;
            while ((nRead = is.read()) != -1) {
                if(nRead != 59) {
                    bos.write(nRead);
                }
                else{
                    String currentSql = new String(bos.toByteArray(), "UTF-8");
                    db.execSQL(currentSql);
                    bos.reset();
                }
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}
