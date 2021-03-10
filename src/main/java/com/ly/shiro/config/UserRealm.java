package com.ly.shiro.config;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * @Author: LY
 * @Date: 2021/3/10 14:26
 * @Description: 自定义realm   shiro的两个功能，认证和鉴权
 * shiro最终是通过Realm获取安全数据的（如用户、角色、权限），也就是说认证或者授权都会通过Realm进行数据操作
 **/
public class UserRealm extends AuthorizingRealm {


    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("执行授权");
        //给资源进行授权
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //添加资源的授权字符串,要和授权那边的字符串一致
        info.addStringPermission("user:add");
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("执行认证");
        //假设数据库的用户名和密码
        String name = "666";
        String password = "123";
        //编写shiro判断逻辑，也就是判断用户名和密码
        //首先是判断用户名， 也就是看和数据库的是否一致，强制转换这个，然后就可以对比了
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        if (!token.getUsername().equals(name)) {
            //也就是用户名不存在，直接返回null就行，shiro的底层会抛出异常的
            //shiro底层 UnKnowAccountException
            return null;
        }
        //判断密码的话，可以认为是shiro可以自动判断，只需要返回AuthenticationInfo的一个子类，并且返回对应的参数即可
        //第一个和最后一个参数可以省略，只需要中间这个参数是密码即可
        return new SimpleAuthenticationInfo("", password, "");
    }

}
