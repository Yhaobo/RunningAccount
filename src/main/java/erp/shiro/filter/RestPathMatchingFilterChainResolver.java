package erp.shiro.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.mgt.FilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.util.WebUtils;
import org.owasp.encoder.Encode;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 自定义restful风格的路径匹配<br/>
 * 自定义的pathPattern格式为"uri路径::请求方式1,请求方式2" 例如"/**::get,post"
 *
 * @author Yhaobo
 * @since 2020/10/6
 */
@Slf4j
public class RestPathMatchingFilterChainResolver extends PathMatchingFilterChainResolver {
    private static final String DEFAULT_PATH_SEPARATOR = "/";
    public static final String PATH_WITH_METHOD_SEPARATOR = "::";

    @Override
    public FilterChain getChain(ServletRequest request, ServletResponse response, FilterChain originalChain) {
        FilterChainManager filterChainManager = getFilterChainManager();
        if (!filterChainManager.hasChains()) {
            return null;
        }

        String requestURI = handlePath(getPathWithinApplication(request));

        //the 'chain names' in this implementation are actually path patterns defined by the user.  We just use them
        //as the chain name for the FilterChainManager's requirements
        for (String pathPattern : filterChainManager.getChainNames()) {
            pathPattern = handlePath(pathPattern);

            //临时用于匹配的变量
            String matchRequestUri = requestURI;
            final String[] pathPatternSplits = pathPattern.split(PATH_WITH_METHOD_SEPARATOR);

            if (pathPatternSplits.length > 1) {
                //是自定义的路径匹配模式 格式为:"uri路径::请求方式1,请求方式2"
                //  获取当前请求方式, 然后将当前请求uri拼接成自定义格式
                String httpMethod = WebUtils.toHttp(request).getMethod();
                matchRequestUri = requestURI + PATH_WITH_METHOD_SEPARATOR + httpMethod;
            }

            // If the path does match, then pass on to the subclass implementation for specific checks:
            if (pathMatches(pathPattern, matchRequestUri)) {
                if (log.isTraceEnabled()) {
                    log.trace("Matched path pattern [" + pathPattern + "] for requestURI [" + Encode.forHtml(requestURI) + "].  " +
                            "Utilizing corresponding filter chain...");
                }
                return filterChainManager.proxy(originalChain, pathPattern);
            }
        }

        return null;
    }

    /**
     * in spring web, the requestURI "/resource/menus" ---- "resource/menus/" both can access the resource<br/>
     * but the pathPattern match "/resource/menus" can not match "resource/menus/"<br/>
     * user can use requestURI + "/" to simply bypassed chain filter, to bypassed shiro protect
     */
    public static String handlePath(String path) {
        if (path != null && !DEFAULT_PATH_SEPARATOR.equals(path) && path.endsWith(DEFAULT_PATH_SEPARATOR)) {
            //删除路径最后的"/"
            return path.substring(0, path.length() - 1);
        }
        return path;
    }
}
