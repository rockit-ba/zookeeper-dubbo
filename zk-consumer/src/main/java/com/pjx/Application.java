package com.pjx;

import org.I0Itec.zkclient.ZkClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }

    @Bean
    public List<String> serversList(){
        List<String> list = new ArrayList<>();
        //建立连接，此处连接为自己电脑的zookeeper连接
        ZkClient zkClient = new ZkClient("192.168.1.23:2181", 5000);

        String rootNode = "/name_server";
        //得到下面所有的子节点，也就是注册的服务
        List<String> childrens = zkClient.getChildren(rootNode);
        for (String children : childrens) {
            //得到子节点全路径
            String sonPath = rootNode+"/"+children;
            //获取对应的val也就是对应服务的ip
            String sonVal = zkClient.readData(sonPath);
            list.add(sonVal);
        }

        return list;
    }

}
