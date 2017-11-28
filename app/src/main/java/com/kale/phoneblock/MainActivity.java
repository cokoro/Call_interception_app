package com.kale.phoneblock;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class MainActivity extends Activity
{
    private ImageView iv;

    private Button button;
    public Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv = (ImageView) findViewById(R.id.iv);
        /*button = (Button)findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {// 在UI Thread当中实例化AsyncTask对象，并调用execute方法

                download(v);
                //实例化异步任务的类
                //DownloadAsynctask task = new DownloadAsynctask(iv, context);
                //execute方法执行后，会调用异步任务的doInBackground方法
                //task.execute("http://p2.so.qhmsg.com/t01fb3e43c8cd9ee917.jpg");

            }
        });*/

    }
    //点击“下载”按钮
    public void download(View view){
        //实例化异步任务的类
        DownloadAsynctask task = new DownloadAsynctask(iv, this);
        //execute方法执行后，会调用异步任务的doInBackground方法
        task.execute("http://p2.so.qhmsg.com/t01fb3e43c8cd9ee917.jpg");
    }
}