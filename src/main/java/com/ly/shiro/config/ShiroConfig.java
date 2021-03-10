package com.ly.shiro.config;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Author: LY
 * @Date: 2021/3/10 14:44
 * @Description:
 **/
// 配置spring容器(应用上下文)
@Configuration
public class ShiroConfig {

    /**
     * 创建Realm bean会让方法返回的对象放入到spring的环境，以便使用
     */
    @Bean(name = "userRealm")
    public UserRealm getRealm(){
        return new UserRealm();
    }

    /**
     * 创建DefaultWebSecurityManager
     */
    //这里的传参有点特殊可以调用一个注解，这里有个细节，想看看注解区不区分大小写
    //实测，会报错。。。所以是区分大小写的
    // @Qualifier 注释指定注入 Bean 的名称,是用来消除歧义的
    //这里也要有Bean
    @Bean(name = "defaultWebSecurityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("userRealm") UserRealm userRealm){
        DefaultWebSecurityManager defaultWebSecurityManager=new DefaultWebSecurityManager();
        //关联realm，并且这个是需要传参的
        defaultWebSecurityManager.setRealm(userRealm);
        return defaultWebSecurityManager;
    }

    /**
     * 创建shiroFilterFactoryBean
     */
    //也是需要注解和参数的
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("defaultWebSecurityManager") DefaultWebSecurityManager defaultWebSecurityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean=new ShiroFilterFactoryBean();
        //设置一个安全管理器来关联SecurityManager
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager);

        //添加shiro内置过滤器
        //先创建一个Map集合
        Map<String,String> filterMap= new LinkedHashMap<>();
        //然后往里面添加东西
        // anon:无序认证（登录）就可以访问
        //authc:必须认证才可以访问
        //user:如果使用rememberMe的功能可以直接访问
        //perms:该资源必须得到资源权限才可以访问
        //role:该资源必须得到角色权限才可以访问
        filterMap.put("/add","authc");
        filterMap.put("/update","authc");

        /**
         * 修改调整的登录页面
         */
        //然后这个跳转的页面，就是需要control来进行自动装入这个页面
        shiroFilterFactoryBean.setLoginUrl("/login");

        /**
        *   授权
        */
        //授权过滤器 这边使用的是perms，也就是该资源必须得到资源授权才可以访问
        //所以当未授权的时候shiro就会自动跳转到一个未授权的页面
        filterMap.put("/add","perms[user:add]");
        //所以要设置未授权的页面
        shiroFilterFactoryBean.setUnauthorizedUrl("/unAuth");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);
        return shiroFilterFactoryBean;
    }



}
