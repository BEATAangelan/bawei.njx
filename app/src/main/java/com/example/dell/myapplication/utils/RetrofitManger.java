package com.example.dell.myapplication.utils;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RetrofitManger {
    private String BASE_URL="http://172.17.8.100/small/";
    private static  RetrofitManger retrofitManger;
    private BaseApis baseApis;
    public static RetrofitManger getInstance(){
        if(retrofitManger==null){
            retrofitManger=new RetrofitManger();
        }
        return retrofitManger;
    }
    private RetrofitManger(){
        OkHttpClient build1 = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();
        Retrofit build = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(build1)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        baseApis = build.create(BaseApis.class);
    }
    //get请求
    public RetrofitManger get(String url,HttpListener listener){
        baseApis.get(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserever(listener));
        return retrofitManger;

    }
    //post请求
    public RetrofitManger post(String url, Map<String,String> map, HttpListener listener){
        baseApis.post(url,map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserever(listener));
        return retrofitManger;
    }
    private Observer getObserever(final HttpListener httpListener){
        Observer<ResponseBody> observer = new Observer<ResponseBody>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(httpListener!=null){
                    httpListener.onFailed(e);
                }
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    String string = responseBody.string();
                    if(httpListener!=null){
                        httpListener.onSuccess(string);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        return observer;
    }
    public interface HttpListener{
        void onSuccess(String data);
        void onFailed(Throwable e);
    }
}
