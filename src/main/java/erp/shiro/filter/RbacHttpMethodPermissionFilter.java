package erp.shiro.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import erp.util.R;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.HttpMethodPermissionFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义返回json数据的鉴权过滤器
 * @author Yhaobo
 * @date 2021-03-09 13:49
 */
public class RbacHttpMethodPermissionFilter extends HttpMethodPermissionFilter {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json");

        Subject subject = getSubject(request, response);
        // If the subject isn't identified, redirect to login URL
        if (subject.getPrincipal() == null) {
            //没有认证(登录)
            httpServletResponse.getWriter().write(objectMapper.writer().writeValueAsString((R.fail().code(401).message("请先登录"))));
        } else {
            //没有权限
            httpServletResponse.getWriter().write(objectMapper.writer().writeValueAsString((R.fail().code(403).message("权限受限"))));
        }
        return false;
    }
}
