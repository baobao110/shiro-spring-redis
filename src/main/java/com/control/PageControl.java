package com.control;

import com.entity.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping("/Page")
@Controller
public class PageControl {

    @RequestMapping("/tologin")
    public String login() {
        return "login";
    }

    @RequestMapping(value = "/login")
    public String login(User user) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());
        try {
            token.setRememberMe(user.isRemember());//是否记录密码
            subject.login(token);
        } catch (Exception e) {
            /*return "认证失败";*/
            return "failure";
        }
        //编程方式授权，授权方式:编程 注解
        if (subject.hasRole("admin")) {
           try{
               subject.checkPermission("user:update");
           }
           catch (Exception e){
              /* return "没有操作权限";*/
               return "failure";
           }
           return"redirect:/Page/toSuccess.do";//这里发现重定向的妙处
         }
        else{
         /*   return "未授权";*/
            return "failure";
        }
    }
    @RequestMapping("/toSuccess")
    public String success(){
        return "success";
    }


    /*//注解的方式完成授权,这种方式不会用
    @RequiresRoles("admin1")
    @RequiresPermissions("user:select")
    @RequestMapping(value = "/login" ,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String testRole(User user) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());
        try {
            subject.login(token);
        } catch (Exception e) {
            return "认证失败";
        }
        return "success";
    }
*/
}


