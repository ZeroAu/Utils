package com.au.testMain;

import com.au.client.HttpClientUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TestMain {
    public static void main(String[] args) throws IOException {
        System.out.println("hhh");

        Map<String, String> headers = new HashMap<>();
        headers.put("testHeader", "resultHeader");
        Map<String, String> queryParam = new HashMap<>();
        queryParam.put("qqq", "qwer");
        Map<String, String> bodyParam = new HashMap<>();
        bodyParam.put("body", "param");
        System.out.println(HttpClientUtil.sendRequest(HttpClientUtil.GET, headers, "http://localhost/test/testGet?hh=get&yyy=1", queryParam, bodyParam, null, null));
        System.out.println(HttpClientUtil.sendRequest(HttpClientUtil.POST, headers, "http://localhost/test/testPost?hh=post&yyy=2", queryParam, bodyParam, null, null));
        System.out.println(HttpClientUtil.sendRequest(HttpClientUtil.PUT, headers, "http://localhost/test/testPut?hh=put&yyy=3", queryParam, bodyParam, null, null));
        System.out.println(HttpClientUtil.sendRequest(HttpClientUtil.DELETE, headers, "http://localhost/test/testDelete?hh=delete&yyy=4", queryParam, bodyParam, null, null));
    }
}
