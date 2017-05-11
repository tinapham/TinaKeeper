package com.example.tina.doanmang_tinakeeper.model;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.example.tina.doanmang_tinakeeper.model.Config;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;

/**
 * Created by TiNa on 10/05/2017.
 */

//Class is extending AsyncTask because this class is going to perform a networking operation
public class ReceiveMail extends AsyncTask<Void,Void,Void> {
    Context context;
    StringBuilder sb;
    Store store;
    Message[] msgs;
    Multipart m;
    BodyPart bp;
    String disposition, fileName;
    InputStream base64dec;
    String errorMsg;

    public ReceiveMail(Context context) {
        this.context = context;
    }

    //Progressdialog to show while sending email
    private ProgressDialog progressDialog;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //Showing progress dialog while sending email
        progressDialog = ProgressDialog.show(context,"Receiving message","Please wait...",false,false);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //Dismissing the progress dialog
        progressDialog.dismiss();
        //Showing a success message
        Toast.makeText(context,"Message Received", Toast.LENGTH_LONG).show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "imaps");
        props.setProperty("mail.imaps.host", "imaps.gmail.com");
        props.setProperty("mail.imaps.port", "993");
        props.setProperty("mail.imaps.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.imaps.socketFactory.fallback", "false");

        Session s = Session.getInstance(props);
        sb = new StringBuilder();
        try{
            Store store = s.getStore("imaps");
            store.connect("imap.gmail.com", Config.EMAIL, Config.PASSWORD);
            Folder inbox = store.getFolder("Inbox");
            inbox.open(Folder.READ_WRITE);
            Message[] msgs =inbox.getMessages();
            m=(Multipart)msgs[inbox.getMessageCount()-1].getContent();
            for(int i=0;i<m.getCount();i++) {
                bp = m.getBodyPart(i);
                disposition = bp.getDisposition();
                if(disposition!=null && (disposition.equals("ATTACHMENT"))){
                    fileName = bp.getFileName();
                    Log.i("Receive",fileName);
                    String str = (String)bp.getContent();
                    base64dec = new ByteArrayInputStream(str.getBytes());
                    String dir = Environment.getExternalStorageDirectory().getAbsolutePath();
                    OutputStream output = new FileOutputStream(dir+"/"+fileName);
                    byte data[] = new byte[8192];
                    long total = 0;
                    int count;
                    while ((count = base64dec.read(data)) != -1){
                        total += count;
                        output.write(data, 0, count);
//                        publishProgress((int)total);
                        Log.i("Receive",data.toString());
                    }
                    output.flush();
                    output.close();base64dec.close();

                }
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
