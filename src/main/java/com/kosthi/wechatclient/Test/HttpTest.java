package com.kosthi.wechatclient.Test;

import org.junit.Test;
import org.springframework.web.client.RestTemplate;

// 未完善
public class HttpTest {
    public static void main(String[] args) throws Exception {
//        OkHttpExample example = new OkHttpExample();
//        String json = "{\"isOnline\": true}";
//        String response = example.sendPutRequest("http://example.com/api/users/123/status", json);
//        System.out.println(response);
    }

    @Test
    public void simpleTest() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://jsonplaceholder.typicode.com/posts/1";
        String str = restTemplate.getForObject(url, String.class);
        System.out.println(str);
    }
}
