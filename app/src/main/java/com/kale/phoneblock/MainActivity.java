package com.kale.phoneblock;

import android.app.Activity;
import android.app.ProgressDialog;
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
    private Button button;
    private ImageView imageView;
    private ProgressDialog progressDialog;
    private final String IMAGE_PATH = "http://developer.android.com/images/home/kk-hero.jpg";
    //    private final String IMAGE_PATH2 = "http://ww2.sinaimg.cn/mw690/69c7e018jw1e6hd0vm3pej20fa0a674c.jpg";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button)findViewById(R.id.button);
        imageView = (ImageView)findViewById(R.id.imageView);
        //    弹出要给ProgressDialog
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("提示信息");
        progressDialog.setMessage("正在下载中，请稍后......");
        //    设置setCancelable(false); 表示我们不能取消这个弹出框，等下载完成之后再让弹出框消失
        progressDialog.setCancelable(false);
        //    设置ProgressDialog样式为圆圈的形式
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {// 在UI Thread当中实例化AsyncTask对象，并调用execute方法
                new MyAsyncTask().execute(IMAGE_PATH);
            }
        });
    }

    /**
     * 定义一个类，让其继承AsyncTask这个类
     * Params: String类型，表示传递给异步任务的参数类型是String，通常指定的是URL路径
     * Progress: Integer类型，进度条的单位通常都是Integer类型
     * Result：byte[]类型，表示我们下载好的图片以字节数组返回
     * @author xiaoluo
     *
     */
    public class MyAsyncTask extends AsyncTask<String, Integer, byte[]>
    {
        @Override
        public void onPreExecute()
        {
            super.onPreExecute();
            //    在onPreExecute()中我们让ProgressDialog显示出来
            progressDialog.show();
        }
        @Override
        public byte[] doInBackground(String... params)
        {
            //    通过Apache的HttpClient来访问请求网络中的一张图片
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(params[0]);
            byte[] image = new byte[]{};
            try
            {
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                if(httpEntity != null && httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
                {
                    image = EntityUtils.toByteArray(httpEntity);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
                httpClient.getConnectionManager().shutdown();
            }
            return image;
        }
        @Override
        public void onProgressUpdate(Integer... values)
        {
            super.onProgressUpdate(values);
        }
        @Override
        public void onPostExecute(byte[] result)
        {
            super.onPostExecute(result);
            //    将doInBackground方法返回的byte[]解码成要给Bitmap
            Bitmap bitmap = BitmapFactory.decodeByteArray(result, 0, result.length);
            //    更新我们的ImageView控件
            imageView.setImageBitmap(bitmap);
            //    使ProgressDialog框消失
            progressDialog.dismiss();
        }
    }


}