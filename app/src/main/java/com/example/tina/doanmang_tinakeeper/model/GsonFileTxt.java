package com.example.tina.doanmang_tinakeeper.model;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.example.tina.doanmang_tinakeeper.adapter.MyDatabaseHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by TiNa on 11/05/2017.
 */

public class GsonFileTxt {
    private Context context;
    private String fileName;

    public GsonFileTxt(Context context, String fileName) {
        this.context = context;
        this.fileName = fileName;
    }

    public void writeGson(){
        MyDatabaseHelper db = new MyDatabaseHelper(context);
        List<Expense> list = db.getAllExpense();
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        String string = gson.toJson(list);
        try {
            File myFile = new File(Environment.getExternalStorageDirectory() + "/database.txt");
            myFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(myFile);
            OutputStreamWriter myOutWriter =new OutputStreamWriter(fOut);
            myOutWriter.append(string);
            myOutWriter.close();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public List<Expense> readGson(){
        List<Expense> expenseList = new ArrayList<Expense>();
        try {
            File myFile = new File(Environment.getExternalStorageDirectory() + "/database.txt");
            FileInputStream fIn = new FileInputStream(myFile);
            BufferedReader myReader = new BufferedReader(new InputStreamReader(fIn));
            String aDataRow = "";
            String aBuffer = ""; //Holds the text
            while ((aDataRow = myReader.readLine()) != null)
            {
                aBuffer += aDataRow ;
            }
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
            expenseList = Arrays.asList(gson.fromJson(aBuffer, Expense[].class));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return expenseList;
    }
}
