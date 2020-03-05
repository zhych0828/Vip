package com.feijian;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
@MapperScan(value = "com.feijian.dao")
public class VipApplication {

    public static void main(String[] args) {
        SpringApplication.run(VipApplication.class, args);
        log.info("项目已启动");
    }
}