package com.example.dell.myapplication.persenter;

import java.util.Map;

public interface IPersenter {
    void getRequest(String url, Class clazz);
    void postRequest(String url, Map<String,String> map, Class clazz);
}
