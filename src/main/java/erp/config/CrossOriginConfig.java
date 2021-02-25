package erp.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 跨域配置,只有在开发环境中启用
 *
 * @author Yhaobo
 * @since 2020/10/3
 */
@SpringBootConfiguration
@Profile("dev")
public class CrossOriginConfig {
    /**
     * 设置一个过滤器，放在shiro的过滤器之前，这样就可以保证在所有请求到来的时候，都会给response加上跨域headers
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        // 允许cookies跨域
        config.setAllowCredentials(true);
        // #允许向该服务器提交请求的源，*表示全部允许，在SpringMVC中，如果设成*，会自动转成当前请求头中的Origin
        config.addAllowedOrigin("http://localhost:8081");
        config.addAllowedOrigin("http://localhost");
        // #允许访问的头信息,*表示全部
        config.addAllowedHeader("*");
        // 预检请求的缓存时间（秒），即在这个时间段里，对于相同的跨域请求不会再预检了
        config.setMaxAge(1800L);
        // 允许提交请求的方法，*表示全部允许
        config.addAllowedMethod("*");
        // 作用路径
        source.registerCorsConfiguration("/**", config);

        return new FilterRegistrationBean<>(new CorsFilter(source));
    }
}