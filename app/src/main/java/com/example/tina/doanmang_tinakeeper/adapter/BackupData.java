package com.example.tina.doanmang_tinakeeper.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.tina.doanmang_tinakeeper.MainActivity;
import com.example.tina.doanmang_tinakeeper.model.Expense;

import java.util.List;

/**
 * Created by TiNa on 11/05/2017.
 */

public class BackupData extends AsyncTask<Void,Void,Void> {
    //Declaring Variables
    private Context context;

    //Progressdialog to show while sending email
    private ProgressDialog progressDialog;

    public BackupData(Context context) {
        this.context = context;
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Synchronization");
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.show();
    }
    @Override
    protected Void doInBackground(Void... params) {
        try{
            //read Gson to list Expense
            GsonFileTxt gsonFileTxt = new GsonFileTxt(context,"database.txt");
            List<Expense> list = gsonFileTxt.readGson();
            //Write to database
            MyDatabaseHelper db = new MyDatabaseHelper(context);
            for(int i=0;i<list.size();i++) {
                try {
                    db.addExpense(list.get(i));
                } catch (SQLiteException e){
                    e.printStackTrace();
                }
                Log.i("Main Activity", list.get(i).getCategory());
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onProgressUpdate(Void... values) {
        Intent intent = new Intent(context,MainActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void onPostExecute(Void aVoid) {
        //Dismissing the progress dialog
        progressDialog.dismiss();
        //Showing a success message
        Toast.makeText(context,"Done", Toast.LENGTH_LONG).show();
    }
}