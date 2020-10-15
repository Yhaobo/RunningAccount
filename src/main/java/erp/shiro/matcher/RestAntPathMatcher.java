package erp.shiro.matcher;

import erp.shiro.filter.RestPathMatchingFilterChainResolver;
import erp.util.MyException;
import org.apache.shiro.util.AntPathMatcher;

/**
 * @author Yhaobo
 * @since 2020/10/9
 */
public class RestAntPathMatcher extends AntPathMatcher {
    @Override
    protected boolean doMatch(String pattern, String path, boolean fullMatch) {
        final String[] patternSplits = pattern.split(RestPathMatchingFilterChainResolver.PATH_WITH_METHOD_SEPARATOR);
        if (patternSplits.length > 1) {
            //是自定义格式
            if (patternSplits.length == 2) {
                final String[] pathSplits = path.split(RestPathMatchingFilterChainResolver.PATH_WITH_METHOD_SEPARATOR);
                //分为路径和方法两部分
                String patternMethod = patternSplits[1];
                String pathMethod = pathSplits[1];

                final String[] patternMethods = patternMethod.split(",");
                for (String method : patternMethods) {
                    //多个方法只要一个匹配就行
                    if (pathMethod.equalsIgnoreCase(method)) {
                        return super.doMatch(patternSplits[0], pathSplits[0], fullMatch);
                    }
                }
                return false;
            } else {
                throw new MyException("shiro自定义路径匹配格式不正确, 正确格式为'URI路径::请求方式1,请求方式2'");
            }
        } else {
            //默认格式
            return super.doMatch(patternSplits[0], path, fullMatch);
        }
    }
}
