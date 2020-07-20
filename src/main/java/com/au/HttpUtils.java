package com.au;

import java.util.Map;

public class HttpUtils {

    public final static String GET = "GET";
    public final static String POST = "POST";
    public final static String PUT = "PUT";
    public final static String DELETE = "DELETE";

    public static String sendRequest(String method, Map<String, String> headers, String url, Map<String, String> params){
        String result = null;
        switch (method){
            case GET:
                result = sendGet(headers, url, params);
                break;
            case POST:
                result = sendPost(headers, url, params);
                break;
            case PUT:
                result = sendPut(headers, url, params);
                break;
            case DELETE:
                result = sendDelete(headers, url, params);
                break;
        }
        return result;
    }

    private static String sendGet(Map<String, String> headers, String url, Map<String, String> params) {
        return null;
    }

    private static String sendPost(Map<String, String> headers, String url, Map<String, String> params) {
        return null;
    }

    private static String sendPut(Map<String, String> headers, String url, Map<String, String> params) {
        return null;
    }

    private static String sendDelete(Map<String, String> headers, String url, Map<String, String> params) {
        return null;
    }

}
