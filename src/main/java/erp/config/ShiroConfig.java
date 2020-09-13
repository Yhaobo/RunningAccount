package erp.config;

import erp.shiro.realms.Realm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * @author Yhaobo
 * @date 2020/3/14
 */
@SpringBootConfiguration
public class ShiroConfig {
    // 配置自定义 realm
    @Bean
    public Realm realm() {
        // 设置用于匹配密码的CredentialsMatcher
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        credentialsMatcher.setHashIterations(7);
        credentialsMatcher.setHashAlgorithmName("md5");
        return new Realm(credentialsMatcher);
    }

    // 配置url过滤器
    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition shiroFilterChainDefinition = new DefaultShiroFilterChainDefinition();
        shiroFilterChainDefinition.addPathDefinition("/index.html", "anon");
        shiroFilterChainDefinition.addPathDefinition("/swagger*/**", "anon");
        shiroFilterChainDefinition.addPathDefinition("/v2/api-docs/**", "anon");
        shiroFilterChainDefinition.addPathDefinition("/webjars/springfox-swagger-ui/**", "anon");
        shiroFilterChainDefinition.addPathDefinition("/static/**", "anon");
        shiroFilterChainDefinition.addPathDefinition("/user/login", "anon");
        shiroFilterChainDefinition.addPathDefinition("/druid/**", "anon");
        shiroFilterChainDefinition.addPathDefinition("/detail/add*", "roles[user]");
        shiroFilterChainDefinition.addPathDefinition("/detail/update*", "roles[user]");
        shiroFilterChainDefinition.addPathDefinition("/detail/delete*", "roles[user]");
        shiroFilterChainDefinition.addPathDefinition("/setting/add*", "roles[user]");
        shiroFilterChainDefinition.addPathDefinition("/setting/update*", "roles[user]");
        shiroFilterChainDefinition.addPathDefinition("/excel/*", "roles[user]");
        shiroFilterChainDefinition.addPathDefinition("/user/getUsername", "roles[admin]");
        shiroFilterChainDefinition.addPathDefinition("/user/alter", "roles[admin]");
        shiroFilterChainDefinition.addPathDefinition("/user/exit", "logout");
        shiroFilterChainDefinition.addPathDefinition("/**", "authc");
        return shiroFilterChainDefinition;
    }

    // 配置security并设置userReaml，避免xxxx required a bean named 'authorizer' that could not be found.的报错
    @Bean
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(realm());
        return securityManager;
    }

    // 这个Bean的作用是使得@RequiresRoles和@RequiresPermissions注解生效
    @Bean
    public static DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator =
                new DefaultAdvisorAutoProxyCreator();
        // setUsePrefix(true)用于解决一个奇怪的bug。在引入spring aop的情况下。
        // 在@Controller注解的类的方法中加入@RequiresRole等shiro注解，会导致该方法无法映射请求，
        // 导致返回404，加入这项配置能解决这个bug
        defaultAdvisorAutoProxyCreator.setUsePrefix(true);
        return defaultAdvisorAutoProxyCreator;
    }
}
