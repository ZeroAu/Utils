package com.au.testMain;

import com.au.TimeUtils;
import com.au.client.BaseHttpUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestMain {
    public static void main(String[] args) {

        try {

            Map<String, String> headers = new HashMap<>();
            headers.put("testHeader", "resultHeader");
            Map<String, String> queryParam = new HashMap<>();
            queryParam.put("qqq", "qwer");
            Map<String, String> bodyParam = new HashMap<>();
            bodyParam.put("body", "param");

            for (int count = 1; count < 1000; count++) {

                ExecutorService executorService = Executors.newFixedThreadPool(1000);
                CountDownLatch latch = new CountDownLatch(1000);
                Long startTime = System.currentTimeMillis();
                for (int i = 0; i < 1000; i++) {
                    int num = i;
                    executorService.submit(() -> {
                        try {
                            System.out.println(num + ": " + BaseHttpUtil.sendRequest(BaseHttpUtil.GET, headers, "https://127.0.0.1:8443/test/testGet?hh=get&yyy=1", queryParam, bodyParam, null, null));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        latch.countDown();
                    });
                }
                latch.await();
                Long endTime = System.currentTimeMillis();
                System.out.println("*************** 耗时：" + (endTime - startTime) + "ms ***************");
                executorService.shutdown();
            }

//        System.out.println(BaseHttpUtil.sendRequest(BaseHttpUtil.GET, headers, "https://127.0.0.1:8443/test/testGet?hh=get&yyy=1", queryParam, bodyParam, null, null));
//        System.out.println(BaseHttpUtil.sendRequest(BaseHttpUtil.POST, headers, "http://localhost/test/testPost?hh=post&yyy=2", queryParam, bodyParam, null, null));
//        System.out.println(BaseHttpUtil.sendRequest(BaseHttpUtil.PUT, headers, "http://localhost/test/testPut?hh=put&yyy=3", queryParam, bodyParam, null, null));
//        System.out.println(BaseHttpUtil.sendRequest(BaseHttpUtil.DELETE, headers, "http://localhost/test/testDelete?hh=delete&yyy=4", queryParam, bodyParam, null, null));
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
