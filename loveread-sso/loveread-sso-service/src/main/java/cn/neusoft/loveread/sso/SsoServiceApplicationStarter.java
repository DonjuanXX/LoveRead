package cn.neusoft.loveread.sso;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.util.concurrent.CountDownLatch;

@MapperScan(value = "cn.neusoft.loveread.manager.mapper")
@SpringBootApplication
public class SsoServiceApplicationStarter {
    public static void main(String[] args) throws InterruptedException{
        new SpringApplicationBuilder()
                .sources(SsoServiceApplicationStarter.class)
                .web(WebApplicationType.NONE)
                .run(args);
        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await();
    }
}
