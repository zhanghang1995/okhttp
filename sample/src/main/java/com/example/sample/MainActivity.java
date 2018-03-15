package com.example.sample;

import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

public class MainActivity extends AppCompatActivity  {
    private OkHttpClient okHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        okHttpClient = new OkHttpClient();
    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.do_Get:{
////                try {
////                    doGet();
////                    Toast.makeText(this,"HELLO",Toast.LENGTH_SHORT).show();
////                    System.out.println("TEst");
////                } catch (Exception e) {
////                    e.printStackTrace();
////                }
//                break;
//            }
//            case R.id.do_POST:{
//                Toast.makeText(this,"HELLO",Toast.LENGTH_SHORT).show();
//                break;
//            }
//        }
//    }

    public void doGet(View v) throws Exception {
        Toast.makeText(MainActivity.this,"HELLO",Toast.LENGTH_SHORT).show();
        //1. 拿到okHttpClient 对象
//         okHttpClient = new OkHttpClient();
        //2. 构造Request
        Request.Builder builder = new Request.Builder();
        Request request = builder.get().url("http://www.baidu.com/").build();
        requstBOdy(request);

    }

    private void requstBOdy(Request request) {
        //3. 将Request封装成为Call
        Call call = okHttpClient.newCall(request);
//        Response response = call.execute();
        //回掉方法
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                L.e("onFailure"+e.getMessage());
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("TEst");
                L.e("onResponse:");
                String responce = response.body().string();
                //此回掉的方法在子线程里面运行，为了防止进行大文件下载时候对UI线程的阻塞
                //解决办法，使用Hanlder或者runOnUIthread进行UI线程的更新操作L.e(responce)
                System.out.println(responce);
            }
        });
    }

    public void doPost(View view){
        Request.Builder builder  = new Request.Builder();
        FormBody formBody = new FormBody.Builder().add("name","zhanghang").build();
        Request requst = builder.url("http://...."+"login").post(formBody).build();
        requstBOdy(requst);
    }

    public void doPostString (View view){
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain;charset=utf-8"), "{username:hyman,password:123}");
        Request.Builder builder  = new Request.Builder();
        Request requst = builder.url("http://...."+"login").post(requestBody).build();
        requstBOdy(requst);
    }

    public void doPostFile(View view){
        String bir = Environment.getExternalStorageState();
        File file = new File(bir,"banner2.jpg");
        if(!file.exists()){
            L.e(file.getAbsolutePath()+"not exist!");
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/actet-stream"),file);
        //如果MediaType不确定，可以使用application/actet-steam进行代替
        Request.Builder builder = new Request.Builder();
        Request request = builder.url("http://...."+"login").post(requestBody).build();
        requstBOdy(request);
    }

    public void doUpload(View view){
        File file = new File(Environment.getExternalStorageDirectory(),"banner2.jpg");
        if(!file.exists()){
            L.e(file.getAbsolutePath()+"not exit!");
            return ;
        }
        //mime type
        MultipartBody body = new MultipartBody.Builder("AaB03x")
                .setType(MultipartBody.FORM)
                .addFormDataPart("files", null, new MultipartBody.Builder("BbC04y")
                        .addFormDataPart("username","king")
                        .addFormDataPart("password","123")
                        .addPart(Headers.of("Content-Disposition", "form-data; filename=\"img.png\""),
                                RequestBody.create(MediaType.parse("image/png"), file))
                        .build())
                .build();
        Request.Builder builder = new Request.Builder();
        Request request = builder.url("http://").post(body).build();
        requstBOdy(request);
    }

    public void doDownload(View v){
        Request.Builder builder = new Request.Builder();
        final Request request = builder
                .get()
                .url("http://")
                .build();
        //3. 将Request封装成为Call
        Call call = okHttpClient.newCall(request);
//        Response response = call.execute();
        //回掉方法
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                L.e("onFailure"+e.getMessage());
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
            L.e("onResponse: ");
                InputStream inputStream = response.body().byteStream();
                int length = 0;
                File file = new File(Environment.getExternalStorageDirectory(),"king.jpg");
                byte [] buffer = new byte[128];
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                while((length = inputStream.read(buffer))!= -1){
                    fileOutputStream.write(buffer,0,length);
                }
                fileOutputStream.flush();
                fileOutputStream.close();
                inputStream.close();
            }

        });

    }
}
