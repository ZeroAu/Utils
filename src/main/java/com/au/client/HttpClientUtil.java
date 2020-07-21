package com.au.client;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

public class HttpClientUtil {

    public final static String GET = "GET";
    public final static String POST = "POST";
    public final static String PUT = "PUT";
    public final static String DELETE = "DELETE";

    public static String sendRequest(String method, Map<String, String> headers, String reqUrl, Map<String, String> queryParams, Map<String, String> bodyParams, String encodeCharset, String decodeCharset) throws IOException {

        String responseContent = null;
        CloseableHttpResponse response = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();

        encodeCharset = StringUtils.isEmpty(encodeCharset) ? "UTF-8" : encodeCharset;
        decodeCharset = StringUtils.isEmpty(decodeCharset) ? "UTF-8" : decodeCharset;
        try {
            if (MapUtils.isNotEmpty(queryParams)){
                reqUrl = splicingUrl(reqUrl, queryParams, encodeCharset);
            }

            HttpEntityEnclosingRequestBase request = null;

            switch (method){
                case GET:
                    request = new HttpGet(reqUrl);
                    break;
                case POST:
                    request = new HttpPost(reqUrl);
                    break;
                case PUT:
                    request = new HttpPut(reqUrl);
                    break;
                case DELETE:
                    request = new HttpDelete(reqUrl);
                    break;
            }

            if (MapUtils.isNotEmpty(bodyParams)){
                StringEntity entity = getStringEntity(bodyParams, encodeCharset);
                request.setEntity(entity);
            }

            if (MapUtils.isNotEmpty(headers)){
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    request.addHeader(entry.getKey(), entry.getValue());
                }
            }

            response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            if (null != entity){
                responseContent = EntityUtils.toString(entity, decodeCharset);
                EntityUtils.consume(entity);
            }
        } finally {
            if (response != null){
                response.close();
            }
            httpClient.close();
        }

        return responseContent;
    }

    private static StringEntity getStringEntity(Map<String, String> bodyParams, String encodeCharset) {
        StringEntity entity;

        JSONObject jsonObject = new JSONObject();
        bodyParams.forEach((key, value) -> {
            jsonObject.put(key, value);
        });
        entity = new StringEntity(jsonObject.toString(), encodeCharset);
        entity.setContentEncoding(encodeCharset);
        entity.setContentType("application/json");

        return entity;
    }

    private static String splicingUrl(String url, Map<String, String> params, String encodeCharset) throws UnsupportedEncodingException {
        StringBuilder paramSb = new StringBuilder();

        if (url.contains("?")){
            paramSb.append("&");
        } else {
            paramSb.append("?");
        }

        for (Map.Entry<String, String> entry : params.entrySet()) {
            paramSb.append(URLEncoder.encode(entry.getKey(), encodeCharset) + "=" + URLEncoder.encode(entry.getValue(), encodeCharset) + "&");
        }

        paramSb.deleteCharAt(paramSb.length() - 1);

        return url + paramSb.toString();
    }
}
