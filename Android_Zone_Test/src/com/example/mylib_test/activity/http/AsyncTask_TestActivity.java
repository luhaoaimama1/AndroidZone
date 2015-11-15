package com.example.mylib_test.activity.http;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.example.mylib_test.R;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class AsyncTask_TestActivity extends Activity {  
    
    private static final String TAG = "ASYNC_TASK";  
      
    private Button execute,cancel;  
    private ProgressBar progressBar;  
    private TextView textView;  
    private MyTask mTask;  
      
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.a_asynctask_test);  
          
        execute = (Button) findViewById(R.id.execute);  
        execute.setOnClickListener(new View.OnClickListener() {  
            @Override  
            public void onClick(View v) {  
                //ע��ÿ����newһ��ʵ��,�½�������ֻ��ִ��һ��,���������쳣  
                mTask = new MyTask();  
                mTask.execute("http://img.my.csdn.net/uploads/201407/26/1406383264_3954.jpg");  
                execute.setEnabled(false);  
                cancel.setEnabled(true);  
            }  
        });  
        cancel = (Button) findViewById(R.id.cancel);  
        cancel.setOnClickListener(new View.OnClickListener() {  
            @Override  
            public void onClick(View v) {  
                //ȡ��һ������ִ�е�����,onCancelled�������ᱻ����  
                mTask.cancel(true);  
                textView.setText("cancelled");  
                progressBar.setProgress(0);  
                execute.setEnabled(true);  
                cancel.setEnabled(false);  
            }  
        });  
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);  
        textView = (TextView) findViewById(R.id.text_view);  
          
    }  
      
    private class MyTask extends AsyncTask<String, Integer, String> {  
        //onPreExecute����������ִ�к�̨����ǰ��һЩUI����  
        @Override  
        protected void onPreExecute() {  
            Log.i(TAG, "onPreExecute() called");  
            textView.setText("loading...");  
        }  
          
        //doInBackground�����ڲ�ִ�к�̨����,�����ڴ˷������޸�UI  
        @Override  
        protected String doInBackground(String... params) {  
            Log.i(TAG, "doInBackground(Params... params) called");  
            try {  
                HttpClient client = new DefaultHttpClient();  
                HttpGet get = new HttpGet(params[0]);  
                HttpResponse response = client.execute(get);  
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {  
                    HttpEntity entity = response.getEntity();  
                    InputStream is = entity.getContent();  
                    long total = entity.getContentLength();  
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();  
                    byte[] buf = new byte[1024];  
                    int count = 0;  
                    int length = -1;  
                    while ((length = is.read(buf)) != -1) {  
                        baos.write(buf, 0, length);  
                        count += length;  
                        //����publishProgress��������,���onProgressUpdate��������ִ��  
                        publishProgress((int) (((float)count / total) * 100)); //Notify your activity that you had canceled the task
                        //Ϊ����ʾ����,����500����  
                        Thread.sleep(500);  
                    }  
                    String temp = new String(baos.toByteArray(), "gb2312");
                    return temp;  
                }  
            } catch (Exception e) {  
                Log.e(TAG, e.getMessage());  
            }  
            return null;  
        }  
          
        //onProgressUpdate�������ڸ��½�����Ϣ  
        @Override  
        protected void onProgressUpdate(Integer... progresses) {  
            Log.i(TAG, "onProgressUpdate(Progress... progresses) called");  
            progressBar.setProgress(progresses[0]);  
            textView.setText("loading..." + progresses[0] + "%");  
        }  
          
        //onPostExecute����������ִ�����̨��������UI,��ʾ���  
        @Override  
        protected void onPostExecute(String result) {  
            Log.i(TAG, "onPostExecute(Result result) called");  
            textView.setText(result);  
            execute.setEnabled(true);  
            cancel.setEnabled(false);  
        }  
          
        //onCancelled����������ȡ��ִ���е�����ʱ����UI  
        @Override  
        protected void onCancelled() {  
            Log.i(TAG, "onCancelled() called");  
        }  
        @Override
        protected void onCancelled(String result) {
        	super.onCancelled(result);
        	   Log.i(TAG, "onCancelled() result");  
        }
    }  
}  