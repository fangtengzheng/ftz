package com.example.a.entity;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.example.a.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PayUtil {
     @Autowired
     private OrderDetailService orderService;
    //appid
    private final String APP_ID = "9021000149699883";
    //应用私钥
    private final String APP_PRIVATE_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCxE/pwo/vk/WefY2h044QST+l22zAAa/XFuU/rz3aBx0TajxhVA4cyOPX8JE8usI/2jqggL9147K75SZuYtUazIklvsiOah9CYDzoYpDMm/u9qb/vzZWZCnnMw+AgpN9Hn4R0F6Rr2MJidle6wdbMrGMIH4ocAIKGV63FbprjJWBdx/m2jMBMIU2Vk7x1L5VXqGiW2lka43lC0879mT7b2bnNPt+E5u8EFp82KXZOkoedVpt8pIYhSd8OOB63AseLfz7bX7Dn6odwnR8RoUTiGtLE3jJo6LuGiUXpvuwPBJVGt9yGAg9s/IVDW2sFeVXI82+9jorduQemuytUqVUUVAgMBAAECggEBAIqlOtRRANwVMLoSSmoi7XitCv3dtmLOJNvQMWxPvtj0HcwGeCWwIn3HLYpjH4BODBofLjItZ/nTT1iuMl7JqHZ/ZQJIdyn6Ju+4FGPQxKBGwUwULDAxd3S3vTddLH6hTv+AOYo0LsEN4aCZEQ8myMb4los36T3HTt0kQFhjXJF/0WpDYv5q+HCWL9iD0NBKrqjN77x5DNZBJ3dgNmzVQW6BzgTbrXK1O2Bh0Z8fAPaE/KkIJSuHSpaPN2lr/8Mxkd3bErVy2qRq4qHc0tQ1R3dMl8EtRvaaGAvY+zooeuHHLrxDZK/6JSyKzoccdN4gab4fZNiAmTBTrrz6S+XZwe0CgYEA7LnPzh4bFEwIvr9/xdz3380ybyW8EvAP65PgTRdae9QeB2Ql1X8NAKhLUhbpGbjaD4puL8X7ECfZ0V6REtTIUbRXQ54xHxh5xTsd2euURxtyod/PTS3svT7k0J683dU87RJZ/TXZKrxXRMxkvUzBk6qaUOyOSkELkXUg2rJnt58CgYEAv37mSBBYRvu37r+jh5tAlUqqiRREtKdJBpmlfsQFaz/AxBef8ZCm6Icpu/V8IQJ3P/qSqRfDq/9xeCbLndqmGH53tsJESCRl0/n9rh2HFBwJn8zQ30A1W2wV0IzZgPlpo+LPoW267tHnnIcC3XnWC0UdqyNAqQ9QXKRnOfFRFssCgYEAoQ5aH3XEHq8MVBqdp9SKoC1PXudhadECQ9i8BjcZTJ4oQAcW5oYmJohZlmQX+3QUh/Y6uS4NfhRe0V9xfEpgb5YuuMZrzSgt0M5DpPvuSeKWvyMyOuQ/6RFdQ8J75MErTm/p4ag9NCInjZsx/si1+u1bSCtpM4PFhxUVYTV38ykCgYBfuRdzvnfiQ8HV3oXoK3tFJWXJ0A/oHhcq7XMEwwfSbLP3UbfqENzeL3J1OmkqNwlVOfRGAlMVj0MxRHNUHRAZ/l3pRYLMbSVZqQVeG/SB4OIOt1tRvc7uPEFLVp0DE6ny+lW/5BIlf1e/rt2HyHduesYx3hVcbQNGzAettytJsQKBgBzmjOL613rLGKQ3PvKkcXKHETgpZJ1FoRN3DLYEGCgh+i6oi3XMZPVKJhT1eNfrI+Z3gX8ZsJIPMiNUmeRYZ1Yd2IK9WLCGZnuGBv5BNnlJfCou8uML1UXlcFq5M1CdXISGMHuft8SLTc0mVXocNRha/S4atxluDb5ifLKrlDE4";
    private final String CHARSET = "UTF-8";
    // 支付宝公钥
    private final String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnharjETIOA0TZEuw1a24kINnbwWU0dZVwCErLs8pr8HAvCx6ludbL54AeNqzBA0vt/eipKoV8TkiCunCo7TDeb8ITZsA6vNbTQQ4bKdwI2JCHwAh8HU5VLBL0dL5GUquDwgwjO1p59rCIPYe12SmG0O/xsW3381F/WKW8unn0OUZYCpbRUNgni9puK+5k7XQYhYjTB4sUzbD3lWHIKRmUeF1JNSbhGC9CzLJ5W6vDlFPLCCsHuQAJC3Y5eWLPCOqW4coD22o8wsJ9vo364btggG2YkZ1hAxz7ijgk5KFQ/2jGv864h2EPO1LsMvA2VRWJ1Inkd4aMd+Gsu87obCBnQIDAQAB";
    //这是沙箱接口路径,正式路径为https://openapi.alipay.com/gateway.do
    private final String GATEWAY_URL = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";
    private final String FORMAT = "JSON";
    //签名方式
    private final String SIGN_TYPE = "RSA2";
    //支付宝异步通知路径,付款完毕后会异步调用本项目的方法,必须为公网地址
    private final String NOTIFY_URL = "http://xht685.natappfree.cc/api/alipay/toSuccess";

    //支付宝同步通知路径,也就是当付款完毕后跳转本项目的页面,可以不是公网地址
    private final String RETURN_URL = "http://localhost:8080/api/alipay/toSuccess";
    private AlipayClient alipayClient = null;
    //支付宝官方提供的接口
    public String sendRequestToAlipay(String outTradeNo, Float totalAmount, String subject) throws AlipayApiException {
        alipayClient = new DefaultAlipayClient(GATEWAY_URL, APP_ID, APP_PRIVATE_KEY, FORMAT, CHARSET, ALIPAY_PUBLIC_KEY, SIGN_TYPE);

        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(RETURN_URL);
        alipayRequest.setNotifyUrl(NOTIFY_URL);

        String body = "";

        // 修正后的参数（关键修改点）
        String bizContent = String.format(
                "{\"out_trade_no\":\"%s\"," +
                        "\"total_amount\":\"%.2f\"," +
                        "\"subject\":\"%s\"," +
                        "\"body\":\"%s\"," +
                        "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}",
                outTradeNo, totalAmount, subject, body
        );

        alipayRequest.setBizContent(bizContent);

        var response = alipayClient.pageExecute(alipayRequest);
        if (response != null) {
            return response.getBody();
        } else {
            throw new AlipayApiException("支付宝页面支付接口返回null");
        }
    }

    // 测试支付宝连接
    public String testAlipayConnection() {
        try {
            if (alipayClient == null) {
                alipayClient = new DefaultAlipayClient(GATEWAY_URL, APP_ID, APP_PRIVATE_KEY, FORMAT, CHARSET, ALIPAY_PUBLIC_KEY, SIGN_TYPE);
            }
            
            // 创建一个简单的查询请求来测试连接
            AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
            JSONObject bizContent = new JSONObject();
            bizContent.put("out_trade_no", "TEST_ORDER_" + System.currentTimeMillis());
            request.setBizContent(bizContent.toString());
            
            System.out.println("测试支付宝连接...");
            AlipayTradeQueryResponse response = alipayClient.execute(request);
            
            if (response != null) {
                return "支付宝连接测试成功，响应: " + response.getBody();
            } else {
                return "支付宝连接测试失败：响应为null";
            }
        } catch (Exception e) {
            return "支付宝连接测试异常: " + e.getMessage();
        }
    }

    //    通过订单编号查询
    public String query(String id){
        System.out.println("开始查询支付宝订单，订单号: " + id);
        
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", id);
        request.setBizContent(bizContent.toString());
        
        System.out.println("支付宝查询请求参数: " + bizContent.toString());
        
        AlipayTradeQueryResponse response = null;
        String body = null;
        
        try {
            // 确保 alipayClient 已初始化
            if (alipayClient == null) {
                System.out.println("初始化支付宝客户端...");
                alipayClient = new DefaultAlipayClient(GATEWAY_URL, APP_ID, APP_PRIVATE_KEY, FORMAT, CHARSET, ALIPAY_PUBLIC_KEY, SIGN_TYPE);
            }
            
            System.out.println("执行支付宝查询请求...");
            response = alipayClient.execute(request);
            
            if (response != null) {
                body = response.getBody();
                System.out.println("支付宝响应体: " + body);
                
                if (response.isSuccess()) {
                    System.out.println("支付宝查询调用成功");
                } else {
                    System.out.println("支付宝查询调用失败，错误码: " + response.getCode() + ", 错误信息: " + response.getMsg());
                }
            } else {
                System.out.println("支付宝接口返回null，请检查网络连接和配置");
            }
        } catch (AlipayApiException e) {
            System.out.println("支付宝接口调用异常: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("系统异常: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("查询结果: " + body);
        return body;
    }
}
