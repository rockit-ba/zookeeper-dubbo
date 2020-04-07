package com.pjx.controller;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class ConsumerController {

    @Autowired
    private List<String> serversList;

    private AtomicInteger init = new AtomicInteger(1);


    @GetMapping("/name")
    public String getName() throws Exception{

        //第一次是1(使用httpclient来请求)
        String s = serversList.get(init.get() - 1);
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet("http://"+s+"/name");
        CloseableHttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();

        //示例使用轮训的方式，取模的数量是负载方的服务数
        //首次访问init=1，取模不等于0，把他设置为2，然后返回结果
        if(init.get() % 2 != 0){
            init.incrementAndGet();
            return EntityUtils.toString(entity);

        }
        //第二次init=2，取模=0，把init重置为1，然后返回
        if(init.get() % 2 == 0){
            init.set(1);
        }

        return EntityUtils.toString(entity);

    }
}
