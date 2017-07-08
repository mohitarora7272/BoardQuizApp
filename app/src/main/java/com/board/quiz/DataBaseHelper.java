package com.board.quiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

//import net.sqlcipher.database.SQLiteDatabase;

@SuppressWarnings("ALL")
public class DataBaseHelper extends SQLiteOpenHelper {
    private String TAG = this.getClass().getSimpleName();
    private static String DB_PATH = "";
    private static String DB_NAME = "quizsimple";
    private SQLiteDatabase mDataBase;
    private final Context mContext;

    // Contacts table name
    private static final String TABLE_CONTACTS = "quiz_master";
    private static final String KEY_ID = "id";
    private static final String KEY_QUESTION = "question";
    private static final String KEY_OPT1 = "opt1";
    private static final String KEY_OPT2 = "opt2";
    private static final String KEY_OPT3 = "opt3";
    private static final String KEY_OPT4 = "opt4";
    private static final String KEY_ANSWER = "answer";
    private static final String KEY_CATEGORYNAME = "category";

    public static final String TABLE_NAME = "payment_master";

    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, 3);// 1? its Database Version
        DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        this.mContext = context;
    }

    public void createDataBase() throws IOException {
        // If database not exists copy it from the assets
        boolean mDataBaseExist = checkDataBase();
        if (!mDataBaseExist) {
            this.getReadableDatabase();
            this.close();
            try {
                // Copy the database from assests
                copyDataBase();
                Log.e(TAG, "createDatabase database created");
            } catch (IOException mIOException) {
                throw new Error("ErrorCopyingDataBase");
            }
        }
    }

    // Check that the database exists here: /data/data/your package/databases/Da
    // Name
    private boolean checkDataBase() {
        File dbFile = new File(DB_PATH + DB_NAME);
        return dbFile.exists();
    }

    public void deleteDatabase() {
        File dbFile = new File(DB_PATH + DB_NAME);
        dbFile.delete();
    }

    // Copy the database from assets
    private void copyDataBase() throws IOException {
        InputStream mInput = mContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream mOutput = new FileOutputStream(outFileName);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer)) > 0) {
            mOutput.write(mBuffer, 0, mLength);
        }
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    // Open the database, so we can query it
    public boolean openDataBase() throws SQLException {
        String mPath = DB_PATH + DB_NAME;
        mDataBase = SQLiteDatabase.openDatabase(mPath, null,
                SQLiteDatabase.CREATE_IF_NECESSARY);
        // mDataBase = SQLiteDatabase.openDatabase(mPath, null,
        // SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        return mDataBase != null;
    }

    @Override
    public synchronized void close() {
        if (mDataBase != null)
            mDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase arg0) {
        //InitializeSQLCipher();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    private void InitializeSQLCipher() {
//		SQLiteDatabase.loadLibs(mContext);
//		File databaseFile = mContext.getDatabasePath(DB_PATH);
//		databaseFile.mkdirs();
//		databaseFile.delete();
//		SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(databaseFile, "test123", null);database.execSQL("create table t1(a, b)");
//		database.execSQL("insert into t1(a, b) values(?, ?)", new Object[]{"one for the money",
//				"two for the show"});
    }

    void addContact(QuizPojo contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // values.put(KEY_ID, contact.get_id());
        values.put(KEY_QUESTION, contact.get_question());
        values.put(KEY_OPT1, contact.get_option1());
        values.put(KEY_OPT2, contact.get_option2());
        values.put(KEY_OPT3, contact.get_option3());
        values.put(KEY_OPT4, contact.get_option4());
        values.put(KEY_ANSWER, contact.get_answer());
        values.put(KEY_CATEGORYNAME, contact.getCategory_name());

        // Inserting Row
        db.insert(TABLE_CONTACTS, null, values);
        db.close(); // Closing database connection
    }

    // Getting single contact
    QuizPojo getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CONTACTS, new String[]{KEY_ID,
                        KEY_QUESTION, KEY_OPT1, KEY_OPT2, KEY_OPT3, KEY_OPT4,
                        KEY_ANSWER}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        QuizPojo contact = new QuizPojo(cursor.getString(1),
                cursor.getString(2), cursor.getString(3), cursor.getString(4),
                cursor.getString(5), cursor.getString(6), cursor.getString(7));
        // return contact
        cursor.close();
        db.close();
        return contact;
    }

    // Getting All Contacts
    public void deletealldata() {
        // Select All Query
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, null, null);
        db.close();
    }

    // Getting All Contacts
    public List<QuizPojo> getAllContacts() {
        List<QuizPojo> contactList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                QuizPojo contact = new QuizPojo();
                contact.set_id(Integer.parseInt(cursor.getString(0)));
                contact.set_question(cursor.getString(1));
                contact.set_option1(cursor.getString(2));
                contact.set_option2(cursor.getString(3));
                contact.set_option3(cursor.getString(4));
                contact.set_option4(cursor.getString(5));
                contact.set_answer(cursor.getString(6));

                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return contact list
        return contactList;
    }

    public String getCurCategory(String strQuestion) {
        String curCategory = "";

        //select category from quiz_master where question='The term Tee is connected with'
        String selectQuery = "select category from " + TABLE_CONTACTS + " where question='" + strQuestion + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                curCategory = cursor.getString(0);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return curCategory;
    }

    public ContentValues getCurPaymentMethod() {
        String selectQuery = "select * from payment_master where payID='1'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        ContentValues contentValues = new ContentValues();
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                String paymentkind = cursor.getString(1);
                String reg_date = cursor.getString(2);
                contentValues.put("paymentkind", paymentkind);
                contentValues.put("reg_date", reg_date);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return contentValues;
    }

    public String getFreeLife() {
        String selectQuery = "select * from payment_master where payID='1'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        String freeLife = "";

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                freeLife = cursor.getString(3);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return freeLife;
    }

    public boolean setCurFreeLife(String freeLife) {

        //String selectQuery = "UPDATE payment_master SET free_life='" + freeLife + "' WHERE payID='1'";
        SQLiteDatabase db = this.getWritableDatabase();
        //Cursor cursor = db.rawQuery(selectQuery, null);

        //int nTemp = cursor.getColumnCount();
        ContentValues contentValues = new ContentValues();
        contentValues.put("free_life", freeLife);

        try {
            db.update("payment_master", contentValues, "payID = ?", new String[]{"1"});
        } catch (Exception ex) {
            ex.getMessage();
            return false;
        }

        //cursor.close();
        db.close();

        return true;
    }

    public boolean setCurPaymentMethod(String paymentKind, String regDate) {

        String selectQuery = "UPDATE payment_master SET paymentkind='" + paymentKind + "', reg_date='" + regDate + "' WHERE payID='1'";

        SQLiteDatabase db = this.getWritableDatabase();
        //Cursor cursor = db.rawQuery(selectQuery, null);
        //int nTemp = cursor.getColumnCount();

        ContentValues contentValues = new ContentValues();
        contentValues.put("paymentkind", paymentKind);
        contentValues.put("reg_date", regDate);

        try {
            db.update("payment_master", contentValues, "payID = ?", new String[]{"1"});
        } catch (Exception ex) {
            ex.getMessage();
            return false;
        }

        //cursor.close();
        db.close();

        return true;
    }

    public List<QuizPojo> getCategory() {
        List<QuizPojo> categoryList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT DISTINCT category FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                QuizPojo contact = new QuizPojo();
                contact.setCategory_name(cursor.getString(0));
                categoryList.add(contact);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return contact list
        return categoryList;
    }

    public List<QuizPojo> getQuestion(String categoryname) {
        List<QuizPojo> questionList = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT DISTINCT question, opt1, opt2, opt3, opt4, answer FROM "
                + TABLE_CONTACTS + " where " + KEY_CATEGORYNAME + "= ? ORDER BY RANDOM()";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, new String[]{categoryname});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                QuizPojo contact = new QuizPojo();
                contact.set_question(cursor.getString(0));
                contact.set_option1(cursor.getString(1));
                contact.set_option2(cursor.getString(2));
                contact.set_option3(cursor.getString(3));
                contact.set_option4(cursor.getString(4));
                contact.set_answer(cursor.getString(5));

                // Adding contact to list
                questionList.add(contact);
            } while (cursor.moveToNext());
        }
        // return contact list
        return questionList;
    }

    public int getCategoryCount() {
        String countQuery = "SELECT * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        // return count
        return cursor.getCount();
    }

    public int getCategoryCount(String categoryname) {
        String selectQuery = "SELECT DISTINCT question, opt1, opt2, opt3, opt4, answer FROM "
                + TABLE_CONTACTS + " where " + KEY_CATEGORYNAME + "= ? ORDER BY RANDOM()";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, new String[]{categoryname});
        // return count
        return cursor.getCount();
    }
}