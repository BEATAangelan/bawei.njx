package com.example.dell.myapplication.model;

import com.example.dell.myapplication.model.IModel;
import com.example.dell.myapplication.utils.MCallBack;
import com.example.dell.myapplication.utils.RetrofitManger;
import com.google.gson.Gson;

import java.util.Map;

public class IModelmpl implements IModel {
    MCallBack callBack;
    @Override
    public void getRequest(String url, final Class clazz, MCallBack mCallBack) {
        callBack = mCallBack;
        RetrofitManger.getInstance().get(url, new RetrofitManger.HttpListener() {
            @Override
            public void onSuccess(String data) {
                Gson gson = new Gson();
                Object o = gson.fromJson(data, clazz);
                if(callBack !=  null){
                    callBack.setData(o);
                }
            }

            @Override
            public void onFailed(Throwable e) {

            }
        });

    }

    @Override
    public void postRequest(String url, Map<String, String> map, final Class clazz, MCallBack mCallBack) {
        callBack = mCallBack;
        RetrofitManger.getInstance().post(url, map, new RetrofitManger.HttpListener() {
            @Override
            public void onSuccess(String data) {
                Gson gson = new Gson();
                Object o = gson.fromJson(data, clazz);
                callBack.setData(o);
            }

            @Override
            public void onFailed(Throwable e) {

            }
        });
    }
}
