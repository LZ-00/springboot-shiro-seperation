package com.lz.controller;

import com.lz.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author 乐。
 */
@Controller
@Slf4j
public class LoginRegisterController {
    @Autowired
    SessionDAO sessionDAO;

    @GetMapping({"/index","/"})
    public String index(Model model) {
        Subject subject = SecurityUtils.getSubject();
        User user=(User) subject.getPrincipal();
        if (user == null){
            return "login";
        }else{
            model.addAttribute("user",user);
            model.addAttribute("onlineCount",sessionDAO.getActiveSessions().size());
            return "index";
        }
    }

    /**
     * 跳转到登录界面
     * @return
     */
    @GetMapping("/login")
    String toLogin(HttpServletRequest request,Model model){
        if (SecurityUtils.getSubject().isAuthenticated()) {
            return "redirect:/index";
        }else {
            System.out.println(request.getContextPath());
            System.out.println(request.getPathInfo());
            System.out.println(request.getServletPath());
            model.addAttribute("msg",request.getContextPath());
            return "login";
        }
    }

    @PostMapping("/login")
    String login(String username, String password, Boolean rememberMe
            , Model model
            , HttpSession session, HttpServletRequest request){
        log.info("invoke======>login(post)");
        System.out.println("invoke======>login(post)");
        rememberMe = rememberMe==null ? false : true;
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password,rememberMe);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(usernamePasswordToken);
            User user = (User) subject.getPrincipal();

            session.setAttribute("user",user);
            model.addAttribute("user",user);
            return "index";

        } catch (AuthenticationException e) {
            String failureMessage = (String) request.getAttribute("shiroLoginFailure");
            System.out.println("failureMessage: "+failureMessage);
            model.addAttribute("msg",e.getMessage());
            return "login";
        }
    }


    /**
     * 登出  这个方法没用到,用的是shiro默认的logout
     * @param model
     * @return
     */
    @RequestMapping("/logout")
    public String logout( Model model) {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        model.addAttribute("msg","安全退出！");
        return "login";
    }

    /**
     * 跳转到无权限页面
     * @param model
     * @return
     */
    @RequestMapping("/unauthorized")
    public String unauthorized(Model model) {
        System.out.println("invoke unauthorized!");
        return "unauthorized";
    }
}
