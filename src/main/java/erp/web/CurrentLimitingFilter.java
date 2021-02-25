package erp.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import erp.util.R;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.time.Duration;

/**
 * 用于请求限流的过滤器，基于令牌桶算法实现
 *
 * @author Yhaobo
 * @date 2021-02-24 19:39
 */
@Slf4j
@WebFilter(urlPatterns = "/*")
public class CurrentLimitingFilter implements Filter {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public static final String WARNING_MSG = "请求过于频繁，请稍后再试";

    public final Bucket bucket = createNewBucket();

    // 令牌桶初始容量
    public static final long CAPACITY = 100;

    // 补充桶的时间间隔
    public static final long SECONDS = 1;

    // 每次补充token的个数
    public static final long REFILL_TOKENS = 20;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        log.info("令牌桶可用的Token数量:{} ", bucket.getAvailableTokens());

        if (bucket.tryConsume(1)) {
            // 直接放行
            chain.doFilter(request, response);
        } else {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writer().writeValueAsString(R.fail().code(HttpStatus.SERVICE_UNAVAILABLE.value()).message(WARNING_MSG)));
        }
    }

    @Override
    public void destroy() {

    }

    private Bucket createNewBucket() {
        Duration refillDuration = Duration.ofSeconds(SECONDS);
        Refill refill = Refill.of(REFILL_TOKENS, refillDuration);
        Bandwidth limit = Bandwidth.classic(CAPACITY, refill);
        return Bucket4j.builder().addLimit(limit).build();
    }
}
