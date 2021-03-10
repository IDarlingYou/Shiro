package com.ly.shiro.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author: LY
 * @Date: 2021/3/10 14:09
 * @Description:
 **/
@Controller
public class TestController {

    @GetMapping("/tes")
    public String testThymeleaf(Model model) {
        //把数据存入到model中
        model.addAttribute("name", "你来我不相信,你走我就当你没来过");
        //返回界面
        return "test";
    }

    @GetMapping("/add")
    public String add() {
        return "/good/add";
    }

    @GetMapping("/update")
    public String update() {
        return "/good/update";
    }

    //用户的的登录
    @RequestMapping("/login")
    public String login() {
        //同理返回路径
        return "login";
    }

    @RequestMapping("/unAuth")
    public String unAuth() {
        //同理返回路径
        return "unAuth";
    }

    /**
     * 登录逻辑处理
     */
    //这里的话 我把页面就改成了loginlogin
    @RequestMapping("/login2")
    //model这个参数是用来存一些可用的信息
    public String loginlogin(String name, String password, Model model) {
        /**
         * 这里就开始使用shiro编写认证了
         */
        //1.获取subject,调用Sercurity的方法
        Subject subject = SecurityUtils.getSubject();
        //然后封装用户数据
        UsernamePasswordToken token = new UsernamePasswordToken(name, password);
        //然后调用subject的登录方法，并使用try catch来分别捕获用户名和密码这两个异常
        try {
            subject.login(token);
            //登录成功就直接去test界面好了
            return "redirect:/tes";
            //然后修改两个不同的异常
        } catch (UnknownAccountException e) {
            //这个是用户名不存在的异常
            model.addAttribute("msg", "用户名不存在");
            //然后就需要返回到登录页面
            return "/login";
        } catch (IncorrectCredentialsException e) {
            //这个是密码错误的异常
            model.addAttribute("msg", "密码错误");
            //然后就需要返回到登录页面
            return "/login";
        }
    }



}