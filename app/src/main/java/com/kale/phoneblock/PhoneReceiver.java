package com.kale.phoneblock;

/**
 * Created by chenkexin on 2017/11/28.
 */

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.internal.telephony.ITelephony;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import static android.widget.Toast.*;

public class PhoneReceiver extends BroadcastReceiver {
    String TAG = "PhoneReceiver";
    private ImageView imageView;
    private ProgressDialog progressDialog;
    private final String IMAGE_PATH = "http://developer.android.com/images/home/kk-hero.jpg";
    public static ImageView iv;

    @Override
    public void onReceive(final Context context, Intent intent) {
        TelephonyManager mtelephony = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        mtelephony.listen(new PhoneStateListener(){
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                super.onCallStateChanged(state, incomingNumber);
                switch (state) {
                    case TelephonyManager.CALL_STATE_RINGING:
                        // CALL_STATE_RINGING
                        Log.d("MyLittleDebugger", "I'm in " + state + " and the number is " + incomingNumber);
                        //Toast.makeText(context.getApplicationContext(), incomingNumber,Toast.LENGTH_LONG).show();
                        //Toast.makeText(context.getApplicationContext(), "CALL_STATE_RINGING", Toast.LENGTH_LONG).show();
                        testReadAllContacts(context, incomingNumber);
                        break;
                    default:
                        break;
                }
            }
        },PhoneStateListener.LISTEN_CALL_STATE);
    }
    //根据号码获取联系人的姓名
    public void testContactNameByNumber(Context context, String incomingNumber) {
        String number = "110";
        Uri uri = Uri.parse("content://com.android.contacts/data/phones/filter/"+number);

        ContentResolver resolver = context.getContentResolver();
        Log.d("MyLittleDebugger", "I'm in ");
        //getContext().getContentResolver().notifyChange(rowUri, null);
        Cursor cursor = resolver.query(uri, new String[]{android.provider.ContactsContract.Data.DISPLAY_NAME}, null, null, null);
        if(cursor.moveToFirst()){
            String name = cursor.getString(0);
            Log.i(TAG, name);

        }
        cursor.close();
    }
    public void testReadAllContacts(Context context, String incomingNumber) {
        Log.d("MyLittleDebugger", "I'm in ");
        Cursor cursor = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        Log.d("MyLittleDebugger", "I'm after cursor");
        int contactIdIndex = 0;
        int nameIndex = 0;
        int numIndex = 0;

        if(cursor.getCount() > 0) {
            contactIdIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
        }
        int is_find = 0;
        while(cursor.moveToNext()) {
            String contactId = cursor.getString(contactIdIndex);
            String name = cursor.getString(nameIndex);
            String contact_number = cursor.getString(nameIndex);
            Log.i(TAG, contactId);
            Log.i(TAG, name);

            Cursor phones = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId,
                    null, null);
            int phoneIndex = 0;
            if(phones.getCount() > 0) {
                phoneIndex = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            }
            while(phones.moveToNext()) {
                String phoneNumber = phones.getString(phoneIndex);
                Log.i(TAG, phoneNumber);
                Log.d("MyLittleDebugger", " the number is " + incomingNumber);
                if (phoneNumber.equals(incomingNumber)){
                    is_find = 1;
                    break;
                }
            }
            if (is_find != 0)
                break;
        }
        if (is_find != 1) {
            //this.MyAsyncTask().execute(IMAGE_PATH);
            iv = new ImageView(context.getApplicationContext());
            View view = new View(context.getApplicationContext());
            download(view,context);
            Toast toast = Toast.makeText(context.getApplicationContext(), "!!!Unknown number!!!", Toast.LENGTH_LONG);
            LinearLayout toastView = (LinearLayout) toast.getView();//获取Toast的LinearLayout，注意需要是线性布局
            //LinearLayout relativeLayout = (LinearLayout)toastView.findViewById(R.id.toast_linear);
            toastView.addView(iv,0);
            toast.setGravity(Gravity.FILL_HORIZONTAL, 0, 0);

            toast.show();
            /*AlertDialog.Builder ImageDialog = new AlertDialog.Builder(context.getApplicationContext());
            ImageDialog.setTitle("Unknown number!");
            ImageDialog.setView(iv);
            ImageDialog.show();*/
            //ImageView view = new ImageView(context);
            //
            // imageView = (ImageView)findViewById(R.id.imageView);
            //    弹出要给ProgressDialog
            /*
            progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("提示信息");
            progressDialog.setMessage("正在下载中，请稍后......");
            //    设置setCancelable(false); 表示我们不能取消这个弹出框，等下载完成之后再让弹出框消失
            progressDialog.setCancelable(false);
            //    设置ProgressDialog样式为圆圈的形式
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

            try {
                //Bitmap img = ImageUtil.getImage("http://i.cs.hku.hk/~twchim/police/warning.jpg");//这里的URL请大家随便在网上copy一个，或者自建一个服务器项目。
                ImageView view = new ImageView(context);
                //view.setImageBitmap(img);
                Toast toast = Toast.makeText(context.getApplicationContext(), "I can not find...", Toast.LENGTH_LONG);
                LinearLayout toastView = (LinearLayout) toast.getView();//获取Toast的LinearLayout，注意需要是线性布局
                toastView.addView(imageView);
                //toast.addView(view);
                //.setView(view);
                toast.show();
            } catch (Exception e) {
                e.printStackTrace();
            }/*
            ImageView view = new ImageView(context);
            view.setImageBitmap(bitmap);
            //ImageView image = ImageView.setImageBitmap(bitmap);
            Toast toast = Toast.makeText(context.getApplicationContext(), "I can not find...", Toast.LENGTH_LONG);
            LinearLayout toastView = (LinearLayout) toast.getView();//获取Toast的LinearLayout，注意需要是线性布局
            toastView.addView(view);
            //toast.addView(view);
                    //.setView(view);
            toast.show();*/
        }else{
            Toast.makeText(context.getApplicationContext(), "Cool!!",
                    Toast.LENGTH_LONG).show();
        }

    }
    public static Bitmap getBitmap(String path) throws IOException {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if(conn.getResponseCode() == 200){
            InputStream inputStream = conn.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        }
        return null;
    }
    //点击“下载”按钮
    public static void download(View view, Context context){
        //实例化异步任务的类
        DownloadAsynctask task = new DownloadAsynctask(iv, context);
        //task.execute("http://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1511881410532&di=a7c484084b711c0a280671fc61d942af&imgtype=0&src=http%3A%2F%2Fimg.25pp.com%2Fuploadfile%2Fsoft%2Fimages%2F2013%2F0221%2F20130221011758497.jpg");
        //execute方法执行后，会调用异步任务的doInBackground方法
        //task.execute("http://p2.so.qhmsg.com/t01fb3e43c8cd9ee917.jpg");
        task.execute("http://i.cs.hku.hk/~kxchen/alert.jpg");
    }

}

