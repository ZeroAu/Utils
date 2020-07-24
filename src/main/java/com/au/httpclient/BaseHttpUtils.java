package com.au.httpclient;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class BaseHttpUtils {

    public final static String GET = "GET";
    public final static String POST = "POST";
    public final static String PUT = "PUT";
    public final static String DELETE = "DELETE";

    private final static CloseableHttpClient httpClient = HttpClients.createDefault();
    private final static CloseableHttpClient httpsClient = createSSLClientDefault();

    public static String sendHttpGet(Map<String, String> headers, String reqUrl, Map<String, String> params) throws IOException {
        return sendRequest(GET, headers, reqUrl, params, null, null, null);
    }

    public static String sendHttpPost(Map<String, String> headers, String reqUrl, Map<String, String> params) throws IOException {
        return sendRequest(POST, headers, reqUrl, null, params, null, null);
    }

    public static String sendHttpPut(Map<String, String> headers, String reqUrl, Map<String, String> params) throws IOException {
        return sendRequest(PUT, headers, reqUrl, null, params, null, null);
    }

    public static String sendHttpDelete(Map<String, String> headers, String reqUrl, Map<String, String> params) throws IOException {
        return sendRequest(DELETE, headers, reqUrl, null, params, null, null);
    }

    public static String sendHttpGet(Map<String, String> headers, String reqUrl, Map<String, String> queryParams, Map<String, String> bodyParams) throws IOException {
        return sendRequest(GET, headers, reqUrl, queryParams, bodyParams, null, null);
    }

    public static String sendHttpPost(Map<String, String> headers, String reqUrl, Map<String, String> queryParams, Map<String, String> bodyParams) throws IOException {
        return sendRequest(POST, headers, reqUrl, queryParams, bodyParams, null, null);
    }

    public static String sendHttpPut(Map<String, String> headers, String reqUrl, Map<String, String> queryParams, Map<String, String> bodyParams, String encodeCharset, String decodeCharset) throws IOException {
        return sendRequest(PUT, headers, reqUrl, queryParams, bodyParams, encodeCharset, decodeCharset);
    }

    public static String sendHttpDelete(Map<String, String> headers, String reqUrl, Map<String, String> queryParams, Map<String, String> bodyParams) throws IOException {
        return sendRequest(DELETE, headers, reqUrl, queryParams, bodyParams, null, null);
    }

    public static String sendHttpGet(Map<String, String> headers, String reqUrl, Map<String, String> params, String encodeCharset, String decodeCharset) throws IOException {
        return sendRequest(GET, headers, reqUrl, params, null, encodeCharset, decodeCharset);
    }

    public static String sendHttpPost(Map<String, String> headers, String reqUrl, Map<String, String> queryParams, Map<String, String> bodyParams, String encodeCharset, String decodeCharset) throws IOException {
        return sendRequest(POST, headers, reqUrl, queryParams, bodyParams, encodeCharset, decodeCharset);
    }

    public static String sendHttpPut(Map<String, String> headers, String reqUrl, Map<String, String> queryParams, Map<String, String> bodyParams) throws IOException {
        return sendRequest(PUT, headers, reqUrl, queryParams, bodyParams, null, null);
    }

    public static String sendHttpDelete(Map<String, String> headers, String reqUrl, Map<String, String> queryParams, Map<String, String> bodyParams, String encodeCharset, String decodeCharset) throws IOException {
        return sendRequest(DELETE, headers, reqUrl, queryParams, bodyParams, encodeCharset, decodeCharset);
    }

    public static String sendRequest(String method, Map<String, String> headers, String reqUrl, Map<String, String> queryParams, Map<String, String> bodyParams, String encodeCharset, String decodeCharset) throws IOException {

        String responseContent = null;

        CloseableHttpClient httpClient = getHttpClient(reqUrl);

        if (httpClient != null){
            encodeCharset = StringUtils.isEmpty(encodeCharset) ? "UTF-8" : encodeCharset;
            decodeCharset = StringUtils.isEmpty(decodeCharset) ? "UTF-8" : decodeCharset;

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

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                HttpEntity entity = response.getEntity();
                if (null != entity) {
                    responseContent = EntityUtils.toString(entity, decodeCharset);
                    EntityUtils.consume(entity);
                }
            }
//            request.releaseConnection();
        }


        return responseContent;
    }

    private static CloseableHttpClient getHttpClient(String reqUrl) {
        if (reqUrl.startsWith("http://")){
            return httpClient;
        } else if (reqUrl.startsWith("https://")) {
            return httpsClient;
        } else {
            return null;
        }
    }
    private static CloseableHttpClient createSSLClientDefault(){
        try {
            //信任所有
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, (TrustStrategy) (chain, authType) -> true).build();
            SSLConnectionSocketFactory sslCSF = new SSLConnectionSocketFactory(sslContext);
            return HttpClients.custom().setSSLSocketFactory(sslCSF).build();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return  HttpClients.createDefault();
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
