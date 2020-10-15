package erp.shiro.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import erp.shiro.matcher.RestAntPathMatcher;
import erp.util.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.PatternMatcher;
import org.apache.shiro.web.filter.authz.RolesAuthorizationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.owasp.encoder.Encode;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义权限过滤器, 让shiro返回json数据(默认跳转页面), 且匹配自定义Rest风格路径
 *
 * @author Yhaobo
 * @since 2020/10/4
 */
@Slf4j
public class RestRolesAuthorizationFilter extends RolesAuthorizationFilter {

    private final PatternMatcher pathMatcher = new RestAntPathMatcher();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected boolean pathsMatch(String path, ServletRequest request) {
        String requestURI = getPathWithinApplication(request);
        requestURI = RestPathMatchingFilterChainResolver.handlePath(requestURI);
        path = RestPathMatchingFilterChainResolver.handlePath(path);
        log.trace("Attempting to match pattern '{}' with current requestURI '{}'...", path, Encode.forHtml(requestURI));

        final String[] pathSplits = path.split(RestPathMatchingFilterChainResolver.PATH_WITH_METHOD_SEPARATOR);

        if (pathSplits.length == 1) {
            //默认格式
            return pathsMatch(pathSplits[0], requestURI);
        } else {
            //自定义格式
            //  获取当前请求的 http method
            String httpMethod = WebUtils.toHttp(request).getMethod();
            //  将请求uri拼接上请求方式
            requestURI = requestURI + RestPathMatchingFilterChainResolver.PATH_WITH_METHOD_SEPARATOR + httpMethod;
            return pathsMatch(path, requestURI);
        }
    }

    @Override
    protected boolean pathsMatch(String pattern, String path) {
        //调用自定义的 RestAntPathMatcher 类来匹配
        return pathMatcher.matches(pattern, path);
    }

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
