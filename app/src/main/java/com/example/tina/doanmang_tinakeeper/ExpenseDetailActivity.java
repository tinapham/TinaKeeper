package com.example.tina.doanmang_tinakeeper;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.tina.doanmang_tinakeeper.model.Expense;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.sql.Date;
import java.util.List;

public class ExpenseDetailActivity extends AppCompatActivity {
    private static final int MY_REQUEST_CODE = 1000;
    Expense expense;
    TextView txtMoney,txtNote,txtDate,txtCategory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_detail);
        getFormWidget();

        Intent intent = this.getIntent();
        Gson gson = new Gson();
        String strObj = intent.getStringExtra("expense");
        Type type = new TypeToken<Expense>(){}.getType();
        expense = gson.fromJson(strObj, type);
        Log.i("Detail",expense.getCategory());
        try{
            txtMoney.setText("$"+String.valueOf(expense.getMoney()));
            txtCategory.setText(expense.getCategory());
            txtDate.setText(expense.getDate().toString());
            txtNote.setText(expense.getNotes());
        } catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    public void getFormWidget(){
        txtMoney = (TextView) findViewById(R.id.textMoney);
        txtNote = (TextView) findViewById(R.id.textNotes);
        txtDate = (TextView) findViewById(R.id.textCalendar);
        txtCategory = (TextView) findViewById(R.id.textCategory);
    }

    // Người dùng Click vào nút Save.
    public void buttonCancelClicked(View view)  {
        // Không làm gì, trở về MainActivity.
        this.onBackPressed();
    }
    public void buttonEditClicked(View view) {
        Intent intent = new Intent(this, AddEditExpenseActivity.class);
        Gson gson = new Gson();
        intent.putExtra("expense", gson.toJson(expense));
        startActivityForResult(intent,MY_REQUEST_CODE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            boolean needRefresh = data.getBooleanExtra("needRefresh",true);
            Gson gson = new Gson();
            String strObj = data.getStringExtra("expenseReturn");
            expense = gson.fromJson(strObj, Expense.class);
            // Refresh ListView
            if(needRefresh) {
                txtMoney.setText("$"+String.valueOf(expense.getMoney()));
                txtCategory.setText(expense.getCategory());
                txtDate.setText(expense.getDate().toString());
                txtNote.setText(expense.getNotes());
            }
        }
    }
}
