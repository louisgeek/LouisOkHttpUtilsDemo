package com.louisgeek.louisokhttputilsdemo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.cache.CacheMode;
import com.lzy.okhttputils.callback.BitmapCallback;
import com.lzy.okhttputils.callback.FileCallback;
import com.lzy.okhttputils.callback.StringCallback;
import com.lzy.okhttputils.request.BaseRequest;

import java.io.File;

import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    /**
     * https://github.com/jeasonlzy0216/OkHttpUtils
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //基本的网络请求
        OkHttpUtils.get(Urls.URL_METHOD)     // 请求方式和请求url
                .tag(this)                       // 请求的 tag, 主要用于取消对应的请求
                .cacheKey("cacheKey")            // 设置当前请求的缓存key,建议每个不同功能的请求设置一个
                .cacheMode(CacheMode.DEFAULT)    // 缓存模式，详细请看缓存介绍
                .execute(new JsonCallback<RequestInfo>(RequestInfo.class) {
                    @Override
                    public void onResponse(boolean isFromCache, RequestInfo requestInfo, Request request, @Nullable Response response) {
                        // requestInfo 对象即为所需要的结果对象
                    }
                });

//请求 Bitmap 对象
        OkHttpUtils.get(Urls.URL_IMAGE)//
                .tag(this)//
                .execute(new BitmapCallback() {
                    @Override
                    public void onResponse(boolean isFromCache, Bitmap bitmap, Request request, @Nullable Response response) {
                        // bitmap 即为返回的图片数据
                    }
                });
        //请求 文件下载
        OkHttpUtils.get(Urls.URL_DOWNLOAD)//
                .tag(this)//
                .execute(new FileCallback("/sdcard/temp/", "file.jpg") {  //文件下载时，需要指定下载的文件目录和文件名
                    @Override
                    public void onResponse(boolean isFromCache, File file, Request request, @Nullable Response response) {
                        // file 即为文件数据，文件保存在指定布幕
                    }
                });
        //普通Post，直接上传String类型的文本

        //不建议这么用，该方法上传字符串会清空实体中其他所有的参数，但头信息不清除
        OkHttpUtils.post(Urls.URL_TEXT_UPLOAD)//
                .tag(this)//
                .postString("这是要上传的长文本数据！")//
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
                        //上传成功
                    }
                });
       // 普通Post，直接上传Json类型的文本
        //不建议这么用，该方法上传字符串会清空实体中其他所有的参数，但头信息不清除
        OkHttpUtils.post(Urls.URL_TEXT_UPLOAD)//
                .tag(this)//
                .postJson("{\"des\": \"这里面要写标准的json格式数据\"}")//
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
                        //上传成功
                    }
                });
        /**
         * 请求功能的所有配置讲解

         以下代码包含了以下内容：

         一次普通请求所有能配置的参数，真实使用时不需要配置这么多，按自己的需要选择性的使用即可
         多文件和多参数的表单上传，同时支持进度监听
         自签名网站https的访问，调用setCertificates方法即可
         为单个请求设置超时，比如涉及到文件的需要设置读写等待时间多一点。
         */
        OkHttpUtils.get("URL_METHOD") // 请求方式和请求url, get请求不需要拼接参数，支持get，post，put，delete，head，options请求
                .tag(this)               // 请求的 tag, 主要用于取消对应的请求
                .connTimeOut(10000)      // 设置当前请求的连接超时时间
                .readTimeOut(10000)      // 设置当前请求的读取超时时间
                .writeTimeOut(10000)     // 设置当前请求的写入超时时间
                .cacheKey("cacheKey")    // 设置当前请求的缓存key,建议每个不同功能的请求设置一个
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST) // 缓存模式，详细请看第四部分，缓存介绍
                .setCertificates(getAssets().open("srca.cer")) // 自签名https的证书，可变参数，可以设置多个
                .addInterceptor(interceptor)            // 添加自定义拦截器
                .headers("header1", "headerValue1")     // 添加请求头参数
                .headers("header2", "headerValue2")     // 支持多请求头参数同时添加
                .params("param1", "paramValue1")        // 添加请求参数
                .params("param2", "paramValue2")        // 支持多请求参数同时添加
                .params("file1", new File("filepath1")) // 可以添加文件上传
                .params("file2", new File("filepath2")) // 支持多文件同时添加上传
                        //这里给出的泛型为 RequestInfo，同时传递一个泛型的 class对象，即可自动将数据结果转成对象返回
                .execute(new DialogCallback<RequestInfo>(this, RequestInfo.class) {
                    @Override
                    public void onBefore(BaseRequest request) {
                        // UI线程 请求网络之前调用
                        // 可以显示对话框，添加/修改/移除 请求参数
                    }

                    @Override
                    public RequestInfo parseNetworkResponse(Response response) {
                        // 子线程，可以做耗时操作
                        // 根据传递进来的 response 对象，把数据解析成需要的 RequestInfo 类型并返回
                        return null;
                    }

                    @Override
                    public void onResponse(boolean isFromCache, RequestInfo requestInfo, Request request, @Nullable Response response) {
                        // UI 线程，请求成功后回调
                        // isFromCache 表示当前回调是否来自于缓存
                        // requestInfo 返回泛型约定的实体类型参数
                        // request     本次网络的请求信息，如果需要查看请求头或请求参数可以从此对象获取
                        // response    本次网络访问的结果对象，包含了响应头，响应码等，如果数据来自于缓存，该对象为null
                    }

                    @Override
                    public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                        // UI 线程，请求失败后回调
                        // isFromCache 表示当前回调是否来自于缓存
                        // call        本次网络的请求对象，可以根据该对象拿到 request
                        // response    本次网络访问的结果对象，包含了响应头，响应码等，如果网络异常 或者数据来自于缓存，该对象为null
                        // e           本次网络访问的异常信息，如果服务器内部发生了错误，响应码为 400~599之间，该异常为 null
                    }

                    @Override
                    public void onAfter(boolean isFromCache, @Nullable RequestInfo requestInfo, Call call, @Nullable Response response, @Nullable Exception e) {
                        // UI 线程，请求结束后回调，无论网络请求成功还是失败，都会调用，可以用于关闭显示对话框
                        // isFromCache 表示当前回调是否来自于缓存
                        // requestInfo 返回泛型约定的实体类型参数，如果网络请求失败，该对象为　null
                        // call        本次网络的请求对象，可以根据该对象拿到 request
                        // response    本次网络访问的结果对象，包含了响应头，响应码等，如果网络异常 或者数据来自于缓存，该对象为null
                        // e           本次网络访问的异常信息，如果服务器内部发生了错误，响应码为 400~599之间，该异常为 null
                    }

                    @Override
                    public void upProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                        // UI 线程，文件上传过程中回调，只有请求方式包含请求体才回调（GET,HEAD不会回调）
                        // currentSize  当前上传的大小（单位字节）
                        // totalSize 　 需要上传的总大小（单位字节）
                        // progress     当前上传的进度，范围　0.0f ~ 1.0f
                        // networkSpeed 当前上传的网速（单位秒）
                    }

                    @Override
                    public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                        // UI 线程，文件下载过程中回调
                        //参数含义同　上传相同
                    }
                });


      //  取消请求
/*
        //每个请求前都设置了一个参数tag，取消则通过OkHttpUtils.cancel(tag)执行。 例如：在Activity中，当Activity销毁取消请求，可以在onDestory里面统一取消。

        @Override
        protected void onDestroy() {
            super.onDestroy();

            //根据 Tag 取消请求
            OkHttpUtils.getInstance().cancelTag(this);
        }*/


        //同步的请求

        /*//execute方法不传入callback即为同步的请求，返回Response对象，需要自己解析

        Response response = OkHttpUtils.get("http://www.baidu.com")//
                .tag(this)//
                .headers("aaa", "111")//
                .params("bbb", "222")
                .execute();*/
    }
}
