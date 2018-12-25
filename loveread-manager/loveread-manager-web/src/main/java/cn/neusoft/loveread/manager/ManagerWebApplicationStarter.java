package cn.neusoft.loveread.manager;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(value = "cn.neusoft.loveread.manager.mapper")
@SpringBootApplication
public class ManagerWebApplicationStarter{
        public static void main(String[] args) {
            SpringApplication.run(ManagerWebApplicationStarter.class, args);
        }
}
