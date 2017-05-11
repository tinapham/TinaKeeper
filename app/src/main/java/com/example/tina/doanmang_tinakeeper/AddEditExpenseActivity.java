package com.example.tina.doanmang_tinakeeper;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.tina.doanmang_tinakeeper.adapter.MyDatabaseHelper;
import com.example.tina.doanmang_tinakeeper.model.Expense;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

public class AddEditExpenseActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String TAG = "AddExpense";
    private Calendar cal;
    private java.util.Date date;
    private ImageButton btnDate;
    private EditText txtMoney;
    private EditText txtDate;
    private EditText txtNote;
    private Spinner spinnerCategory;
    Expense expense;
    private static final int MODE_CREATE = 1;
    private static final int MODE_EDIT = 2;
    private boolean needRefresh;
    private String textCategory;
    private String[] listCategory;

    private int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_expense);
        getFormWidget();

        Intent intent = this.getIntent();
        Gson gson = new Gson();
        String strObj = intent.getStringExtra("expense");
        expense = gson.fromJson(strObj, Expense.class);

        //Set ngày giờ hiện tại khi mới chạy lần đầu
        cal=Calendar.getInstance();
        SimpleDateFormat dft=new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String textDate=dft.format(cal.getTime());
        date = cal.getTime();
        //hiển thị lên giao diện
        txtDate.setText(textDate);
        //show cửa sổ datepicker
        btnDate.setOnClickListener(showDatePicker);
        txtDate.setOnClickListener(showDatePicker);

        //Spinner list category
        // Create an ArrayAdapter using the string array and a default spinner layout
        listCategory = getResources().getStringArray(R.array.cate_array);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.cate_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerCategory.setAdapter(adapter);
        spinnerCategory.setOnItemSelectedListener(this);

        if(expense== null)  {
            this.mode = MODE_CREATE;
        } else  {
            this.mode = MODE_EDIT;
            String result = String.valueOf(dft.format(expense.getDate()));
            this.txtMoney.setText(String.valueOf(expense.getMoney()));
            this.txtDate.setText(result);
            this.txtNote.setText(expense.getNotes());
            this.spinnerCategory.setSelection(getIndex(spinnerCategory, expense.getCategory()));
        }
    }
    public void getFormWidget(){
        txtMoney = (EditText) findViewById(R.id.txt_money);
        txtNote = (EditText) findViewById(R.id.txt_note);
        txtDate = (EditText) findViewById(R.id.txt_date);
        btnDate = (ImageButton) findViewById(R.id.btn_date);
        spinnerCategory = (Spinner) findViewById(R.id.spinner);
    }
    //get Index for Spinner
    private int getIndex(Spinner spinner, String myString){
        int index = 0;
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).equals(myString)){
                index = i;
            }
        }
        return index;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        textCategory= listCategory[position];
    }

    public void onNothingSelected(AdapterView<?> parent) {
        textCategory= listCategory[0];
    }

    //hàm hiển thị cửa sổ ngày tháng
    View.OnClickListener showDatePicker = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            DatePickerDialog.OnDateSetListener callback = new DatePickerDialog.OnDateSetListener() {
                //Sự kiện khi click vào nút Done trên Dialog
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    // Set text cho textView
                    String textDate= day + "-" + (month + 1) + "-" + year;
                    txtDate.setText(textDate);
//                    //Lưu vết lại ngày mới cập nhật
                    cal.set(year, month, day);
                    date = cal.getTime();
//                    Log.i(TAG, test.toString());
//                    Log.i(TAG,textDate);
                }
            };
            String s = txtDate.getText() + "";
            //Lấy ra chuỗi của editText Date
            String strArrtmp[] = s.split("-");
            int ngay = Integer.parseInt(strArrtmp[0]);
            int thang = Integer.parseInt(strArrtmp[1]) - 1;
            int nam = Integer.parseInt(strArrtmp[2]);
            //Hiển thị ra Dialog
            DatePickerDialog pic = new DatePickerDialog(
                    AddEditExpenseActivity.this,
                    callback, nam, thang, ngay);
            pic.show();
        }
    };

    // Khi người dùng Click vào button Cancel.
    public void buttonCancelClicked(View view)  {
        // Không làm gì, trở về MainActivity.
        this.onBackPressed();
    }

    // Người dùng Click vào nút Save.
    public void buttonSaveClicked(View view)  {
        MyDatabaseHelper db = new MyDatabaseHelper(this);
        //đưa dữ liệu để lưu trữ vào database
        long money;
        if(this.txtMoney.getText().toString().equals("")){
            money =0;
        } else {
            money = Long.parseLong(this.txtMoney.getText().toString());
        }
        Date day = new Date(date.getTime());
        String note = this.txtNote.getText().toString();
        String category = this.textCategory;
        if(mode==MODE_CREATE ) {
            try{
                Random rn = new Random();
                int id = rn.nextInt(16000);
                if(money ==0){
                    Toast.makeText(getBaseContext(),
                            "Please enter value", Toast.LENGTH_LONG).show();
                    return;
                }
                this.expense= new Expense(id,category,note,money,day);
                db.addExpense(expense);
            } catch (Exception e){
                e.printStackTrace();
            }
        } else  {
            this.expense.setMoney(money);
            this.expense.setCategory(category);
            this.expense.setNotes(note);
            this.expense.setDate(day);
            db.updateExpense(expense);
        }
        this.needRefresh = true;
        // Trở lại MainActivity.
        this.onBackPressed();

    }
//     Khi Activity này hoàn thành,
//     có thể cần gửi phản hồi gì đó về cho Activity đã gọi nó.
    @Override
    public void finish() {
        // Chuẩn bị dữ liệu Intent.
        Intent data = new Intent();
        // Yêu cầu MainActivity refresh lại ListView hoặc không.
        data.putExtra("needRefresh", needRefresh);
        Gson gson = new Gson();
        data.putExtra("expenseReturn", gson.toJson(expense));
        // Activity đã hoàn thành OK, trả về dữ liệu.
        this.setResult(Activity.RESULT_OK, data);
        super.finish();
    }
}
