package erp;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@EnableTransactionManagement
@SpringBootApplication
@ServletComponentScan
@MapperScan("erp.dao")
public class WebApplication {

    public static void main(String[] args) {
        log.error(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date())+"\t 打开服务器");
        SpringApplication.run(WebApplication.class, args);
    }
}
