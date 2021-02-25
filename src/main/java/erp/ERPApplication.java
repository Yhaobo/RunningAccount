package erp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.time.LocalDateTime;

@EnableTransactionManagement
@SpringBootApplication
@ServletComponentScan("erp.web")
public class ERPApplication {
    public static void main(String[] args) {
        SpringApplication.run(ERPApplication.class, args);
        System.out.println(LocalDateTime.now() + "\t服务端已就绪");
    }
}
