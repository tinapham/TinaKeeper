package com.example.tina.doanmang_tinakeeper;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.tina.doanmang_tinakeeper.adapter.MyDatabaseHelper;
import com.example.tina.doanmang_tinakeeper.model.Expense;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddEditExpense extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
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

        txtMoney = (EditText) findViewById(R.id.txt_money);
        txtNote = (EditText) findViewById(R.id.txt_note);
        txtDate = (EditText) findViewById(R.id.txt_date);
        btnDate = (ImageButton) findViewById(R.id.btn_date);


        //Set ngày giờ hiện tại khi mới chạy lần đầu
        cal=Calendar.getInstance();
        SimpleDateFormat dft=null;
        //Định dạng kiểu ngày / tháng /năm
        dft=new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String textDate=dft.format(cal.getTime());
        //hiển thị lên giao diện
        txtDate.setText(textDate);
        //show cửa sổ datepicker
        btnDate.setOnClickListener(showDatePicker);
        txtDate.setOnClickListener(showDatePicker);


        //Spinner list category
        spinnerCategory = (Spinner) findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        listCategory = getResources().getStringArray(R.array.cate_array);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.cate_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerCategory.setAdapter(adapter);
        spinnerCategory.setOnItemSelectedListener(this);

//        Intent intent = this.getIntent();
//        this.expense = (Expense) intent.getSerializableExtra("expense");
//        if(expense== null)  {
//            this.mode = MODE_CREATE;
//        } else  {
//            this.mode = MODE_EDIT;
//            this.txtMoney.setText(expense.getMoney());
//            this.txtDate.setText(String.valueOf(expense.getDate()));
//            this.txtNote.setText(expense.getNotes());
//            //this.spinnerCategory.set
//        }
    }
    //Class tạo sự kiện

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        textCategory= listCategory[position];
        Log.i(TAG,textCategory);
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
                    AddEditExpense.this,
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
        int money;
        if(this.txtMoney.getText().toString().equals("")){
            money =0;
        } else {
            money = Integer.parseInt(this.txtMoney.getText().toString());
        }
        Date day = new Date(date.getTime());
        String note = this.txtNote.getText().toString();
        String category = this.textCategory;
        int id = db.getExpenseCount()+1;
        Log.i(TAG,String.valueOf(id));

        if(money ==0){
            Toast.makeText(getBaseContext(),
                    "Please enter value", Toast.LENGTH_LONG).show();
            return;
        }

        this.expense= new Expense(id,category,note,money,day);
        db.addExpense(expense);


//        if(mode==MODE_CREATE ) {
//            this.expense= new Expense(id,category,note,money,new Date(dateStr));
//            db.addExpense(expense);
//        } else  {
//            this.expense.setMoney(money);
//            this.expense.setCategory(category);
//            this.expense.setNotes(note);
//            this.expense.setCategory(category);
//            db.updateExpense(expense);
//        }

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

        // Activity đã hoàn thành OK, trả về dữ liệu.
        this.setResult(Activity.RESULT_OK, data);
        super.finish();
    }
}
