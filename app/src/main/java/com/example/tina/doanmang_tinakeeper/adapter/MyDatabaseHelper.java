package com.example.tina.doanmang_tinakeeper.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.tina.doanmang_tinakeeper.model.Expense;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class MyDatabaseHelper extends SQLiteOpenHelper  {
    private static final String TAG = "SQLite";
    // Phiên bản
    private static final int DATABASE_VERSION = 1;
    // Tên cơ sở dữ liệu.
    private static final String DATABASE_NAME = "Expense_Manager";
    // Tên bảng: Expense.
    private static final String TABLE_EXPENSE = "Expense";
    private static final String COLUMN_EXPENSE_ID ="Expense_Id";
    private static final String COLUMN_EXPENSE_CATEGORY ="Expense_Category";
    private static final String COLUMN_EXPENSE_NOTES = "Expense_Notes";
    private static final String COLUMN_EXPENSE_MONEY = "Expense_Money";
    private static final String COLUMN_EXPENSE_DATE = "Expense_Date";

    public MyDatabaseHelper(Context context)  {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "MyDatabaseHelper.onCreate ... ");
        // Script tạo bảng.
        try{
            String script = "CREATE TABLE " + TABLE_EXPENSE + "("
                    + COLUMN_EXPENSE_ID + " INTEGER PRIMARY KEY," + COLUMN_EXPENSE_CATEGORY + " NVARCHAR(50),"
                    + COLUMN_EXPENSE_NOTES + " NVARCHAR(100)," + COLUMN_EXPENSE_MONEY + " INTEGER,"
                    + COLUMN_EXPENSE_DATE + " TEXT"+")";
            // Chạy lệnh tạo bảng.
            db.execSQL(script);
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "MyDatabaseHelper.onUpgrade ... ");
        try{
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSE);
            // Và tạo lại.
            onCreate(db);
        } catch (Exception e){
            e.printStackTrace();
        }

    }
    // Nếu trong bảng Note chưa có dữ liệu,
    // Trèn vào mặc định 2 bản ghi.
    public void createDefaultExpenseIfNeed() {
//        int count = this.getExpenseCount();
//        if(count ==0 ) {
//            Expense note1 = new Expense(1,"Food", "Eating out",20000, Date.valueOf("2017-03-11"));
//            Expense note2 = new Expense(2,"Salary", "Salary of February",1000000,Date.valueOf("2017-03-27"));
//            this.addExpense(note1);
//            this.addExpense(note2);
//        }


        int count = this.getExpenseCount();
        if(count ==0 ) {
            Expense note1 = new Expense(1,"Food", "Eating out",20000, Date.valueOf("2017-03-11"));
            Expense note2 = new Expense(2,"Food", "Zé house",100000, Date.valueOf("2017-04-17"));
            this.addExpense(note1);
            this.addExpense(note2);
        }
    }
    //đếm số bản ghi
    public int getExpenseCount() {
        Log.i(TAG, "MyDatabaseHelper.getNotesCount ... " );

        String countQuery = "SELECT  * FROM " + TABLE_EXPENSE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();

        // return count
        return count;
    }

    //thêm bản ghi
    public void addExpense(Expense expense) {
        Log.i(TAG, "MyDatabaseHelper.addNote ... " + expense.getDateString());

        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "INSERT INTO EXPENSE VALUES (" + expense.getId()+",'"+expense.getCategory()+"','"
                + expense.getNotes()+"',"+expense.getMoney()+",'"+expense.getDateString()+"')";

        db.execSQL(sql);
        // Đóng kết nối database.
        db.close();

//        void addContact(Expense expense) {
//            SQLiteDatabase db = this.getWritableDatabase();
//
//            ContentValues values = new ContentValues();
//            values.put(KEY_CATEGORY, expense.getCategory()); // Expense Name
//            values.put(KEY_NOTES, expense.getNotes()); // Expense Phone
//
//            // Inserting Row
//            db.insert(TABLE_EXPENSE, null, values);
//            db.close(); // Closing database connection
//        }
    }

    public Expense getExpense(int id) {
        Log.i(TAG, "MyDatabaseHelper.getNote ... " + id);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_EXPENSE, new String[] { COLUMN_EXPENSE_ID,
                        COLUMN_EXPENSE_CATEGORY, COLUMN_EXPENSE_NOTES,COLUMN_EXPENSE_MONEY,
                        COLUMN_EXPENSE_DATE }, COLUMN_EXPENSE_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Expense expense = new Expense(Integer.parseInt(cursor.getString(0)),cursor.getString(1),
                    cursor.getString(2), Integer.parseInt(cursor.getString(3)), Date.valueOf(cursor.getString(4)));
        cursor.close();
        db.close();
        return expense;
    }

    public List<Expense> getAllExpense() {
        Log.i(TAG, "MyDatabaseHelper.getAllNotes ... " );

        List<Expense> list = new ArrayList<Expense>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_EXPENSE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        // Duyệt trên con trỏ, và thêm vào danh sách.
        if (cursor.moveToFirst()) {
            do {
                Expense expense = new Expense();
                expense.setId(Integer.parseInt(cursor.getString(0)));
                expense.setCategory(cursor.getString(1));
                expense.setNotes(cursor.getString(2));
                expense.setMoney(cursor.getDouble(3));
//                Date date = cursor.getString(4);
                Log.i(TAG,cursor.getString(4));
                expense.setDate(Date.valueOf(cursor.getString(4)));
                Log.i(TAG,expense.toString());
                // Thêm vào danh sách.
                list.add(expense);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return note list
        return list;
    }
    public int updateExpense(Expense Expense){
        Log.i(TAG, "MyDatabaseHelper.updateExpense ... "  + Expense.getCategory());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_EXPENSE_ID,Expense.getId());
        values.put(COLUMN_EXPENSE_CATEGORY, Expense.getCategory());
        values.put(COLUMN_EXPENSE_NOTES, Expense.getNotes());
        values.put(COLUMN_EXPENSE_MONEY, Expense.getMoney());
        values.put(COLUMN_EXPENSE_DATE, String.valueOf(Expense.getDate()));

        // updating row
        return db.update(TABLE_EXPENSE, values, COLUMN_EXPENSE_ID + " = ?",
                new String[]{String.valueOf(Expense.getId())});
    }

    public void deleteExpense(Expense Expense){
        Log.i(TAG, "MyDatabaseHelper.updateExpense ... " + Expense.getCategory() );

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EXPENSE, COLUMN_EXPENSE_ID + " = ?",
                new String[] { String.valueOf(Expense.getId()) });
        db.close();
    }

    public void deleteExpense(int id){
        Log.i(TAG, "MyDatabaseHelper.updateExpense ... " + id );

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EXPENSE, COLUMN_EXPENSE_ID + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
    }
}