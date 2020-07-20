package com.au;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.CharSet;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

public class HttpClientUtil {

    private static Logger logger = LogManager.getLogger(HttpClientUtil.class);

    public final static String GET = "GET";
    public final static String POST = "POST";
    public final static String PUT = "PUT";
    public final static String DELETE = "DELETE";

    public static String sendRequest(String method, Map<String, String> headers, String reqUrl, Map<String, String> params, String encodeCharset, String decodeCharset) throws IOException {
        logger.info(String.format("sendGetRequest headers = %s, url = %s, params = %s, decodeCharset = %s,", headers, reqUrl, params, decodeCharset));

        String responseContent = null;
        CloseableHttpResponse response = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();

        try {
            if (MapUtils.isNotEmpty(params)){
                reqUrl = splicingUrl(reqUrl, params);
            }

            reqUrl = URLEncoder.encode(reqUrl, StringUtils.isEmpty(encodeCharset) ? "UTF-8" : encodeCharset);

            HttpRequestBase httpRequest = null;

            switch (method){
                case GET:
                    httpRequest = new HttpGet(reqUrl);
                    break;
                case POST:
                    httpRequest = new HttpPost(reqUrl);
                    break;
                case PUT:
                    httpRequest = new HttpPut(reqUrl);
                    break;
                case DELETE:
                    httpRequest = new HttpDelete(reqUrl);
                    break;
            }

            if (MapUtils.isNotEmpty(headers)){
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    httpRequest.addHeader(entry.getKey(), entry.getValue());
                }
            }

            response = httpClient.execute(httpRequest);
            HttpEntity entity = response.getEntity();
            if (null != entity){
                responseContent = EntityUtils.toString(entity, StringUtils.isEmpty(decodeCharset) ? "UTF-8" : decodeCharset);
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

    private static String splicingUrl(String url, Map<String, String> params) {
        StringBuilder sb = new StringBuilder(url);

        sb.append("?");

        params.forEach((key, value) -> {
            sb.append(key + "=" + value + "&");
        });

        sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }


}
