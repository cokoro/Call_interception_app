package com.kale.phoneblock;

/**
 * Created by chenkexin on 2017/11/28.
 */


import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.Toast;

public class DownloadAsynctask extends AsyncTask<String, Integer, byte[]>{

    private ImageView iv;
    private Context context; //上下文对象

    /*
     * 通过构造方法，把MainActivity中的数据传递过来
     */
    public DownloadAsynctask(ImageView iv, Context context) {
        super();
        this.iv = iv;
        this.context = context;
    }

    /*
     * 调用当前类的execute方法后，最先运行此方法，准备数据（加载进度条）
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    /*
     * 后台获取网络上的资源
     */
    @Override
    protected byte[] doInBackground(String... params) {
        try {
            URL url = new URL(params[0]);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);

            byte[] result = null;
            if (conn.getResponseCode() ==200) {
                InputStream is = conn.getInputStream();
                //获取下载的总数据量
                int totalSize = conn.getContentLength();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                byte[] buffer = new byte[1024];
                int len = 0;
                //记录当前下载的数据量
                int currentSize = 0;
                while((len = is.read(buffer))!=-1){
                    baos.write(buffer, 0, len);
                    currentSize += len; //更新当前下载的数据量
                    //通过publishProgress方法来唤醒异步任务中的onProgressUpdate方法
                    publishProgress(totalSize, currentSize); //把数据传给onProgressUpdate进行更新UI
                }
                //返回的结果会被onPostExecute方法接收
                result = baos.toByteArray();
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /*
     * 获取doInBackground方法传递过来的参数(result)，进行更新UI
     */
    @Override
    protected void onPostExecute(byte[] result) {
        super.onPostExecute(result);
        if (result != null) {
            //把doInBackground方法传递过来的数据转换为Bitmap
            Bitmap bm = BitmapFactory.decodeByteArray(result, 0, result.length);
            iv.setImageBitmap(bm);
        }else {
            Toast.makeText(context, "网络错误", Toast.LENGTH_LONG).show();
        }
    }

    /*
     * 调用publishProgress方法，会触发此方法的调用
     */
    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        //更新进度条
    }
}