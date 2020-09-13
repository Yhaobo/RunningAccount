package erp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Yhaobo
 * @since 2020/9/7
 */
@Configuration//配置类
@EnableSwagger2 //swagger注解
public class SwaggerConfig {

    @Bean
    public Docket webApiConfig() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("webApi")
                .apiInfo(webApiInfo())
                .select()
//                .paths(Predicates.not(PathSelectors.regex("/admin/.*")))
//                .paths(Predicates.not(PathSelectors.regex("/error.*")))
                .build();
    }

    private ApiInfo webApiInfo() {

        return new ApiInfoBuilder()
                .title("网站-API文档")
                .description("API文档")
                .version("1.0")
//                .contact(new Contact("java", "http://atguigu.com", "1123@qq.com"))
                .build();
    }
}