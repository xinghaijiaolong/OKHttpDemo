package com.example.test.okhttpdemo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends Activity {
    private String url = "http://ip.taobao.com/service/getIpInfo.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void get(View view) {
        //框架的gradle地址 https://github.com/hongyangAndroid/okhttputils
        OkHttpUtils
                .post()
                .url(url)
                .addParams("ip", "63.223.108.42")
                .build()
                .execute(new com.zhy.http.okhttp.callback.Callback() {
                    @Override
                    public Object parseNetworkResponse(Response response, int id) throws Exception {
                        TestJson testJson = JSON.parseObject(response.body().string(), TestJson.class);
                        Log.e("message", testJson.getData().getCountry_id());
                        //aaa
                        return null;
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("message", e.toString());
                        //444
                    }

                    @Override
                    public void onResponse(Object response, int id) {

                    }
                });
    }

    public void post(View view) {

        OkHttpClient okHttpClient = new OkHttpClient()
                .newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();
        RequestBody body = new FormBody.Builder()
                .add("ip", "63.223.108.42")
                .build();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Connection", "close")
                .post(body)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                Log.e("message", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // response.body().string()调用一次，之后会关闭资源，所以再次调用返回null
                //注意response.body().string()返回的内容
                String json = response.body().string();
                TestJson testJson = JSON.parseObject(json, TestJson.class);
                Log.e("message", testJson.getData().getCountry_id());
            }
        });

    }
}
