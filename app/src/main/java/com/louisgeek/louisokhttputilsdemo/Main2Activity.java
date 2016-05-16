package com.louisgeek.louisokhttputilsdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.finalteam.okhttpfinal.FileDownloadCallback;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        doPostInfo();
        doDownload();

    }

    private void doPostInfo() {
        List<File> files = new ArrayList<>();
        File file = new File("...");
        RequestParams params = new RequestParams(this);//请求参数
        params.addFormDataPart("username", "mUserName");//表单数据
        params.addFormDataPart("password", "mPassword");//表单数据
        params.addFormDataPart("file", file);//上传单个文件
        params.addFormDataPart("files", files);//上传多个文件
        params.addHeader("token", "token");//添加header信息
        HttpRequest.post(Api.LOGIN, params, new BaseHttpRequestCallback<LoginResponse>() {

            //请求网络前
            @Override
            public void onStart() {
                buildProgressDialog().show();
            }

            //这里只是网络请求成功了（也就是服务器返回JSON合法）没有没有分装具体的业务成功与失败，大家可以参考demo去分装自己公司业务请求成功与失败
            @Override
            protected void onSuccess(LoginResponse loginResponse) {
                toast(loginResponse.getMsg());
            }

            //请求失败（服务返回非法JSON、服务器异常、网络异常）
            @Override
            public void onFailure(int errorCode, String msg) {
                toast("网络异常~，请检查你的网络是否连接后再试");
            }

            //请求网络结束
            @Override
            public void onFinish() {
                dismissProgressDialog();
            }
        });
    }

    private void doDownload() {
        String url = "http://219.128.78.33/apk.r1.market.hiapk.com/data/upload/2015/05_20/14/com.speedsoftware.rootexplorer_140220.apk";
        File saveFile = new File("/sdcard/rootexplorer_140220.apk");
        HttpRequest.download(url, saveFile, new FileDownloadCallback() {
            //开始下载
            @Override
            public void onStart() {
                super.onStart();
            }

            //下载进度
            @Override
            public void onProgress(int progress, long networkSpeed) {
                super.onProgress(progress, networkSpeed);
                mPbDownload.setProgress(progress);
                //String speed = FileUtils.generateFileSize(networkSpeed);
            }

            //下载失败
            @Override
            public void onFailure() {
                super.onFailure();
                Toast.makeText(getBaseContext(), "下载失败", Toast.LENGTH_SHORT).show();
            }

            //下载完成（下载成功）
            @Override
            public void onDone() {
                super.onDone();
                Toast.makeText(getBaseContext(), "下载成功", Toast.LENGTH_SHORT).show();
            }
        });
    }

}