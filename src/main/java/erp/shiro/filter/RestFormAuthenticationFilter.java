package erp.shiro.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import erp.util.R;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义认证过滤器, 让shiro返回json数据(默认跳转页面)
 *
 * @author Yhaobo
 * @since 2020/10/4
 */
public class RestFormAuthenticationFilter extends FormAuthenticationFilter {
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 在访问controller前判断是否登录，返回json，不进行重定向。
     *
     * @return true-继续往下执行，false-该filter过滤器已经处理，不继续执行其他过滤器
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json");
        httpServletResponse.getWriter().write(objectMapper.writer().writeValueAsString((R.fail().code(401).message("请先登录"))));
        return false;
    }

}

