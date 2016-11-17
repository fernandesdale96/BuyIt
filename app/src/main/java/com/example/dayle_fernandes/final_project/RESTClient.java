package com.example.dayle_fernandes.final_project;

/**
 * Created by dayle_fernandes on 18-Nov-16.
 */

import com.loopj.android.http.*;

/**
 * Created by gopa2000 on 11/16/16.
 */

public class RESTClient {
    private static final String BASE_URL = "http://10.0.2.2/FinalProject/";

    private static AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler){
        asyncHttpClient.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler){
        asyncHttpClient.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl){
        return BASE_URL + relativeUrl;
    }

}