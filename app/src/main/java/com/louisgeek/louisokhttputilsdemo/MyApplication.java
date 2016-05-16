package com.louisgeek.louisokhttputilsdemo;

import android.app.Application;

import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.model.HttpHeaders;
import com.lzy.okhttputils.model.HttpParams;

import cn.finalteam.okhttpfinal.OkHttpFinal;
import cn.finalteam.okhttpfinal.OkHttpFinalConfiguration;

/**
 * Created by louisgeek on 2016/5/12.
 */
public class MyApplication  extends Application{

    public static final int HTTP_LIB_OKHTTPUTILS_LZY=0;
    public static final int HTTP_LIB_OKHTTPUTILS_ZHY=1;
    public static final int HTTP_LIB_OKHTTPFINAL=2;

    private String CER_12306 = "-----BEGIN CERTIFICATE-----\n" +
            "MIICmjCCAgOgAwIBAgIIbyZr5/jKH6QwDQYJKoZIhvcNAQEFBQAwRzELMAkGA1UEBhMCQ04xKTAn\n" +
            "BgNVBAoTIFNpbm9yYWlsIENlcnRpZmljYXRpb24gQXV0aG9yaXR5MQ0wCwYDVQQDEwRTUkNBMB4X\n" +
            "DTA5MDUyNTA2NTYwMFoXDTI5MDUyMDA2NTYwMFowRzELMAkGA1UEBhMCQ04xKTAnBgNVBAoTIFNp\n" +
            "bm9yYWlsIENlcnRpZmljYXRpb24gQXV0aG9yaXR5MQ0wCwYDVQQDEwRTUkNBMIGfMA0GCSqGSIb3\n" +
            "DQEBAQUAA4GNADCBiQKBgQDMpbNeb34p0GvLkZ6t72/OOba4mX2K/eZRWFfnuk8e5jKDH+9BgCb2\n" +
            "9bSotqPqTbxXWPxIOz8EjyUO3bfR5pQ8ovNTOlks2rS5BdMhoi4sUjCKi5ELiqtyww/XgY5iFqv6\n" +
            "D4Pw9QvOUcdRVSbPWo1DwMmH75It6pk/rARIFHEjWwIDAQABo4GOMIGLMB8GA1UdIwQYMBaAFHle\n" +
            "tne34lKDQ+3HUYhMY4UsAENYMAwGA1UdEwQFMAMBAf8wLgYDVR0fBCcwJTAjoCGgH4YdaHR0cDov\n" +
            "LzE5Mi4xNjguOS4xNDkvY3JsMS5jcmwwCwYDVR0PBAQDAgH+MB0GA1UdDgQWBBR5XrZ3t+JSg0Pt\n" +
            "x1GITGOFLABDWDANBgkqhkiG9w0BAQUFAAOBgQDGrAm2U/of1LbOnG2bnnQtgcVaBXiVJF8LKPaV\n" +
            "23XQ96HU8xfgSZMJS6U00WHAI7zp0q208RSUft9wDq9ee///VOhzR6Tebg9QfyPSohkBrhXQenvQ\n" +
            "og555S+C3eJAAVeNCTeMS3N/M5hzBRJAoffn3qoYdAO1Q8bTguOi+2849A==\n" +
            "-----END CERTIFICATE-----";

    @Override
    public void onCreate() {
        super.onCreate();
        setupOkHttp();
    }

    private void setupOkHttp() {
        setupOkHttp(HTTP_LIB_OKHTTPUTILS_LZY);
    }

    private void setupOkHttp(int http_lib){
        switch (http_lib){
            case HTTP_LIB_OKHTTPUTILS_LZY:
                initOkHttpUtils__Lzy();
                break;
            case HTTP_LIB_OKHTTPUTILS_ZHY:
               // initOkHttpUtils__Zhy();
                break;
            case HTTP_LIB_OKHTTPFINAL:
                initOkHttp_F();
                break;
        }
    }

    private void initOkHttpUtils__Lzy() {
        HttpHeaders headers = new HttpHeaders();
        headers.put("commonHeaderKey1", "commonHeaderValue1");    //所有的 header 都 不支持 中文
        headers.put("commonHeaderKey2", "commonHeaderValue2");
        HttpParams params = new HttpParams();
        params.put("commonParamsKey1", "commonParamsValue1");     //所有的 params 都 支持 中文
        params.put("commonParamsKey2", "这里支持中文参数");

        //必须调用初始化
        OkHttpUtils.init(this);
        //以下都不是必须的，根据需要自行选择
        OkHttpUtils.getInstance()//
                .debug("OkHttpUtils_Lzy")                                              //是否打开调试
                .setConnectTimeout(OkHttpUtils.DEFAULT_MILLISECONDS)               //全局的连接超时时间
                .setReadTimeOut(OkHttpUtils.DEFAULT_MILLISECONDS)                  //全局的读取超时时间
                .setWriteTimeOut(OkHttpUtils.DEFAULT_MILLISECONDS)                 //全局的写入超时时间
                .addCommonHeaders(headers)                                         //设置全局公共头
                .addCommonParams(params);                                          //设置全局公共参数
        //.addInterceptor(interceptor);                                      //添加自定义拦截器
    }
/*    private  void initOkHttpUtils__Zhy() {
        //这里可以设置自签名证书
        com.zhy.http.okhttp.OkHttpUtils okHttpUtils=com.zhy.http.okhttp.OkHttpUtils.getInstance();
        okHttpUtils.setCertificates(new InputStream[]{new Buffer().writeUtf8(CER_12306).inputStream()});
        okHttpUtils.debug("OkHttpUtils_Zhy")
                .setConnectTimeout(100000, TimeUnit.MILLISECONDS);
        okHttpUtils.setReadTimeout(100000, TimeUnit.MILLISECONDS);
        okHttpUtils.setWriteTimeout(100000, TimeUnit.MILLISECONDS);

        //使用https，但是默认信任全部证书
        //OkHttpUtils.getInstance().setCertificates();

        //使用这种方式，设置多个OkHttpClient参数
        //OkHttpUtils.getInstance(new OkHttpClient.Builder().build());

    }*/
    private void initOkHttp_F() {
        OkHttpFinalConfiguration.Builder builder = new OkHttpFinalConfiguration.Builder();
        OkHttpFinal.getInstance().init(builder.build());
    }
}
