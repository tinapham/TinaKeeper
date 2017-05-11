package com.example.tina.doanmang_tinakeeper;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.tina.doanmang_tinakeeper.adapter.GsonFileTxt;
import com.example.tina.doanmang_tinakeeper.adapter.SendMail;
import com.example.tina.doanmang_tinakeeper.model.Expense;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class SendMailFragment extends Fragment {
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
        GsonFileTxt gsonFileTxt = new GsonFileTxt(getActivity(),"database.txt");
        gsonFileTxt.writeGson();
        String subject="FoodList";
        String message="";
        //Creating SendMail object
        SendMail sm = new SendMail(getActivity(), "phamthiquynhanh172@gmail.com", subject, message);
        //Executing sendmail to send email
        sm.execute();
    }
}
