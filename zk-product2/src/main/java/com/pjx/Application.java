package com.pjx;

import org.I0Itec.zkclient.ZkClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Application {
    public static void main(String[] args) {
        register();
        SpringApplication.run(Application.class,args);
    }

    @GetMapping("/name")
    public String getName(){
        return "9002服务器返回："+"吉祥君";
    }

    public static void register(){
        //建立连接
        ZkClient zkClient = new ZkClient("192.168.1.23:2181", 5000);
        //创建节点
        String rootNode = "/name_server";
        if(!zkClient.exists(rootNode)){
            zkClient.createPersistent(rootNode);
        }

        //为了方便观看直接在此定义
        //子节点的名称
        String sonNode = rootNode+"/name_02";
        //子节点的value
        String sonNodeVal = "127.0.0.1:9002";
        //如果不存在
        if(!zkClient.exists(sonNode)){
            zkClient.createEphemeral(sonNode,sonNodeVal);
        }

        System.out.println(sonNode+"服务注册成功！地址："+"---->"+sonNode);

    }
}
