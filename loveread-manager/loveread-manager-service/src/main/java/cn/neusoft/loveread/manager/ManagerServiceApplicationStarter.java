package cn.neusoft.loveread.manager;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.util.concurrent.CountDownLatch;

@MapperScan(value = "cn.neusoft.loveread.manager.mapper")
@SpringBootApplication
public class ManagerServiceApplicationStarter {
    public static void main(String[] args) throws InterruptedException {
//        SpringApplication.run(ManagerServiceApplicationStarter.class, args);
        new SpringApplicationBuilder()
                .sources(ManagerServiceApplicationStarter.class)
                .web(WebApplicationType.NONE)
                .run(args);
        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await();
    }
}
