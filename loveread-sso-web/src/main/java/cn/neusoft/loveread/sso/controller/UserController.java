package cn.neusoft.loveread.sso.controller;


import cn.neusoft.loveread.common.pojo.LoveReadResult;
import cn.neusoft.loveread.common.utils.CookieUtils;
import cn.neusoft.loveread.pojo.TbUser;
import cn.neusoft.loveread.pojo.TbUserDesc;
import cn.neusoft.loveread.sso.service.UserService;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/user")
public class UserController {

    @Reference
    private UserService userService;
    @Value("${TOKEN_KEY}")
    private String TOKEN_KEY;

    @RequestMapping("/check/{param}/{type}")
    @ResponseBody
    public LoveReadResult checkData(@PathVariable String param, @PathVariable Integer type) {
        return userService.checkData(param, type);
    }

    @PostMapping("/register")
    @ResponseBody
    public LoveReadResult register(TbUser tbUser, String favorite) {
        if (favorite.equals(""))
            return userService.register(tbUser);
        return userService.setFavorite(favorite, tbUser);
    }

    @PostMapping("/login")
    @ResponseBody
    public LoveReadResult login(String username, String password, HttpServletRequest request, HttpServletResponse response) {
        LoveReadResult result = userService.login(username, password);
        //Token写入cookie,浏览器关闭就过期
        if (result.getStatus() == 200) {
            String token = result.getData().toString();
            CookieUtils.setCookie(request, response, TOKEN_KEY, token);
        }
        return result;
    }

    @RequestMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        CookieUtils.deleteCookie(request, response, TOKEN_KEY);
        return "logoutSuccess";
    }

    @RequestMapping("/token/{token}")
    @ResponseBody
    public Object getUserByToken(@PathVariable String token, String callback) {
        LoveReadResult result = userService.getUserByToken(token);
        if (StringUtils.isNotEmpty(callback)) {//如果是Jsonp请求
            //把结果封装成一个js语句响应
            MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
            mappingJacksonValue.setJsonpFunction(callback);
            return mappingJacksonValue;
        }
        return result;
    }

    @RequestMapping("/favorite/update")
    @ResponseBody
    public LoveReadResult updateFavorite(String favorite, Long id) {
        return userService.updateFavorite(favorite, id);
    }

    @GetMapping("/favorite/{id}")
    @ResponseBody
    public String getFavoriteById(@PathVariable Long id) {
        return userService.getFavoriteById(id);
    }

    @PostMapping("/addHome")
    @ResponseBody
    public LoveReadResult addHome(TbUserDesc tbUserDesc) {
        return userService.addHome(tbUserDesc);
    }

    @GetMapping("/homeList/{id}")
    @ResponseBody
    public Object homeList(@PathVariable Long id) {
        return userService.getDescById(id);
    }

}
