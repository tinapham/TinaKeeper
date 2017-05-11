package com.example.tina.doanmang_tinakeeper;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.TypedArray;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.tina.doanmang_tinakeeper.adapter.MyDatabaseHelper;
import com.example.tina.doanmang_tinakeeper.adapter.RecyclerDataAdapter;
import com.example.tina.doanmang_tinakeeper.model.Expense;
import com.github.clans.fab.FloatingActionButton;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


/**
 * Created by TiNa on 06/03/2017.
 */

public class DayList extends Fragment {
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
                Intent intent = new Intent(getActivity(), AddEditExpense.class);
                startActivityForResult(intent,MY_REQUEST_CODE);
            }
        });

        //cấu hình cho recyclerview
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerview.setLayoutManager(layoutManager);

        adapter = new RecyclerDataAdapter(getContext(), expenseList);
        recyclerview.setAdapter(adapter);
        // Đăng ký Context menu cho recyclerview.
        registerForContextMenu(this.recyclerview);
        return view;
    }
    public void getFormWidgets(){
        btnGetDate= (TextView) view.findViewById(R.id.button_getdate);
        recyclerview = (RecyclerView) view.findViewById(R.id.rv_list_day);
        txtTotal = (TextView) view.findViewById(R.id.txt_total);
    }
    public void getDefaultInfor() {
        //Set ngày giờ hiện tại khi mới chạy lần đầu
        cal=Calendar.getInstance();
        date = cal.getTime();
        SimpleDateFormat dft=null;
        dft=new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String strDate=dft.format(cal.getTime());
//        hiển thị lên giao diện
        btnGetDate.setText(strDate);
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View view,
                                    ContextMenu.ContextMenuInfo menuInfo)    {

        super.onCreateContextMenu(menu, view, menuInfo);
        menu.setHeaderTitle("Select The Action");

        // groupId, itemId, order, title
        menu.add(0, MENU_ITEM_VIEW , 0, "View Expense");
        menu.add(0, MENU_ITEM_CREATE , 1, "Create Expense");
        menu.add(0, MENU_ITEM_EDIT , 2, "Edit Expense");
        menu.add(0, MENU_ITEM_DELETE, 4, "Delete Expense");
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

//    @Override
//    public boolean onContextItemSelected(MenuItem item){
//        AdapterView.AdapterContextMenuInfo
//                info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//
//        final Expense selectedExpense = (Expense) this.adapter.getItem(info.position);
//
//        if(item.getItemId() == MENU_ITEM_VIEW){
//            //Toast.makeText(getContext(),selectedExpense.(), Toast.LENGTH_LONG).show();
//        }
//        else if(item.getItemId() == MENU_ITEM_CREATE){
//            Intent intent = new Intent(getContext(), AddEditExpense.class);
//
//            // Start AddEditExpenseActivity, có phản hồi.
//            this.startActivityForResult(intent, MY_REQUEST_CODE);
//        }
//        else if(item.getItemId() == MENU_ITEM_EDIT ){
//            Intent intent = new Intent(getContext(), AddEditExpense.class);
//            intent.putExtra("expense", selectedExpense.toString());
//
//            // Start AddEditExpenseActivity, có phản hồi.
//            this.startActivityForResult(intent,MY_REQUEST_CODE);
//        }
//        else if(item.getItemId() == MENU_ITEM_DELETE){
//            // Hỏi trước khi xóa.
//            new AlertDialog.Builder(getContext())
//                    .setMessage(selectedExpense.getMoney()+". Are you sure you want to delete?")
//                    .setCancelable(false)
//                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            deleteExpense(selectedExpense);
//                        }
//                    })
//                    .setNegativeButton("No", null)
//                    .show();
//        }
//        else {
//            return false;
//        }
//        return true;
//    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == MY_REQUEST_CODE ) {
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
        long total=0;
        for(int i=0;i<list.size();i++){
            String category = list.get(i).getCategory();
            long money = list.get(i).getMoney();
            if(category.equals("Deposits")||category.equals("Salary")||category.equals("Savings")){
                income+=money;
            } else {
                expense+=money;
            }
        }
        total = income - expense;
        if(total>=0){
            txtTotal.setText("$"+String.valueOf(total));
        } else {
            txtTotal.setText("-$"+String.valueOf(Math.abs(total)));
        }
    }

}
