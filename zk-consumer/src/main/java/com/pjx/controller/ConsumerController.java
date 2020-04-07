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

    private AtomicInteger init = new AtomicInteger(0);

    @GetMapping("/name")
    public String getName() throws Exception{

        //第一次是1(使用httpclient来请求)
        String s = serversList.get(init.get());
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet("http://"+s+"/name");
        CloseableHttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        if(serversList.size() == 1){
            return EntityUtils.toString(entity);
        }
        //第一次是0，size=2
        if(init.get() < serversList.size()-1){
            init.getAndIncrement();
            return EntityUtils.toString(entity);
        }
        //如果init不小于serversList.size()-1，重置为0
        init.set(0);

        return EntityUtils.toString(entity);

    }
}
