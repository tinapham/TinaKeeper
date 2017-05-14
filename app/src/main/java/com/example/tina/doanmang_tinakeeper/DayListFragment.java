package com.example.tina.doanmang_tinakeeper;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.tina.doanmang_tinakeeper.adapter.MyDatabaseHelper;
import com.example.tina.doanmang_tinakeeper.adapter.RecyclerDataAdapter;
import com.example.tina.doanmang_tinakeeper.model.Expense;
import com.github.clans.fab.FloatingActionButton;
import com.google.gson.Gson;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


/**
 * Created by TiNa on 06/03/2017.
 */

public class DayListFragment extends Fragment {
    private static final String TAG = "DAYLIST";
    private static final int MENU_ITEM_VIEW = 111;
    private static final int MENU_ITEM_EDIT = 222;
    private static final int MENU_ITEM_CREATE = 333;
    private static final int MENU_ITEM_DELETE = 444;
    private static final int MY_REQUEST_CODE = 1000;
    private TextView btnGetDate;
    private TextView txtTotal;
    private View view;
    private RecyclerView recyclerview;
    private RecyclerDataAdapter adapter;
    private Calendar cal;
    private java.util.Date date;
    private final List<Expense> expenseList = new ArrayList<Expense>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_day_list, container, false);
        getFormWidgets();
        getDefaultInfor();
        btnGetDate.setOnClickListener(showDatePicker);

        //cấu hình database
        try{
            this.expenseList.clear();
            MyDatabaseHelper db = new MyDatabaseHelper(getActivity());
            Log.i(TAG,new Date(date.getTime()).toString());
            List<Expense> list=  db.getExpenseByDate(new Date(date.getTime()));
            calculateMoney(list);

            this.expenseList.addAll(list);
        } catch (Exception e){
            e.printStackTrace();
        }

        //Xử lý nút (+)
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddEditExpenseActivity.class);
                startActivityForResult(intent,MENU_ITEM_CREATE);
            }
        });

        //cấu hình cho recyclerview
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerview.setLayoutManager(layoutManager);

        adapter = new RecyclerDataAdapter(getActivity(), expenseList);
        adapter.setOnItemClickListener(new RecyclerDataAdapter.ClickListener(){
            @Override
            public void onItemClick(int position, View v) {
                Gson gson = new Gson();
                Intent intent = new Intent(getActivity(), ExpenseDetailActivity.class);
                intent.putExtra("expense", gson.toJson(expenseList.get(position)));
                startActivityForResult(intent,MENU_ITEM_VIEW);
            }
        });
        recyclerview.setAdapter(adapter);
        return view;
    }
    public void getFormWidgets(){
        btnGetDate= (TextView) view.findViewById(R.id.button_getdate);
        recyclerview = (RecyclerView) view.findViewById(R.id.list);
        txtTotal = (TextView) view.findViewById(R.id.txt_total);
    }
    public void getDefaultInfor() {
        //Set ngày giờ hiện tại khi mới chạy lần đầu
        cal=Calendar.getInstance();
        date = cal.getTime();
        SimpleDateFormat dft = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String strDate=dft.format(cal.getTime());
//        hiển thị lên giao diện
        btnGetDate.setText(strDate);
    }

    View.OnClickListener showDatePicker = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            DatePickerDialog.OnDateSetListener callback = new DatePickerDialog.OnDateSetListener() {
                //Sự kiện khi click vào nút Done trên Dialog
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    // Set text cho textView
                    btnGetDate.setText(day + "/" + (month + 1) + "/" + year);
                    //Lưu vết lại ngày mới cập nhật
                    cal.set(year, month, day);
                    date = cal.getTime();
                    //Load lại list theo ngày
                    reloadList(new Date(date.getTime()));
                    adapter.notifyDataSetChanged();
                }
            };
            String s = btnGetDate.getText() + "";
            //Lấy ra chuỗi của textView Date
            String strArrtmp[] = s.split("/");
            int ngay = Integer.parseInt(strArrtmp[0]);
            int thang = Integer.parseInt(strArrtmp[1]) - 1;
            int nam = Integer.parseInt(strArrtmp[2]);
            //Hiển thị ra Dialog
            DatePickerDialog pic = new DatePickerDialog(getContext(), callback, nam, thang, ngay);
            pic.setCanceledOnTouchOutside(true);
            pic.closeOptionsMenu();
            pic.show();
        }
    };

    // Người dùng đồng ý xóa một Expense.
    private void deleteExpense(Expense expense)  {
        MyDatabaseHelper db = new MyDatabaseHelper(getContext());
        db.deleteExpense(expense);
        this.expenseList.remove(expense);
        // Refresh ListView.
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            boolean needRefresh = data.getBooleanExtra("needRefresh",true);
            // Refresh ListView
            if(needRefresh) {
                reloadList(new Date(date.getTime()));
                adapter.notifyDataSetChanged();
            }
        }
    }

    public void reloadList(Date date){
        expenseList.clear();
        MyDatabaseHelper db = new MyDatabaseHelper(getActivity());
        List<Expense> list=  db.getExpenseByDate(date);
        calculateMoney(list);
        expenseList.addAll(list);
    }

    public void calculateMoney(List<Expense> list){
        long income=0;
        long expense=0;
        for(int i=0;i<list.size();i++){
            String category = list.get(i).getCategory();
            long money = list.get(i).getMoney();
            if(category.equals("Deposits")||category.equals("Salary")||category.equals("Savings")){
                income+=money;
            } else {
                expense+=money;
            }
        }
        long total = income - expense;
        if(total>=0){
            txtTotal.setText("$"+String.valueOf(total));
        } else {
            txtTotal.setText("-$"+String.valueOf(Math.abs(total)));
        }
    }

}
