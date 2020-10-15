package erp.shiro.filter;

import erp.shiro.matcher.RestAntPathMatcher;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.FilterChainManager;
import org.apache.shiro.web.filter.mgt.FilterChainResolver;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.beans.factory.BeanInitializationException;

/**
 * 为了注入自定义的 RestPathMatchingFilterChainResolver和路径匹配器 RestAntPathMatcher
 *
 * @author Yhaobo
 * @since 2020/10/6
 */
@Slf4j
public class RestShiroFilterFactoryBean extends ShiroFilterFactoryBean {
    @Override
    protected AbstractShiroFilter createInstance() throws Exception {
        log.debug("Creating Shiro Filter instance.");
        SecurityManager securityManager = this.getSecurityManager();
        String msg;
        if (securityManager == null) {
            msg = "SecurityManager property must be set.";
            throw new BeanInitializationException(msg);
        } else if (!(securityManager instanceof WebSecurityManager)) {
            msg = "The security manager does not implement the WebSecurityManager interface.";
            throw new BeanInitializationException(msg);
        } else {
            FilterChainManager manager = this.createFilterChainManager();

            //*************************注入自定义的 RestPathMatchingFilterChainResolver **********************
            PathMatchingFilterChainResolver chainResolver = new RestPathMatchingFilterChainResolver();
            //*************************设置自定义的 RestAntPathMatcher **********************
            chainResolver.setPathMatcher(new RestAntPathMatcher());

            chainResolver.setFilterChainManager(manager);
            return new RestShiroFilterFactoryBean.SpringShiroFilter((WebSecurityManager) securityManager, chainResolver);
        }
    }

    private static final class SpringShiroFilter extends AbstractShiroFilter {
        protected SpringShiroFilter(WebSecurityManager webSecurityManager, FilterChainResolver resolver) {
            if (webSecurityManager == null) {
                throw new IllegalArgumentException("WebSecurityManager property cannot be null.");
            } else {
                this.setSecurityManager(webSecurityManager);
                if (resolver != null) {
                    this.setFilterChainResolver(resolver);
                }
            }
        }
    }
}
