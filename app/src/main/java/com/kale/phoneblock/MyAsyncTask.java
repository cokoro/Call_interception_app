package com.kale.phoneblock;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * Created by chenkexin on 2017/11/28.
 */

/**
 * 定义一个类，让其继承AsyncTask这个类
 * Params: String类型，表示传递给异步任务的参数类型是String，通常指定的是URL路径
 * Progress: Integer类型，进度条的单位通常都是Integer类型
 * Result：byte[]类型，表示我们下载好的图片以字节数组返回
 * @author xiaoluo
 *
 */
/*
public class MyAsyncTask extends AsyncTask<String, Integer, byte[]>
{
    @Override
    public static void onPreExecute()
    {
        super.onPreExecute();
        //    在onPreExecute()中我们让ProgressDialog显示出来
    }
    @Override
    public static byte[] doInBackground(String... params)
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
    public static void onProgressUpdate(Integer... values)
    {
        super.onProgressUpdate(values);
    }
    @Override
    public static void onPostExecute(byte[] result)
    {
        super.onPostExecute(result);
        //    将doInBackground方法返回的byte[]解码成要给Bitmap
        Bitmap bitmap = BitmapFactory.decodeByteArray(result, 0, result.length);
        //    更新我们的ImageView控件
        imageView.setImageBitmap(bitmap);
        //    使ProgressDialog框消失
    }
}
*/