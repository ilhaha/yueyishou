package com.ilhaha.yueyishou;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.ilhaha.yueyishou.mgr.modules.ums.mapper")
public class WebMgrApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebMgrApplication.class, args);
    }

}
