package erp.config;

import erp.shiro.filter.RestFormAuthenticationFilter;
import erp.shiro.filter.RestRolesAuthorizationFilter;
import erp.shiro.filter.RestShiroFilterFactoryBean;
import erp.shiro.realms.MyRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Yhaobo
 * @date 2020/3/14
 */
@SpringBootConfiguration
public class ShiroConfig {
    @Value("${erp.password.hashIterations}")
    private int hashIterations;
    @Value("${erp.password.algorithmName}")
    private String algorithmName;

    /**
     * 配置自定义 realm
     */
    @Bean
    public MyRealm realm() {
        // 设置用于匹配密码的CredentialsMatcher
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        credentialsMatcher.setHashIterations(hashIterations);
        credentialsMatcher.setHashAlgorithmName(algorithmName);
        return new MyRealm(credentialsMatcher);
    }

    /**
     * 配置过滤器的作用路径
     */
    public Map<String, String> getFilterChainMap() {
        Map<String, String> filterChainMap = new LinkedHashMap<>(32);
        filterChainMap.put("/index.html", "anon");
        filterChainMap.put("/", "anon");
        filterChainMap.put("/favicon.ico", "anon");
        filterChainMap.put("/static/**", "anon");
        filterChainMap.put("/user/login", "anon");
        filterChainMap.put("/user/logout", "anon");

        filterChainMap.put("/swagger*/**", "anon");
        filterChainMap.put("/v2/api-docs/**", "anon");
        filterChainMap.put("/webjars/springfox-swagger-ui/**", "anon");
        filterChainMap.put("/druid/**", "anon");

        //下面为自定义的路径匹配模式
        filterChainMap.put("/user/**::post,put,delete,get", "roles[admin]");

        filterChainMap.put("/**::post,put,delete", "roles[user]");
        filterChainMap.put("/excel/**", "roles[user]");

        filterChainMap.put("/**", "authc");
        return filterChainMap;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        // 创建自定义的 RestShiroFilterFactoryBean
        ShiroFilterFactoryBean shiroFilterFactoryBean = new RestShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        // 1、创建过滤器Map，用来装自定义过滤器
        LinkedHashMap<String, Filter> filterMap = new LinkedHashMap<>();

        // 2、将自定义过滤器放入map中，如果实现了自定义授权过滤器，那就必须在这里注册，否则Shiro不会使用自定义的授权过滤器
        filterMap.put("authc", new RestFormAuthenticationFilter());
        filterMap.put("roles", new RestRolesAuthorizationFilter());

        // 3、将过滤器Ma绑定到shiroFilterFactoryBean上
        shiroFilterFactoryBean.setFilters(filterMap);

        //配置作用路径
        shiroFilterFactoryBean.setFilterChainDefinitionMap(getFilterChainMap());
        return shiroFilterFactoryBean;
    }

    /**
     * 配置security并设置userReaml，避免xxxx required a bean named 'authorizer' that could not be found.的报错
     */
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(realm());
        return securityManager;
    }

    /**
     * 这个Bean的作用是使得@RequiresRoles和@RequiresPermissions注解生效
     */
    @Bean
    public static DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator =
                new DefaultAdvisorAutoProxyCreator();
        // setUsePrefix(true)用于解决一个奇怪的bug。在引入spring aop的情况下。
        // 在@Controller注解的类的方法中加入@RequiresRole等shiro注解，会导致该方法无法映射请求，
        // 导致返回404，加入这项配置能解决这个bug
        defaultAdvisorAutoProxyCreator.setUsePrefix(true);
        return defaultAdvisorAutoProxyCreator;
    }

}