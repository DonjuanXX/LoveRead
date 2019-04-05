package cn.neusoft.loveread.sso.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/page")
public class PageController {
    @RequestMapping("/register")
    public String showRegister(){
        return "register";
    }
    @RequestMapping("/login")
    public String showLogin(){
        return "login";
    }
    @RequestMapping("/favorite-list")
    public String showFavorite(){
        return "favorite";
    }
}
