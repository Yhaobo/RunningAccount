package erp.config;

import erp.shiro.filter.RbacHttpMethodPermissionFilter;
import erp.shiro.filter.RestFormAuthenticationFilter;
import erp.shiro.realms.MySqlRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
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
 * <p>
 * 在ShiroConfig中做什么事情呢？
 * 1 配置shiro安全管理器，向安全管理器中注入Realm域
 * 2 配置Realm域：注入密码比较器
 * 3 配置密码比较器
 * 4 配置拦截路径和放行路径
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
    public MySqlRealm realm() {
        // 设置用于匹配密码的CredentialsMatcher
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        credentialsMatcher.setHashIterations(hashIterations);
        credentialsMatcher.setHashAlgorithmName(algorithmName);
        return new MySqlRealm(credentialsMatcher);
    }

    /**
     * 配置过滤器的作用路径
     */
    private Map<String, String> getFilterChainMap() {
        Map<String, String> filterChainMap = new LinkedHashMap<>(32);
        filterChainMap.put("/index.html", "anon");
        filterChainMap.put("/", "anon");
        filterChainMap.put("/favicon.ico", "anon");
        filterChainMap.put("/static/**", "anon");
        filterChainMap.put("/rbac/user/login", "anon");
        filterChainMap.put("/rbac/user/logout", "anon");

        filterChainMap.put("/swagger*/**", "anon");
        filterChainMap.put("/v2/api-docs/**", "anon");
        filterChainMap.put("/webjars/springfox-swagger-ui/**", "anon");
        filterChainMap.put("/druid/**", "anon");

        //需要鉴权的资源
        filterChainMap.put("/rbac/**", "rest[rbac]");
        filterChainMap.put("/detail/**", "rest[detail]");
        filterChainMap.put("/option/**", "rest[option]");
        filterChainMap.put("/statistics/**", "rest[statistics]");
        filterChainMap.put("/excel/**", "rest[excel]");

        filterChainMap.put("/**", "authc");
        return filterChainMap;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        // 1、创建过滤器Map，用来装自定义过滤器
        LinkedHashMap<String, Filter> filterMap = new LinkedHashMap<>();

        // 2、将自定义过滤器放入map中，如果实现了自定义授权过滤器，那就必须在这里注册，否则Shiro不会使用自定义的授权过滤器
        filterMap.put("authc", new RestFormAuthenticationFilter());
        filterMap.put("rest", new RbacHttpMethodPermissionFilter());

        // 3、将过滤器Ma绑定到shiroFilterFactoryBean上
        shiroFilterFactoryBean.setFilters(filterMap);

        //配置作用路径
        shiroFilterFactoryBean.setFilterChainDefinitionMap(getFilterChainMap());
        return shiroFilterFactoryBean;
    }

    /**
     * 配置SecurityManager
     */
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //设置realm
        securityManager.setRealm(realm());
        //设置缓存（缓存当前用户拥有的权限或角色信息）
        securityManager.setCacheManager(new MemoryConstrainedCacheManager());
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

    public int getHashIterations() {
        return hashIterations;
    }

    public String getAlgorithmName() {
        return algorithmName;
    }
}