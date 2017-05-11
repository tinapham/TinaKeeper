package com.example.tina.doanmang_tinakeeper;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tina.doanmang_tinakeeper.adapter.MyDatabaseHelper;
import com.example.tina.doanmang_tinakeeper.model.Expense;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class SendMail extends Fragment {
    private View view;
    private EditText txtGetDate;
    private Button startBtn;
    private Calendar cal;
    List<Expense> expenseList = new ArrayList<Expense>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_send_mail, container, false);
        startBtn = (Button) view.findViewById(R.id.btn_send);
        startBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                sendEmail();
            }
        });





        txtGetDate = (EditText) view.findViewById(R.id.txt_get_date);
        //        cau hinh cho button lay ngay thang nam
        //Set ngày giờ hiện tại khi mới chạy lần đầu
        cal= Calendar.getInstance();
        SimpleDateFormat dft=new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String strDate=dft.format(cal.getTime());
//        hiển thị lên giao diện
        txtGetDate.setText(strDate);
        txtGetDate.setOnClickListener(showDatePicker);
        return view;
    }
    View.OnClickListener showDatePicker = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            DatePickerDialog.OnDateSetListener callback = new DatePickerDialog.OnDateSetListener() {
                //Sự kiện khi click vào nút Done trên Dialog
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    // Set text cho textView
                    txtGetDate.setText(day + "/" + (month + 1) + "/" + year);
                    //Lưu vết lại ngày mới cập nhật
                    cal.set(year, month, day);
                    //date = cal.getTime();
                }
            };
            String s = txtGetDate.getText() + "";
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
    protected void sendEmail() {
        readGson();


//        Log.i("Send email", "");
//        String[] TO = {""};
//        String[] CC = {""};
//        Intent emailIntent = new Intent(Intent.ACTION_SEND);
//
//        emailIntent.setData(Uri.parse("mailto:"));
//        emailIntent.setType("text/plain");
//        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
//        emailIntent.putExtra(Intent.EXTRA_CC, CC);
//        emailIntent.putExtra(Intent.EXTRA_SUBJECT, txtGetDate.getText());
//        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");
//
//        try {
//            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
//            Log.i("Finished sending email","");
//        }
//        catch (android.content.ActivityNotFoundException ex) {
//            Toast.makeText(getContext(), "There is no email client installed.", Toast.LENGTH_SHORT).show();
//        }
    }
    
    public void writeGson(){
        MyDatabaseHelper db = new MyDatabaseHelper(getActivity());
        List<Expense> list = db.getAllExpense();
        Gson gson = new Gson();
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
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    public void readGson(){
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
            Gson gson = new Gson();
            expenseList = Arrays.asList(gson.fromJson(aBuffer, Expense[].class));
            Toast.makeText(getContext(), expenseList.get(5).getCategory(), Toast.LENGTH_LONG).show();
        }
        catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }
}
